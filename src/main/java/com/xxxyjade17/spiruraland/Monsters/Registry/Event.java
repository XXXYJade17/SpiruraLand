package com.xxxyjade17.spiruraland.Monsters.Registry;

import com.xxxyjade17.spiruraland.Monsters.Model.FirstLevel.FirstMonsterModel;
import com.xxxyjade17.spiruraland.Monsters.Monster.FirstLevel.FirstMonster;
import com.xxxyjade17.spiruraland.Monsters.Renderer.FirstLevel.FirstMonsterRenderer;
import com.xxxyjade17.spiruraland.SpiruraLand;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@Mod.EventBusSubscriber(modid = SpiruraLand.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Event {
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterLayerDefinitions evt) {
        evt.registerLayerDefinition(FirstMonsterModel.LAYER_LOCATION, FirstMonsterModel::createBodyLayer);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void applyRenderers(FMLClientSetupEvent event){
        event.enqueueWork(()->{
            EntityRenderers.register(MonstersRegistries.FIRST_MONSTER.get(), FirstMonsterRenderer::new);
        });
    }

    @SubscribeEvent
    public static void applyAttributes(EntityAttributeCreationEvent event) {
        event.put(MonstersRegistries.FIRST_MONSTER.get(), FirstMonster.createAttributes().build());
    }
}
