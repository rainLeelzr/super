package com.wegood.core.web.res;

import java.util.Collection;
import java.util.List;

/**
 * @author Rain
 */
public interface ResRegister {

    /**
     * 获取已经注册过的资源
     * <p>从数据库中获取全部资源</p>
     */
    List<Resource> getAllRegisteredResource();

    List<Resource> getAllRegisteredResourceByPrefixUri(String prefixUri);

    /**
     * 将一个集合的资源全部进行注册
     * <p>记录入数据库</p>
     */
    void register(Collection<Resource> collect);

}
