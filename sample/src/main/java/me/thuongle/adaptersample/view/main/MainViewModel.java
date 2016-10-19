package me.thuongle.adaptersample.view.main;

import android.databinding.BaseObservable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;

import me.thuongle.adaptersample.BR;
import me.thuongle.adaptersample.data.Task;
import me.thuongle.dynamicadapter.handler.ItemBinder;

import java.util.List;

/**
 * Created by thuongleit on 8/11/16.
 */
public class MainViewModel extends BaseObservable {
    public final ObservableBoolean isLoading = new ObservableBoolean();
    public final ObservableBoolean isEmpty = new ObservableBoolean();
    public final ObservableList<Task> data = new ObservableArrayList<>();
    public final ItemBinder itemBinder = new ItemBinder() {
        @Override
        public int getBindingVariable() {
            return BR.item;
        }

        @Override
        public int getLayoutRes() {
            return me.thuongle.adaptersample.R.layout.item_task;
        }
    };

    public MainViewModel() {
        isLoading.set(false);
        isEmpty.set(false);
        data.clear();
    }

    public void setData(List<Task> tasks) {
        isLoading.set(false);
        if (tasks != null) {
            data.addAll(tasks);
        } else {
            isEmpty.set(true);
        }
    }

    public void loading() {
        isLoading.set(true);
        data.clear();
        isEmpty.set(false);
    }

    public void addItem(Task task) {
        if (task != null) {
            data.add(task);
        }
        if (!data.isEmpty()) {
            isEmpty.set(false);
        }
    }


    public void addItem(int position, Task task) {
        if (task != null) {
            data.add(position, task);
        }
        if (!data.isEmpty()) {
            isEmpty.set(false);
        }
    }

    public void removeItem(Task task) {
        data.remove(task);

        if (data.isEmpty()) {
            isEmpty.set(true);
        }
    }
}
