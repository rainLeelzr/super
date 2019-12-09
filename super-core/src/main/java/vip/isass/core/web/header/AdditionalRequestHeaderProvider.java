package vip.isass.core.web.header;

/**
 * @author Rain
 */
public interface AdditionalRequestHeaderProvider {

    String getHeaderName();

    String getValue();

    /**
     * 当已存在同名的请求头时，是否覆盖旧的值
     */
    boolean override();

    boolean support(String method, String uri);

}
