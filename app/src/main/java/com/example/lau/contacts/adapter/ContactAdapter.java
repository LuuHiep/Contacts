package com.example.lau.contacts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lau.contacts.R;
import com.example.lau.contacts.model.Contacts;

import java.util.List;

public class ContactAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Contacts> contactsList;

    public ContactAdapter(Context context, int layout, List<Contacts> contactsList) {
        this.context = context;
        this.layout = layout;
        this.contactsList = contactsList;
    }

    @Override
    public int getCount() {
        return contactsList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder{
        TextView tvName, tvPhoneNumber;

    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout,null);
            holder.tvName = (TextView) view.findViewById( R.id.tv_name );
            holder.tvPhoneNumber = (TextView) view.findViewById( R.id.tv_phone_number );
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Contacts contacts = contactsList.get(i);
        holder.tvName.setText(contacts.getName());
        holder.tvPhoneNumber.setText(contacts.getPhoneNumber());

        return view;
    }
}
