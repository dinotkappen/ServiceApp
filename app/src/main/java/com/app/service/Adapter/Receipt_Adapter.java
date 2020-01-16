package com.app.service.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.ActivityCompat;
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
import android.widget.TextView;

import com.app.service.Fragment.ReceiptDetailsFragment;
import com.app.service.Model.Reciept_Model;
import com.app.service.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class Receipt_Adapter extends RecyclerView.Adapter<Receipt_Adapter.MyViewHolder> {

    private Context mContext;
    private List<Reciept_Model> receipt_ModelList;
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_title,txt_Price,txt_date,txt_status;
        public CardView card_view_reciept;






        public MyViewHolder(View view) {
            super(view);

            txt_title = (TextView) view.findViewById(R.id.txt_title);
            txt_Price = (TextView) view.findViewById(R.id.txt_Price);
            txt_date = (TextView) view.findViewById(R.id.txt_date);
            txt_status = (TextView) view.findViewById(R.id.txt_status);
            card_view_reciept = (CardView) view.findViewById(R.id.card_view_reciept);

        }
    }


    public Receipt_Adapter(Context mContext, List<Reciept_Model> receipt_ModelList) {
        this.mContext = mContext;
        this.receipt_ModelList = receipt_ModelList;
    }
    @Override
    public Receipt_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.receipt_item_card, parent, false);


        return new Receipt_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final Receipt_Adapter.MyViewHolder holder, int position) {
        Reciept_Model itemSubModel = receipt_ModelList.get(position);
        SharedPreferences preferences;
        final Receipt_Adapter.MyViewHolder viewHolder = (Receipt_Adapter.MyViewHolder)holder;


        viewHolder.txt_title.setText(itemSubModel.getReieptReference());
        viewHolder.txt_date.setText(itemSubModel.getReciept_date());
        viewHolder.txt_status.setText(itemSubModel.getReciept_status());
        if(itemSubModel.getCountry_id().equals("174"))
        {
            viewHolder.txt_Price.setText("QAR "+itemSubModel.getReceipt_price());
            Log.v("priceQAR", itemSubModel.getReceipt_price());
        }
        else
        {
            try {
                Double qar = Double.parseDouble(itemSubModel.getReceipt_price());
               String price = "" + (qar * 0.27);
                Log.v("priceUSD", price);

                viewHolder.txt_Price.setText("USD "+price);
            }
            catch (Exception ex)
            {
                Log.v("Dollar",ex.getMessage().toString());
            }
        }


        viewHolder.card_view_reciept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("receiptId", "" + itemSubModel.getReciept_id());
                editor.apply();
                Fragment fragment = new ReceiptDetailsFragment();
                FragmentManager fragmentManager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

    }





    @Override
    public int getItemCount() {
        return receipt_ModelList.size();
    }

    public void removeItem(int position) {
        receipt_ModelList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, receipt_ModelList.size());
    }
}
