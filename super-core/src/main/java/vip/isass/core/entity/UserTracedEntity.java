package vip.isass.core.entity;

import java.io.Serializable;

/**
 * @author Rain
 */
public interface UserTracedEntity<FK extends Serializable> extends IEntity {

    String CREATE_USER_ID = "create_user_id";
    String CREATE_USER_ID_PROPERTY = "createUserId";

    String CREATE_USER_NAME = "create_user_name";
    String CREATE_USER_NAME_PROPERTY = "createUserName";

    String MODIFY_USER_ID = "modify_user_id";
    String MODIFY_USER_ID_PROPERTY = "modifyUserId";

    String MODIFY_USER_NAME = "modify_user_name";
    String MODIFY_USER_NAME_PROPERTY = "modifyUserName";

    /**
     * 获取创建用户的 id
     */
    FK getCreateUserId();

    /**
     * 设置创建用户的 id
     */
    UserTracedEntity<FK> setCreateUserId(FK createUserId);

    /**
     * 获取创建用户的用户名
     */
    String getCreateUserName();

    /**
     * 设置创建用户的用户名
     */
    UserTracedEntity<FK> setCreateUserName(String createUserName);

    /**
     * 获取修改用户的 id
     */
    FK getModifyUserId();

    /**
     * 设置修改用户的 id
     */
    UserTracedEntity<FK> setModifyUserId(FK modifyUserId);

    /**
     * 获取修改用户的用户名
     */
    String getModifyUserName();

    /**
     * 设置修改用户的用户名
     */
    UserTracedEntity<FK> setModifyUserName(String modifyUserName);


    UserTracedEntity<FK> randomUserTracedId();

    @Override
    default UserTracedEntity<FK> randomEntity() {
        return randomUserTracedId()
                .setCreateUserName(randomString())
                .setModifyUserName(randomString());
    }

}
