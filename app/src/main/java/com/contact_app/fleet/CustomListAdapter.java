package com.contact_app.fleet;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

class CustomListAdapter extends ArrayAdapter<RetrieveContactRecord>{

    private List<RetrieveContactRecord> mUserRecords;
    private ArrayList<RetrieveContactRecord> arrayList;

    CustomListAdapter(Activity context,
                      List<RetrieveContactRecord> userRecords) {
        super(context, 0, userRecords);
        mUserRecords = userRecords;
        arrayList = new ArrayList<>();
        arrayList.addAll(mUserRecords);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        //LayoutInflater inflater = context.getLayoutInflater();
        View viewConverter = view;
        if (view == null)
            viewConverter =
                    LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        RetrieveContactRecord currentUser = mUserRecords.get(position);

        TextView txtName =
                (TextView) viewConverter.findViewById(R.id.list_names);
        txtName.setText(currentUser.getName());

        CircleImageView profileImage =
                (CircleImageView) viewConverter.findViewById(R.id.profile_image);

        TextView txtIntro =
                (TextView) viewConverter.findViewById(R.id.list_intro);
        txtIntro.setText(currentUser.getIntro());

        byte[] imgByte = currentUser.getPicture();
        if (imgByte != null) {
            ByteArrayInputStream imageStream = new ByteArrayInputStream(imgByte);
            profileImage.setImageBitmap(BitmapFactory.decodeStream(imageStream));
        } else {
            profileImage.setImageResource(R.drawable.ic_person);
        }

        return viewConverter;
    }

    void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mUserRecords.clear();
        if (charText.length() == 0) {
            mUserRecords.addAll(arrayList);
        } else {
            for (RetrieveContactRecord record : arrayList) {
                if (record.getName().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    mUserRecords.add(record);
                }else if (record.getIntro().toLowerCase(Locale.getDefault())
                        .contains(charText)){
                    mUserRecords.add(record);
                }
            }
        }
        notifyDataSetChanged();
    }
}

