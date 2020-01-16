package com.app.service.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.service.Fragment.ReceiptDetailsFragment;
import com.app.service.Model.AttachmentItemsModel;
import com.app.service.Model.AttachmentItemsModel;
import com.app.service.R;
import com.bumptech.glide.Glide;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class AttachmentItemsAdapter extends RecyclerView.Adapter<AttachmentItemsAdapter.MyViewHolder> {

    private Context mContext;
    private List<AttachmentItemsModel> attachment_ModelList;
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_title;
        public ImageView imgDelete;



        public MyViewHolder(View view) {
            super(view);

            txt_title = (TextView) view.findViewById(R.id.txtAttachmentName);
            imgDelete = (ImageView) view.findViewById(R.id.imgDelete);


        }
    }


    public AttachmentItemsAdapter(Context mContext, List<AttachmentItemsModel> attachment_ModelList) {
        this.mContext = mContext;
        this.attachment_ModelList = attachment_ModelList;
    }
    @Override
    public AttachmentItemsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attachment_items_layout, parent, false);


        return new AttachmentItemsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AttachmentItemsAdapter.MyViewHolder holder, int position) {
        AttachmentItemsModel itemSubModel = attachment_ModelList.get(position);
        final AttachmentItemsAdapter.MyViewHolder viewHolder = (AttachmentItemsAdapter.MyViewHolder)holder;
        viewHolder.txt_title.setText(itemSubModel.getAttachmentName());
        viewHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(position);
            }
        });




    }





    @Override
    public int getItemCount() {
        return attachment_ModelList.size();
    }

    public void removeItem(int position) {
        attachment_ModelList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, attachment_ModelList.size());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}