package com.jokotech.recycleradapter;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.jokotech.recycleradapter.handler.ChildItemsClickBinder;
import com.jokotech.recycleradapter.handler.ClickHandler;
import com.jokotech.recycleradapter.handler.ItemBinder;
import com.jokotech.recycleradapter.handler.ItemTouchHandler;
import com.jokotech.recycleradapter.handler.LongClickHandler;
import com.jokotech.recycleradapter.handler.OnItemTouchListener;

import java.util.List;

public class RecyclerViewBinding {

    @SuppressWarnings("unchecked")
    @BindingAdapter(value = {"itemBinder", "items", "swipeEnabled", "clickHandler", "longClickHandler", "itemTouchHandler", "childItemsClickBinder"}, requireAll = false)
    public static <T> void setItemViewBinder(RecyclerView recyclerView,
                                             ItemBinder itemBinder,
                                             List<T> items,
                                             Boolean swipeEnabled,
                                             ClickHandler<T> clickHandler,
                                             LongClickHandler<T> longClickHandler,
                                             ItemTouchHandler<T> itemTouchHandler,
                                             ChildItemsClickBinder<T> childItemsClickBinder) {

        if (itemBinder == null) {
            throw new IllegalArgumentException("itemViewBinder must not be null");
        }

        RecyclerAdapterInterface<T> adapter = (RecyclerAdapterInterface<T>) recyclerView.getAdapter();

        if (adapter == null) {
            adapter = new BindingRecyclerViewAdapter<>(itemBinder, items);
            adapter.setClickHandler(clickHandler);
            adapter.setLongClickHandler(longClickHandler);
            adapter.setItemTouchHandler(itemTouchHandler);
            adapter.setChildItemsClickBinder(childItemsClickBinder);

            if (swipeEnabled != null && swipeEnabled) {
                ItemTouchHelper.Callback callback = new ItemTouchHelperCallback((OnItemTouchListener) adapter, recyclerView.getContext());
                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
                itemTouchHelper.attachToRecyclerView(recyclerView);
            }
            recyclerView.setAdapter((BindingRecyclerViewAdapter) adapter);
        } else {
            adapter.setItems(items);
        }
    }
}