package com.xxxyjade17.spiruraland.Spirura.Capability;

import net.minecraft.nbt.CompoundTag;

public interface ISpirura {
    int getRank();
    void setRank(int rank);
    int getLevel();
    void setLevel(int level);
    void addExperience(int experience);
    int getExperience();
    void setExperience(int experience);

    void breakShackle();
    void setShackle(boolean shackle);
    boolean hasShackle();
    float getBreakRate();
    void setBreakRate(float breakRate);
    float getRateIncrease();
    void setRateIncrease(float rateIncrease);

    void saveData(CompoundTag nbt);
    void loadData(CompoundTag nbt);
}
