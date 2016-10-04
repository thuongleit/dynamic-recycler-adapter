# Using RecyclerView with Android Data Binding
Developing dynamic RecyclerView by using Android Data Binding

![demo](https://github.com/thuongleit/dynamic-recycler-adapter/blob/master/demo.gif)

## How to start

Just clone this repository and start playing with it! If you want to use some parts of this repository in your project read below.


### Change your gradle file

- In your main  build.gradle add:
 
```gradle
classpath 'com.android.tools.build:gradle:1.5.0'
```

- In your app build.gradle add:
    
    
```gradle
    dataBinding {
        enabled = true
    }
```

