package com.example.jpdeguzman.popularmovies.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String IMAGE_RECOMMENDED_SIZE = "w500";

    private Context mContext;
    private ArrayList<String> mImageList;

    public ImageAdapter(Context context, ArrayList<String> imageList) {
        mContext = context;
        mImageList = imageList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(mContext);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        Picasso.with(mContext).setLoggingEnabled(true);
        Picasso.with(mContext)
                .load(IMAGE_BASE_URL + IMAGE_RECOMMENDED_SIZE + mImageList.get(position))
                .into(imageView);

        return imageView;
    }

    @Override
    public int getCount() {
        return mImageList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
