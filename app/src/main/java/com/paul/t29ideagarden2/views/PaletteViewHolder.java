package com.paul.t29ideagarden2.views;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.paul.t29ideagarden2.R;

/**
 * Created by paul on 3/28/17.
 */

public class PaletteViewHolder extends RecyclerView.ViewHolder {

    protected TextView titleText;
    protected TextView contentText;
    protected CardView card;

    public PaletteViewHolder(View itemView) {
        super(itemView);
        titleText = (TextView) itemView.findViewById(R.id.name);
        contentText = (TextView) itemView.findViewById(R.id.hexValue);
        card = (CardView) itemView;
    }
}