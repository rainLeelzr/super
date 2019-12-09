package com.wegood.core.sequence;


import com.wegood.core.support.Support;

/**
 * 获取序列
 *
 * @author Rain
 */
public interface Sequence<T> extends Support {

    /**
     * 获取序列
     */
    T next();

}
