package com.yoursh.dfgden.yorsh.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yoursh.dfgden.yorsh.R;
import com.yoursh.dfgden.yorsh.fragments.GameFragment;
import com.yoursh.dfgden.yorsh.fragments.PlayerTurnFragment;
import com.yoursh.dfgden.yorsh.fragments.TaskFragment;
import com.yoursh.dfgden.yorsh.models.PlayerModel;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    public static final String ARG_PLAYER_LIST = "arg_player_list";

    private ArrayList<PlayerModel> playerModels;
    private static int numberPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getDataFromIntent();
        setFirstFragment();
        showTurnFragment();
        getSupportFragmentManager().addOnBackStackChangedListener(this);
    }

    @Override
    public void onBackStackChanged() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            finish();
        }
    }

    private void showTurnFragment(){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!GameActivity.this.isDestroyed()){
                    setTaskFragment();
                }
            }
        }, 2000);

    }

    private void setFirstFragment() {
        if (getSupportFragmentManager().findFragmentByTag("start") == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer,
                    PlayerTurnFragment.getInstance(playerModels.get(numberPlayer)), "start").commitAllowingStateLoss();
        }
    }

    private void setTaskFragment() {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,
                    TaskFragment.getInstance()).commitAllowingStateLoss();
    }


    private void getDataFromIntent(){
        Intent intent = getIntent();
        playerModels = (ArrayList<PlayerModel>) intent.getSerializableExtra(ARG_PLAYER_LIST);
    }

    public static Intent getGameIntent(Context context,ArrayList<PlayerModel> playerModels){
        Intent intent = new Intent(context, GameActivity.class);
        intent.putExtra(ARG_PLAYER_LIST,playerModels);
        return intent;
    }


}
