package vip.isass.core.net.tag;

import cn.hutool.core.lang.Assert;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * @author Rain
 */
@Getter
@Setter
public class Tag<T> {

    private String key;

    private T value;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tag<?> tag = (Tag<?>) o;
        return Objects.equals(key, tag.key) && Objects.equals(value, tag.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    private Tag() {
    }

    public static <T> Tag<T> of(String key) {
        Tag<T> tag = new Tag<>();
        tag.setKey(key);
        return tag;
    }

    public static <T> Tag<T> of(String key, T value) {
        Tag<T> tag = new Tag<>();
        tag.setKey(key);
        tag.setValue(value);
        return tag;
    }

    @SafeVarargs
    public static <T> Tag<Collection<T>> of(String key, T... valueArr) {
        Assert.notNull(valueArr, "valueArr 必填");
        Assert.isTrue(
            valueArr.length <= TagUtil.VALUE_NUM_LIMIT,
            "标签值列表数量超过上限[{}]",
            TagUtil.VALUE_NUM_LIMIT);
        TagUtil.checkValueType(valueArr[0]);

        Collection<T> valueList = new ArrayList<>(valueArr.length);
        Collections.addAll(valueList, valueArr);

        return of(key, valueList);
    }

    public static <T> Tag<Collection<T>> of(String key, Collection<T> values) {
        Assert.notEmpty(values, "values 必填");
        Assert.isTrue(
            values.size() <= TagUtil.VALUE_NUM_LIMIT,
            "标签值列表数量超过上限[{}]",
            TagUtil.VALUE_NUM_LIMIT);
        TagUtil.checkValueType(values.iterator().next());

        Tag<Collection<T>> tag = new Tag<>();
        tag.setKey(key);
        tag.setValue(values);
        return tag;
    }

}
