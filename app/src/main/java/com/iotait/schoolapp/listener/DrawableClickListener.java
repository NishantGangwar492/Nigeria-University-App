package com.iotait.schoolapp.listener;

public interface DrawableClickListener {
    void onClick(DrawablePosition target);

    enum DrawablePosition {TOP, BOTTOM, LEFT, RIGHT}
}
