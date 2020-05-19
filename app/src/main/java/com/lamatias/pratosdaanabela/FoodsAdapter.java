package com.lamatias.pratosdaanabela;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lamatias.pratosdaanabela.logic.Food;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FoodsAdapter extends RecyclerView.Adapter<FoodsAdapter.ViewHolder> implements Serializable {

    private List<Food> list;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    FoodsAdapter(Context context, Iterator<Food> it) {
        this.mInflater = LayoutInflater.from(context);
        list=new ArrayList<>();
        while(it.hasNext ())
            list.add (it.next ());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.myTextView.setText (list.get (position).getFood ( ));
    }

    @Override
    public int getItemCount() {
        return list.size ();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        Button button;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.foodRV);
            myTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            myTextView.setSelected(true);
            myTextView.setSingleLine(true);
            button = itemView.findViewById(R.id.trashB);
            itemView.setOnClickListener(this);
            button.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                if(view.getId() == button.getId())
                    mClickListener.onDeleteFoodClick(view, getAdapterPosition());
                else
                    mClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    // convenience method for getting data at click position
    Food getItem(int id) {
        return list.get (id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
        void onDeleteFoodClick(View view, int position);
    }
}
