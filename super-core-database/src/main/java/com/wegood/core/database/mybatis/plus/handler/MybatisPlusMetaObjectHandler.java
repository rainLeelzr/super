package com.wegood.core.database.mybatis.plus.handler;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.wegood.core.entity.LogicDeleteEntity;
import com.wegood.core.entity.TimeTracedEntity;
import com.wegood.core.entity.UserTracedEntity;
import com.wegood.core.entity.VersionEntity;
import com.wegood.core.login.LoginUser;
import com.wegood.core.login.LoginUserUtil;
import com.wegood.core.support.LocalDateTimeUtil;
import org.apache.ibatis.reflection.MetaObject;

/**
 * @author Rain
 */
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        // version
        Object version = getFieldValByName(VersionEntity.VERSION, metaObject);
        if (version == null) {
            setFieldValByName(VersionEntity.VERSION, VersionEntity.DEFAULT_VERSION, metaObject);
        }

        LoginUser loginUser = LoginUserUtil.getLoginUser();

        // createUserId
        setFieldValByName(
            UserTracedEntity.CREATE_USER_ID_PROPERTY,
            loginUser == null
                ? ""
                : StrUtil.nullToEmpty(loginUser.getUserId()),
            metaObject);

        // createUserName
        setFieldValByName(
            UserTracedEntity.CREATE_USER_NAME_PROPERTY,
            loginUser == null
                ? StrUtil.subPre(Thread.currentThread().getName(), 16)
                : StrUtil.nullToEmpty(StrUtil.subPre(loginUser.getNickName(), 16)),
            metaObject);

        // modifyUserId
        setFieldValByName(UserTracedEntity.MODIFY_USER_ID_PROPERTY,
            loginUser == null
                ? ""
                : StrUtil.nullToEmpty(loginUser.getUserId()),
            metaObject);

        // modifyUserName
        setFieldValByName(UserTracedEntity.MODIFY_USER_NAME_PROPERTY,
            loginUser == null
                ? StrUtil.subPre(Thread.currentThread().getName(), 16)
                : StrUtil.nullToEmpty(StrUtil.subPre(loginUser.getNickName(), 16)),
            metaObject);

        // createTime
        Object createTime = getFieldValByName(TimeTracedEntity.MODIFY_TIME_PROPERTY, metaObject);
        if (createTime == null) {
            setFieldValByName(TimeTracedEntity.CREATED_TIME_PROPERTY, LocalDateTimeUtil.now(), metaObject);
        }

        // modifyTime
        setFieldValByName(TimeTracedEntity.MODIFY_TIME_PROPERTY, LocalDateTimeUtil.now(), metaObject);

        // delete_flag
        setFieldValByName(LogicDeleteEntity.DELETE_FLAG_PROPERTY, LogicDeleteEntity.DEFAULT_DELETE_FLAG, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        LoginUser loginUser = LoginUserUtil.getLoginUser();

        // modifyUserId
        setFieldValByName(UserTracedEntity.MODIFY_USER_ID_PROPERTY,
            loginUser == null
                ? ""
                : StrUtil.nullToEmpty(loginUser.getUserId()),
            metaObject);

        // modifyUserName
        setFieldValByName(UserTracedEntity.MODIFY_USER_NAME_PROPERTY,
            loginUser == null
                ? StrUtil.subPre(Thread.currentThread().getName(), 16)
                : StrUtil.nullToEmpty(loginUser.getNickName()),
            metaObject);

        // modifyTime
        setFieldValByName(TimeTracedEntity.MODIFY_TIME_PROPERTY, LocalDateTimeUtil.now(), metaObject);

    }
}
