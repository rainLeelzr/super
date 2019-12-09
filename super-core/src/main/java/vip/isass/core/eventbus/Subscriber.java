// package vip.isass.core.eventbus;
//
// import javax.annotation.PostConstruct;
// import javax.annotation.PreDestroy;
//
// /**
//  * @author Rain
//  */
// public interface Subscriber {
//
//     BlueEventBus getBlueEventBus();
//
//     @PostConstruct
//     default void register() {
//         BlueEventBus eventBus = getBlueEventBus();
//         if (eventBus == null) {
//             return;
//         }
//         eventBus.register(this);
//     }
//
//     @PreDestroy
//     default void unregister() {
//         BlueEventBus eventBus = getBlueEventBus();
//         if (eventBus == null) {
//             return;
//         }
//         eventBus.unregister(this);
//     }
//
// }
