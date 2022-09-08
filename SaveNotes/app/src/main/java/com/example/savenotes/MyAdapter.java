package com.example.savenotes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private final LayoutInflater mInflater;
    private List<Notes> notes;
    private OnClickListener onClickListener;


    public MyAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public interface OnClickListener {
        void onLongClick(View view, int position);
        void onShortClick(View view,int position);
    }

    public void setOnClickListener(OnClickListener listener) {
        onClickListener = listener;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitleText,mTextDesc;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitleText = itemView.findViewById(R.id.text_view_title);
            mTextDesc = itemView.findViewById(R.id.text_view_description);
        }
    }
    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_content,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyAdapter.MyViewHolder holder, final int position) {
        final Notes current = notes.get(position);
        holder.mTitleText.setText(current.getTitle());
        holder.mTextDesc.setText(current.getContent());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onClickListener.onLongClick(v,position);
                return true;
            }

        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onShortClick(v,position);
            }
        });
    }

    void setNotes(List<Notes> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    public Notes getNotesAtPosition(int position){
        return notes.get(position);
    }

    @Override
    public int getItemCount() {
        if (notes != null){
            return notes.size();
        } else return 0;
    }


}
