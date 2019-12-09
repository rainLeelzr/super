package com.wegood.core.net.request.worker.event;

import com.wegood.core.net.request.Request;
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
public class WorkExceptionEvent extends ApplicationEvent {

    private Request request;

    private Exception exception;

    public WorkExceptionEvent() {
        super(Boolean.TRUE);
    }

}
