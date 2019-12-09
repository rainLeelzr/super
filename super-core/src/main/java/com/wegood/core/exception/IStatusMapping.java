package com.wegood.core.exception;

import com.wegood.core.exception.code.IStatusMessage;

/**
 * @author Rain
 */
public interface IStatusMapping {

    IStatusMessage getErrorCode(Integer code);

}