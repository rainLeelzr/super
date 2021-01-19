package vip.isass.core.net.socketio;

import cn.hutool.core.util.StrUtil;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.isass.core.support.JsonUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 登陆
 */
@Slf4j
@Component
public class OnLoginListener {

    private static final String LOGIN = "login";

    public static final String USER_ID = "userId";

    @OnEvent(value = LOGIN)
    public void onMessage(SocketIOClient client, AckRequest ackSender, String data) {
        Map<String, String> map = Collections.emptyMap();
        try {
            map = JsonUtil.DEFAULT_INSTANCE.readValue(data, new TypeReference<HashMap<String, String>>() {
            });
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        String userId = map.get(USER_ID);
        if (StrUtil.isNotBlank(userId)) {
            SocketIOClient socketIOClient = SocketIoServer.CLIENT_BY_USER_ID.get(userId);
            if (socketIOClient == null) {
                SocketIoServer.CLIENT_BY_USER_ID.put(userId, client);
                client.set(USER_ID, userId);
                log.info("用户[{}]登陆成功", userId);
            } else {
                log.info("用户[{}]已在其他设备登陆", userId);
                client.sendEvent(LOGIN, "用户已在其他设备登陆");
            }
        }
    }

}
