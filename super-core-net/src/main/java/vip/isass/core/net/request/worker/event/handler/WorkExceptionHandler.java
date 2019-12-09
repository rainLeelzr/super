package vip.isass.core.net.request.worker.event.handler;

import vip.isass.core.net.packet.Packet;
import vip.isass.core.net.request.Request;
import vip.isass.core.net.request.worker.event.WorkExceptionEvent;
import vip.isass.core.serialization.SerializeMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;

/**
 * @author Rain
 */
@Slf4j
public class WorkExceptionHandler implements ApplicationListener<WorkExceptionEvent> {

    /**
     * 网络请求的业务处理过程中抛异常时，触发此方法
     */
    @Override
    public void onApplicationEvent(WorkExceptionEvent event) {
        Request request = event.getRequest();
        Exception e = event.getException();
        log.error("请求处理异常: {}", request == null ? "request=null" : request.toString(), e);

        // 推送消息给客户端
        if (request != null) {
            Packet packet = request.getPacket();
            if (packet != null) {
                packet.setType(Packet.Type.ERROR.getCode());
                packet.setSerializeMode(SerializeMode.JSON.getCode());
                packet.setContent(e.getMessage());
                request.sendResponse(packet);
            }
        }
    }

}
