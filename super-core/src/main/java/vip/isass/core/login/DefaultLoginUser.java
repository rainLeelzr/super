package vip.isass.core.login;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author Rain
 */
@Getter
@Setter
@Accessors(chain = true)
public class DefaultLoginUser implements LoginUser {

    private String userId;

    private String nickName;

    private String loginFrom;

    private Integer version;

    private String tokenFrom;

    @Override
    public String getAllUserId() {
        return this.userId;
    }

}
