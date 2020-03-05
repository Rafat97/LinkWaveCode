package com.example.linkwave;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;



public class ContactListAdepter extends BaseAdapter {

    List<String> ContactName;
    Context myContext;
    LayoutInflater myInflater;


    public ContactListAdepter(List<String> contactNamr, Context myContext) {
        ContactName = contactNamr;
        this.myContext = myContext;
    }

    @Override
    public int getCount() {
        return ContactName.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            myInflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
            convertView = myInflater.inflate(R.layout.contacts_list_item,parent,false);

        }
        TextView CoantactNameTextView = (TextView) convertView.findViewById(R.id.ContactListTextview);

        CoantactNameTextView.setText(ContactName.get(position));

        return convertView;
    }
}
