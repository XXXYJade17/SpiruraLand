package com.xxxyjade17.spiruraland.CreateiveTab;

import com.xxxyjade17.spiruraland.Monsters.Registry.EggsRegistry;
import com.xxxyjade17.spiruraland.SpiruraLand;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MonsterTab {
    public static final DeferredRegister<CreativeModeTab> MONSTER =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SpiruraLand.MODID);

    public static final ResourceKey<CreativeModeTab> MONSTER_TAB_KEY = ResourceKey.create(Registries.CREATIVE_MODE_TAB, new ResourceLocation("armor_tab"));

    public static final Supplier<CreativeModeTab> ARMOR_TAB  =
            MONSTER.register("monster_tab",
                    () -> CreativeModeTab.builder()
//                            .withTabsBefore(CreativeModeTabs.COMBAT)        //将自定义标签页插入原版"战斗"标签页之前
                            .title(Component.translatable("spirura.armor_tab"))        //设置标签名
                            .icon(() -> new ItemStack(EggsRegistry.FIRST_MONSTER_SPAWN_EGG.get()))     //设置物品栏图标
                            .displayItems((pParameters, pOutput) -> {
                                pOutput.accept(EggsRegistry.FIRST_MONSTER_SPAWN_EGG.get());      //添加物品到物品栏
                            })
                            .build());

    public static void register(IEventBus bus) {
        MONSTER.register(bus);
    }
}
