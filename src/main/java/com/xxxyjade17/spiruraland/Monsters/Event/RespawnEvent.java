package com.xxxyjade17.spiruraland.Monsters.Event;

import com.xxxyjade17.spiruraland.Monsters.Monster.FirstLevel.FirstMonster;
import com.xxxyjade17.spiruraland.SpiruraLand;
import net.minecraft.core.BlockPos;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

@Mod.EventBusSubscriber(modid = SpiruraLand.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RespawnEvent {
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();

        // 仅在服务器端处理
        if (entity.level().isClientSide()) {
            return;
        }

        // 检查死亡的实体是否是我们的 FirstMonster
        if (entity instanceof FirstMonster firstMonster) {

            Optional<BlockPos> optionalBirthPos = firstMonster.getSpawnPosition();

            // 检查是否有有效的出生点
            if (optionalBirthPos.isPresent()) {
                BlockPos respawnPos = optionalBirthPos.get();
                ServerLevel serverLevel = (ServerLevel) firstMonster.level();

                // --- 核心修改 ---
                // 1. 取消原版的死亡事件。这将阻止实体被移除和默认的掉落物生成。
                event.setCanceled(true);
                System.out.println("[RespawningMonster] Cancelled death event for: " + firstMonster.getId());

                // 2. 让实体进入“假死”状态
                // 注意：此时实体的 health 可能已经是 0 或更低
                // 我们需要确保它不被其他系统清除，并且在视觉上“消失”
                // 设置无敌和隐形可以防止交互和视觉显示
                firstMonster.setInvulnerable(true); // 防止后续伤害或交互
                firstMonster.setInvisible(true);    // 使其隐形
                firstMonster.setNoAi(true);         // 停止 AI
                firstMonster.setTarget(null);       // 清除目标
                firstMonster.getNavigation().stop();// 停止移动

                // (可选) 可以考虑将其短暂传送到地下或虚空，以确保完全不被看到或交互
                double x = firstMonster.getX(), y = firstMonster.getY(), z = firstMonster.getZ();
                firstMonster.teleportTo(x, -100, z); // 传送到地下

                // 3. 安排复活任务 (使用同一个实体实例)
                System.out.println("[RespawningMonster] Scheduling revival task for: " + firstMonster.getId());
                        serverLevel.getServer().tell(new TickTask(
                        serverLevel.getServer().getTickCount() + (3000 * 20), // 30秒后
                        () -> reviveMonster(firstMonster, respawnPos, serverLevel /*, x, y, z*/) // 将需要的信息传入
                ));

            } else {
                System.out.println("[RespawningMonster] No birth point found for " + firstMonster.getId() + ". Allowing normal death.");
                // 没有出生点，不取消事件，让其正常死亡并消失
            }
        }
    }

    // 用于执行复活逻辑的辅助方法
    private static void reviveMonster(FirstMonster monster, BlockPos respawnPos, ServerLevel serverLevel /*, double originalX, double originalY, double originalZ*/) {
        // 再次检查实体是否仍然存在且世界是否加载 (防止中途实体被意外移除或区块卸载)
        // 注意：检查 isRemoved() 而不是 isAlive()，因为它的 health 可能仍然是 0
        if (!monster.isRemoved() && monster.level() == serverLevel && serverLevel.isAreaLoaded(respawnPos, 1)) {

            System.out.println("[RespawningMonster] Executing revival for: " + monster.getId());

            // 恢复实体的状态
            monster.setHealth(monster.getMaxHealth()); // 完全恢复生命值
            monster.setInvisible(false);              // 取消隐形
            monster.setInvulnerable(false);             // 取消无敌
            monster.setNoAi(false);                   // 恢复 AI

            // 清理可能残留的状态
            monster.clearFire();                       // 清除火焰
            monster.setDeltaMovement(Vec3.ZERO);       // 重置速度
            // monster.removeAllEffects();             // (可选) 移除所有药水效果

            // 传送回复活点
            monster.teleportTo(respawnPos.getX() + 0.5D, respawnPos.getY(), respawnPos.getZ() + 0.5D);

            // 确保它仍然持久存在（如果需要）
//            monster.setPersistenceRequired(true); // 也许不需要，因为removeWhenFarAway=false


            System.out.println("[RespawningMonster] Revived: " + monster.getId() + " at " + respawnPos);

        } else {
            System.out.println("[RespawningMonster] Failed to revive: " + monster.getId() + ". Entity removed or area unloaded. Pos: " + respawnPos);
            // 如果实体因某种原因消失了或无法复活，确保它被彻底移除（以防万一）
            if (!monster.isRemoved() && monster.level() == serverLevel) {
                monster.discard(); // 或者 remove(RemovalReason.DISCARDED)
            }
        }
    }
}