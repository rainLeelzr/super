package vip.isass.core.structure;

/**
 * @author : rain
 * @date : 2023/3/30
 */
public interface IDictTranslationProvider {

    /**
     * 翻译字典
     *
     * @param parentDictCode 字典项的父字典编码
     * @param dictCode       字典项的编码
     * @return 字典名称
     */
    String translate(String parentDictCode, String dictCode);

}
