package me.thuongle.adaptersample.data;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by thuongleit on 8/11/16.
 */
public class TaskRepositoryImpl implements TaskRepository {
    private int mIndex = 0;

    @Override
    public Observable<List<Task>> getTasks() {
        List<Integer> ids = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            ids.add(i);
        }
        return Observable
                .from(ids)
                .doOnNext(index -> mIndex = index)
                .flatMap(index -> Observable.just(createTask(index)))
                .toList();
    }

    @Override
    public Observable<Task> createTask() {
        return Observable.just(createTask(++mIndex));
    }

    private Task createTask(int index) {
        String title = "Task %s";
        String description = "Description %s";

        Task task = new Task();
        task.id = index;
        task.title = String.format(title, task.id);
        task.description = String.format(description, task.id);

        return task;
    }
}
