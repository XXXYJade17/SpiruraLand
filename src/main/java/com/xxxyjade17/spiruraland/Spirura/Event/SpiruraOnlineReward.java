package com.xxxyjade17.spiruraland.Spirura.Event;

import com.xxxyjade17.spiruraland.Config.Config;
import com.xxxyjade17.spiruraland.Config.SpiruraConfig;
import com.xxxyjade17.spiruraland.Config.Translation;
import com.xxxyjade17.spiruraland.Spirura.Capability.Spirura;
import com.xxxyjade17.spiruraland.Spirura.Data.Client.SpiruraData;
import com.xxxyjade17.spiruraland.Spirura.Handler.CapabilityHandler;
import com.xxxyjade17.spiruraland.SpiruraLand;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.LogicalSide;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Mod.EventBusSubscriber(modid = SpiruraLand.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE,value = Dist.DEDICATED_SERVER)
public class SpiruraOnlineReward {
    private static final Config config = Config.getInstance();
    private static final SpiruraConfig spiruraConfig = SpiruraConfig.getInstance();
    private static final Translation translation = Translation.getInstance();
    private static final int TICKS_PER_INCREASE = (int) config.getConfigInfo("ticks_per_increase");
    private static final Map<ServerPlayer, Integer> counters = new HashMap<>();

    @SubscribeEvent
    public static void spiruraOnlineReward(TickEvent.PlayerTickEvent event) {
        if(event.side == LogicalSide.SERVER) {
            if (event.player instanceof ServerPlayer player) {
                Optional<Spirura> optionalSpirura=
                        Optional.ofNullable(player.getCapability(CapabilityHandler.SPIRURA_HANDLER));
                optionalSpirura.ifPresent(spirura -> {
                    int rank = spirura.getRank();
                    int level = spirura.getLevel();
                   if(!spirura.hasShackle()){
                        int ticks=counters.getOrDefault(player,0);
                        ticks++;
                        if(ticks>=TICKS_PER_INCREASE){
                            if(!spirura.hasShackle()){
                                int increasedExperience= spiruraConfig.getIncreasedExperience(rank,level);
                                spirura.addExperience(increasedExperience);
                                PacketDistributor.PLAYER.with(player)
                                        .send(new SpiruraData(
                                                spirura.getRank(),
                                                spirura.getLevel(),
                                                spirura.getExperience(),
                                                spirura.hasShackle(),
                                                spirura.getBreakRate(),
                                                spirura.getRateIncrease()));
                                player.sendSystemMessage(translation.getMessage("online.reward.experience",increasedExperience));
                            }
                            counters.put(player,0);
                        }else{
                            counters.put(player,ticks);
                        }
                   }
                });
            }
        }
    }
}
