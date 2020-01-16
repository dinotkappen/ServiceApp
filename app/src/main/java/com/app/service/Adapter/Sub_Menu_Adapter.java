package com.app.service.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.app.service.Fragment.ServiceDetailFragment;
import com.app.service.Model.Sub_Menu_Model;
import com.app.service.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class Sub_Menu_Adapter extends RecyclerView.Adapter<Sub_Menu_Adapter.MyViewHolder> {

    private Context mContext;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private List<Sub_Menu_Model> sub_menu_ModelList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_title;
        public ImageView img_thumbnail;


        public MyViewHolder(View view) {
            super(view);

            txt_title = (TextView) view.findViewById(R.id.txt_title);
           // txt_desc= (TextView) view.findViewById(R.id.txt_desc);
            img_thumbnail = (ImageView) view.findViewById(R.id.img_thumbnail);

        }
    }


    public Sub_Menu_Adapter(Context mContext, List<Sub_Menu_Model> sub_menu_ModelList) {
        this.mContext = mContext;
        this.sub_menu_ModelList = sub_menu_ModelList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.submenu_card, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Sub_Menu_Model itemSubModel = sub_menu_ModelList.get(position);
        final MyViewHolder viewHolder = (MyViewHolder)holder;
        String title=itemSubModel.getTile_main();

        if (title != null && !title.isEmpty() && !title.equals("null")) {
            holder.txt_title.setText(title);
        }
        String jh=itemSubModel.getSub_item_thumbnail();
        Glide.with(mContext).load(itemSubModel.getSub_item_thumbnail()).into(holder.img_thumbnail);
//        viewHolder.txt_desc.setText(itemSubModel.getDescr());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open another activity on item click
                try {

                    SharedPreferences.Editor editor =mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    String productId=itemSubModel.getSub_item_id();
                    editor.putString("subProductId", ""+itemSubModel.getSub_item_id());
                    String j=""+itemSubModel.getSub_item_id();
                    editor.apply();
                    Fragment fragment = new ServiceDetailFragment();
                    FragmentManager fragmentManager = ((AppCompatActivity) view.getContext()).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                } catch (Exception ex) {
                    String h = ex.getMessage().toString();
                    String k = h;
                }

            }
        });



        holder.img_thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    SharedPreferences.Editor editor =mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putString("subProductId", ""+itemSubModel.getSub_item_id());
                    String j=""+itemSubModel.getSub_item_id();
                    String productId=itemSubModel.getSub_item_id();
                    editor.apply();
                    Fragment fragment = new ServiceDetailFragment();
                    FragmentManager fragmentManager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                } catch (Exception ex) {
                    String h = ex.getMessage().toString();
                    String k = h;
                }

            }
        });

        // loading itemMainModel cover using Glide library



    }





    @Override
    public int getItemCount() {
        return sub_menu_ModelList.size();
    }

    public void removeItem(int position) {
        sub_menu_ModelList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, sub_menu_ModelList.size());
    }
}
