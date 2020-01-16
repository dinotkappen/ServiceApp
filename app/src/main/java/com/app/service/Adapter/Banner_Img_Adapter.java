package com.app.service.Adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.app.service.Model.Banner_Img_Model;
import com.app.service.R;

import java.util.List;

public class Banner_Img_Adapter extends PagerAdapter {


    private List<Banner_Img_Model> data_Banner_Images;
    private LayoutInflater inflater;
    private Context context;


    public Banner_Img_Adapter(Context context, List<Banner_Img_Model> data_Banner_Images) {
        try
        {
            this.context = context;
            this.data_Banner_Images = data_Banner_Images;
            inflater = LayoutInflater.from(context);
        }
        catch (Exception ex)
        {
            String msg=ex.getMessage().toString();
            String x=msg;
        }

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return data_Banner_Images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {

        try {

            View imageLayout = inflater.inflate(R.layout.layout_main_banner, view, false);

            assert imageLayout != null;
            final Banner_Img_Model itemSubModel = data_Banner_Images.get(position);
            final ImageView imageView = (ImageView) imageLayout
                    .findViewById(R.id.imageView_main_banner);
            final LinearLayout linear_img = (LinearLayout) imageLayout
                    .findViewById(R.id.linear_img);





            Glide.with(context)
                    .load(itemSubModel.getThumbnail())
                    .into(imageView);

            view.addView(imageLayout, 0);
            return imageLayout;

        }catch (Exception e)
        {
            String h=e.getMessage().toString();
            String k=h;
        }
        return null;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}