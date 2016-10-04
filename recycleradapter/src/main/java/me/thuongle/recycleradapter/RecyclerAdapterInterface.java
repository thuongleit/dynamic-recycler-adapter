package me.thuongle.recycleradapter;

import android.support.annotation.Nullable;

import me.thuongle.recycleradapter.handler.ChildItemsClickBinder;
import me.thuongle.recycleradapter.handler.ClickHandler;
import me.thuongle.recycleradapter.handler.ItemTouchHandler;
import me.thuongle.recycleradapter.handler.LongClickHandler;

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
