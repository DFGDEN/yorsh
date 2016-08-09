package com.yoursh.dfgden.yorsh.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.yoursh.dfgden.yorsh.R;


/**
 * Created by dfgden on 21.6.16.
 */
public class NamePickDialog extends DialogFragment implements  View.OnClickListener {

    private static final String ARG_PLAYER_NUMBER = "arg_player_number";

        public interface OnDialogClickListener{

        void onAcceptClick(String name);
        void onCancelClick();

         }

    private String[] arrayContentIcons;
    private TextInputLayout tilCreatePlayer;
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
        getDataFromBundle();
        this.arrayContentIcons = getActivity().getApplicationContext().getResources().getStringArray(R.array.players_ava);
        this.avaId = getActivity().getApplicationContext().getResources().getIdentifier(arrayContentIcons[playerNumber],
                "drawable", getActivity().getApplicationContext().getPackageName());
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_namepick, container, false);
        ((ImageView)view.findViewById(R.id.imgAvatar)).setImageResource(avaId);
        getDialog().setTitle(getActivity().getString(R.string.namepick_title));

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        tilCreatePlayer = (TextInputLayout) view.findViewById(R.id.tilCreatePlayer);
        tilCreatePlayer.setHint(getActivity().getString(R.string.namepick_edttxt_hint));
        edtCreatePlayer = (EditText) view.findViewById(R.id.edtCreatePlayer);
        edtCreatePlayer.setOnClickListener(this);
        (view.findViewById(R.id.layoutOK)).setOnClickListener(this);
        (view.findViewById(R.id.layoutCancel)).setOnClickListener(this);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.layoutOK:
                if (onDialogClickListener!=null){
                    onDialogClickListener.onAcceptClick(edtCreatePlayer.getText() +"");
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


    private void getDataFromBundle() {
        if (getArguments() != null) {
            this.playerNumber =  getArguments().getInt(ARG_PLAYER_NUMBER);
        }
    }

    public static NamePickDialog getInstance(int playerNumber) {
        NamePickDialog namePickDialog = new NamePickDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_PLAYER_NUMBER,playerNumber);
        namePickDialog.setArguments(bundle);
        return namePickDialog;
    }


}
