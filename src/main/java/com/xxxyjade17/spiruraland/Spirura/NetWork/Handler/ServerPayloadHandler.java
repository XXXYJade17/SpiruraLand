package com.xxxyjade17.spiruraland.Spirura.NetWork.Handler;

import com.xxxyjade17.spiruraland.Spirura.Data.Client.SpiruraData;
import com.xxxyjade17.spiruraland.Spirura.Handler.CapabilityHandler;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

import java.util.Optional;

public class ServerPayloadHandler {
    private static ServerPayloadHandler INSTANCE;

    public static ServerPayloadHandler getINSTANCE() {
        if(INSTANCE==null){
            INSTANCE=new ServerPayloadHandler();
        }
        return INSTANCE;
    }

    public void handleSpiruraData(SpiruraData data, PlayPayloadContext context) {
        context.player().ifPresent(player -> {
            if (player instanceof ServerPlayer serverPlayer) {
                Optional.ofNullable(player.getCapability(CapabilityHandler.SPIRURA_HANDLER))
                        .ifPresent(spirura -> {
                            PacketDistributor.PLAYER.with(serverPlayer)
                                    .send(new SpiruraData(
                                            spirura.getRank(),
                                            spirura.getLevel(),
                                            spirura.getExperience(),
                                            spirura.hasShackle(),
                                            spirura.getBreakRate(),
                                            spirura.getRateIncrease()
                                    ));
                        });
            }
        });
    }
}
