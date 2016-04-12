package com.elearnna.popularmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Ahmed on 4/8/2016.
 */
public class ImageAdapter extends BaseAdapter {
    private Context context;
    private String[] images;
    private int numOfImages;
    public ImageAdapter(Context contxt, String[] images){
        this.context = contxt;
        this.images = images;
        this.numOfImages = images.length;
    }

    @Override
    public int getCount() {
        return numOfImages;
    }

    @Override
    public Object getItem(int position) {
        return images[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null){
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(500,500));
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            imageView.setPadding(8, 8, 8 ,8);
        } else {
            imageView = (ImageView) convertView;
        }
        String url = (String) getItem(position);
        Picasso.with(context)
                .load(url)
                .into(imageView);

        return imageView;
    }
}
