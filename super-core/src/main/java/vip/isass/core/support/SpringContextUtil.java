package vip.isass.core.support;

import cn.hutool.core.map.MapUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * @author Rain
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 是否已经初始化 spring 环境
     */
    public static boolean isInitialized() {
        return applicationContext != null;
    }

    /**
     * 创建一个java对象，并添加到spring，让spring管理
     */
    public static <T> T addBeanToSpringContext(Class<T> beanClass) {
        //获取BeanFactory
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();

        //创建bean信息
        String beanName = getBeanNameByBeanType(beanClass);
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(beanClass);

        //动态注册bean
        defaultListableBeanFactory.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());

        @SuppressWarnings("unchecked")
        T bean = (T) applicationContext.getBean(beanName);
        return bean;
    }

    /**
     * 根据name获取bean
     */
    public static Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);
    }

    /**
     * 根据name获取bean
     */
    public static <T> T getBean(String name, Class<T> requiredType) {
        return applicationContext.getBean(name, requiredType);
    }

    /**
     * 根据bean类型获取bean
     */
    public static <T> T getBean(Class<T> requiredType) {
        return applicationContext.getBean(requiredType);
    }

    public static <T> Collection<T> getBeans(Class<T> requiredType) {
        Map<String, T> beans = applicationContext.getBeansOfType(requiredType);
        if (MapUtil.isEmpty(beans)) {
            return Collections.emptyList();
        }

        return beans.values();
    }

    public static <T> Collection<T> getBeans(Class<T> requiredType, ParameterizedTypeReference parameterizedTypeReference) {
        Map<String, T> beans = applicationContext.getBeansOfType(requiredType);
        if (MapUtil.isEmpty(beans)) {
            return Collections.emptyList();
        }

        return beans.values();
    }

    public static <T, P> T getBean(Class<T> requiredType, Class<P> type) {
        Map<String, T> beans = applicationContext.getBeansOfType(requiredType);
        if (MapUtil.isEmpty(beans)) {
            return null;
        }

        for (T bean : beans.values()) {
            ResolvableType resolvableType = ResolvableType.forClass(bean.getClass());
            System.out.println(resolvableType);
            Class<?> resolve = resolvableType.getSuperType().getGeneric(0).resolve();
            System.out.println(resolve);
            if (type.equals(resolve)) {
                return bean;
            }
        }
        return null;
    }

    public static <T, P> T getBeanOfSupport(Class<T> requiredType, Class<P> supportType) {
        Map<String, T> beans = applicationContext.getBeansOfType(requiredType);
        if (MapUtil.isEmpty(beans)) {
            return null;
        }

        for (T bean : beans.values()) {
            if (!(bean instanceof Support)) {
                continue;
            }
            if (((Support) bean).support(supportType)) {
                return bean;
            }
        }
        return null;
    }

    /**
     * 根据bean类型，构造器参数获取bean
     */
    public static <T> T getBean(Class<T> requiredType, Object... objects) {
        return applicationContext.getBean(requiredType, objects);
    }

    /**
     * 根据类名获取bean
     */
    public static <T> T getBeanByNameOfBeanType(Class<T> beanClass) {
        String beanName = getBeanNameByBeanType(beanClass);
        @SuppressWarnings("unchecked")
        T bean = (T) applicationContext.getBean(beanName);
        return bean;
    }

    /**
     * 根据非限定类名获取bean的名称，即将类名的首字母小写
     */
    public static String getBeanNameByBeanType(Class beanClass) {
        String beanName = beanClass.getSimpleName();
        return beanName.substring(0, 1).toLowerCase() + beanName.substring(1, beanName.length());
    }

}
