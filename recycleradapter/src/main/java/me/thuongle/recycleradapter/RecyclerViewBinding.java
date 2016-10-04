package me.thuongle.recycleradapter;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import me.thuongle.recycleradapter.handler.ChildItemsClickBinder;
import me.thuongle.recycleradapter.handler.ClickHandler;
import me.thuongle.recycleradapter.handler.ItemBinder;
import me.thuongle.recycleradapter.handler.ItemTouchHandler;
import me.thuongle.recycleradapter.handler.LongClickHandler;
import me.thuongle.recycleradapter.handler.OnItemTouchListener;

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