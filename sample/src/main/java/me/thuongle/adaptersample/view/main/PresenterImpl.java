package me.thuongle.adaptersample.view.main;

import android.support.annotation.NonNull;
import android.util.Log;

import me.thuongle.adaptersample.BR;
import me.thuongle.adaptersample.data.Task;
import me.thuongle.adaptersample.data.TaskRepository;
import me.thuongle.dynamicadapter.handler.ChildItemsClickBinder;
import me.thuongle.dynamicadapter.handler.ClickHandler;
import me.thuongle.dynamicadapter.handler.ItemTouchHandler;
import me.thuongle.dynamicadapter.handler.LongClickHandler;

import java.util.Arrays;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by thuongleit on 8/11/16.
 */
public class PresenterImpl implements MainContract.Presenter {
    private static final String TAG = PresenterImpl.class.getSimpleName();

    @NonNull
    private MainContract.View mView;
    @NonNull
    private final MainViewModel mViewModel;
    @NonNull
    private final TaskRepository mTaskRepository;
    @NonNull
    private CompositeSubscription mSubscriptions;
    private Subscription mPendingSubscription;

    public PresenterImpl(@NonNull MainContract.View view, @NonNull MainViewModel viewModel, @NonNull TaskRepository taskRepository) {
        this.mView = view;
        this.mViewModel = viewModel;
        this.mTaskRepository = taskRepository;

        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {
        mSubscriptions.add(getTasks());
    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void onDestroy() {
        mSubscriptions = null;
        mView = null;
    }

    public ClickHandler<Task> clickHandler = model -> mView.onItemClick(model);

    public LongClickHandler<Task> longClickHandler = model -> mView.onItemLongClick(model);

    public ItemTouchHandler<Task> itemTouchHandler = new ItemTouchHandler<Task>() {
        @Override
        public void onItemMove(int position, Task model) {

        }

        @Override
        public void onItemDismiss(int position, Task model) {
            try {
                final Task clone = (Task) model.clone();

                mView.addPendingRemove(position, clone);
            } catch (CloneNotSupportedException e) {
                Log.e(TAG, e.getMessage(), e);
                return;
            }
            mViewModel.removeItem(model);
        }
    };

    public ChildItemsClickBinder<Task> childItemsClickBinder = new ChildItemsClickBinder<Task>() {
        @Override
        public int getBindingVariable() {
            return BR.childHandlers;
        }

        @Override
        public List<ClickHandler<Task>> clickHandlers() {
            ClickHandler<Task> clickHandler1 = model -> {
                mView.onChildItem1Click(model);
                mViewModel.removeItem(model);
            };
            ClickHandler<Task> clickHandler2 = model -> mView.onChildItem2Click(model);

            return Arrays.asList(clickHandler1, clickHandler2);
        }
    };

    public Subscription getTasks() {
        mViewModel.loading();
        return mTaskRepository
                .getTasks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mViewModel::setData);
    }

    @Override
    public void createTask() {
        mSubscriptions.add(mTaskRepository
                .createTask()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> {
                    mViewModel.addItem(item);
                    mView.onItemCreated(item);
                }));
    }

    @Override
    public void undoRemovedProduct(int position, Task task) {
        mViewModel.addItem(position, task);
    }
}
