package com.alibaba.android.arouter.core;

import android.content.Context;
import android.util.LruCache;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.service.AutowiredService;
import com.alibaba.android.arouter.facade.template.ISyringe;

import java.util.ArrayList;
import java.util.List;

import static com.alibaba.android.arouter.utils.Consts.SUFFIX_AUTOWIRED;

/**
 * param inject service impl.
 *
 * @author zhilong <a href="mailto:zhilong.lzl@alibaba-inc.com">Contact me.</a>
 * @version 1.0
 * @since 2017/2/28 下午6:08
 */
@Route(path = "/arouter/service/autowired")
public class AutowiredServiceImpl implements AutowiredService {
    private LruCache<String, ISyringe> classCache;
    private List<String> blackList;

    @Override
    public void init(Context context) {
        classCache = new LruCache<>(50);
        blackList = new ArrayList<>();
    }

    @Override
    public void autowire(Object instance) {
        doInject(instance, null);
    }

    /**
     * Recursive injection
     *
     * @param instance who call me.
     * @param parent   parent of me.
     */
    private void doInject(Object instance, Class<?> parent) {
        // 没父类，获取当前的class；否则获取父类的
        Class<?> clazz = null == parent ? instance.getClass() : parent;

        // 获取到对应的注射器，因为注射器类名为包名+类名+固定后缀，所以能反射找到注射器类
        ISyringe syringe = getSyringe(clazz);
        if (null != syringe) {
            // 直接调用注射
            syringe.inject(instance);
        }

        Class<?> superClazz = clazz.getSuperclass();
        // has parent and its not the class of framework.
        if (null != superClazz && !superClazz.getName().startsWith("android")) {
            // 有父类，并且不是系统类。用父类的注射器［syringe］注入[instance]（instance为父类的子类），以完成父类的赋值操作
            doInject(instance, superClazz);
        }
    }

    // 获取注射器
    private ISyringe getSyringe(Class<?> clazz) {
        String className = clazz.getName();

        try {
            // 没在黑列表，则获取；否则在黑列表，说明之前已经获取并且发生了异常，所以不再继续获取
            if (!blackList.contains(className)) {
                // 先在缓存获取，缓存没有则创建
                ISyringe syringeHelper = classCache.get(className);
                if (null == syringeHelper) {  // No cache.
                    syringeHelper = (ISyringe) Class.forName(clazz.getName() + SUFFIX_AUTOWIRED).getConstructor().newInstance();
                }
                classCache.put(className, syringeHelper);
                return syringeHelper;
            }
        } catch (Exception e) {
            blackList.add(className);    // This instance need not autowired.
        }

        return null;
    }
}
