package com.xxxyjade17.spiruraland.Monsters.Monster.FirstLevel;

import com.xxxyjade17.spiruraland.Attributes.Attribute.AttackRange;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

public class FirstMonster extends Monster {
    private final EntityDataAccessor<Boolean> IS_ATTACKED = SynchedEntityData.defineId(FirstMonster.class, EntityDataSerializers.BOOLEAN);
    private BlockPos SpawnPosition;
    private int deathTimer = 0;

    public FirstMonster(EntityType<? extends Monster> entityType, Level level){
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.ATTACK_DAMAGE, 1.0D)
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(AttackRange.ATTACK_RANGE.get(),2.0D);
    }

//    @Override
//    protected void defineSynchedData() {
//        super.defineSynchedData();
//        this.entityData.define(IS_ATTACKED, false);
//    }

    @Override
    protected void registerGoals() {
//        this.goalSelector.addGoal(2, new FirstMonsterGoal(this));
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            if(SpawnPosition==null){
                SpawnPosition = this.blockPosition();
            }
            boolean isDead = this.isDeadOrDying();
            if (this.isDeadOrDying()) {
                if (deathTimer == 0) {
                    deathTimer = 19;
                }
                if (deathTimer > 0) {
                    deathTimer--;
                    System.out.println("deathTimer:" + deathTimer);
                    if (deathTimer == 0) {
                        System.out.println("revive");
                        this.revive();
                    }
                }
            } else if (getTarget() == null) {
                this.getNavigation().stop();
            }
        }
    }

    @Override
    public void revive() {
        this.setHealth(this.getMaxHealth());
//        this.setIsAttacked(false);
        this.teleportTo(SpawnPosition.getX() + 0.5, SpawnPosition.getY(), SpawnPosition.getZ() + 0.5);

//        this.setRemoved(Entity.RemovalReason.NONE);
//        this.level().addFreshEntity(this);
        deathTimer = 0;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
//        this.setIsAttacked(true);
        return super.hurt(pSource, pAmount);
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
    public void remove(RemovalReason reason) {
    }

//    public boolean isAttacked(){
//        return this.entityData.get(IS_ATTACKED);
//    }
//
//    public void setIsAttacked(boolean attacked){
//        this.entityData.set(IS_ATTACKED, attacked);
//    }e
}
