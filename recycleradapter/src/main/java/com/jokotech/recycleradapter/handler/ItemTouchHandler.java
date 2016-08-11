package com.jokotech.recycleradapter.handler;

public interface ItemTouchHandler<T> {

    void onItemMove(int position, T model);

    void onItemDismiss(int position, T model);
}
