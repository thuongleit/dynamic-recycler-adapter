
package com.jokotech.recycleradapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.jokotech.recycleradapter.handler.OnItemTouchListener;
import com.jokotech.recycleradapter.util.Utils;

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final OnItemTouchListener mTouchListener;
    private Context context;
    Paint p = new Paint();

    public ItemTouchHelperCallback(OnItemTouchListener touchListener, Context context) {
        this.mTouchListener = touchListener;
        this.context = context;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        // Enable drag up and down and right swipe in right direction
        final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        final int swipeFlags = ItemTouchHelper.END;
        // final int swipeFlags =  ItemTouchHelper.END | ItemTouchHelper.START; Enable swipe in both direction
        return makeMovementFlags(dragFlags, swipeFlags);

    }

    @Override
    public long getAnimationDuration(RecyclerView recyclerView, int animationType, float animateDx, float animateDy) {
        // return animationType == ItemTouchHelper.ANIMATION_TYPE_DRAG ? DEFAULT_DRAG_ANIMATION_DURATION : DEFAULT_SWIPE_ANIMATION_DURATION;
        return animationType == ItemTouchHelper.ANIMATION_TYPE_DRAG ? DEFAULT_DRAG_ANIMATION_DURATION : 350;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        if (source.getItemViewType() != target.getItemViewType()) {
            return false;
        }

        // Notify the adapter of the move
        mTouchListener.onItemMove(source.itemView, source.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int i) {
        // Notify the adapter of the dismissal
        mTouchListener.onItemDismiss(viewHolder.itemView, viewHolder.getAdapterPosition());
    }


    @Override
    public int getBoundingBoxMargin() {
        return super.getBoundingBoxMargin();
    }

    @Override
    public void onChildDraw(final Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        // Fade out the view as it is swiped out of the parent's bounds
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            View itemView = viewHolder.itemView;

            Bitmap icon;

            if (dX > 0) {

                icon = BitmapFactory.decodeResource(context.getResources(), android.R.drawable.ic_menu_delete);

                // Set color for right swipe
                p.setColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));

                // Draw Rect with varying right side, equal to displacement dX
                c.drawRect((float) itemView.getLeft() + Utils.dpToPx(0), (float) itemView.getTop(), dX + Utils.dpToPx(0),
                        (float) itemView.getBottom(), p);

                // Set the image icon for right swipe
                c.drawBitmap(icon, (float) itemView.getLeft() + Utils.dpToPx(16), (float) itemView.getTop() +
                        ((float) itemView.getBottom() - (float) itemView.getTop() - icon.getHeight()) / 2, p);

                icon.recycle();

            }
        }
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            // Let the view holder know that this item is being moved or dragged
            ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
            itemViewHolder.onItemSelected();
        }

        super.onSelectedChanged(viewHolder, actionState);

       /* final boolean swiping = actionState == ItemTouchHelper.ACTION_STATE_SWIPE;
        swipeRefreshLayout.setEnabled(!swiping);*/
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        // Tell the view holder it's time to restore the idle state
        ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
        itemViewHolder.onItemClear();

    }

    @Override
    public float getMoveThreshold(RecyclerView.ViewHolder viewHolder) {
        //  return super.getMoveThreshold(viewHolder);
        return 0.1f;
        //  return super.getMoveThreshold(0.5f);
    }

    @Override
    public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
        //if (viewHolder instanceof RecyclerView.ViewHolder) return 1f;
        //return super.getSwipeThreshold(viewHolder);
        return 0.9f;
    }
}
