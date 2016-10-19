package me.thuongle.dynamicadapter.handler;

/**
 * Interface for receiving notification when an {@link T} is clicked.
 *
 * @param <T> model type of the object to be sent in click event
 */
public interface ClickHandler<T> {

    void onClick(T model);
}