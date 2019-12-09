package vip.isass.core.web.security.authentication.jwt;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 终端配置
 *
 * @author Rain
 */
@Getter
@Setter
@Accessors(chain = true)
public class EndProperties {

    private Boolean allEndsSameTimeOnline;

    /**
     * 直接在线终端，不需要管其他终端有没有在线的终端，如果需要同端多登，还需要配置 sameEndsOnline
     */
    private List<String> directOnlineEnds;

    /**
     * 互斥终端，只能其中一个在线
     */
    private List<String> mutexEnds;

    /**
     * 同端多登，可以多台同一终端类型同时在线的终端
     */
    private List<String> sameEndsOnline;

}
