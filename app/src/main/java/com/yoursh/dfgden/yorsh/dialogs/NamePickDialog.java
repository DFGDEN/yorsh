package com.yoursh.dfgden.yorsh.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.yoursh.dfgden.yorsh.R;
import com.yoursh.dfgden.yorsh.adaptors.NamePickAdapter;
import com.yoursh.dfgden.yorsh.database.DataProvider;
import com.yoursh.dfgden.yorsh.database.listeners.GetPlayersListener;
import com.yoursh.dfgden.yorsh.models.PlayerModel;

import java.util.ArrayList;
import java.util.Random;


/**
 * Created by dfgden on 21.6.16.
 */
public class NamePickDialog extends DialogFragment implements  View.OnClickListener {



        public interface OnDialogClickListener{

        void onAcceptClick(PlayerModel playerModel);
        void onCancelClick();

         }

    private String[] arrayContentIcons;
    private String[] arrayContentTurnIcons;
    private NamePickAdapter namePickAdapter;
    private TextInputLayout tilCreatePlayer;
    private String secretKey= "";
    private EditText edtCreatePlayer;
    private OnDialogClickListener onDialogClickListener;
    private int playerNumber;
    private int avaId;


    public void setOnDialogClickListener(OnDialogClickListener onDialogClickListener) {
        this.onDialogClickListener = onDialogClickListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.arrayContentIcons = getActivity().getApplicationContext().getResources().getStringArray(R.array.players_ava);
        this.arrayContentTurnIcons = getActivity().getApplicationContext().getResources().getStringArray(R.array.players_turn_ava);
        this.namePickAdapter = new NamePickAdapter(getActivity());
        Random random = new Random();
        this.playerNumber = random.nextInt(arrayContentIcons.length) ;
        this.avaId = getActivity().getApplicationContext().getResources().getIdentifier(arrayContentIcons[playerNumber],
                "drawable", getActivity().getApplicationContext().getPackageName());
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_namepick, container, false);
        ((ImageView)view.findViewById(R.id.imgAvatar)).setImageResource(avaId);
        getDialog().setTitle(getActivity().getString(R.string.namepick_input_name));
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        tilCreatePlayer = (TextInputLayout) view.findViewById(R.id.tilCreatePlayer);
        tilCreatePlayer.setHint(getActivity().getString(R.string.namepick_input_name));
        edtCreatePlayer = (EditText) view.findViewById(R.id.edtCreatePlayer);
        edtCreatePlayer.setOnClickListener(this);
        (view.findViewById(R.id.layoutOK)).setOnClickListener(this);
        (view.findViewById(R.id.layoutCancel)).setOnClickListener(this);
        (view.findViewById(R.id.btnSecretKey)).setOnClickListener(this);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycleList);
        initRecycleList(recyclerView);
        DataProvider.getInstance().getPlayers(new GetPlayersListener() {
            @Override
            public void getPlayers(ArrayList<PlayerModel> playerModels) {
                namePickAdapter.updPlayerModels(playerModels);
                if (getActivity()!=null){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            namePickAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSecretKey:
                showSecretDialog();
                break;


            case R.id.layoutOK:
                PlayerModel playerModel = new PlayerModel(
                        edtCreatePlayer.getText() +"",
                        arrayContentIcons[playerNumber],
                        arrayContentTurnIcons[playerNumber],
                        0,
                        0,
                        secretKey
                );

                DataProvider.getInstance().savePlayer(playerModel);

                if (onDialogClickListener!=null){
                    onDialogClickListener.onAcceptClick(playerModel);
                }

                    getDialog().dismiss();
                    break;
            case R.id.layoutCancel:
                if (onDialogClickListener!=null){
                    onDialogClickListener.onCancelClick();
                }

                getDialog().dismiss();
                break;
        }
    }

    private void initRecycleList(RecyclerView recycleList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        RecyclerView.ItemAnimator scaleInAnimationAdapter = new DefaultItemAnimator();
        recycleList.setItemAnimator(scaleInAnimationAdapter);
        recycleList.setLayoutManager(linearLayoutManager);
        recycleList.setAdapter(namePickAdapter);
    }

    private void showSecretDialog(){
        SecretKeyDialog secretKeyDialog = SecretKeyDialog.getInstance();
        secretKeyDialog.show(getActivity().getSupportFragmentManager(), "secret_key");
        secretKeyDialog.setOnDialogClickListener(new SecretKeyDialog.OnDialogClickListener() {
            @Override
            public void onAcceptClick(String key) {
              if (key.contains("&")){
                  secretKey = key;
              }
            }

            @Override
            public void onCancelClick() {

            }
        });

    }


    public static NamePickDialog getInstance() {
        NamePickDialog namePickDialog = new NamePickDialog();
        Bundle bundle = new Bundle();
        namePickDialog.setArguments(bundle);
        return namePickDialog;
    }


}
