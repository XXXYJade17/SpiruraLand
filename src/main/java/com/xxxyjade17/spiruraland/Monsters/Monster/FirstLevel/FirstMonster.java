package com.xxxyjade17.spiruraland.Monsters.Monster.FirstLevel;

import com.xxxyjade17.spiruraland.Attributes.Attribute.AttackRange;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class FirstMonster extends Monster {
    // 用于存储出生点位置的同步数据。即使实体数据被同步到客户端，客户端也不直接使用它来复活。
    // 主要用于服务器端持久化和命令修改。
    private static final EntityDataAccessor<Optional<BlockPos>> DATA_SPAWN_POSITION =
            SynchedEntityData.defineId(FirstMonster.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);

    private static final int REVIVAL_TICKS = 100;
    // 在内存中存储出生点，避免频繁访问 NBT 或 SyncedData
    @Nullable
    private BlockPos spawnPosition = null;
    private int timer=REVIVAL_TICKS;
    private boolean isReviving = false;

    public FirstMonster(EntityType<? extends Monster> entityType, Level level){
        super(entityType, level);
    }

    // 定义实体数据
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SPAWN_POSITION, Optional.empty());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.ATTACK_DAMAGE, 1.0D)
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(AttackRange.ATTACK_RANGE.get(),2.0D);
    }

    // 首次在世界中生成时调用（包括自然生成、刷怪蛋、指令等）
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor levelAccessor,
                                        DifficultyInstance difficulty,
                                        MobSpawnType reason,
                                        @Nullable SpawnGroupData spawnData,
                                        @Nullable CompoundTag dataTag) {
        // 如果出生点尚未被设定（例如非复活情况），则记录当前位置为出生点
        if (this.spawnPosition == null) {
            this.setSpawnPosition(this.blockPosition(), levelAccessor.getLevel()); // 使用 setSpawnPosition 来同时更新内存和同步数据
        }
        return super.finalizeSpawn(levelAccessor, difficulty, reason, spawnData, dataTag);
    }

    // Tick 方法，可以在这里添加一些逻辑，比如确保出生点有效
    @Override
    public void tick() {
        super.tick();
        // 简单的保险措施：如果 birthPoint 在内存中丢失（理论上不应发生），尝试从同步数据恢复
        if (!level().isClientSide && this.spawnPosition == null) {
            getSpawnPosition().ifPresent(pos -> this.spawnPosition = pos);
            // 如果同步数据也没有，就用当前位置作为最后的手段
            if (this.spawnPosition == null) {
                setSpawnPosition(this.blockPosition(), this.level());
            }
        }
        if(this.isReviving){
            timer--;
            if(timer<=0){
                ServerLevel serverLevel = (ServerLevel) this.level();
                BlockPos respawnPos = this.getSpawnPosition().get();
                if (!this.isRemoved() && this.level() == serverLevel && serverLevel.isAreaLoaded(respawnPos, 1)) {

                    System.out.println("[RespawningMonster] Executing revival for: " + this.getId());

                    // 恢复实体的状态
                    this.setHealth(this.getMaxHealth()); // 完全恢复生命值
                    this.setInvisible(false);              // 取消隐形
                    this.setInvulnerable(false);             // 取消无敌

                    // 清理可能残留的状态
                    this.clearFire();                       // 清除火焰
                    this.setDeltaMovement(Vec3.ZERO);       // 重置速度
                    this.setPose(Pose.STANDING);
                    // monster.removeAllEffects();             // (可选) 移除所有药水效果

                    // 传送回复活点
                    System.out.println("spawnpos:"+respawnPos);
                    this.teleportTo(respawnPos.getX() + 0.5D, respawnPos.getY(), respawnPos.getZ() + 0.5D);

                    // 确保它仍然持久存在（如果需要）
//            monster.setPersistenceRequired(true); // 也许不需要，因为removeWhenFarAway=false
                    setIsReviving(false);

                } else {
                    System.out.println("[RespawningMonster] Failed to revive: " + this.getId() + ". Entity removed or area unloaded. Pos: " + respawnPos);
                    // 如果实体因某种原因消失了或无法复活，确保它被彻底移除（以防万一）
                    if (!this.isRemoved() && this.level() == serverLevel) {
                        this.discard(); // 或者 remove(RemovalReason.DISCARDED)
                    }
                }
            }
        }


    }

    @Override
    public void die(DamageSource damageSource) {
        System.out.println("diediedie");
        this.gameEvent(GameEvent.ENTITY_DIE);
        this.level().broadcastEntityEvent(this, (byte)3);
        this.setPose(Pose.DYING);
        this.setHealth(0.001f);
//        this.deathTime-=REVIVAL_TICKS;
        this.setIsReviving(true);
        this.timer = REVIVAL_TICKS;
        this.setInvulnerable(true); // 防止后续伤害或交互
        this.setInvisible(true);    // 使其隐形
        this.setTarget(null);       // 清除目标
        this.getNavigation().stop();// 停止移动
    }

//    @Override
//    public void die(DamageSource damageSource) {
//        // 触发死亡逻辑，播放声音、动画等，但实际的移除和复活由我们控制
//        super.die(damageSource); // 这个方法会设置生命值为0，并可能触发一些事件
//
//        if (!this.level().isClientSide()) {
//            ServerLevel serverLevel = (ServerLevel) this.level();
//            Optional<BlockPos> optionalBirthPos = getSpawnPosition(); // 从同步数据获取出生点
//
//            if (optionalBirthPos.isPresent()) {
//                BlockPos respawnPos = optionalBirthPos.get();
//                EntityType<?> type = this.getType(); // 获取当前怪物的类型
//
//                // 移除当前实体（使其消失，掉落物等由 super.die() 和后续事件处理）
//                // 注意：不应立即移除，因为可能还有事件监听器需要访问此实体。
//                // 更好的方法是让原版死亡流程自然移除它，我们只负责安排复活。
//                // this.remove(RemovalReason.KILLED); // 谨慎使用，可能干扰原版掉落等
//
//                // 使用服务器的计划任务功能，在30秒后执行复活操作
//                serverLevel.getServer().tell(new TickTask(
//                        serverLevel.getServer().getTickCount() + (3000 * 20), // 30秒 * 20刻/秒
//                        () -> {
//                            // 检查世界是否仍然加载，防止在卸载的区块复活导致问题
//                            if (serverLevel.isAreaLoaded(respawnPos, 1)) { // 检查复活点附近区域是否加载
//                                FirstMonster newMonster = (FirstMonster) type.create(serverLevel);
//                                if (newMonster != null) {
//                                    // 设置新怪物的位置到出生点
//                                    newMonster.moveTo(respawnPos.getX() + 0.5D, respawnPos.getY(), respawnPos.getZ() + 0.5D, this.getYRot(), this.getXRot()); // 稍微调整X/Z中心，防止卡墙
//                                    // 关键：将出生点信息传递给新生成的怪物实例
//                                    newMonster.setSpawnPosition(respawnPos, serverLevel);
//                                    // 可选：重置生命值等状态（通常 create 时已是满状态）
//                                    newMonster.setHealth(newMonster.getMaxHealth());
//
//                                    // 触发生成事件并添加到世界
//                                    // newMonster.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(respawnPos), MobSpawnType.REINFORCEMENT, null, null); // 使用 REINFORCEMENT 或自定义类型避免影响刷怪上限？
//                                    serverLevel.addFreshEntityWithPassengers(newMonster); // 添加到世界
//
//                                    System.out.println("RespawningMonster respawned at: " + respawnPos);
//                                }
//                            } else {
//                                System.out.println("RespawningMonster could not respawn: Area not loaded at " + respawnPos);
//                            }
//                        }
//                ));
//                System.out.println("RespawningMonster died, scheduled respawn at: " + respawnPos);
//
//            } else {
//                System.out.println("RespawningMonster died, but birth position was not set. Cannot respawn.");
//                // 如果没有出生点，就按正常怪物死亡处理（它已经被 super.die() 标记为死亡）
//            }
//        }
//        // 客户端不需要处理复活逻辑
//    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        // 将出生点保存到 NBT
        getSpawnPosition().ifPresent(pos -> {
            compound.put("BirthPoint", NbtUtils.writeBlockPos(pos));
            System.out.println("Saving BirthPoint to NBT: " + pos);
        });
        // 如果需要跨维度，保存维度: compound.putString("BirthDimension", this.birthDimension.location().toString());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        // 从 NBT 读取出生点
        if (compound.contains("BirthPoint", CompoundTag.TAG_COMPOUND)) {
            BlockPos loadedPos = NbtUtils.readBlockPos(compound.getCompound("BirthPoint"));
            // 从 NBT 加载后，立即设置到内存和同步数据中
            // 注意：此时 level 可能还未完全准备好，但 BlockPos 本身可以存储
            // 在 tick 或 finalizeSpawn 中会确保 level 可用
            this.spawnPosition = loadedPos;
            this.entityData.set(DATA_SPAWN_POSITION, Optional.of(loadedPos));
            System.out.println("Loaded BirthPoint from NBT: " + loadedPos);
        } else {
            System.out.println("No BirthPoint found in NBT.");
            // 如果 NBT 没有记录，我们依赖 finalizeSpawn 或 tick 来设置初始值
            this.entityData.set(DATA_SPAWN_POSITION, Optional.empty());
        }
        // 读取维度: if (compound.contains("BirthDimension")) ...
    }

    /**
     * 设置此怪物的出生点（服务器端调用）。
     * @param pos 新的出生点位置。
     * @param level 对应的世界。
     */
    public void setSpawnPosition(BlockPos pos, Level level) {
        if (!level.isClientSide()) {
            this.spawnPosition = pos.immutable(); // 存储不可变副本
            // this.birthDimension = level.dimension(); // 如果需要跨维度，也记录维度
            this.entityData.set(DATA_SPAWN_POSITION, Optional.of(this.spawnPosition)); // 更新同步数据
            System.out.println("RespawningMonster birth point set to: " + pos);
        }
    }

    /**
     * 获取当前记录的出生点位置（主要从同步数据获取）。
     * @return Optional 包含出生点 BlockPos，如果未设置则为空。
     */
    public Optional<BlockPos> getSpawnPosition() {
        // 优先从同步数据获取，因为这是最可靠的持久化来源
        Optional<BlockPos> posFromData = this.entityData.get(DATA_SPAWN_POSITION);
        // 如果内存中的 birthPoint 与同步数据不一致（理论上不应发生，除非加载时），以同步数据为准
        if (posFromData.isPresent() && !posFromData.get().equals(this.spawnPosition)) {
            this.spawnPosition = posFromData.get();
        }
        return posFromData;
    }

    public void setIsReviving(boolean isReviving) {
        this.isReviving = isReviving;
        System.out.println("RespawningMonster isReviving set to: " + isReviving);
    }
}
