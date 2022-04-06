package vip.isass.core.net.tag;

import vip.isass.core.net.end.End;
import vip.isass.core.net.session.Session;

import java.util.Collection;
import java.util.Map;

/**
 * 标签管理器，用于管理 session 的标签以及标签值，以便业务程序可以根据标签找到相关 session, 对其推送消息
 * <p>
 * 名称定义：
 * tagKey: 标签键
 * tagValue: 标签值
 * tagKey + tagValue = tagPair: 标签键值对
 * Tag: 存储键值对的对象，方便业务调用方使用。为节省存储空间，实际存储时标签数据时，标签键和标签对都使用 string 存储
 * session: 会话
 *
 * @author Rain
 */
public interface TagManager {

    // region add

    /**
     * 给指定的会话增加标签键
     *
     * @param session 需要被添加标签的会话
     * @param tagKey  被添加的标签键
     */
    <C, E extends End> void addTag(Session<C, E> session, String tagKey);

    /**
     * 给指定的会话增加标签键集合
     *
     * @param session 需要被添加标签的会话
     * @param tagKeys 被添加的标签键集合
     */
    <C, E extends End> void addTags(Session<C, E> session, Collection<String> tagKeys);

    /**
     * 给指定的会话增加标签键值对
     *
     * @param session  需要被添加标签的会话
     * @param tagKey   被添加的标签键
     * @param tagValue 被添加的标签值
     */
    <C, E extends End> void addTagValue(Session<C, E> session, String tagKey, String tagValue);

    /**
     * 给指定的会话增加标签键值对
     *
     * @param session  需要被添加标签的会话
     * @param tagPairs 被添加的标签键值对集合
     */
    <C, E extends End> void addTagValues(Session<C, E> session, Collection<Map<String, String>> tagPairs);

    /**
     * 给指定的 session 集合，添加标签值集合
     *
     * @param sessions 需要被添加标签的会话集合
     * @param tagPairs 被添加的标签键值对集合
     */
    <C, E extends End> void addTagValues(Collection<Session<C, E>> sessions, Collection<Map<String, String>> tagPairs);

    // endregion

    // region has

    /**
     * 判断给定的会话是否含有给定的标签键
     *
     * @param session 会话
     * @param tagKey  标签键
     * @return 是否含有给定的标签键
     */
    <C, E extends End> boolean hasTagKey(Session<C, E> session, String tagKey);

    /**
     * 判断给定的会话是否含有给定的标签键值对
     *
     * @param session 会话
     * @param tagKeys 标签键
     * @return 是否含有给定的标签键值对
     */
    <C, E extends End> boolean hasAllTagKey(Session<C, E> session, Collection<String> tagKeys);

    /**
     * 判断给定的会话是否含有给定的标签键值对
     *
     * @param session  会话
     * @param tagKey   标签键
     * @param tagValue 标签值
     * @return 是否含有给定的标签键值对
     */
    <C, E extends End> boolean hasTagKey(Session<C, E> session, String tagKey, String tagValue);

    // endregion

    // region remove

    /**
     * 删除给定会话的符合给定标签键条件的标签
     *
     * @param session 需要被删除标签的会话
     * @param tagKey  需要被删除的标签键
     */
    <C, E extends End> void removeByTagKey(Session<C, E> session, String tagKey);

    /**
     * 删除给定会话的符合给定标签键和标签值条件的标签
     *
     * @param session  需要被删除标签的会话
     * @param tagKey   需要被删除的标签键
     * @param tagValue 需要被删除的标签值
     */
    <C, E extends End> void removeByTagPair(Session<C, E> session, String tagKey, String tagValue);

    /**
     * 删除给定会话集合的符合给定标签键条件的标签
     *
     * @param sessions 需要被删除标签的会话集合
     * @param tagKey   需要被删除的标签键
     */
    <C, E extends End> void removeBySessionsAndTagKey(Collection<Session<C, E>> sessions, String tagKey);

    /**
     * 删除给定的会话集合中符合给定标签键条件的标签
     *
     * @param sessions 需要被删除标签的会话集合
     * @param tagKeys  需要被删除的标签键集合
     */
    <C, E extends End> void removeTagBySessionsAndTagKeys(Collection<Session<C, E>> sessions, Collection<String> tagKeys);

    /**
     * 删除给定的会话集合中符合给定标签条件的标签
     *
     * @param sessions 需要被删除标签的会话集合
     * @param tags     需要被删除的标签键值对集合
     * @return 被删除的会话集合
     */
    <C, E extends End, T> void removeTags(Collection<Session<C, E>> sessions, Collection<Tag<T>> tags);

    /**
     * 删除符合给定标签键值对条件的会话
     *
     * @param tagKey   需要被删除的标签键
     * @param tagValue 需要被删除的标签值
     * @return 被删除的会话集合
     */
    <C, E extends End> Collection<Session<C, E>> removeByTagPair(String tagKey, String tagValue);

    /**
     * 删除给定会话的所有标签
     *
     * @param session 需要被删除标签的会话
     */
    <C, E extends End> void removeAllTags(Session<C, E> session);

    /**
     * 删除给定会话集合的所有标签
     *
     * @param sessions 需要被删除标签的会话集合
     */
    <C, E extends End> void removeAllTags(Collection<Session<C, E>> sessions);

    /**
     * 删除给定的标签键
     *
     * @param tagKey 需要被删除的标签键
     */
    void removeAllTagKey(String tagKey);

    /**
     * 删除所有给定的标签键
     *
     * @param tagKeys 需要被删除的标签键集合
     */
    void removeAllTagKeys(Collection<String> tagKeys);

    // endregion

}
