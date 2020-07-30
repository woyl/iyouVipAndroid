package com.nc.player.player;

import android.content.Context;

public abstract class PlayerFactory<P extends AbstractPlayer> {

    public abstract P createPlayer(Context context);
}
