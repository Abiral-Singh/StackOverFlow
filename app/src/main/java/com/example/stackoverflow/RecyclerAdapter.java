package com.example.stackoverflow;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyRecyclerViewHolder> {

    ArrayList<String> data;
    Context context;
    private OnViewClickListener onViewClickListener;

    public RecyclerAdapter(Context context, ArrayList<String> data,OnViewClickListener onViewClickListener) {
        this.data = data;
        this.context = context;
        this.onViewClickListener = onViewClickListener;
    }
    @Override
    public MyRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_view_layout,parent,false);
        return new MyRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyRecyclerViewHolder holder, int position) {
        holder.textView.setText(data.get(position));
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onViewClickListener.onViewClick(holder.textView.getText().toString());
               // Toast.makeText(context,holder.textView.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    public interface OnViewClickListener{
        void onViewClick(String tag);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyRecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        RelativeLayout relativeLayout;

        public MyRecyclerViewHolder(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.textView_post);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout_recycler);
        }
    }
}
