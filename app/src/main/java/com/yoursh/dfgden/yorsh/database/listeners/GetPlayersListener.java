package com.yoursh.dfgden.yorsh.database.listeners;

import com.yoursh.dfgden.yorsh.models.PlayerModel;

import java.util.ArrayList;

/**
 * Created by dfgden on 8/14/16.
 */
public interface GetPlayersListener {

    void getPlayers(ArrayList<PlayerModel> playerModels);
}
