package com.jokotech.adaptersample.view.main;

import com.jokotech.adaptersample.data.Task;

/**
 * Created by thuongleit on 8/11/16.
 */
public interface MainContract {

    interface View {

        void onItemClick(Task task);

        void onItemLongClick(Task task);

        void onChildItem1Click(Task task);

        void onChildItem2Click(Task task);

        void onItemCreated(Task item);

        void addPendingRemove(int position, Task task);

    }

    interface Presenter {

        void subscribe();

        void unsubscribe();

        void onDestroy();

        void createTask();

        void undoRemovedProduct(int position, Task task);
    }
}
