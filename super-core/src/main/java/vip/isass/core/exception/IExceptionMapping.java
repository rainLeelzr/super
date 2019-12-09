package vip.isass.core.exception;

import vip.isass.core.exception.code.IStatusMessage;

/**
 * @author Rain
 */
public interface IExceptionMapping {

    IStatusMessage getStatusCode(Class<? extends Exception> exception);

}