package me.thuongle.recycleradapter;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.thuongle.recycleradapter.handler.ChildItemsClickBinder;
import me.thuongle.recycleradapter.handler.ClickHandler;
import me.thuongle.recycleradapter.handler.ItemBinder;
import me.thuongle.recycleradapter.handler.ItemTouchHandler;
import me.thuongle.recycleradapter.handler.LongClickHandler;
import me.thuongle.recycleradapter.handler.OnItemTouchListener;

import java.lang.ref.WeakReference;
import java.util.List;

public class BindingRecyclerViewAdapter<T> extends RecyclerView.Adapter<BindingRecyclerViewAdapter.ViewHolder> implements View.OnClickListener, View.OnLongClickListener, OnItemTouchListener, RecyclerAdapterInterface<T> {
    private static final int ITEM_MODEL = -124;

    @NonNull
    private final WeakReferenceOnListChangedCallback mOnListChangedCallback;

    @NonNull
    private final ItemBinder mItemBinder;
    @Nullable
    private ObservableList<T> mItems;
    @Nullable
    private LayoutInflater mInflater;
    @Nullable
    private ClickHandler<T> mClickHandler;
    @Nullable
    private LongClickHandler<T> mLongClickHandler;
    @Nullable
    private ItemTouchHandler<T> mItemTouchHandler;
    @Nullable
    private ChildItemsClickBinder<T> mChildItemsClickBinder;

    public BindingRecyclerViewAdapter(@NonNull ItemBinder itemBinder, @Nullable List<T> items) {
        mItemBinder = itemBinder;
        mOnListChangedCallback = new WeakReferenceOnListChangedCallback<>(this);
        setItems(items);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int layoutId) {
        if (mInflater == null) {
            mInflater = LayoutInflater.from(viewGroup.getContext());
        }

        ViewDataBinding binding = DataBindingUtil.inflate(mInflater, layoutId, viewGroup, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        assert mItems != null;
        final T item = mItems.get(position);
        viewHolder.binding.setVariable(mItemBinder.getBindingVariable(), item);

        if (mChildItemsClickBinder != null) {
            viewHolder.binding.setVariable(mChildItemsClickBinder.getBindingVariable(), mChildItemsClickBinder.clickHandlers());
        }

        viewHolder.binding.getRoot().setTag(ITEM_MODEL, item);
        viewHolder.binding.getRoot().setOnClickListener(this);
        viewHolder.binding.getRoot().setOnLongClickListener(this);
        viewHolder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mItemBinder.getLayoutRes();
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        if (mItems != null) {
            mItems.removeOnListChangedCallback(mOnListChangedCallback);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onClick(View v) {
        if (mClickHandler != null) {
            T item = (T) v.getTag(ITEM_MODEL);
            mClickHandler.onClick(item);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean onLongClick(View v) {
        if (mLongClickHandler != null) {
            T item = (T) v.getTag(ITEM_MODEL);
            mLongClickHandler.onLongClick(item);
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean onItemMove(View v, int fromPosition, int toPosition) {
        if (mItemTouchHandler != null) {
            T item = (T) v.getTag(ITEM_MODEL);
            mItemTouchHandler.onItemMove(toPosition, item);
            return true;
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean onItemDismiss(View v, int position) {
        if (mItemTouchHandler != null) {
            T item = (T) v.getTag(ITEM_MODEL);
            mItemTouchHandler.onItemDismiss(position, item);
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setItems(@Nullable List<T> items) {
        if (mItems == items) {
            return;
        }

        if (mItems != null) {
            mItems.removeOnListChangedCallback(mOnListChangedCallback);
            notifyItemRangeRemoved(0, mItems.size());
        }

        if (items instanceof ObservableList) {
            mItems = (ObservableList<T>) items;
            notifyItemRangeInserted(0, mItems.size());
            mItems.addOnListChangedCallback(mOnListChangedCallback);
        } else if (items != null) {
            mItems = new ObservableArrayList<>();
            mItems.addOnListChangedCallback(mOnListChangedCallback);
            mItems.addAll(items);
        } else {
            this.mItems = null;
        }
    }

    @Override
    @Nullable
    public ObservableList<T> getItems() {
        return mItems;
    }

    @Override
    @Nullable
    public T getAdapterItem(int position) {
        return (mItems != null) ? mItems.get(position) : null;
    }

    @Override
    public void setClickHandler(@Nullable ClickHandler<T> clickHandler) {
        mClickHandler = clickHandler;
    }

    @Override
    public void setChildItemsClickBinder(@Nullable ChildItemsClickBinder<T> childItemsClickBinder) {
        mChildItemsClickBinder = childItemsClickBinder;
    }

    @Override
    public void setLongClickHandler(@Nullable LongClickHandler<T> longClickHandler) {
        mLongClickHandler = longClickHandler;
    }

    @Override
    public void setItemTouchHandler(@Nullable ItemTouchHandler<T> itemTouchHandler) {
        mItemTouchHandler = itemTouchHandler;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        final ViewDataBinding binding;
//        final Drawable background;

        ViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
//            background = binding.getRoot().getBackground();
        }

        @Override
        public void onItemSelected() {
//            Context context = binding.getRoot().getContext();
//            int themeColor = Utils.getThemeColor(context, R.attr.itemSelectedColor);
//            if (themeColor == 0) {
//                themeColor = Utils.getThemeColor(context, R.attr.colorAccent);
//            }
//            binding.getRoot().setBackgroundColor(ContextCompat.getColor(context, themeColor));
        }

        @Override
        public void onItemClear() {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                binding.getRoot().setBackground(background);
//            }else {
//                binding.getRoot().setBackgroundDrawable(background);
//            }
        }
    }

    private static class WeakReferenceOnListChangedCallback<T> extends ObservableList.OnListChangedCallback {

        private final WeakReference<BindingRecyclerViewAdapter<T>> adapterReference;

        public WeakReferenceOnListChangedCallback(BindingRecyclerViewAdapter<T> bindingRecyclerViewAdapter) {
            this.adapterReference = new WeakReference<>(bindingRecyclerViewAdapter);
        }

        @Override
        public void onChanged(ObservableList sender) {
            RecyclerView.Adapter adapter = adapterReference.get();
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onItemRangeChanged(ObservableList sender, int positionStart, int itemCount) {
            RecyclerView.Adapter adapter = adapterReference.get();
            if (adapter != null) {
                adapter.notifyItemRangeChanged(positionStart, itemCount);
            }
        }

        @Override
        public void onItemRangeInserted(ObservableList sender, int positionStart, int itemCount) {
            RecyclerView.Adapter adapter = adapterReference.get();
            if (adapter != null) {
                adapter.notifyItemRangeInserted(positionStart, itemCount);
            }
        }

        @Override
        public void onItemRangeMoved(ObservableList sender, int fromPosition, int toPosition, int itemCount) {
            RecyclerView.Adapter adapter = adapterReference.get();
            if (adapter != null) {
                adapter.notifyItemMoved(fromPosition, toPosition);
            }
        }

        @Override
        public void onItemRangeRemoved(ObservableList sender, int positionStart, int itemCount) {
            RecyclerView.Adapter adapter = adapterReference.get();
            if (adapter != null) {
                adapter.notifyItemRangeRemoved(positionStart, itemCount);
            }
        }
    }
}