package com.xxxyjade17.spiruraland.Spirura.Command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.logging.LogUtils;
import com.xxxyjade17.spiruraland.Config.SpiruraConfig;
import com.xxxyjade17.spiruraland.Config.Translation;
import com.xxxyjade17.spiruraland.Spirura.Capability.Spirura;
import com.xxxyjade17.spiruraland.Spirura.Data.Server.SpiruraSavedData;
import com.xxxyjade17.spiruraland.Spirura.Handler.CapabilityHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.UUID;

public class AdminCommand {
    private static AdminCommand INSTANCE;
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final SpiruraConfig spiruraConfig = SpiruraConfig.getInstance();
    private static final Translation translations = Translation.getInstance();

    public static AdminCommand getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new AdminCommand();
        }
        return INSTANCE;
    }

    public void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        this.adminGetSpirura(dispatcher);
        this.adminBreakShackle(dispatcher);
        this.adminSetSpirura(dispatcher);
    }

    private void adminGetSpirura(CommandDispatcher<CommandSourceStack> dispatcher){
        LiteralArgumentBuilder<CommandSourceStack> adminGetSpirura =
                Commands.literal("SpiruraCore")
                        .requires(source -> source.hasPermission(4))
                        .then(Commands.literal("get")
                                .then(Commands.argument("player", StringArgumentType.word())
                                        .executes(this::getSpirura)));

        LiteralArgumentBuilder<CommandSourceStack> aGS =
                Commands.literal("SC")
                        .requires(source -> source.hasPermission(4))
                        .then(Commands.literal("get")
                                .then(Commands.argument("player", StringArgumentType.word())
                                        .executes(this::getSpirura)));

        dispatcher.register(adminGetSpirura);
        dispatcher.register(aGS);
    }

    private int getSpirura(CommandContext<CommandSourceStack> context) {
        String name = StringArgumentType.getString(context,"player");
        UUID uuid = SpiruraSavedData.getUUID(name);
        int newRank = IntegerArgumentType.getInteger(context,"new");
        if(uuid!=null) {
            ServerPlayer player = context.getSource().getServer().getPlayerList().getPlayer(uuid);
            Optional<Spirura> optionalSpirura =
                    Optional.ofNullable(player.getCapability(CapabilityHandler.SPIRURA_HANDLER));
            optionalSpirura.ifPresent(spirura -> {
                int rank = spirura.getRank();
                int level=spirura.getLevel();
                int experience=spirura.getExperience();
                context.getSource().sendSuccess(()->
                        translations.getMessage("command.admin.get",name,rank,level,experience),false);
            });
            return 1;
        } else {
            return 0;
        }
    }

    private void adminBreakShackle(CommandDispatcher<CommandSourceStack> dispatcher){
        LiteralArgumentBuilder<CommandSourceStack> adminBreakShackle =
                Commands.literal("SpiruraCore")
                        .requires(source -> source.hasPermission(4))
                        .then(Commands.argument("player", StringArgumentType.word())
                                .then(Commands.literal("break")
                                        .executes(this::breakShackle)));

        LiteralArgumentBuilder<CommandSourceStack> aBS =
                Commands.literal("SC")
                        .requires(source -> source.hasPermission(4))
                        .then(Commands.argument("player", StringArgumentType.word())
                                .then(Commands.literal("break")
                                        .executes(this::breakShackle)));

        dispatcher.register(adminBreakShackle);
        dispatcher.register(aBS);
    }

    private int breakShackle(CommandContext<CommandSourceStack> context){
        String name = StringArgumentType.getString(context,"player");
        UUID uuid = SpiruraSavedData.getUUID(name);
        if(uuid!=null) {
            ServerPlayer player = context.getSource().getServer().getPlayerList().getPlayer(uuid);
            Optional<Spirura> optionalSpirura =
                    Optional.ofNullable(player.getCapability(CapabilityHandler.SPIRURA_HANDLER));
            optionalSpirura.ifPresent(spirura -> {
                if(!spirura.hasShackle()){
                    context.getSource().sendFailure(translations.getMessage("command.admin.shackle.none"));
                }else{
                    spirura.setBreakRate(1);
                    spirura.breakShackle();
                    context.getSource().sendSuccess(() ->
                            translations.getMessage("command.admin.shackle.break",name),false);
                }
            });
            return 1;
        } else {
            return 0;
        }
    }

    private void adminSetSpirura(CommandDispatcher<CommandSourceStack> dispatcher){
        LiteralArgumentBuilder<CommandSourceStack> adminSetSpirura =
                Commands.literal("SpiruraCore")
                        .requires(source -> source.hasPermission(4))
                        .then(Commands.argument("player",StringArgumentType.word())
                        .then(Commands.literal("set")
                                .then(Commands.literal("rank")
                                        .then(Commands.argument("new", IntegerArgumentType.integer())
                                                .executes(this::setRank)))
                                .then(Commands.literal("level")
                                        .then(Commands.argument("new",IntegerArgumentType.integer())
                                                .executes(this::setLevel)))
                                .then(Commands.literal("experience")
                                        .then(Commands.argument("new",IntegerArgumentType.integer())
                                                .executes(this::setExperience))))
                        .then(Commands.literal("add")
                                .then(Commands.literal("experience")
                                        .then(Commands.argument("num",IntegerArgumentType.integer())
                                                .executes(this::addExperience)))));

        LiteralArgumentBuilder<CommandSourceStack> aSS =
                Commands.literal("SC")
                        .requires(source -> source.hasPermission(4))
                        .then(Commands.argument("player",StringArgumentType.word())
                                .then(Commands.literal("set")
                                        .then(Commands.literal("rank")
                                                .then(Commands.argument("new", IntegerArgumentType.integer())
                                                        .executes(this::setRank)))
                                        .then(Commands.literal("level")
                                                .then(Commands.argument("new",IntegerArgumentType.integer())
                                                        .executes(this::setLevel)))
                                        .then(Commands.literal("experience")
                                                .then(Commands.argument("new",IntegerArgumentType.integer())
                                                        .executes(this::setExperience))))
                                .then(Commands.literal("add")
                                        .then(Commands.literal("experience")
                                                .then(Commands.argument("num",IntegerArgumentType.integer())
                                                        .executes(this::addExperience)))));

        dispatcher.register(adminSetSpirura);
        dispatcher.register(aSS);
    }

    private int setRank(CommandContext<CommandSourceStack> context){
        String name = StringArgumentType.getString(context,"player");
        UUID uuid = SpiruraSavedData.getUUID(name);
        int newRank = IntegerArgumentType.getInteger(context,"new");
        if(uuid!=null) {
            ServerPlayer player = context.getSource().getServer().getPlayerList().getPlayer(uuid);
            Optional<Spirura> optionalSpirura =
                    Optional.ofNullable(player.getCapability(CapabilityHandler.SPIRURA_HANDLER));
            optionalSpirura.ifPresent(spirura -> {
                        if (spirura.hasShackle()) {
                            spirura.setShackle(false);
                            spirura.setBreakRate(0);
                            spirura.setRateIncrease(0);
                            spirura.setExperience(0);
                            spirura.setRank(newRank);
                            context.getSource().sendSuccess(() ->
                                    translations.getMessage("command.admin.set.rank",name,spiruraConfig.getRankName(newRank)), false);
                        }else{
                            spirura.setExperience(0);
                            spirura.setRank(newRank);
                            context.getSource().sendSuccess(() ->
                                    translations.getMessage("spirura.admin.set.rank",name,spiruraConfig.getRankName(newRank)), false);
                        }
                    });
            return 1;
        } else {
            return 0;
        }
    }

    private int setLevel(CommandContext<CommandSourceStack> context){
        String name = StringArgumentType.getString(context,"player");
        UUID uuid = SpiruraSavedData.getUUID(name);
        int newLevel = IntegerArgumentType.getInteger(context,"new");
        if(uuid!=null) {
            ServerPlayer player = context.getSource().getServer().getPlayerList().getPlayer(uuid);
            Optional<Spirura> optionalSpirura =
                    Optional.ofNullable(player.getCapability(CapabilityHandler.SPIRURA_HANDLER));
            optionalSpirura.ifPresent(spirura -> {
                if (spirura.hasShackle()) {
                    spirura.setShackle(false);
                    spirura.setBreakRate(0);
                    spirura.setRateIncrease(0);
                    spirura.setExperience(0);
                    spirura.setLevel(newLevel);
                    context.getSource().sendSuccess(() ->
                            translations.getMessage("command.admin.set.level",name,spiruraConfig.getLevelName(newLevel)), false);
                }else{
                    spirura.setExperience(0);
                    spirura.setLevel(newLevel);
                    context.getSource().sendSuccess(() ->
                            translations.getMessage("command.admin.set.level",name,spiruraConfig.getLevelName(newLevel)), false);
                }
            });
            return 1;
        } else {
            return 0;
        }
    }

    private int setExperience(CommandContext<CommandSourceStack> context){
        String name = StringArgumentType.getString(context,"player");
        UUID uuid = SpiruraSavedData.getUUID(name);
        int newExperience = IntegerArgumentType.getInteger(context,"new");
        if(uuid!=null) {
            ServerPlayer player = context.getSource().getServer().getPlayerList().getPlayer(uuid);
            Optional<Spirura> optionalSpirura =
                    Optional.ofNullable(player.getCapability(CapabilityHandler.SPIRURA_HANDLER));
            optionalSpirura.ifPresent(spirura -> {
                spirura.setExperience(newExperience);
                context.getSource().sendSuccess(() ->
                        translations.getMessage("command.admin.set.experience",name,newExperience), false);
            });
            return 1;
        } else {
            return 0;
        }
    }

    private int addExperience(CommandContext<CommandSourceStack> context){
        String name = StringArgumentType.getString(context,"player");
        UUID uuid = SpiruraSavedData.getUUID(name);
        int addExperience = IntegerArgumentType.getInteger(context,"num");
        if(uuid!=null) {
            ServerPlayer player = context.getSource().getServer().getPlayerList().getPlayer(uuid);
            Optional<Spirura> optionalSpirura =
                    Optional.ofNullable(player.getCapability(CapabilityHandler.SPIRURA_HANDLER));
            optionalSpirura.ifPresent(spirura -> {
                spirura.addExperience(addExperience);
                context.getSource().sendSuccess(() ->
                        translations.getMessage("command.admin.add.experience",name,addExperience), false);
            });
            return 1;
        } else {
            return 0;
        }
    }
}
