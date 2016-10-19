package me.thuongle.dynamicadapter.handler;

/**
 * Interface for receiving notification when an {@link T} is swiped.
 *
 * @param <T> model type of the object to be sent in long-click event
 */
public interface ItemTouchHandler<T> {

    /**
     * Action when {@link T} is moved (swiped left to right)
     *
     * @param position of the item {@link T} in recycler view (in object data)
     * @param model    object to be moved
     */
    void onItemMove(int position, T model);

    /**
     * Action when {@link T} is dismissed from recycler view
     *
     * @param position of the item {@link T} in recylcer view (in object data)
     * @param model    object to be dismissed
     */
    void onItemDismiss(int position, T model);
}
