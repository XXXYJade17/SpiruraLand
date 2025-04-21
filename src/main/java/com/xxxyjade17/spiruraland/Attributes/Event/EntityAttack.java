package com.xxxyjade17.spiruraland.Attributes.Event;

import com.xxxyjade17.spiruraland.Attributes.Attribute.*;
import com.xxxyjade17.spiruraland.Config.AttributesConfig;
import com.xxxyjade17.spiruraland.Config.Config;
import com.xxxyjade17.spiruraland.Monsters.Monster.Interface.ISpiruraMonster;
import com.xxxyjade17.spiruraland.SpiruraLand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.EntityEvent;
import net.neoforged.neoforge.event.entity.living.LivingAttackEvent;
import net.neoforged.neoforge.event.entity.living.LivingHurtEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;

import java.util.Random;

@Mod.EventBusSubscriber(modid = SpiruraLand.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE,value = Dist.DEDICATED_SERVER)
public class EntityAttack {
    private static final Config config = Config.getInstance();
    private static final AttributesConfig attributesConfig = AttributesConfig.getInstance();
    private static final Random RANDOM = new Random();
    private static final double THRESHOLD_DEFENCE = (double) config.getConfigInfo("threshold_defence");

    @SubscribeEvent
    public static void onLivingAttack(LivingHurtEvent event) {
        double baseDamageValue = 0;
        double critChanceValue = 0;
        double critMultiplierValue = 0;
        double lifeStealChanceValue = 0;
        double lifeStealMultiplierValue = 0;
        double defenseValue = 0;
        double immunityValue = 0;

        DamageSource source = event.getSource();
        if ("mob".equals(source.getMsgId()) || "player".equals(source.getMsgId())) {
            if (source.getEntity() instanceof ServerPlayer player) {
                baseDamageValue = getPlayerAttributeValue(player, Attributes.ATTACK_DAMAGE);
                critChanceValue = getPlayerAttributeValue(player, CritChance.CRIT_CHANCE.get());
                critMultiplierValue = getPlayerAttributeValue(player, CritMultiplier.CRIT_MULTIPLIER.get());
                lifeStealChanceValue = getPlayerAttributeValue(player, LifeStealChance.LIFE_STEAL_CHANCE.get());
                lifeStealMultiplierValue = getPlayerAttributeValue(player, LifeStealMultiplier.LIFE_STEAL_MULTIPLIER.get());
            } else if (source.getEntity() instanceof Monster monster) {
                baseDamageValue = monster.getAttribute(Attributes.ATTACK_DAMAGE) != null
                        ? monster.getAttribute(Attributes.ATTACK_DAMAGE).getValue()
                        : 0;
                critChanceValue = monster.getAttribute(CritChance.CRIT_CHANCE.get()) != null
                        ? monster.getAttribute(CritChance.CRIT_CHANCE.get()).getValue()
                        : 0;
                critMultiplierValue = monster.getAttribute(CritMultiplier.CRIT_MULTIPLIER.get()) != null
                        ? monster.getAttribute(CritMultiplier.CRIT_MULTIPLIER.get()).getValue()
                        : 0;
                lifeStealChanceValue = monster.getAttribute(LifeStealChance.LIFE_STEAL_CHANCE.get()) != null
                        ? monster.getAttribute(LifeStealChance.LIFE_STEAL_CHANCE.get()).getValue()
                        : 0;
                lifeStealMultiplierValue = monster.getAttribute(LifeStealMultiplier.LIFE_STEAL_MULTIPLIER.get()) != null
                        ? monster.getAttribute(LifeStealMultiplier.LIFE_STEAL_MULTIPLIER.get()).getValue()
                        : 0;

            }

            if (event.getEntity() instanceof ServerPlayer player) {
                defenseValue = getPlayerAttributeValue(player, Defence.DEFENCE.get());
                immunityValue = getPlayerAttributeValue(player, Immunity.IMMUNITY.get());
            } else if (event.getEntity() instanceof Monster monster) {
                defenseValue = monster.getAttribute(Defence.DEFENCE.get()) != null
                        ? monster.getAttribute(Defence.DEFENCE.get()).getValue()
                        : 0;
                immunityValue = monster.getAttribute(Immunity.IMMUNITY.get()) != null
                        ? monster.getAttribute(Immunity.IMMUNITY.get()).getValue()
                        : 0;
            }

            float finalAmount = calculateAmount(baseDamageValue, critChanceValue, critMultiplierValue, defenseValue, immunityValue);
            float finalLifeSteal = calculateLifeSteal(finalAmount, lifeStealChanceValue, lifeStealMultiplierValue);
            if (finalAmount != 0) {
                event.setAmount(finalAmount);
            }
            if (finalAmount != 0) {
                event.getEntity().heal(finalLifeSteal);
            }
        }
    }

    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        if(event.getEntity() instanceof ServerPlayer player){
            Entity target = event.getTarget();
            double attackRange = player.getAttributeValue(AttackRange.ATTACK_RANGE.get())!=0
                    ?player.getAttributeValue(AttackRange.ATTACK_RANGE.get())
                    :attributesConfig.getDefaultAttributeValue("attack_range");
            event.setCanceled(attackRange*attackRange<target.distanceToSqr(player));
        }
    }

    @SubscribeEvent
    public static void onAttackPlayer(LivingAttackEvent event){
        if(event.getEntity() instanceof ServerPlayer player && event.getSource().getEntity() instanceof Monster monster){
            if(monster.getAttribute(AttackRange.ATTACK_RANGE.get()) != null){
                int attackRange = (int) monster.getAttribute(AttackRange.ATTACK_RANGE.get()).getValue();
                event.setCanceled(attackRange*attackRange<monster.distanceToSqr(player));
            }
        }
    }

    private static double getPlayerAttributeValue(LivingEntity entity, Attribute attribute){
        return entity!=null && entity.getAttribute(attribute)!=null
                ? entity.getAttributeValue(attribute)
                : 0;
    }

    private static float calculateAmount(double damage, double critChance, double critMultiplier, double defence, double immunity){
        double finalAmount=damage;
        if(RANDOM.nextFloat()<critChance){
            finalAmount*=critMultiplier;
        }
        return (float) (finalAmount*(1-defence/(THRESHOLD_DEFENCE+defence))*(1-immunity));
    }

    private static float calculateLifeSteal(float finalAmount,double lifeStealChance,double lifeStealMultiplier){
        return RANDOM.nextFloat()<lifeStealChance? (float) (finalAmount * lifeStealMultiplier) :0;
    }
}
