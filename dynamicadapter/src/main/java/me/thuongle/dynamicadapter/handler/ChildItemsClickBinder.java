package me.thuongle.dynamicadapter.handler;

import java.util.List;

public interface ChildItemsClickBinder<T> {

    int getBindingVariable();

    List<ClickHandler<T>> clickHandlers();
}
