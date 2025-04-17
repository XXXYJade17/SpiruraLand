package com.xxxyjade17.spiruraland.Attributes.Attribute;

import com.xxxyjade17.spiruraland.Config.AttributesConfig;
import com.xxxyjade17.spiruraland.SpiruraLand;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class Defence {
    private static final AttributesConfig attributesConfig = AttributesConfig.getInstance();
    private static final double DEFAULT_VALUE = attributesConfig.getDefaultAttributeValue("defence");
    private static final double MAX_VALUE = attributesConfig.getMaxAttributeValue("defence");
    private static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Registries.ATTRIBUTE, SpiruraLand.MODID);
    public static final Supplier<Attribute> DEFENCE =
            ATTRIBUTES.register("defence",
                    ()->new RangedAttribute("spiruraattribute.defence", DEFAULT_VALUE, DEFAULT_VALUE, MAX_VALUE).setSyncable(true));

    public static void register(IEventBus bus) {
        ATTRIBUTES.register(bus);
    }
}
