package com.jdk8.defaultFun;

/**
 * @author spuerKun
 * @date 17/12/7.
 */
public interface Resizable extends Drawable {
    int getWidth();

    int getHeight();

    void setWidth(int width);

    void setHeight(int height);

    void setAbsoluteSize(int width, int height);
}
