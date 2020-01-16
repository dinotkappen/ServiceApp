package com.app.service.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.app.service.Fragment.SubMenuFragment;
import com.app.service.Model.All_Service_Model;
import com.app.service.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class All_Service_Adapter extends RecyclerView.Adapter<All_Service_Adapter.MyViewHolder> {

    private Context mContext;
    private List<All_Service_Model> all_Service_Model;
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail;
        public LinearLayout layout_card;
        CardView card_view;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            layout_card = (LinearLayout) view.findViewById(R.id.layout_card);
            card_view = (CardView) view.findViewById(R.id.card_view);

        }
    }


    public All_Service_Adapter(Context mContext, List<All_Service_Model> all_Service_Model) {
        this.mContext = mContext;
        this.all_Service_Model = all_Service_Model;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_service_card, parent, false);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final All_Service_Model itemMainModel = all_Service_Model.get(position);
        // holder.title.setText(all_Service_Model.get(position).getName());
//        for (int i = 0; i < all_Service_Model.size(); i++) {
//            String h = all_Service_Model.get(i).getName().toString();
//            String p = h;
//            holder.title.setText(all_Service_Model.get(i).getName());
//        }
        holder.title.setText(itemMainModel.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SharedPreferences.Editor editor = mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putString("productId", "" + all_Service_Model.get(position).getId());
                    editor.apply();
                    String h = "" + all_Service_Model.get(position).getId();
                    String l = h;
                    Fragment fragment = new SubMenuFragment();
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
//        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    SharedPreferences.Editor editor =mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
//                    editor.putString("productId", ""+all_Service_Model.get(position).getId());
//                    editor.apply();
//                    String h=""+all_Service_Model.get(position).getId();
//                    String l=h;
//                    Fragment fragment = new SubMenuFragment();
//                    FragmentManager fragmentManager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.replace(R.id.fragment_container, fragment);
//                    fragmentTransaction.addToBackStack(null);
//                    fragmentTransaction.commit();
//                } catch (Exception ex) {
//                    String h = ex.getMessage().toString();
//                    String k = h;
//                }
//            }
//        });

        // loading itemMainModel cover using Glide library
        Glide.with(mContext).load(itemMainModel.getThumbnail()).into(holder.thumbnail);

    }


    @Override
    public int getItemCount() {
        return all_Service_Model.size();
    }
}
