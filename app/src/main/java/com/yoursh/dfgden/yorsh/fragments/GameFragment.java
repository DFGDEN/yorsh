package com.yoursh.dfgden.yorsh.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yoursh.dfgden.yorsh.R;
import com.yoursh.dfgden.yorsh.activities.GameActivity;
import com.yoursh.dfgden.yorsh.adaptors.GamePlayersAdapter;
import com.yoursh.dfgden.yorsh.dialogs.NamePickDialog;
import com.yoursh.dfgden.yorsh.models.PlayerModel;


/**
 * Created by dfgden on 8/1/16.
 */
public class GameFragment extends Fragment implements View.OnClickListener {

    private GamePlayersAdapter gamePlayersAdapter;
    private RecyclerView recyclerView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.gamePlayersAdapter = new GamePlayersAdapter(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        this.recyclerView = (RecyclerView) view.findViewById(R.id.recycleList);
        initRecycleList(recyclerView);
        view.findViewById(R.id.viewStartGame).setOnClickListener(this);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNamePickerDialog();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.viewStartGame:
                if (gamePlayersAdapter.getPlayers().size() < 2 ){
                    Toast.makeText(getContext(), R.string.game_start_game_error_toast, Toast.LENGTH_SHORT).show();
                } else  {
                   getActivity().startActivity(GameActivity.getGameIntent(getContext(),gamePlayersAdapter.getPlayers()));
                }

                break;
        }
    }

    private void showNamePickerDialog(){
        NamePickDialog namePickDialog = NamePickDialog.getInstance();
        namePickDialog.show(getActivity().getSupportFragmentManager(), "name_picker");
        namePickDialog.setOnDialogClickListener(new NamePickDialog.OnDialogClickListener() {
            @Override
            public void onAcceptClick(PlayerModel playerModel) {
                gamePlayersAdapter.addPlayer(playerModel);
            }

            @Override
            public void onCancelClick() {

            }
        });

    }

    private void initRecycleList(RecyclerView recycleList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        RecyclerView.ItemAnimator scaleInAnimationAdapter = new DefaultItemAnimator();
        recycleList.setItemAnimator(scaleInAnimationAdapter);
        recycleList.setLayoutManager(linearLayoutManager);
        recycleList.setAdapter(gamePlayersAdapter);
    }

    public static GameFragment getInstance() {
        GameFragment playersFragment = new GameFragment();
        Bundle bundle = new Bundle();
        playersFragment.setArguments(bundle);
        return playersFragment;
    }


}
