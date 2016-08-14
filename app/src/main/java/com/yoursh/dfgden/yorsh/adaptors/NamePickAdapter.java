package com.yoursh.dfgden.yorsh.adaptors;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yoursh.dfgden.yorsh.R;
import com.yoursh.dfgden.yorsh.models.PlayerModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by dfgden on 8/14/16.
 */
public class NamePickAdapter extends RecyclerView.Adapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<PlayerModel> playerModels;




    public NamePickAdapter(Activity activity) {
        this.context = activity.getApplicationContext();
        this.layoutInflater = LayoutInflater.from(activity);
        this.playerModels = new ArrayList<>();
    }


    public void updPlayerModels(ArrayList<PlayerModel> playerModels) {
        this.playerModels.clear();
        this.playerModels.addAll(playerModels);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.adaptor_namepick, parent, false);
        return new ViewBasicItem(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewBasicItem viewBasicItem = (ViewBasicItem) holder;
        PlayerModel player = playerModels.get(position);
        viewBasicItem.imgIcon.setImageResource(context.getResources().getIdentifier(player.getIconContentName(),
                "drawable", context.getPackageName()));
        viewBasicItem.txtName.setText(player.getName());

    }

    @Override
    public int getItemCount() {
        return playerModels.size();
    }


    static class ViewBasicItem extends RecyclerView.ViewHolder {

        private CircleImageView imgIcon;
        private TextView txtName;

        public ViewBasicItem(View itemView) {
            super(itemView);
            imgIcon = (CircleImageView) itemView.findViewById(R.id.imgIcon);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
        }
    }
}
