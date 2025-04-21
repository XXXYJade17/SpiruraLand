package com.xxxyjade17.spiruraland.Monsters.Monster.FirstLevel;

import com.xxxyjade17.spiruraland.Attributes.Attribute.AttackRange;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import java.util.Timer;
import java.util.TimerTask;

public class FirstMonster1 extends Monster {
    private static final EntityDataAccessor<BlockPos> SPAWN_POSITION = SynchedEntityData.defineId(FirstMonster1.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<Boolean> IS_RESPAWNING = SynchedEntityData.defineId(FirstMonster1.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_ATTACKED = SynchedEntityData.defineId(FirstMonster1.class, EntityDataSerializers.BOOLEAN);

    public FirstMonster1(EntityType<? extends Monster> entityType, Level level){
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.ATTACK_DAMAGE, 1.0D)
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(AttackRange.ATTACK_RANGE.get(),2.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SPAWN_POSITION, null);
        this.entityData.define(IS_RESPAWNING, false);
        this.entityData.define(IS_ATTACKED, false);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide && getSpawnPosition() == null) {
            setSpawnPosition(this.blockPosition());
        }
    }

    @Override
    protected void registerGoals() {
//        this.goalSelector.addGoal(2, new FirstMonsterGoal(this));
    }

    public void respawn() {
        if (this.level().isClientSide() || getSpawnPosition() == null || getIsRespawning()) {
            return;
        }

        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        setIsRespawning(true);
        System.out.println("Scheduling respawn for entity " + this.getId() + " at " + getSpawnPosition()); // 调试日志

        final BlockPos respawnLocation = getSpawnPosition(); // 为 lambda 捕获
        final EntityType<?> entityType = this.getType(); // 为 lambda 捕获

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Executing respawn task for type " + entityType.getDescriptionId() + " at " + respawnLocation); // 调试日志
                FirstMonster1 newMonster = (FirstMonster1) entityType.create(serverLevel);
                if (newMonster != null) {
                    newMonster.moveTo(respawnLocation.getX() + 0.5, respawnLocation.getY(), respawnLocation.getZ() + 0.5);
                    newMonster.setSpawnPosition(respawnLocation);
                    newMonster.setIsRespawning(false);
                    newMonster.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(respawnLocation), MobSpawnType.REINFORCEMENT, null, null);

                    if (serverLevel.addFreshEntity(newMonster)) {
                        System.out.println("Successfully respawned entity at " + respawnLocation); // 调试日志
                    }
                }
                this.cancel(); // 取消定时器
            }
        }, 300*20);
    }

    public void setSpawnPosition(BlockPos pos) {
        this.entityData.set(SPAWN_POSITION, pos);
    }

    public BlockPos getSpawnPosition() {
        return this.entityData.get(SPAWN_POSITION);
    }

    public void setIsRespawning(boolean isRespawning) {
        this.entityData.set(IS_RESPAWNING, isRespawning);
    }

    public boolean getIsRespawning() {
        return this.entityData.get(IS_RESPAWNING);
    }

    public void setIsAttacked(boolean isAttacked) {
        this.entityData.set(IS_ATTACKED, isAttacked);
    }

    public boolean getIsAttacked() {
        return this.entityData.get(IS_ATTACKED);
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        return super.hurt(pSource, pAmount);
    }


    @Override
    public void die(DamageSource damageSource) {
        super.die(damageSource);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.ZOMBIE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ZOMBIE_DEATH;
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, SpawnGroupData pSpawnData, CompoundTag pDataTag) {
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    @Override
    public boolean shouldDropExperience() {
        return false;
    }

    @Override
    protected boolean shouldDropLoot() {
        return false;
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || getSpawnPosition() != null;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return getSpawnPosition() == null &&!this.requiresCustomPersistence();
    }
}
