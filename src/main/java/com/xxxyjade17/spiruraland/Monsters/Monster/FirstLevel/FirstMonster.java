package com.xxxyjade17.spiruraland.Monsters.Monster.FirstLevel;

import com.xxxyjade17.spiruraland.Attributes.Attribute.AttackRange;
import com.xxxyjade17.spiruraland.Monsters.Goal.FirstMonsterGoal;
import com.xxxyjade17.spiruraland.Monsters.Monster.Interface.ISpiruraMonster;
import com.xxxyjade17.spiruraland.Spirura.Capability.Spirura;
import com.xxxyjade17.spiruraland.Spirura.Handler.CapabilityHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class FirstMonster extends Monster implements ISpiruraMonster {

    private Boolean isReviving = false;
    @Nullable private BlockPos spawnPosition = BlockPos.ZERO;
    private static final int REVIVAL_TICKS = 100;
    private int reviveTimer = REVIVAL_TICKS;

    private static final EntityDataAccessor<Optional<BlockPos>> DATA_SPAWN_POSITION =
            SynchedEntityData.defineId(FirstMonster.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);

    public FirstMonster(EntityType<? extends Monster> entityType, Level level){
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SPAWN_POSITION, Optional.empty());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.ATTACK_DAMAGE, 1.0D)
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(AttackRange.ATTACK_RANGE.get(),2.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(0, new FirstMonsterGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, true));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor levelAccessor,
                                        DifficultyInstance difficulty,
                                        MobSpawnType reason,
                                        @Nullable SpawnGroupData spawnData,
                                        @Nullable CompoundTag dataTag) {
        if(!this.level().isClientSide()) {
            if (this.spawnPosition == BlockPos.ZERO) {
                this.setSpawnPosition(this.blockPosition());
            }
            System.out.println("ID: "+this.getId());
            return super.finalizeSpawn(levelAccessor, difficulty, reason, spawnData, dataTag);
        }
        return null;
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide && this.spawnPosition == BlockPos.ZERO) {
            getSpawnPosition().ifPresent(pos -> {
                if(pos != BlockPos.ZERO){
                    this.spawnPosition = pos;
                }else{
                    setSpawnPosition(this.blockPosition());
                }
            });
        }
        if(this.isReviving){
            if(!this.isInvisible()){
                this.setInvisible(true);
            }
            reviveTimer--;
            if(reviveTimer<=0){
                ServerLevel serverLevel = (ServerLevel) this.level();
                if (!this.isRemoved() && this.level() == serverLevel && serverLevel.isAreaLoaded(spawnPosition, 1)) {
                    // 恢复实体的状态
                    this.setHealth(this.getMaxHealth()); // 完全恢复生命值
                    this.setInvisible(false);              // 取消隐形
                    this.setInvulnerable(false);             // 取消无敌

                    // 清理可能残留的状态
                    this.clearFire();                       // 清除火焰
                    this.setDeltaMovement(Vec3.ZERO);       // 重置速度
                    this.setPose(Pose.STANDING);

                    // 传送回复活点
                    this.teleportTo(spawnPosition.getX() + 0.5D, spawnPosition.getY(), spawnPosition.getZ() + 0.5D);

                    setIsReviving(false);

                }
            }
        }
    }

    @Override
    public void die(DamageSource damageSource) {
        this.setHealth(0.001f);
        this.setIsReviving(true);
        this.reviveTimer = REVIVAL_TICKS;
        this.setInvulnerable(true); // 防止后续伤害或交互
        this.setTarget(null);       // 清除目标
        this.getNavigation().stop();// 停止移动
        if(damageSource.getEntity() instanceof ServerPlayer player){
            Optional<Spirura> optionalSpirura =
                    Optional.ofNullable(player.getCapability(CapabilityHandler.SPIRURA_HANDLER));
            optionalSpirura.ifPresent(spirura -> {
                spirura.addExperience(5);
            });
        }
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.getEntity() instanceof ServerPlayer player) {
            // 设置攻击源为目标
            this.setTarget(player);
        }
        return super.hurt(pSource, pAmount);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        if (!this.level().isClientSide()) {
            super.addAdditionalSaveData(compound);
            getSpawnPosition().ifPresent(pos -> {
                compound.put("SpawnPosition", NbtUtils.writeBlockPos(pos));
            });
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        if(!this.level().isClientSide()) {
            super.readAdditionalSaveData(compound);
            // 从 NBT 读取出生点
            if (compound.contains("SpawnPosition", CompoundTag.TAG_COMPOUND)) {
                BlockPos loadedPos = NbtUtils.readBlockPos(compound.getCompound("SpawnPosition"));
                // 从 NBT 加载后，立即设置到内存和同步数据中
                // 注意：此时 level 可能还未完全准备好，但 BlockPos 本身可以存储
                // 在 tick 或 finalizeSpawn 中会确保 level 可用
                this.spawnPosition = loadedPos;
                this.entityData.set(DATA_SPAWN_POSITION, Optional.of(loadedPos));
            } else {
                this.entityData.set(DATA_SPAWN_POSITION, Optional.empty());
            }
        }
    }

    /**
     * 设置此怪物的出生点（服务器端调用）。
     * @param pos 新的出生点位置。
     */
    public void setSpawnPosition(BlockPos pos) {
        if (!this.level().isClientSide()) {
            this.spawnPosition = pos.immutable(); // 存储不可变副本
            this.entityData.set(DATA_SPAWN_POSITION, Optional.of(this.spawnPosition)); // 更新同步数据
        }
    }

    /**
     * 获取当前记录的出生点位置（主要从同步数据获取）。
     * @return Optional 包含出生点 BlockPos，如果未设置则为空。
     */
    public Optional<BlockPos> getSpawnPosition() {
        if(!this.level().isClientSide()) {
            Optional<BlockPos> posFromData = this.entityData.get(DATA_SPAWN_POSITION);
            if (posFromData.isPresent() && !posFromData.get().equals(this.spawnPosition)) {
                this.spawnPosition = posFromData.get();
            }
            return posFromData;
        }
        return Optional.empty();
    }

    public void setIsReviving(boolean isReviving) {
        this.isReviving = isReviving;
    }

    public Boolean getIsReviving() {
        return this.isReviving;
    }
}
