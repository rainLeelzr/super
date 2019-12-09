// package vip.isass.core.mq.core.consumer;
//
// import cn.hutool.core.util.ArrayUtil;
// import cn.hutool.core.util.StrUtil;
// import vip.isass.core.mq.MqAutoConfiguration;
// import vip.isass.core.support.ExceptionCatcher;
// import vip.isass.core.support.SpringContextUtil;
// import org.springframework.aop.support.AopUtils;
// import org.springframework.beans.BeansException;
// import org.springframework.context.ApplicationContext;
// import org.springframework.context.ApplicationContextAware;
// import org.springframework.context.ApplicationListener;
// import org.springframework.context.event.ContextRefreshedEvent;
// import org.springframework.stereotype.Component;
// import org.springframework.stereotype.Controller;
// import org.springframework.stereotype.Service;
// import org.springframework.web.bind.annotation.RestController;
//
// import javax.annotation.PreDestroy;
// import javax.annotation.Resource;
// import java.lang.reflect.Method;
// import java.util.*;
// import java.util.stream.Collectors;
// import java.util.stream.Stream;
//
// /**
//  * @author Rain
//  */
// @Component
// public class MqConsumerAutoConfiguration implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {
//
//     private ApplicationContext applicationContext;
//
//     @Override
//     public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//         this.applicationContext = applicationContext;
//     }
//
//     @Resource
//     private List<MqConsumer> consumers;
//
//     @Resource
//     private MqAutoConfiguration mqAutoConfiguration;
//
//     private Map<Method, EventListener> methods;
//
//     @Override
//     public void onApplicationEvent(ContextRefreshedEvent event) {
//         if (methods != null) {
//             return;
//         }
//
//         // 查找所有含有 XSuperEventListener 注解的方法
//         methods = new HashMap<>(16);
//         final Map<Method, Object> targetBeans = new HashMap<>(16);
//
//         // 获取有可能出现EventListener注解的bean
//         Map<String, Object> beans = getBeans();
//         for (Map.Entry<String, Object> entry : beans.entrySet()) {
//             Object bean = entry.getValue();
//             Class<?> targetClass = AopUtils.getTargetClass(bean);
//             Method[] declaredMethods = targetClass.getDeclaredMethods();
//             if (ArrayUtil.isEmpty(declaredMethods)) {
//                 continue;
//             }
//             // 收集类中所有含有 EventListener 注解的方法
//             final Collection<Method> matchMethods = Stream.of(declaredMethods)
//                 .filter(Objects::nonNull)
//                 .filter(m -> m.getAnnotation(EventListener.class) != null)
//                 .collect(Collectors.toList());
//
//             if (matchMethods.isEmpty()) {
//                 continue;
//             }
//
//             // 过滤掉父类的方法，将过滤得到的结果放到methods和targetBeans
//             matchMethods.stream()
//                 .map(m -> AopUtils.getMostSpecificMethod(m, targetClass))
//                 .distinct()
//                 .forEach(m -> {
//                     EventListener annotation = m.getAnnotation(EventListener.class);
//                     methods.put(m, annotation);
//                     targetBeans.put(m, bean);
//                 });
//         }
//
//         if (methods.isEmpty()) {
//             methods = null;
//             return;
//         }
//
//
//         // 遍历订阅方法，找到实现厂商进行实现
//         methods.forEach((m, l) -> {
//             if (mqAutoConfiguration.getDisable().contains(l.consumerId())) {
//                 return;
//             }
//
//             String manufacturer = StrUtil.blankToDefault(l.manufacturer(), mqAutoConfiguration.getDefaultManufacturer());
//             MqConsumer xConsumer = consumers.stream().filter(c -> c.getManufacturer().equals(manufacturer))
//                 .findAny()
//                 .orElseThrow(() -> new IllegalArgumentException(StrUtil.format("厂商[{}]没有实现consumer")));
//
//             try {
//                 // 获取一个新的消费者bean
//                 xConsumer = SpringContextUtil.getBean(xConsumer.getClass());
//             } catch (Exception e) {
//                 throw new RuntimeException(e.getMessage(), e);
//             }
//
//             // 执行订阅
//             xConsumer.setRuntimeBean(targetBeans.get(m))
//                 .setEventListener(l)
//                 .setRuntimeMethod(m)
//                 .setSubscribeModel(l.subscribeModel())
//                 .setConsumerId(l.consumerId())
//                 .setTopic(l.topic())
//                 .setTag(l.tag())
//                 .setConsumeThreadNumber(l.consumeThreadNumber())
//                 .subscribe();
//         });
//     }
//
//     private Map<String, Object> getBeans() {
//         Map<String, Object> beansMap = new HashMap<>(100);
//         beansMap.putAll(ExceptionCatcher.supplierOrDefault(() -> applicationContext.getBeansWithAnnotation(Controller.class), Collections.emptyMap()));
//         beansMap.putAll(ExceptionCatcher.supplierOrDefault(() -> applicationContext.getBeansWithAnnotation(RestController.class), Collections.emptyMap()));
//         beansMap.putAll(ExceptionCatcher.supplierOrDefault(() -> applicationContext.getBeansWithAnnotation(Service.class), Collections.emptyMap()));
//         beansMap.putAll(ExceptionCatcher.supplierOrDefault(() -> applicationContext.getBeansWithAnnotation(Component.class), Collections.emptyMap()));
//         return beansMap;
//     }
//
//     @PreDestroy
//     public void destroy() {
//         methods = null;
//     }
// }
