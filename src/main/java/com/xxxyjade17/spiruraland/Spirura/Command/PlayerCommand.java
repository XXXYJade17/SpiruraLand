package com.xxxyjade17.spiruraland.Spirura.Command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.logging.LogUtils;
import com.xxxyjade17.spiruraland.Config.SpiruraConfig;
import com.xxxyjade17.spiruraland.Config.Translation;
import com.xxxyjade17.spiruraland.Spirura.Capability.Spirura;
import com.xxxyjade17.spiruraland.Spirura.Handler.CapabilityHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;

import java.util.Optional;

public class PlayerCommand {
    private static PlayerCommand INSTANCE;
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final SpiruraConfig spiruraConfig= SpiruraConfig.getInstance();
    private static final Translation translations = Translation.getInstance();

    public static PlayerCommand getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerCommand();
        }
        return INSTANCE;
    }

    public void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher){
        playerGetSpirura(dispatcher);
        playerBreakShackle(dispatcher);
    }

    private void playerGetSpirura(CommandDispatcher<CommandSourceStack> dispatcher){
        LiteralArgumentBuilder<CommandSourceStack> playerGetSpirura =
                Commands.literal("SpiruraCore")
                        .requires(source -> source.hasPermission(0))
                        .then(Commands.literal("get")
                                .executes(this::getSpirura));

        LiteralArgumentBuilder<CommandSourceStack> pGS =
                Commands.literal("SC")
                        .requires(source -> source.hasPermission(0))
                        .then(Commands.literal("get")
                                .executes(this::getSpirura));

        dispatcher.register(playerGetSpirura);
        dispatcher.register(pGS);
    }

    private int getSpirura(CommandContext<CommandSourceStack> context){
        try {
            ServerPlayer player = context.getSource().getPlayerOrException();
            Optional<Spirura> optionalSpirura =
                    Optional.ofNullable(player.getCapability(CapabilityHandler.SPIRURA_HANDLER));
            optionalSpirura.ifPresent(spirura -> {
                int rank = spirura.getRank();
                int level = spirura.getLevel();
                int experience = spirura.getExperience();
                String rankName= spiruraConfig.getRankName(rank);
                String levelName= spiruraConfig.getLevelName(level);
                context.getSource().sendSuccess(() ->
                        translations.getMessage("command.player.get",rankName,levelName,experience),false);
            });
            return 1;
        } catch (Exception e) {
            LOGGER.error("Failed to get Player's Spirura:",e);
            return 0;
        }
    }

    private void playerBreakShackle(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> playerBreakShackle =
                Commands.literal("SpiruraCore")
                        .requires(source -> source.hasPermission(0))
                        .then(Commands.literal("break")
                                .executes(this::breakShackle));

        LiteralArgumentBuilder<CommandSourceStack> pBS =
                Commands.literal("SC")
                        .requires(source -> source.hasPermission(0))
                        .then(Commands.literal("break")
                                .executes(this::breakShackle));

        dispatcher.register(playerBreakShackle);
        dispatcher.register(pBS);
    }

    private int breakShackle(CommandContext<CommandSourceStack> context) {
        try {
            ServerPlayer player = context.getSource().getPlayerOrException();
            Optional<Spirura> optionalSpirura =
                    Optional.ofNullable(player.getCapability(CapabilityHandler.SPIRURA_HANDLER));
            optionalSpirura.ifPresent(spirura -> {
                if(spirura.hasShackle()){
                    spirura.breakShackle();
                    if(spirura.hasShackle()){
                        context.getSource().sendSuccess(() ->
                                translations.getMessage("command.player.shackle.break.success"),false);
                    }else{
                        context.getSource().sendFailure(translations.getMessage("command.player.shackle.break.fail"));
                    }
                }else{
                    context.getSource().sendFailure(translations.getMessage("command.player.shackle.none"));
                }
            });
            return 1;
        } catch (CommandSyntaxException e) {
            LOGGER.error("Failed to break through shackle:",e);
            return 0;
        }
    }
}
