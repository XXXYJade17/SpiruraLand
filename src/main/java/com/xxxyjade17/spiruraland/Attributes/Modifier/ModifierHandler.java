package com.xxxyjade17.spiruraland.Attributes.Modifier;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

public class ModifierHandler {
    private ModifierHandler(){

    }

    public static AttributeModifier createAdditionModifier(UUID uuid, String modifierName, double amount){
        return new AttributeModifier(uuid,modifierName,amount,AttributeModifier.Operation.ADDITION);
    }

    public static AttributeModifier createMultiplyBaseModifier(UUID uuid, String modifierName, double amount){
        return new AttributeModifier(uuid,modifierName,amount,AttributeModifier.Operation.MULTIPLY_BASE);
    }

    public static AttributeModifier createMultiplyTotalModifier(UUID uuid, String modifierName, double amount){
        return new AttributeModifier(uuid,modifierName,amount,AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
}
