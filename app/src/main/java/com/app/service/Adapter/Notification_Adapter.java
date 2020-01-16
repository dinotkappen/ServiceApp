package com.app.service.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.service.Model.Notification_Model;
import com.app.service.R;

import java.util.List;

public class Notification_Adapter extends RecyclerView.Adapter<Notification_Adapter.MyViewHolder> {

    private Context mContext;
    private List<Notification_Model> notification_item_ModelList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_title,txt_msg,txt_date;


        public MyViewHolder(View view) {
            super(view);

            txt_title = (TextView) view.findViewById(R.id.txt_title);
            txt_msg = (TextView) view.findViewById(R.id.txt_msg);
            txt_date = (TextView) view.findViewById(R.id.txt_date);

        }
    }


    public Notification_Adapter(Context mContext, List<Notification_Model> notification_item_ModelList) {
        this.mContext = mContext;
        this.notification_item_ModelList = notification_item_ModelList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_item_card, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Notification_Model itemSubModel = notification_item_ModelList.get(position);
        final MyViewHolder viewHolder = (MyViewHolder)holder;
        viewHolder.txt_title.setText(itemSubModel.getTitle());
        viewHolder.txt_msg.setText(itemSubModel.getMsg());
        viewHolder.txt_date.setText(itemSubModel.getDate());

    }





    @Override
    public int getItemCount() {
        return notification_item_ModelList.size();
    }

    public void removeItem(int position) {
        notification_item_ModelList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, notification_item_ModelList.size());
    }
}
