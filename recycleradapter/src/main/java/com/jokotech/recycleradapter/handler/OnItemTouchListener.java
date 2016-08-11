package com.jokotech.recycleradapter.handler;

import android.view.View;

public interface OnItemTouchListener {

    boolean onItemMove(View v, int fromPosition, int toPosition);

    boolean onItemDismiss(View v, int position);
}
