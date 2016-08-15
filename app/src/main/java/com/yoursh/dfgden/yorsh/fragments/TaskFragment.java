package com.yoursh.dfgden.yorsh.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yoursh.dfgden.yorsh.R;
import com.yoursh.dfgden.yorsh.models.PlayerModel;

import java.util.Random;

/**
 * Created by dfgden on 8/15/16.
 */
public class TaskFragment extends Fragment implements View.OnClickListener {


    private String[] arrayTaskIcons;
    private int numberTask;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.arrayTaskIcons = getActivity().getApplicationContext().getResources().getStringArray(R.array.task_icons);
        Random random = new Random();
        this.numberTask = random.nextInt(arrayTaskIcons.length) ;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ((ImageView) view.findViewById(R.id.imgIcon)).setImageResource(getContext().getResources().getIdentifier(arrayTaskIcons[numberTask],
                "drawable", getContext().getPackageName()));

        ( view.findViewById(R.id.btnOk)).setOnClickListener(this);
        (view.findViewById(R.id.btnDeny)).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnOk:
                Toast.makeText(getContext(),"ВЫПОЛНИЛ", Toast.LENGTH_SHORT).show();

                break;
            case R.id.btnDeny:
                Toast.makeText(getContext(),"НЕ ВЫПОЛНИЛ", Toast.LENGTH_SHORT).show();
                break;
        }
    }

//    private void getDataFromIntent(){
//        if (getArguments()!= null){
//            playerModel = (PlayerModel) getArguments().getSerializable(ARG_KEY_PLAYER);
//        }
//    }

    public static TaskFragment getInstance() {
        TaskFragment taskFragment = new TaskFragment();
        Bundle bundle = new Bundle();
        taskFragment.setArguments(bundle);
        return taskFragment;
    }


}
