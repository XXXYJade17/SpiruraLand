package com.xxxyjade17.spiruraland.Monsters.Command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.logging.LogUtils;
import com.xxxyjade17.spiruraland.Monsters.Monster.Interface.ISpiruraMonster;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import org.slf4j.Logger;

public class MonsterCommand {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static MonsterCommand INSTANCE;

    private MonsterCommand() {
    }

    public static MonsterCommand getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new MonsterCommand();
        }

        return INSTANCE;
    }

    public void register(CommandDispatcher<CommandSourceStack> dispatcher){
        this.setMonsterSpawnCommand(dispatcher);
    }

    private void setMonsterSpawnCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> setSpawn = Commands.literal("SpiruraLand")
                .requires(source -> source.hasPermission(4))
                .then(Commands.argument("MonsterID", IntegerArgumentType.integer())
                        .then(Commands.argument("PosX", IntegerArgumentType.integer())
                                .then(Commands.argument("PosY", IntegerArgumentType.integer())
                                        .then(Commands.argument("PosZ", IntegerArgumentType.integer())
                                                .executes(this::setSpawn)))));
        dispatcher.register(setSpawn);
    }

    private int setSpawn(CommandContext<CommandSourceStack> context) {
        int monsterID = IntegerArgumentType.getInteger(context, "MonsterID");
        int posX = IntegerArgumentType.getInteger(context, "PosX");
        int posY = IntegerArgumentType.getInteger(context, "PosY");
        int posZ = IntegerArgumentType.getInteger(context, "PosZ");

        for (Entity entity : context.getSource().getLevel().getAllEntities()){
            if(entity.getId() == monsterID && entity instanceof ISpiruraMonster monster){
                BlockPos pos = new BlockPos(posX,posY,posZ);
                monster.setSpawnPosition(pos);
                return 1;
            }
        }
        return 0;
        //TODO: set monster spawn
    }
}
