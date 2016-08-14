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
import android.widget.Toast;

import com.yoursh.dfgden.yorsh.R;
import com.yoursh.dfgden.yorsh.customviews.patternview.PatternView;


public class SecretKeyDialog extends DialogFragment implements  View.OnClickListener, PatternView.OnPatternDetectedListener {


    private PatternView patternView;




    public interface OnDialogClickListener{

        void onAcceptClick(String name);
        void onCancelClick();

    }

    private OnDialogClickListener onDialogClickListener;

    public void setOnDialogClickListener(OnDialogClickListener onDialogClickListener) {
        this.onDialogClickListener = onDialogClickListener;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_secretkey, container, false);
        getDialog().setTitle(getActivity().getString(R.string.secretkey_title));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        patternView = (PatternView) view.findViewById(R.id.patternView);
        patternView.setOnPatternDetectedListener(this);
        (view.findViewById(R.id.layoutOK)).setOnClickListener(this);
        (view.findViewById(R.id.layoutCancel)).setOnClickListener(this);
    }

    @Override
    public void onPatternDetected() {
        if (!patternView.getPatternString().contains("&")) {
            Toast.makeText(getContext(),"error",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.layoutOK:
                if (onDialogClickListener!=null){
                    onDialogClickListener.onAcceptClick(patternView.getPatternString());
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


    public static SecretKeyDialog getInstance() {
        SecretKeyDialog secretKeyDialog = new SecretKeyDialog();
        Bundle bundle = new Bundle();
        secretKeyDialog.setArguments(bundle);
        return secretKeyDialog;
    }


}
