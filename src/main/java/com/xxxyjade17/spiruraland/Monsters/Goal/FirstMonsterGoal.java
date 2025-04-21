package com.xxxyjade17.spiruraland.Monsters.Goal;

import com.xxxyjade17.spiruraland.Monsters.Monster.Interface.ISpiruraMonster;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class FirstMonsterGoal extends Goal {

    private Monster monster; // 使用 Mob 而不是具体的 Monster，更通用
    private final double speed = 1D; // 回家时的移动速度
    private final double maxDistance = 15*15; // 离家最大距离的平方（效率更高）
    @Nullable
    private BlockPos spawnPosition; // 家的位置
    private final PathNavigation navigation;

    public FirstMonsterGoal(Monster monster) {
        this.monster = monster;
        this.navigation = monster.getNavigation();
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        System.out.println("FirstMonsterGoal use successfully!");
    }

    // ----- 获取家位置的逻辑 -----
    // 在 Goal 启动前或 canUse 检查时，动态获取家的位置
    private boolean tryAndSetHomePos() {
        // 这里假设你的 Mob 有一个 getSpawnPosition() 方法返回 Optional<BlockPos>
        // 如果你的 Mob 类名不是 FirstMonster，或者方法名不同，需要相应修改
        if (this.monster instanceof ISpiruraMonster spiruraMonster) {
            this.spawnPosition = spiruraMonster.getSpawnPosition().orElse(null);
            return this.spawnPosition != null;
        }
        // 如果不是你的特定怪物，或者它没有 getSpawnPosition 方法，就返回 false
        return false;
    }
    // ---------------------------

    /**
     * 决定 Goal 是否可以开始执行。
     */
    @Override
    public boolean canUse() {
        // 必须先能获取到家
        if (!this.tryAndSetHomePos()) {
            return false;
        }

        // 计算当前位置离家的距离平方
        Vec3 currentPosition = this.monster.position();
        double distSqr = currentPosition.distanceToSqr(Vec3.atCenterOf(this.spawnPosition));
        //System.out.println("ReturnHomeGoal: Target found. Distance^2 from home = " + distSqr);


        // 如果距离大于设定的最大距离，则启动回家 Goal
        if (distSqr > this.maxDistance) {
            System.out.println("ReturnHomeGoal: Too far from home! canUse = true. Distance^2: ");
            return true;
        } else {
            // System.out.println("ReturnHomeGoal: Close enough to home, canUse = false");
            return false;
        }
    }

    /**
     * 决定 Goal 是否可以继续执行。
     */
    @Override
    public boolean canContinueToUse() {
        System.out.println("canContinueToUse use successfully!");
        // 如果导航未完成，并且距离家还比较远（大于停止距离），就继续执行
        if (!this.navigation.isDone()) {
            if (this.spawnPosition == null) return false; // 防止中途 homePos 丢失
            double distSqr = this.monster.position().distanceToSqr(Vec3.atCenterOf(this.spawnPosition));
            //System.out.println("ReturnHomeGoal: Continuing. Navigation not done, Dist^2 = " + distSqr);
            return distSqr > 1;
        } else {
            // System.out.println("ReturnHomeGoal: Stopping. Navigation done.");
            return false; // 导航完成就停止
        }
    }

    /**
     * Goal 开始执行。
     */
    @Override
    public void start() {
        System.out.println("ReturnHomeGoal: Starting to return home to " + this.spawnPosition);
        // 清除当前的攻击目标，强制停止攻击行为
        this.monster.setTarget(null);
        // 开始导航回家
        this.navigation.moveTo(this.spawnPosition.getX() + 0.5, this.spawnPosition.getY(), this.spawnPosition.getZ() + 0.5, this.speed);
    }

    /**
     * Goal 停止执行。
     */
    @Override
    public void stop() {
        System.out.println("ReturnHomeGoal: Stopped returning home.");
        // 停止导航
        this.navigation.stop();
    }

    /**
     * Goal 每 tick 执行。
     */
    @Override
    public void tick() {
        System.out.println("tick use successfully!");
        // 通常情况下，导航系统会自动处理移动，这里可以留空
        // 如果需要，可以在这里检查路径是否仍然有效，或者目标是否被阻挡等
        if (this.spawnPosition != null && this.monster.position().distanceToSqr(Vec3.atCenterOf(this.spawnPosition)) <= 1) {
            // 已经回到家附近了，但是 canContinueToUse 可能下一 tick 才变 false
            // 可以在这里立即停止导航（可选）
            if (!this.navigation.isDone()) {
                // System.out.println("ReturnHomeGoal: Reached home area during tick, stopping navigation early.");
                // this.navigation.stop(); // 可能导致抖动，canContinueToUse 控制即可
            }
        } else if (this.spawnPosition != null && this.navigation.isDone()) {
            // 走到一半路径没了？尝试重新寻路
            System.out.println("ReturnHomeGoal: Navigation finished unexpectedly? Retrying path to " + this.spawnPosition);
            this.navigation.moveTo(this.spawnPosition.getX() + 0.5, this.spawnPosition.getY(), this.spawnPosition.getZ() + 0.5, this.speed);
        } else if (this.spawnPosition == null && !this.tryAndSetHomePos()){
            // 罕见情况：中途 homePos 获取失败
            System.out.println("ReturnHomeGoal: Home pos lost during tick, cannot continue.");
            // 这里 Goal 应该会在下个 canContinueToUse 检查时停止
        }

    }
}