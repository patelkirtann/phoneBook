package com.contact_app.fleet;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kt_ki on 3/21/2017.
 */

class CustomListAdapter extends ArrayAdapter<String> {

    private final List<Bitmap> images;
    private List<String> names;

    CustomListAdapter(Activity context,
                      List<String> names, List<Bitmap> imageId) {
        super(context, 0, names);
        this.names = names;
        this.images = imageId;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        //LayoutInflater inflater = context.getLayoutInflater();
        View viewConverter = view;
        if (view == null)
            viewConverter = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);

        TextView txtName = (TextView) viewConverter.findViewById(R.id.list_names);
        txtName.setText(names.get(position));

        CircleImageView profileImage = (CircleImageView) viewConverter.findViewById(R.id.profile_image);
        profileImage.setImageBitmap(images.get(position));



        return viewConverter;
    }
}
