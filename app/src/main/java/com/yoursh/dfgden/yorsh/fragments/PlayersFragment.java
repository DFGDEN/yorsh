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

import com.yoursh.dfgden.yorsh.R;
import com.yoursh.dfgden.yorsh.adaptors.PlayersAdaptor;
import com.yoursh.dfgden.yorsh.dialogs.NamePickDialog;
import com.yoursh.dfgden.yorsh.utils.mappers.PlayerMapper;

/**
 * Created by dfgden on 8/1/16.
 */
public class PlayersFragment extends Fragment {

    private PlayersAdaptor playersAdaptor;
    private RecyclerView recyclerView;
    private PlayerMapper playerMapper;
    private int playersCount;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.playersAdaptor = new PlayersAdaptor(getActivity());
        this.playerMapper = new PlayerMapper(getActivity().getApplicationContext());
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
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNamePickerDialog();
            }
        });
    }

    private void showNamePickerDialog(){
        NamePickDialog namePickDialog = NamePickDialog.getInstance(playersCount);
        namePickDialog.show(getActivity().getSupportFragmentManager(), "name_picker");
        namePickDialog.setOnDialogClickListener(new NamePickDialog.OnDialogClickListener() {
            @Override
            public void onAcceptClick(String name) {
                playersAdaptor.addPlayer(playerMapper.map(playersCount,name));
                playersCount++;
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
        recycleList.setAdapter(playersAdaptor);
    }

    public static PlayersFragment getInstance() {
        PlayersFragment playersFragment = new PlayersFragment();
        Bundle bundle = new Bundle();
        playersFragment.setArguments(bundle);
        return playersFragment;
    }

}
