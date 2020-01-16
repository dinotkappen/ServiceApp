package com.app.service.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.service.Model.Service_List_Model;
import com.bumptech.glide.Glide;
import com.app.service.Fragment.SubMenuFragment;
import com.app.service.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class Main_Menu_Adapter extends RecyclerView.Adapter<Main_Menu_Adapter.MyViewHolder> {
    //    ArrayList menu_NameArrayList;
//    ArrayList service_ImagesArrayList;
//    ArrayList service_IDArrayList;
    Context context;
    MyViewHolder holder;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    List<Service_List_Model> dataArray;
    SharedPreferences preferences;

    public Main_Menu_Adapter(Context context, List<Service_List_Model> dataArray) {
        this.context = context;
//        this.menu_NameArrayList = menu_NameArrayList;
//        this.service_ImagesArrayList = service_ImagesArrayList;
//        this.service_IDArrayList = service_IDArrayList;
        this.dataArray = dataArray;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_menu_main, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

//        int count = menu_NameArrayList.size();

        holder.menu_Name.setText("" + dataArray.get(position).getService_name());
        Glide.with(context).load(dataArray.get(position).getService_icon()).into(holder.menu_Icon);
        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open another activity on item click
                try {

                    SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putString("productId", "" + dataArray.get(position).getServie_id());
                    editor.putString("serviceName", "" + dataArray.get(position).getService_name());
                    editor.apply();
                    String h = "" + dataArray.get(position).getServie_id();
                    String l = h;
                    Fragment fragment = new SubMenuFragment();
                    FragmentManager fragmentManager = ((AppCompatActivity) view.getContext()).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();



//                    SubMenuFragment subMenuFragment = new SubMenuFragment();
//                    ((AppCompatActivity) view.getContext()).getSupportFragmentManager()
//                            .beginTransaction()
//                            .replace(R.id.fragment_container, subMenuFragment)
//                            .commit();
//                    ((AppCompatActivity) view.getContext()).getFragmentManager().popBackStack();


                } catch (Exception ex) {
                    String h = ex.getMessage().toString();
                    String k = h;
                }

            }
        });

        // set the data in items

        //  int h=Integer.parseInt(service_ImagesArrayList.get(position));
        //  holder.menu_Icon.setImageResource(""+service_ImagesArrayList.get(position));


        //  Glide.with(mContext).load(itemMainModel.getThumbnail()).into(holder.thumbnail);

        // implement setOnClickListener event on item view.


    }

    @Override
    public int getItemCount() {
        return dataArray.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView menu_Name;
        ImageView menu_Icon;
        LinearLayout linear_single_items;

        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            menu_Name = (TextView) itemView.findViewById(R.id.menu_Name);
            menu_Icon = (ImageView) itemView.findViewById(R.id.menu_Icon);
            preferences = context.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
            int deviceWidth = preferences.getInt("deviceWidth", 0);
            Log.v("deviceWidth", "" + deviceWidth);
            int menuWidth;
            if (deviceWidth > 760) {
                linear_single_items = (LinearLayout) itemView.findViewById(R.id.linear_single_items);

                menuWidth = ((deviceWidth - 100) / 3);
                ViewGroup.LayoutParams params = linear_single_items.getLayoutParams();
// Changes the height and width to the specified *pixels*
                params.height = menuWidth;
                params.width = menuWidth;
                // menuWidth=((deviceWidth-140)/3);
                linear_single_items.setLayoutParams(params);

                Log.v("menuWidth", "" + menuWidth);
                menu_Icon.getLayoutParams().height = menuWidth - 210;
                menu_Icon.getLayoutParams().width = menuWidth - 200;
                menu_Name.getLayoutParams().width = menuWidth - 100;

            }

        }
    }
}