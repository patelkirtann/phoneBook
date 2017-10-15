package com.contact_app.fleet.CallLog;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.contact_app.fleet.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kt_ki on 8/20/2017.
 */

public class CallLogViewHolder extends RecyclerView.ViewHolder {
    CircleImageView imageView;
    TextView name;
    TextView time;
    ImageButton add;
    TextView date;
    TextView duration;

    public CallLogViewHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.tv_log_name);
        time = (TextView) itemView.findViewById(R.id.tv_log_time);
        imageView = (CircleImageView) itemView.findViewById(R.id.civ_log_image);
        add = (ImageButton) itemView.findViewById(R.id.bt_addCallLogContact);
        date = (TextView) itemView.findViewById(R.id.tv_log_date);
        duration = (TextView) itemView.findViewById(R.id.tv_log_duration);
    }
}

