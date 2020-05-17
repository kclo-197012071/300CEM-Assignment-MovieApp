package com.example.a300cem_assignment_movieapp;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MovieListAdapter extends BaseAdapter {

    private String[][] mMovieListArr;
    private LayoutInflater inflater;
    private int indentionBase;

    static class ViewHolder{
        LinearLayout rlBorder;
        TextView mSearchedMovieInfo_tv;
    }

    public MovieListAdapter(String[][] data, LayoutInflater inflater) {
        this.mMovieListArr = data;
        this.inflater = inflater;
        indentionBase = 100;
    }

    public MovieListAdapter(String[][] data) {
        this.mMovieListArr = data;
    }

    @Override
    public int getCount() {
        return mMovieListArr.length;
    }

    @Override
    public Object getItem(int position) {
        return mMovieListArr[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_listview_movielist, null);
            holder.rlBorder = (LinearLayout) convertView.findViewById(R.id.llBorder);
            holder.mSearchedMovieInfo_tv = (TextView) convertView.findViewById(R.id.tv_searchedMovieInfo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mSearchedMovieInfo_tv.setText(mMovieListArr[position][1]);
        holder.rlBorder.setBackgroundColor(Color.parseColor("#FFDBC9"));


        return convertView;
    }
}
