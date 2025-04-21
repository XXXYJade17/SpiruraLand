package com.xxxyjade17.spiruraland.Monsters.Monster.Interface;

import net.minecraft.core.BlockPos;

import java.util.Optional;

public interface ISpiruraMonster {
    Boolean isReviving = false;
    BlockPos spawnPosition = BlockPos.ZERO;
    void setSpawnPosition(BlockPos pos);
    Optional<BlockPos> getSpawnPosition();
    void setIsReviving(boolean isReviving);
    Boolean getIsReviving();
}
