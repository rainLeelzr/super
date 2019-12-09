package vip.isass.core.web.security.authentication.jwt;

import cn.hutool.core.collection.CollUtil;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 终端配置
 *
 * @author Rain
 */
@Configuration
public class EndsConfiguration {

    @Autowired(required = false)
    private EndPropertiesLoader endPropertiesLoader;

    /**
     * 所有终端是否可以同时在线。是则放弃所有判断，直接在线
     */
    @Getter
    private boolean allEndsSameTimeOnline = false;

    /**
     * 直接在线终端，不需要管其他终端有没有在线的终端，如果需要同端多登，还需要配置 sameEndsOnline
     */
    private List<String> directOnlineEnds = CollUtil.newArrayList("ipad", "web-pc", "web-h5", "web-apidoc", "windows", "mac");

    /**
     * 互斥终端，只能其中一个在线
     */
    @Getter
    private List<String> mutexEnds = CollUtil.newArrayList("ios-wegood", "android-wegood");

    /**
     * 同端多登，可以多台同一终端类型同时在线的终端
     */
    private List<String> sameEndsOnline = CollUtil.newArrayList("web-pc", "web-h5", "web-apidoc");

    public boolean isDirectOnlineEnds(String end) {
        return this.directOnlineEnds.contains(end.toLowerCase());
    }

    public boolean isSameEndsOnline(String end) {
        return this.sameEndsOnline.contains(end.toLowerCase());
    }

    public boolean isMutexEnds(String end) {
        return this.mutexEnds.contains(end.toLowerCase());
    }

    @PostConstruct
    public void init() {
        if (endPropertiesLoader != null) {
            EndProperties endProperties = endPropertiesLoader.load();
            if (endProperties != null) {
                this.allEndsSameTimeOnline = endProperties.getAllEndsSameTimeOnline();
                this.directOnlineEnds = endProperties.getDirectOnlineEnds();
                this.mutexEnds = endProperties.getMutexEnds();
                this.sameEndsOnline = endProperties.getSameEndsOnline();
            }
        }
    }

}
