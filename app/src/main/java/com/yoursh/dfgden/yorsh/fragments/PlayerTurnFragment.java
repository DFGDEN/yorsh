package com.yoursh.dfgden.yorsh.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yoursh.dfgden.yorsh.R;
import com.yoursh.dfgden.yorsh.models.PlayerModel;

import java.util.ArrayList;

/**
 * Created by dfgden on 8/15/16.
 */
public class PlayerTurnFragment extends Fragment {

    public static final String ARG_KEY_PLAYER = "arg_key_player";

    private PlayerModel playerModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromIntent();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_turn,container,false);
        ((ImageView) view.findViewById(R.id.imgIcon)).setImageResource(getContext().getResources().getIdentifier(playerModel.getIconContentTurnName(),
                "drawable", getContext().getPackageName()));

        ((TextView) view.findViewById(R.id.txtTurn)).setText(playerModel.getName());

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void getDataFromIntent(){
        if (getArguments()!= null){
            playerModel = (PlayerModel) getArguments().getSerializable(ARG_KEY_PLAYER);
        }
    }

    public static PlayerTurnFragment getInstance(PlayerModel playerModel) {
        PlayerTurnFragment playerTurnFragment = new PlayerTurnFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_KEY_PLAYER,playerModel);
        playerTurnFragment.setArguments(bundle);
        return playerTurnFragment;
    }
}
