package com.vinaykpro.vincihealthbot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OptionsAdapter extends RecyclerView.Adapter {
    List<String> optionsList;
    Context c;
    public OptionsAdapter(List<String> optionsList,Context c) {
        this.optionsList = optionsList;
        this.c = c;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.option_layout, parent, false);
        return new OptionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        OptionViewHolder viewHolder = (OptionViewHolder) holder;
        viewHolder.optionValue.setText(optionsList.get(position));
        viewHolder.optionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) c).choosenOption(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return optionsList.size();
    }

    public class OptionViewHolder extends RecyclerView.ViewHolder {
        TextView optionValue;
        ConstraintLayout optionbutton;
        public OptionViewHolder(@NonNull View itemView) {
            super(itemView);
            optionbutton = itemView.findViewById(R.id.optionbutton);
            optionValue = itemView.findViewById(R.id.optionvalue);
        }
    }
}
