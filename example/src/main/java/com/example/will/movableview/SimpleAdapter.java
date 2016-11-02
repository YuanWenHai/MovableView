package com.example.will.movableview;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.will.RemovableView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Will on 2016/10/18.
 */

public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.ViewHolder> {
    private ArrayList<String> list = new ArrayList<>();
    private int lastAnimatedIndex = -1;
    private RecyclerView mRecyclerView;
    public SimpleAdapter(){
        for(int i=0;i<100;i++){
            list.add(String.valueOf(i));
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.text.setText(list.get(position));
        holder.text.setBackgroundColor(Color.rgb(getRandomNumber(),getRandomNumber(),getRandomNumber()));
        animateView(holder.itemView,position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView text;
        public ViewHolder(View view){
            super(view);
            //((RemovableView) view).disallowMove(true);
            ((RemovableView) view).setOnRemoveCallback(new RemovableView.OnRemoveCallback() {
                @Override
                public void onRemove(View view) {
                    Toast.makeText(mRecyclerView.getContext(), "you removed "+list.get(getAdapterPosition()), Toast.LENGTH_SHORT).show();
                    list.remove(getAdapterPosition());
                    lastAnimatedIndex = getAdapterPosition()-1;
                    notifyDataSetChanged();
                }
            });
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mRecyclerView.getContext(),"you clicked "+list.get(getAdapterPosition()),Toast.LENGTH_SHORT).show();
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(mRecyclerView.getContext(),"you  long clicked "+list.get(getAdapterPosition()),Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
            text = (TextView) view.findViewById(R.id.text);
            ((RemovableView) view).setAutoRemoveMultiplier(0.3f);
        }
    }
    private void animateView(final View view, final int position){
        if(lastAnimatedIndex < position){
            final TranslateAnimation translateAnimation = new TranslateAnimation(mRecyclerView.getWidth(),mRecyclerView.getX(),0,0);
            translateAnimation.setDuration(300);
            view.setVisibility(View.INVISIBLE);
            mRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.startAnimation(translateAnimation);
                    view.setVisibility(View.VISIBLE);
                    lastAnimatedIndex = position;
                }
            },200);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }
    private int getRandomNumber(){
        return  new Random().nextInt(99-10)+10;
    }
}
