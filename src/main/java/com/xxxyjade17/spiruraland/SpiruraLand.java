package com.xxxyjade17.spiruraland;

import com.mojang.logging.LogUtils;
import com.xxxyjade17.spiruraland.Attributes.Registry.AttributesRegistries;
import com.xxxyjade17.spiruraland.CreateiveTab.MonsterTab;
import com.xxxyjade17.spiruraland.Monsters.Registry.EggsRegistry;
import com.xxxyjade17.spiruraland.Monsters.Registry.MonstersRegistries;
import com.xxxyjade17.spiruraland.Spirura.Command.AdminCommand;
import com.xxxyjade17.spiruraland.Spirura.Command.PlayerCommand;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

@Mod(SpiruraLand.MODID)
public class SpiruraLand {
    public static final String MODID = "spiruraland";
    private static final Logger LOGGER = LogUtils.getLogger();

    public SpiruraLand(IEventBus bus) {
        NeoForge.EVENT_BUS.addListener((ServerStartingEvent event) ->
                PlayerCommand.getINSTANCE().registerCommand(event.getServer().getCommands().getDispatcher())
        );
        NeoForge.EVENT_BUS.addListener((ServerStartingEvent event) ->
                AdminCommand.getINSTANCE().registerCommand(event.getServer().getCommands().getDispatcher())
        );
        AttributesRegistries.register(bus);
        MonstersRegistries.register(bus);
        EggsRegistry.register(bus);
        MonsterTab.register(bus);
    }
}
