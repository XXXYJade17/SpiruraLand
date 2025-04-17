package com.xxxyjade17.spiruraland.Spirura.NetWork.Handler;

import com.xxxyjade17.spiruraland.Spirura.Data.Client.SpiruraData;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public class ClientPayloadHandler {
    private static ClientPayloadHandler INSTANCE;

    private int rank;
    private int level;
    private int experience;
    private boolean shackle;
    private float breakRate;
    private float rateIncrease;


    public static ClientPayloadHandler getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new ClientPayloadHandler();
        }
        return INSTANCE;
    }

    public void handleSpiruraData(SpiruraData data, PlayPayloadContext context) {
        rank = data.rank();
        level = data.level();
        experience = data.experience();
        shackle = data.shackle();
        breakRate = data.breakRate();
        rateIncrease = data.rateIncrease();
    }
}
