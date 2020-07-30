package com.jfkj.im.utils;

import java.util.Collection;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/11/25
 * </pre>
 */
public class CollectionUtils {
    public static <E> int getSize(Collection<E> c) {
        return c == null ? 0 : c.size();
    }
}
