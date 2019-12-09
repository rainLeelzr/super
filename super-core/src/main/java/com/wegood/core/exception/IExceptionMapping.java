package com.wegood.core.exception;

import com.wegood.core.exception.code.IStatusMessage;

/**
 * @author Rain
 */
public interface IExceptionMapping {

    IStatusMessage getStatusCode(Class<? extends Exception> exception);

}