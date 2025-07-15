package com.vinaykpro.vincihealthbot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class ChatAdapter extends RecyclerView.Adapter {
    List<String> messageList = new ArrayList<>();
    Context c;
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    public ChatAdapter(List<String> messageList,Context c) {
        this.messageList = messageList;
        this.c = c;
    }

    @Override
    public int getItemViewType(int position) {
        String[] parts = messageList.get(position).split(Pattern.quote("{vin}"));
        return Integer.parseInt(parts[0]);
        //return 2;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;

        if (viewType == 0) {
            view = layoutInflater.inflate(R.layout.sender_msg_layout, parent, false);
            return new SentViewHolder(view);
        } else if(viewType == 1) {
            view = layoutInflater.inflate(R.layout.reciever_msg_layout, parent, false);
            return new RecievedViewHolder(view);
        } else {
            view = layoutInflater.inflate(R.layout.reciever_msg_with_options_layout, parent, false);
            return new RecievedQuestionWithOptionsViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String[] parts = messageList.get(position).split(Pattern.quote("{vin}"));
        if(getItemViewType(position)==0)
        {
            SentViewHolder sentViewHolder = (SentViewHolder) holder;
            sentViewHolder.sentmessage.setText(parts[1]);
            sentViewHolder.senttime.setText(parts[2]);
        } else if(getItemViewType(position) == 1) {
            RecievedViewHolder recievedViewHolder = (RecievedViewHolder) holder;
            recievedViewHolder.recievedmessage.setText(parts[1]);
            recievedViewHolder.recievedtime.setText(parts[2]);
        } else {
            String options[] = parts[2].split("\\|");
            List<String> l = new ArrayList<>(Arrays.asList(options));
            RecievedQuestionWithOptionsViewHolder recievedQnAViewHolder = (RecievedQuestionWithOptionsViewHolder) holder;
            recievedQnAViewHolder.questionview.setText(parts[1]);
            recievedQnAViewHolder.linearLayoutManager = new LinearLayoutManager(recievedQnAViewHolder.itemView.getContext(),RecyclerView.VERTICAL,false);
            recievedQnAViewHolder.linearLayoutManager.setInitialPrefetchItemCount(messageList.size());
            recievedQnAViewHolder.optionsRecyclerView.setLayoutManager(recievedQnAViewHolder.linearLayoutManager);
            recievedQnAViewHolder.adapter = new OptionsAdapter(l,c);
            recievedQnAViewHolder.optionsRecyclerView.setAdapter(recievedQnAViewHolder.adapter);
            recievedQnAViewHolder.optionsRecyclerView.setRecycledViewPool(viewPool);
            recievedQnAViewHolder.adapter.notifyDataSetChanged();
            recievedQnAViewHolder.questionview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recievedQnAViewHolder.adapter.notifyDataSetChanged();
                }
            });
            /*linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new Adapter(messageList,getApplicationContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();*/
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
}

class SentViewHolder extends RecyclerView.ViewHolder{
    TextView sentmessage,senttime;
    public SentViewHolder(@NonNull View itemView) {
        super(itemView);
        sentmessage = itemView.findViewById(R.id.textView3);
        senttime = itemView.findViewById(R.id.textView4);
    }
}

class RecievedViewHolder extends RecyclerView.ViewHolder{
    TextView recievedmessage,recievedtime;
    public RecievedViewHolder(@NonNull View itemView) {
        super(itemView);
        recievedmessage = itemView.findViewById(R.id.textView5);
        recievedtime = itemView.findViewById(R.id.textView6);
    }
}

class RecievedQuestionWithOptionsViewHolder extends  RecyclerView.ViewHolder {
    RecyclerView optionsRecyclerView;
    TextView questionview;
    LinearLayoutManager linearLayoutManager;
    OptionsAdapter adapter;
    public RecievedQuestionWithOptionsViewHolder(@NonNull View itemView) {
        super(itemView);
        questionview = itemView.findViewById(R.id.questionview);
        optionsRecyclerView = itemView.findViewById(R.id.optionsrecyclerview);
    }
}