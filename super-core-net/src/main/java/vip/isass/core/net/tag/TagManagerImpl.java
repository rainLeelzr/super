package vip.isass.core.net.tag;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.map.MapUtil;
import vip.isass.core.net.end.End;
import vip.isass.core.net.session.Session;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

/**
 * 标签管理器实现
 * 名称定义：
 * tagKey: 标签键
 * tagValue: 标签值
 * tagKey + tagValue = tagPair: 标签键值对
 * Tag: 存储键值对的对象，方便业务调用方使用。为节省存储空间，实际存储时标签数据时，标签键和标签对都使用 string 存储
 * session: 会话
 *
 * @author Rain
 */
@SuppressWarnings("rawtypes")
public class TagManagerImpl implements TagManager {

    /**
     * map 不能保存 null, 当只有标签键，没有标签值时，使用空字符串代替标签值
     */
    private static final String BLANK_TAG_VALUE = "";

    /**
     * Map<String, Map<String, Set<Session>>>
     * *     ||          ||           ||
     * *   tagKey     tagValue      session
     */
    private final Map<String, Map<String, Set<Session<?, ?>>>> tagValueAndSessionMapByTagKey = new ConcurrentHashMap<>();

    /**
     * Map<Session, Map<String, String>>
     * *     ||           ||      ||
     * *   session      tagKey  tagValue
     */
    private final Map<Session<?, ?>, Map<String, String>> tagKeyAndTagValueMapBySession = new ConcurrentHashMap<>();

    // region add

    @Override
    public <C, E extends End> void addTag(Session<C, E> session, String tagKey) {
        addTagValues(
            Collections.singleton(session),
            Collections.singleton(MapUtil.<String, String>builder().put(tagKey, BLANK_TAG_VALUE).build()));
    }

    @Override
    public <C, E extends End> void addTags(Session<C, E> session, Collection<String> tagKeys) {
        addTagValues(
            Collections.singleton(session),
            tagKeys.stream()
                .map(t -> MapUtil.<String, String>builder().put(t, BLANK_TAG_VALUE).build())
                .collect(Collectors.toList()));
    }

    @Override
    public <C, E extends End> void addTagValue(Session<C, E> session, String tagKey, String tagValue) {
        addTagValues(
            Collections.singleton(session),
            Collections.singleton(MapUtil.<String, String>builder().put(tagKey, tagValue).build()));
    }

    @Override
    public <C, E extends End> void addTagValues(Session<C, E> session, Collection<Map<String, String>> tagPairs) {
        addTagValues(
            Collections.singleton(session),
            tagPairs);
    }

    @Override
    public <C, E extends End> void addTagValues(Collection<Session<C, E>> sessions, Collection<Map<String, String>> tagPairs) {
        for (Session<C, E> session : sessions) {
            Map<String, String> tagValueMap = tagKeyAndTagValueMapBySession.computeIfAbsent(
                session,
                s -> new ConcurrentHashMap<>(16));
            tagPairs.forEach(tagValueMap::putAll);
        }

        for (Map<String, String> tagValuePair : tagPairs) {
            Set<Map.Entry<String, String>> tagValueEntrySet = tagValuePair.entrySet();
            for (Map.Entry<String, String> tagValueEntry : tagValueEntrySet) {
                Map<String, Set<Session<?, ?>>> stringSetMap = tagValueAndSessionMapByTagKey.computeIfAbsent(tagValueEntry.getKey(), t -> new ConcurrentHashMap<>(16));
                Set<Session<?, ?>> sessionSet = stringSetMap.computeIfAbsent(tagValueEntry.getValue(), t -> new CopyOnWriteArraySet<>());
                sessionSet.addAll(sessions);
            }
        }
    }

    // endregion

    // region has

    @Override
    public <C, E extends End> boolean hasTagKey(Session<C, E> session, String tag) {
        return hasTagKey(session, tag, null);
    }

    @Override
    public <C, E extends End> boolean hasAllTagKey(Session<C, E> session, Collection<String> tagKeys) {
        Map<String, String> tagPairMap = tagKeyAndTagValueMapBySession.get(session);
        if (tagPairMap == null) {
            return false;
        }

        for (String tagKey : tagKeys) {
            if (tagPairMap.get(tagKey) == null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public <C, E extends End> boolean hasTagKey(Session<C, E> session, String tag, String tagValue) {
        Map<String, String> tagPairMap = tagKeyAndTagValueMapBySession.get(session);
        if (tagPairMap == null) {
            return false;
        }

        if (tagValue == null) {
            return tagPairMap.containsKey(tag);
        }

        return tagValue.equals(tagPairMap.get(tag));
    }

    // endregion

    // region remove

    @Override
    public <C, E extends End> void removeByTagKey(Session<C, E> session, String tagKey) {
        removeTags(Collections.singleton(session), Collections.singleton(Tag.of(tagKey)));
    }

    @Override
    public <C, E extends End> void removeByTagPair(Session<C, E> session, String tagKey, String tagValue) {
        removeTags(Collections.singleton(session), Collections.singleton(Tag.of(tagKey, tagValue)));
    }

    @Override
    public <C, E extends End> void removeBySessionsAndTagKey(Collection<Session<C, E>> sessions, String tagKey) {
        removeTags(sessions, Collections.singleton(Tag.of(tagKey)));
    }

    @Override
    public <C, E extends End> void removeTagBySessionsAndTagKeys(Collection<Session<C, E>> sessions, Collection<String> tagKeys) {
        removeTags(
            sessions,
            tagKeys.stream().map(Tag::of).collect(Collectors.toList())
        );
    }

    @Override
    public <C, E extends End, T> void removeTags(Collection<Session<C, E>> sessions, Collection<Tag<T>> tags) {
        for (Session<C, E> session : sessions) {
            Map<String, String> tagPairMap = tagKeyAndTagValueMapBySession.get(session);
            if (tagPairMap == null) {
                // 此会话没有标记任何标签，直接跳过循环
                continue;
            }

            for (Tag tag : tags) {
                // 如果需要删除的标签键值对的标签值是null，则直接删除
                if (tag.getValue() == null) {
                    tagPairMap.remove(tag.getKey());
                }

                String tagValue = tagPairMap.get(tag.getKey());
                if (tag.getValue().toString().equals(tagValue)) {
                    tagPairMap.remove(tag.getKey());
                }
            }

            // 如果session
            if (tagPairMap.isEmpty()) {
                tagKeyAndTagValueMapBySession.remove(session);
            }
        }

        for (Tag tag : tags) {
            // 如果需要删除的标签键值对的标签值是null，则直接删除
            if (tag.getValue() == null) {
                tagValueAndSessionMapByTagKey.remove(tag.getKey());
                continue;
            }

            Map<String, Set<Session<?, ?>>> sessionMapByTagValue = tagValueAndSessionMapByTagKey.get(tag.getKey());
            if (sessionMapByTagValue == null) {
                // 此标签键未纪录任何会话，直接跳过循环
                continue;
            }

            sessionMapByTagValue.remove(tag.getValue().toString());
        }
    }

    @Override
    public <C, E extends End> Collection<Session<C, E>> removeByTagPair(String tagKey, String tagValue) {
        Map<String, Set<Session<C, E>>> tagValueAndSessionMap = MapUtil.get(
            tagValueAndSessionMapByTagKey,
            tagKey,
            new TypeReference<Map<String, Set<Session<C, E>>>>() {
            }
        );
        if (tagValueAndSessionMap == null) {
            return Collections.emptyList();
        }

        Set<Session<C, E>> sessions = tagValueAndSessionMap.get(tagValue);
        if (sessions == null) {
            return Collections.emptyList();
        }

        removeTags(sessions, Collections.singleton(Tag.of(tagKey, tagValue)));
        return sessions;
    }

    @Override
    public <C, E extends End> void removeAllTags(Session<C, E> session) {
        removeAllTags(Collections.singleton(session));
    }

    @Override
    public <C, E extends End> void removeAllTags(Collection<Session<C, E>> sessions) {
        sessions.forEach(session -> {
            for (Map.Entry<String, Map<String, Set<Session<?, ?>>>> entry : tagValueAndSessionMapByTagKey.entrySet()) {
                Map<String, Set<Session<?, ?>>> sessionMapByTagValue = entry.getValue();

                for (Map.Entry<String, Set<Session<?, ?>>> tagValueAdnSessionEntry : sessionMapByTagValue.entrySet()) {
                    Set<Session<?, ?>> loopSessions = tagValueAdnSessionEntry.getValue();
                    loopSessions.remove(session);
                    if (loopSessions.isEmpty()) {
                        sessionMapByTagValue.remove(tagValueAdnSessionEntry.getKey());
                    }
                }

                if (sessionMapByTagValue.isEmpty()) {
                    tagValueAndSessionMapByTagKey.remove(entry.getKey());
                }
            }

            sessions.forEach(tagKeyAndTagValueMapBySession::remove);
        });
    }

    @Override
    public void removeAllTagKey(String tagKey) {
        removeAllTagKeys(Collections.singleton(tagKey));
    }

    @Override
    public void removeAllTagKeys(Collection<String> tagKeys) {
        for (String tagKey : tagKeys) {
            tagValueAndSessionMapByTagKey.remove(tagKey);

            for (Map.Entry<Session<?, ?>, Map<String, String>> sessionAndTagPairEntry : tagKeyAndTagValueMapBySession.entrySet()) {
                Map<String, String> tagPairMap = sessionAndTagPairEntry.getValue();
                tagKeys.forEach(tagPairMap::remove);
                if (tagPairMap.isEmpty()) {
                    tagKeyAndTagValueMapBySession.remove(sessionAndTagPairEntry.getKey());
                }
            }
        }
    }

    // endregion

}
