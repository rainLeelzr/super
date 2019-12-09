package vip.isass.core.sequence;


import vip.isass.core.support.Support;

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
