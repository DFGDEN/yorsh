package com.yoursh.dfgden.yorsh.utils.mappers;

import android.content.Context;

import com.yoursh.dfgden.yorsh.R;
import com.yoursh.dfgden.yorsh.models.Player;

/**
 * Created by dfgden on 8/9/16.
 */
public class PlayerMapper {

    private String[] arrayContentIcons;
    private Context context;

    public PlayerMapper(Context context) {
        this.context = context;
        this.arrayContentIcons = context.getResources().getStringArray(R.array.players_ava);
    }

    public Player map(int count, String name){
        int iconId = context.getResources().getIdentifier(arrayContentIcons[count], "drawable", context.getPackageName());
        return new Player(name,iconId,0);
    }
}
