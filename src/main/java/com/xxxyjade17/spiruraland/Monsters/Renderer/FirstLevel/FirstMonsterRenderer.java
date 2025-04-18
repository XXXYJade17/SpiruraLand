package com.xxxyjade17.spiruraland.Monsters.Renderer.FirstLevel;

import com.xxxyjade17.spiruraland.Monsters.Model.FirstLevel.FirstMonsterModel;
import com.xxxyjade17.spiruraland.Monsters.Monster.FirstLevel.FirstMonster;
import com.xxxyjade17.spiruraland.SpiruraLand;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class FirstMonsterRenderer extends MobRenderer<FirstMonster, FirstMonsterModel> {
    public FirstMonsterRenderer(EntityRendererProvider.Context context) {
        super(context, new FirstMonsterModel(context.bakeLayer(FirstMonsterModel.LAYER_LOCATION)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(FirstMonster entity) {
        return new ResourceLocation(SpiruraLand.MODID,"textures/entity/first_monster.png");
    }
}
