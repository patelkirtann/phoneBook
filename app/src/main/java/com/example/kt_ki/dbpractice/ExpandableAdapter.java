package com.example.kt_ki.dbpractice;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.IntentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by kt_ki on 12/2/2016.
 */

class ExpandableAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> headData;
    private HashMap<String, List<String>> childData;

    ExpandableAdapter(Context context, List<String> headData,
                      HashMap<String, List<String>> childData) {
        super();
        this.context = context;
        this.headData = headData;
        this.childData = childData;
    }

    @Override
    public int getGroupCount() {
        return this.headData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.childData.get(this.headData.get(groupPosition)).size();
//        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.headData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.childData.get(this.headData.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View view, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_head, null);
        }

        TextView lblListHeader = (TextView) view
                .findViewById(R.id.head);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View view, ViewGroup child) {
        final String childText = (String) getChild(groupPosition, childPosition);

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_child, null);
        }

        TextView txtListChild = (TextView) view.findViewById(R.id.tv_id);
        txtListChild.setText(childText);
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }
}
