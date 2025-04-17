package com.xxxyjade17.spiruraland.Spirura.Capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

public class SpiruraProvider implements ICapabilityProvider<Player, Void, Spirura>, INBTSerializable<CompoundTag> {
    private Spirura spirura;

    private Spirura getSpirura(){
        if(spirura==null){
            spirura = new Spirura();
        }
        return spirura;
    }

    @Override
    public @Nullable Spirura getCapability(Player o, Void unused) {
        return getSpirura();
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        getSpirura().saveData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag compoundTag) {
        getSpirura().loadData(compoundTag);
    }
}
