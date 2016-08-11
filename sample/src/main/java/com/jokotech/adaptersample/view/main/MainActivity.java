package com.jokotech.adaptersample.view.main;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jokotech.adaptersample.R;
import com.jokotech.adaptersample.data.Task;
import com.jokotech.adaptersample.data.TaskRepository;
import com.jokotech.adaptersample.data.TaskRepositoryImpl;
import com.jokotech.adaptersample.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private MainContract.Presenter mPresenter;
    private MainViewModel mainViewModel = new MainViewModel();
    private TaskRepository taskRepository = new TaskRepositoryImpl();
    private CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mPresenter = new PresenterImpl(this, mainViewModel, taskRepository);

        setSupportActionBar(binding.toolbar);

        binding.layoutContent.recyclerView.setHasFixedSize(true);
        binding.layoutContent.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        binding.setPresenter((PresenterImpl) mPresenter);
        binding.setViewModel(mainViewModel);

        mCoordinatorLayout = binding.coordinatorLayout;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mPresenter == null) {
            mPresenter = new PresenterImpl(this, mainViewModel, taskRepository);
        }
        mPresenter.subscribe();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.unsubscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void onItemClick(Task task) {
        Toast.makeText(MainActivity.this, "onItemClick task " + task.title, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemLongClick(Task task) {
        Toast.makeText(MainActivity.this, "onItemLongClick task " + task.title, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onChildItem1Click(Task task) {
        Toast.makeText(MainActivity.this, "onChildItem1Click task " + task.title, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onChildItem2Click(Task task) {
        Toast.makeText(MainActivity.this, "onChildItem2Click task " + task.title, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemCreated(Task item) {
        Toast.makeText(MainActivity.this, "New Item Created", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addPendingRemove(int position, Task task) {
        final Snackbar snackbar = Snackbar.make(mCoordinatorLayout, "Item deleted", Snackbar.LENGTH_LONG)
                .setAction("Undo", view -> {
                    mPresenter.undoRemovedProduct(position, task);
                });
        View view = snackbar.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snackbar.show();

        Handler handler = new Handler();
        handler.postDelayed(snackbar::dismiss, 2000);
    }
}
