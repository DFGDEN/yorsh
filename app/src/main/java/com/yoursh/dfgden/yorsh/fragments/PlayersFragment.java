package com.yoursh.dfgden.yorsh.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yoursh.dfgden.yorsh.R;
import com.yoursh.dfgden.yorsh.adaptors.GamePlayersAdapter;
import com.yoursh.dfgden.yorsh.adaptors.PlayersAdapter;
import com.yoursh.dfgden.yorsh.database.DataProvider;
import com.yoursh.dfgden.yorsh.database.listeners.GetPlayersListener;
import com.yoursh.dfgden.yorsh.models.PlayerModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by dfgden on 8/14/16.
 */
public class PlayersFragment extends Fragment {

    private PlayersAdapter playersAdapter;
    private RecyclerView recyclerView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.playersAdapter = new PlayersAdapter(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_players,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        this.recyclerView = (RecyclerView) view.findViewById(R.id.recycleList);
        initRecycleList(recyclerView);
        getPlayers();
    }

    private void initRecycleList(RecyclerView recycleList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        RecyclerView.ItemAnimator scaleInAnimationAdapter = new DefaultItemAnimator();
        recycleList.setItemAnimator(scaleInAnimationAdapter);
        recycleList.setLayoutManager(linearLayoutManager);
        recycleList.setAdapter(playersAdapter);
    }

    private void getPlayers(){

         DataProvider.getInstance().getPlayers(new GetPlayersListener() {
             @Override
             public void getPlayers(ArrayList<PlayerModel> playerModels) {
                 final ArrayList<PlayerModel> players = playerModels;
                 Collections.sort(players, new Comparator<PlayerModel>() {
                     @Override
                     public int compare(PlayerModel lhs, PlayerModel rhs) {
                         return rhs.getAllPoints() > lhs.getAllPoints()? 1 : rhs.getAllPoints() < lhs.getAllPoints() ? -1: 0;
                     }
                 });
                 if (getActivity()!= null){
                     getActivity().runOnUiThread(new Runnable() {
                         @Override
                         public void run() {
                             playersAdapter.updPlayers(players);
                             playersAdapter.notifyDataSetChanged();
                         }
                     });
                 }
             }
         });

    }

    public static PlayersFragment getInstance() {
        PlayersFragment gameFragment = new PlayersFragment();
        Bundle bundle = new Bundle();
        gameFragment.setArguments(bundle);
        return gameFragment;
    }

}
