package com.omdeep.roomdatabaseapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomListAdapterForRoomListview extends BaseAdapter {
    private List<User> list;
    private Context context;
    public CustomListAdapterForRoomListview(List<User> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public User getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.list_adapter, parent, false);
        TextView tvName = convertView.findViewById(R.id.item_name);
        User data = list.get(position);
        tvName.setText(data.getFirstName()+" "+data.getLastName());

        return convertView;
    }

    public void setData(List<User> userList){
        userList.addAll(list);
        notifyDataSetChanged();
    }
}
