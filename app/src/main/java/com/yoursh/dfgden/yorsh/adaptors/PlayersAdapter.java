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
public class PlayersAdapter extends BaseRecycleAdapter {

        private ArrayList<PlayerModel> players;
        private LayoutInflater layoutInflater;
        private Context context;

        public PlayersAdapter(Activity activity) {
            this.context = activity.getApplicationContext();
            this.layoutInflater = LayoutInflater.from(activity);
            this.players = new ArrayList<>();
        }


        public void updPlayers(ArrayList<PlayerModel> players) {
            this.players.clear();
            this.players.addAll(players);
        }

        public void addPlayer(PlayerModel player) {
            this.players.add(player);
            notifyItemInserted(players.size()-1);
        }

        @Override
        public boolean useHeader() {
            return false;
        }

        @Override
        public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindHeaderView(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public boolean useFooter() {
            return false;
        }

        @Override
        public RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindFooterView(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
            View view = layoutInflater.inflate(R.layout.adapter_players, parent, false);
            return new ViewBasicItem(view);
        }

        @Override
        public void onBindBasicItemView(RecyclerView.ViewHolder holder, int position) {
            ViewBasicItem viewBasicItem = (ViewBasicItem) holder;
            PlayerModel player = players.get(position);
            viewBasicItem.imgIcon.setImageResource(context.getResources().getIdentifier(player.getIconContentName(),
                    "drawable", context.getPackageName()));
            viewBasicItem.txtName.setText(player.getName());
            viewBasicItem.txtPoint.setText(player.getAllPoints() + "");
        }

        @Override
        public int getBasicItemCount() {
            return players.size();
        }

static class ViewBasicItem extends RecyclerView.ViewHolder {

    private CircleImageView imgIcon;
    private TextView txtName, txtPoint;

    public ViewBasicItem(View itemView) {
        super(itemView);
        imgIcon = (CircleImageView) itemView.findViewById(R.id.imgIcon);
        txtName = (TextView) itemView.findViewById(R.id.txtName);
        txtPoint = (TextView) itemView.findViewById(R.id.txtPoint);

    }
}
}
