package me.thuongle.dynamicadapter;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.List;

import me.thuongle.dynamicadapter.handler.ChildItemsClickBinder;
import me.thuongle.dynamicadapter.handler.ClickHandler;
import me.thuongle.dynamicadapter.handler.ItemBinder;
import me.thuongle.dynamicadapter.handler.ItemTouchHandler;
import me.thuongle.dynamicadapter.handler.LongClickHandler;
import me.thuongle.dynamicadapter.util.ItemTouchHelperCallback;
import me.thuongle.dynamicadapter.util.OnItemTouchListener;

/*
 * Data Binding Binder class 
 */
public class RecyclerViewBinding {

    @SuppressWarnings("unchecked")
    @BindingAdapter(value = {"itemBinder", "items", "swipeEnabled", "clickHandler", "longClickHandler", "itemTouchHandler", "childItemsClickBinder"}, requireAll = false)
    public static <T> void setBinder(RecyclerView recyclerView,
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