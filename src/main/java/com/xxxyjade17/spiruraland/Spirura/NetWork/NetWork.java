package com.xxxyjade17.spiruraland.Spirura.NetWork;

import com.xxxyjade17.spiruraland.Spirura.Data.Client.SpiruraData;
import com.xxxyjade17.spiruraland.Spirura.NetWork.Handler.ClientPayloadHandler;
import com.xxxyjade17.spiruraland.Spirura.NetWork.Handler.ServerPayloadHandler;
import com.xxxyjade17.spiruraland.SpiruraLand;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

@Mod.EventBusSubscriber(modid = SpiruraLand.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NetWork {
    @SubscribeEvent
    public static void registerPackets(RegisterPayloadHandlerEvent event) {
        IPayloadRegistrar registrar = event.registrar(SpiruraLand.MODID);

        registrar.play(SpiruraData.ID, SpiruraData::new, handler ->
                handler.client(ClientPayloadHandler.getINSTANCE()::handleSpiruraData)
                        .server(ServerPayloadHandler.getINSTANCE()::handleSpiruraData));
    }
}