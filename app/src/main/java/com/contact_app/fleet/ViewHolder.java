package com.contact_app.fleet;

import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

class ViewHolder {
    TextView txtName;
    TextView txtIntro;
    CircleImageView profileImage;

    ViewHolder(View viewConverter) {
        txtName = (TextView) viewConverter.findViewById(R.id.tv_name);
        profileImage = (CircleImageView) viewConverter.findViewById(R.id.profile_image);
        txtIntro = (TextView) viewConverter.findViewById(R.id.list_intro);
    }
}
