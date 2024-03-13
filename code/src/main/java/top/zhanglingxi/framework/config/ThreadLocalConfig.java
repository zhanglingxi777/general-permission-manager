package top.zhanglingxi.framework.config;

import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 自定义ThreadLocal
 */
@Component
public class ThreadLocalConfig {
    public static ThreadLocal<Map> mapThreadLocal = new ThreadLocal<>();

    /**
     * 获取当前线程的变量
     * @return 当前线程的变量
     */
    public Map get() {
        return mapThreadLocal.get();
    }

    /**
     * 设置当前线程的存的变量
     * @param map 前线程的存的变量
     */
    public void set(Map map) {
        mapThreadLocal.set(map);
    }

    /**
     * 移除当前线程的存的变量
     */
    public void remove() {
        mapThreadLocal.remove();
    }
}
