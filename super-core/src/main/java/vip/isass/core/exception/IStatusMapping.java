package vip.isass.core.exception;

import vip.isass.core.exception.code.IStatusMessage;

/**
 * @author Rain
 */
public interface IStatusMapping {

    IStatusMessage getErrorCode(Integer code);

}