package com.xxxyjade17.spiruraland.Spirura.Handler;

import com.xxxyjade17.spiruraland.Spirura.Capability.Spirura;
import com.xxxyjade17.spiruraland.SpiruraLand;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.EntityCapability;

public class CapabilityHandler {
    public static final EntityCapability<Spirura, Void> SPIRURA_HANDLER =
            EntityCapability.createVoid(new ResourceLocation(SpiruraLand.MODID, "spirura_handler"),
                    Spirura.class);
}
