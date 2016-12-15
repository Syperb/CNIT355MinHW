package com.example.cnit355.minhw;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brad on 12/14/2016.
 */

public class ListAdapter extends BaseAdapter {

    Context context;

    protected List<String> listTasks;
    LayoutInflater inflater;

    public ListAdapter(Context context, ArrayList taskList) {
        this.listTasks= taskList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void ListAdapter(Context context, ArrayList<String> listTasks) {
        this.listTasks= listTasks;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public int getCount() {
        return listTasks.size();
    }

    public String getItem(int position) {
        return listTasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {

            holder = new ViewHolder();
            convertView = this.inflater.inflate(R.layout.listview_layout,
                    parent, false);

            holder.txtName = (TextView) convertView
                    .findViewById(R.id.txt_name);
            holder.txtDue = (TextView) convertView
                    .findViewById(R.id.txt_due);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String task = listTasks.get(position);



        holder.txtName.setText(task.substring(0, task.indexOf("`")));
        holder.txtDue.setText(task.substring(task.indexOf("`") + 1, task.length())); //using ("" +) instead of .toString()

        return convertView;
    }

    private class ViewHolder {
        TextView txtName;
        TextView txtDue;
    }

    private void getItemID(int i){

    }

}
