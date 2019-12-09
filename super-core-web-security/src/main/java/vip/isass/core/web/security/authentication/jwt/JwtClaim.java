package vip.isass.core.web.security.authentication.jwt;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author Rain
 */
@Getter
@Setter
@Accessors(chain = true)
public class JwtClaim {

    public static final String USER_ID = "uid";

    public static final String NICK_NAME = "name";

    public static final String FROM = "fr";

    public static final String VERSION = "v";

    /**
     * 用户 id
     */
    private String uid;

    /**
     * 用户昵称
     */
    private String name;

    /**
     * 登录渠道，从什么产品登录
     */
    private String fr;

    /**
     * 版本，当服务端记录对应的登录渠道的版本，大于此值时，此 token 应当失效。
     * 当服务端无对应的登录渠道的版本记录时，此值不作为失效判断的依据。
     * 作用：作为强制用户下线或多端登录踢下线的判断依据。
     */
    private Integer v;

}
