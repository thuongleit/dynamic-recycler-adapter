package com.jokotech.adaptersample.data;

/**
 * Created by thuongleit on 8/11/16.
 */
public class Task implements Cloneable {

    public long id;

    public String title;

    public String description;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
