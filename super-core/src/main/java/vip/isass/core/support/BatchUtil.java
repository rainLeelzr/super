package vip.isass.core.support;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import vip.isass.core.criteria.ICriteria;
import vip.isass.core.criteria.IPageCriteria;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Rain
 */
@Slf4j
public class BatchUtil {

    /**
     * 分页查找所有数据
     */
    public static <R, E, C extends IPageCriteria<E, C>> List<R> findAllByBatchPage(IPageCriteria<E, C> countCriteria,
                                                                                   Function<ICriteria<E, C>, Integer> countFunction,
                                                                                   IPageCriteria<E, C> fetchCriteria,
                                                                                   Function<IPageCriteria<E, C>, IPage<E>> fetchFunction,
                                                                                   Function<IPage<E>, List<R>> consumeFunction) {
        Assert.notNull(countCriteria, "countCriteria");
        int totalCount = countFunction.apply(countCriteria);
        List<R> result = new ArrayList<>(totalCount);

        long currentPage = 1L;
        fetchCriteria.setPageNum(currentPage).setPageSize(fetchCriteria.getPageSize() == 20L ? 1000L : fetchCriteria.getPageSize()).setSearchCountFlag(Boolean.FALSE);
        long totalPageNum = (long) Math.ceil((double) totalCount / fetchCriteria.getPageSize());
        IPage<E> page;
        do {
            log.debug("findAllByBatchPage 进度：{}/{}", currentPage, totalPageNum);
            page = fetchFunction.apply(fetchCriteria);
            result.addAll(consumeFunction.apply(page));
            fetchCriteria.setPageNum(++currentPage);

            log.debug("已获取记录数：{}", result.size());
        } while (currentPage <= totalPageNum);

        return result;
    }

    public static <E, C extends IPageCriteria<E, C>> void batchFunction(IPageCriteria<E, C> countCriteria,
                                                                        Function<IPageCriteria<E, C>, Integer> countFunction,
                                                                        IPageCriteria<E, C> fetchCriteria,
                                                                        Function<IPageCriteria<E, C>, IPage<E>> fetchFunction,
                                                                        Consumer<IPage<E>> consumeFunction) {
        Assert.notNull(countCriteria, "countCriteria");
        int totalCount = countFunction.apply(countCriteria);

        long currentPage = 1L;

        fetchCriteria.setPageNum(currentPage).setPageSize(fetchCriteria.getPageSize() == 20L ? 1000L : fetchCriteria.getPageSize()).setSearchCountFlag(Boolean.FALSE);
        long totalPageNum = (long) Math.ceil((double) totalCount / fetchCriteria.getPageSize());
        IPage<E> page;
        do {
            log.debug("batchFunction 进度：{}/{}", currentPage, totalCount);
            page = fetchFunction.apply(fetchCriteria);
            fetchCriteria.setPageNum(++currentPage);
            consumeFunction.accept(page);
        } while (currentPage <= totalPageNum);

    }

    public static <R, E, C extends IPageCriteria<E, C>> List<R> findAllByBatchPage(IPageCriteria<E, C> countCriteria,
                                                                                   Function<IPageCriteria<E, C>, Integer> countFunction,
                                                                                   IPageCriteria<E, C> fetchCriteria,
                                                                                   Function<IPageCriteria<E, C>, IPage<E>> fetchFunction,
                                                                                   Function<IPage<E>, List<R>> consumeFunction,
                                                                                   int limitResultSize) {
        Assert.notNull(countCriteria, "countCriteria");
        int totalCount = countFunction.apply(countCriteria);
        List<R> result = new ArrayList<>(Math.min(totalCount, limitResultSize));

        long currentPage = 1L;

        fetchCriteria.setPageNum(currentPage).setPageSize(fetchCriteria.getPageSize() == 20L ? 1000L : fetchCriteria.getPageSize()).setSearchCountFlag(Boolean.FALSE);
        long totalPageNum = (long) Math.ceil((double) totalCount / fetchCriteria.getPageSize());
        IPage<E> page;
        do {
            log.debug("findAllByBatchPage 进度：{}/{}", currentPage, totalCount);
            page = fetchFunction.apply(fetchCriteria);
            result.addAll(consumeFunction.apply(page));
            fetchCriteria.setPageNum(++currentPage);

            log.debug("已获取记录数：{},限制记录条数：{}", result.size(), limitResultSize);
            if (result.size() >= limitResultSize) {
                break;
            }
        } while (currentPage <= totalPageNum);

        return result;
    }

    /**
     * 分批处理任务
     */
    public static <T, R> List<R> batchFunction(Integer batchSize, Collection<T> collection, Function<List<T>, List<R>> function) {
        if (CollUtil.isEmpty(collection)) {
            return Collections.emptyList();
        }

        List<R> result = new ArrayList<>(Math.max(100_000, collection.size()));
        List<List<T>> split = CollUtil.split(collection, batchSize == null ? 1000 : batchSize);
        for (int i = 0; i < split.size(); i++) {
            log.debug("正在执行 batchFunction，进度：{}/{}", i + 1, split.size());
            result.addAll(function.apply(split.get(i)));
        }
        return result;
    }

    /**
     * 分批处理任务
     */
    public static <T> void batchConsumer(Integer batchSize, Collection<T> collection, Consumer<List<T>> consumer) {
        if (CollUtil.isEmpty(collection)) {
            return;
        }

        List<List<T>> split = CollUtil.split(collection, batchSize == null ? 1000 : batchSize);
        for (int i = 0; i < split.size(); i++) {
            log.debug("正在执行 batchConsumer，进度：{}/{}", i + 1, split.size());
            consumer.accept(split.get(i));
        }
    }

}
