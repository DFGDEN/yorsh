package com.yoursh.dfgden.yorsh.adaptors;



import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


public abstract class BaseRecycleAdapter extends RecyclerView.Adapter {

    private static final int TYPE_HEADER = -1;
    private static final int TYPE_FOOTER = -2;
    private static final int TYPE_ITEM = -3;


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_HEADER) {
            return onCreateHeaderViewHolder(parent, viewType);
        } else if (viewType == TYPE_FOOTER) {
            return onCreateFooterViewHolder(parent, viewType);
        }
        return onCreateBasicItemViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) {
            onBindHeaderView(holder, position);
        } else if (getItemViewType(position) == TYPE_FOOTER) {
            onBindFooterView(holder, position);
        } else if (getItemViewType(position) == TYPE_ITEM) {
            onBindBasicItemView(holder, position - (useHeader() ? 1 : 0));
        }
    }

    @Override
    public int getItemCount() {
        int itemCount = getBasicItemCount();
        if (useHeader()) {
            itemCount += 1;
        }
        if (useFooter()) {
            itemCount += 1;
        }
        return itemCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && useHeader()) {
            return TYPE_HEADER;
        } else if (position == (getItemCount() - 1) && useFooter()) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    public int getStaticRecycleHeight() {
        int height = 0;
        for (int i = 0; i < getItemCount(); i++) {
            View view = (onCreateViewHolder(null, getItemViewType(i))).itemView;
            view.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            height = height + view.getMeasuredHeight();
        }
        return height;
    }


    public abstract boolean useHeader();

    public abstract RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindHeaderView(RecyclerView.ViewHolder holder, int position);

    public abstract boolean useFooter();

    public abstract RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindFooterView(RecyclerView.ViewHolder holder, int position);

    public abstract RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindBasicItemView(RecyclerView.ViewHolder holder, int position);

    public abstract int getBasicItemCount();

}
