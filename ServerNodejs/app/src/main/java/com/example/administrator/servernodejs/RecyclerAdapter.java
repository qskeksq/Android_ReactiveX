package com.example.administrator.servernodejs;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.servernodejs.domain.Bbs;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.Holder> {

    List<Bbs> data;
    LayoutInflater inflater;

    public RecyclerAdapter(List<Bbs> data, Context context) {
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Bbs bbs = data.get(position);
        holder.setTitle(bbs.title);
        holder.setContent(bbs.content);
        holder.setAuthor(bbs.author);
        holder.setDate(bbs.date);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class Holder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView content;
        private TextView author;
        private TextView date;

        public Holder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View view) {
            title = (TextView) view.findViewById(R.id.title);
            content = (TextView) view.findViewById(R.id.content);
            author = (TextView) view.findViewById(R.id.author);
            date = (TextView) view.findViewById(R.id.date);
        }

        public void setTitle(String title) {
            this.title.setText(title);
        }

        public void setContent(String content) {
            this.content.setText(content);
        }

        public void setAuthor(String author) {
            this.author.setText(author);
        }

        public void setDate(String date) {
            this.date.setText(date);
        }
    }
}
