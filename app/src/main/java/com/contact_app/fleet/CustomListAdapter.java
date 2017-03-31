package com.contact_app.fleet;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kt_ki on 3/21/2017.
 */

class CustomListAdapter extends ArrayAdapter<UserRecord> {

//    private final List<Bitmap> images;
//    private List<String> names;
      private List<UserRecord> mUserRecords;

    CustomListAdapter(Activity context,
                      List<UserRecord> userRecords) {
        super(context, 0, userRecords);
        mUserRecords = userRecords;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        //LayoutInflater inflater = context.getLayoutInflater();
        View viewConverter = view;
        if (view == null)
            viewConverter =
                    LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        UserRecord currentUser = mUserRecords.get(position);

        TextView txtName =
                (TextView) viewConverter.findViewById(R.id.list_names);
        txtName.setText(currentUser.getName());

        CircleImageView profileImage =
                (CircleImageView) viewConverter.findViewById(R.id.profile_image);

        byte[] imgByte = currentUser.getPicture();
        if (imgByte != null) {
            ByteArrayInputStream imageStream = new ByteArrayInputStream(imgByte);
            profileImage.setImageBitmap(BitmapFactory.decodeStream(imageStream));
        } else {
            profileImage.setImageResource(R.drawable.ic_person);
        }


        return viewConverter;
    }

}
