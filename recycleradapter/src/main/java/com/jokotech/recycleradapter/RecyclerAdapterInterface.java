package com.jokotech.recycleradapter;

import android.support.annotation.Nullable;

import com.jokotech.recycleradapter.handler.ChildItemsClickBinder;
import com.jokotech.recycleradapter.handler.ClickHandler;
import com.jokotech.recycleradapter.handler.ItemTouchHandler;
import com.jokotech.recycleradapter.handler.LongClickHandler;

import java.util.List;

public interface RecyclerAdapterInterface<T> {

    void setItems(@Nullable List<T> items);

    @Nullable List<T> getItems();

    @Nullable T getAdapterItem(int position);

    void setClickHandler(@Nullable ClickHandler<T> clickHandler);

    void setChildItemsClickBinder(@Nullable ChildItemsClickBinder<T> childItemsClickBinder);

    void setLongClickHandler(@Nullable LongClickHandler<T> longClickHandler);

    void setItemTouchHandler(@Nullable ItemTouchHandler<T> itemTouchHandler);
}
