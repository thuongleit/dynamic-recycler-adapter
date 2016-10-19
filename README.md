# Creating Dynamic RecyclerView using Android Data Binding

[![Release](https://img.shields.io/badge/jcenter-1.0.0-blue.svg)](https://bintray.com/thuongleit/maven/dynamic-recycler-adapter)

## Overview

This library provides the simple and flexible way to create dynamic RecyclerView using Android Data Binding framework.

Add library as Gradle dependency

```gradle
    repositories {
        jcenter()
    }
    
    dependencies {
		compile 'me.thuongle:dynamic-recycler-adapter:0.2.0'
    }

    // Don't forget to enable data binding
    dataBinding {
        enabled = true
    }
```

![demo](https://github.com/thuongleit/dynamic-recycler-adapter/blob/master/art/demo.gif)

## Features
- [x] Easy and flexible to create recycler view and handle events by Android Data Binding. (Don't deal with adapter anymore).
	- [x] Create and bind objects to recycler view.
	- [x] Handle onClick event of the recycler view.
	- [x] Handle onLongClick event of the recycler view.
	- [x] Handle swipe-to-dismiss item.
	- [x] Handle onClick on child item.
- [ ] Easy and flexible to create expandable recycler view by Android Data Binding.

## Usage

### 1. Define POJO object (if you need it)
```java
public class Task {

    public long id;

    public String title;

    public String description;
}
```

### 2. Create Recycler View layout and item layout using Android Data Binding
__content_main.xml__

```xml
 <layout ...>
 
 	<data>
		<variable
            name="presenter"
            type="me.thuongle.adaptersample.view.main.PresenterImpl" />

        <variable
            name="viewModel"
            type="me.thuongle.adaptersample.view.main.MainViewModel" />
	</data>

	<android.support.v7.widget.RecyclerView
		android:id="@+id/recycler_view"
      	android:layout_width="match_parent"
      	android:layout_height="match_parent"
      	app:itemBinder="@{viewModel.itemBinder}"
      	app:items="@{viewModel.data}"
     	app:clickHandler="@{presenter.clickHandler}"
      	app:longClickHandler="@{presenter.longClickHandler}"
      	app:childItemsClickBinder="@{presenter.childItemsClickBinder}"
      	app:swipeEnabled="@{true}"
      	app:itemTouchHandler="@{presenter.itemTouchHandler}" />
</layout>
```
__Usage__

- `presenter`: Your custom Presenter in MVP Pattern
- `viewModel`: Your custom ViewModel in MVVM Pattern
(more detail: checkout the [Android Architecture Blueprints](https://github.com/googlesamples/android-architecture) or [my custom architecture](https://github.com/thuongleit/android-guidelines/blob/master/android-architecture.md) )

- `app:itemBinder`: required field - [ItemBinder](https://github.com/thuongleit/dynamic-recycler-adapter/blob/master/recycleradapter/src/main/java/com/jokotech/recycleradapter/handler/ItemBinder.java) type - bind the recycler view layout with item layout.

- `app:items`: required field - your POJO object list/array - data model.

- `app:clickHandler`: optional - [ClickHandler](https://github.com/thuongleit/dynamic-recycler-adapter/blob/master/recycleradapter/src/main/java/com/jokotech/recycleradapter/handler/ClickHandler.java) type - handle onClick event of the recycler view.

- `app:longClickHandler`: optional - [LongClickHandler](https://github.com/thuongleit/dynamic-recycler-adapter/blob/master/recycleradapter/src/main/java/com/jokotech/recycleradapter/handler/LongClickHandler.java) type - handle longClick event of the recycler view.

- `app:childItemsClickBinder`: optional - [ChildItemsClickBinder](https://github.com/thuongleit/dynamic-recycler-adapter/blob/master/recycleradapter/src/main/java/com/jokotech/recycleradapter/handler/ChildItemsClickBinder.java) type - handle onClick event on child item layout.

- `app:swipeEnabled`: optional - Boolean type - Enable/Disbale swipe to dismiss/delete item.

- `app:itemTouchHandler`: optional - [ItemTouchHandler](https://github.com/thuongleit/dynamic-recycler-adapter/blob/master/recycleradapter/src/main/java/com/jokotech/recycleradapter/handler/ItemTouchHandler.java) type - handle onSwipe event to dismiss/delete item.

__item_task.xml__

```xml
<layout...>
	<data>
		<import type="java.util.List" />
		
		<import type="me.thuongle.dynamicadapter.handler.ClickHandler" />
		
		<variable
			name="item"
			type="me.thuongle.adaptersample.data.Task" />
			
		<variable
            name="childHandlers"
            type="List" />
    </data>

	<android.support.v7.widget.CardView...>
		<LinearLayout...>
			<TextView
				...
				android:text="@{item.title}" />

			<TextView
				...
				android:text="@{item.description}"/>
				
			<Button
				...
				android:onClick="@{() -> ((ClickHandler) (childHandlers[0])).onClick(item)}"
				android:text="Delete" />
			<Button
				...
				android:onClick="@{() -> ((ClickHandler) (childHandlers[1])).onClick(item)}"
				android:text='Action 2' />
        </LinearLayout>
    </android.support.v7.widget.CardView>
</layout>
```
__Note__

- Define `android:onClick` if you want to handle onClick event on child items in this layout.

### 3. Create binder objects in Java code which is bounded in the layout

__MainViewModel.java__

```java
public final ObservableList<Task> data = new ObservableArrayList<>();
public final ItemBinder itemBinder = new ItemBinder() {
	@Override
   	public int getBindingVariable() {
		return BR.item; // binder from item_task.xml
	}

	@Override
	public int getLayoutRes() {
   		return me.thuongle.adaptersample.R.layout.item_task;
   	}
};
```

__PresenterImpl.java__

```java
//handle onClick event
public ClickHandler<Task> clickHandler = model -> mView.onItemClick(model);

//handle onLongClick event
public LongClickHandler<Task> longClickHandler = model -> mView.onItemLongClick(model);

//handle swipe to dismiss event
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

// handle onClick in child item
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
```

### 4. Bind data object to recycler view
```java
mViewModel::setData
```

## Inspired by
- [android-data-binding-recyclerview](https://github.com/radzio/android-data-binding-recyclerview)

License
=======

    Copyright 2016 Thuong Le

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

