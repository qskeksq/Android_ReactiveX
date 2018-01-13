package com.example.administrator.rxandroidbasic08;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.rxandroidbasic08.domain.Data;
import com.example.administrator.rxandroidbasic08.domain.Row;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.subjects.ReplaySubject;

/**
 * Created by Administrator on 2017-07-20.
 */

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.Holder> {

    List<String> rows = new ArrayList<>();

    public void setDat(List<String> rows){
        this.rows = rows;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.textView.setText(rows.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return rows.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView textView;
        public Holder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
        }
    }
}
