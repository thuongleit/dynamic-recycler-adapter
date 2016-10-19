package me.thuongle.dynamicadapter.handler;

/**
 * Interface for receiving notification when an {@link T} is long-clicked.
 *
 * @param <T> model type of the object to be sent in long-click event
 */
public interface LongClickHandler<T> {

    void onLongClick(T model);
}