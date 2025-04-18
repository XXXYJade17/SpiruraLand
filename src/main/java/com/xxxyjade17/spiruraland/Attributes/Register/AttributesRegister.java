package com.xxxyjade17.spiruraland.Attributes.Register;

import com.xxxyjade17.spiruraland.Attributes.Attribute.*;
import net.neoforged.bus.api.IEventBus;

public class AttributesRegister {
    public static void register(IEventBus bus){
        AttackRange.register(bus);
        CritChance.register(bus);
        CritMultiplier.register(bus);
        Defence.register(bus);
        Immunity.register(bus);
        LifeStealChance.register(bus);
        LifeStealMultiplier.register(bus);
    }
}
