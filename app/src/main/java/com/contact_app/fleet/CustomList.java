package com.contact_app.fleet;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kt_ki on 3/21/2017.
 */

class CustomList extends BaseAdapter {
    private final Activity context;
    private final List<Bitmap> images;
    private List<String> names;

    CustomList(Activity context,
               List<String> names, List<Bitmap> imageId) {
        super();
        this.context = context;
        this.names = names;
        this.images = imageId;

    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Object getItem(int position) {
        return names.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(names.get(position));
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View viewConverter = view;
        if (view == null)
            viewConverter = inflater.inflate(R.layout.list_view, null);

        TextView txtName = (TextView) viewConverter.findViewById(R.id.list_names);

        CircleImageView profileImage = (CircleImageView) viewConverter.findViewById(R.id.profile_image);
        txtName.setText(names.get(position));

        profileImage.setImageBitmap(images.get(position));
        return viewConverter;
    }
}
