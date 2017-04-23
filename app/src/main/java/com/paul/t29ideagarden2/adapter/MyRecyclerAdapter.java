package com.paul.t29ideagarden2.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paul.t29ideagarden2.R;
import com.paul.t29ideagarden2.bean.Idea;
import com.paul.t29ideagarden2.views.PaletteViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by paul on 4/16/17.
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<PaletteViewHolder> {
    private List<Idea> ideas;

    public MyRecyclerAdapter(List<Idea> ideas) {
        this.ideas = new ArrayList<Idea>();
        this.ideas.addAll(ideas);
    }

    @Override
    public PaletteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.card_view, parent, false);

        return new PaletteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PaletteViewHolder holder, int position) {
        Idea idea = ideas.get(position);
        holder.ideaContent.setText(idea.getTitle());
        String tempIdeaDetailsString = "+"+idea.getShared();
        holder.ideaDetails.setText(tempIdeaDetailsString);
        int colorTemp = colorArray[position%6];//tt：这里使用取余来设置颜色，希望没有越界
        holder.card.setCardBackgroundColor(colorTemp);
        // 如果设置了回调，则设置点击事件
        if (mOnItemClickListener != null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemLongClick(holder.itemView, pos);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return ideas.size();
    }
    int[] colorArray={Color.parseColor("#d32f2f"),Color.parseColor("#ff4081"),Color.parseColor("#7b1fa2"),Color.parseColor("#536dfe"),Color.parseColor("#388e3c"),Color.parseColor("#ff5722")};
    public interface OnItemClickListener
    {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;


    //The method that will be used in MainActivity to set OnClickListener;
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener)
    {
        this.mOnItemClickListener = mOnItemClickListener;
    }
}
