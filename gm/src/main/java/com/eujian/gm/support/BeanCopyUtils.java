package com.eujian.gm.support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
@Slf4j
public class BeanCopyUtils {


    /**
     * 拷贝list
     * @param dest
     * @param orig
     * @param <T>
     * @return
     */
    public static <T> List<T> copyList(Class<T> dest, List orig) {
        if (CollectionUtils.isEmpty(orig)) {
            return new ArrayList<>();
        }
        BeanCopier beanCopier = BeanCopier.create(orig.get(0).getClass(),dest,false);
        List<T> resultList = new ArrayList<>(orig.size());
        try {
            for (Object o : orig) {
                if (o == null) {continue;}

                T destObject = dest.newInstance();

                beanCopier.copy(o,destObject,null);
                resultList.add(destObject);
            }
            return resultList;
        } catch (Exception e) {
            log.error("copyList error", e);
        }
        return resultList;
    }


    /**
     * 拷贝对象
     * @param dest
     * @param orig
     * @param <T>
     * @return
     */
    public static <T> T copyProperties(Class<T> dest, Object orig) {
        if (orig == null) {
            return null;
        }
        try {
            T destObject = dest.newInstance();
            copyProperties(destObject, orig);
            return destObject;
        } catch (Exception e) {
            log.error("copyProperties error", e);
            return null;
        }
    }
    /**
     * 使用 org.springframework.beans.BeanUtils 拷贝对象
     * @param dest
     * @param orig
     */
    public static void copyProperties(Object dest, Object orig) {
        try {
            BeanCopier copier = BeanCopier.create(orig.getClass(), dest.getClass(), false);
            copier.copy(orig, dest,null);
        } catch (Exception e) {
            log.error("copyProperties error", e);
        }
    }
}
