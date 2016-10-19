package me.thuongle.adaptersample.data;

import java.util.List;

import rx.Observable;

/**
 * Created by thuongleit on 8/11/16.
 */
public interface TaskRepository {

    Observable<List<Task>> getTasks();

    Observable<Task> createTask();
}
