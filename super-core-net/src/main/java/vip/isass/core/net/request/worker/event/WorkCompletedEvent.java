package vip.isass.core.net.request.worker.event;

import vip.isass.core.net.request.Request;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.context.ApplicationEvent;

/**
 * @author hone 2018/5/18
 * @since
 */
@Getter
@Setter
@Accessors(chain = true)
public class WorkCompletedEvent extends ApplicationEvent {

    private Request request;

    public WorkCompletedEvent() {
        super(Boolean.TRUE);
    }

}
