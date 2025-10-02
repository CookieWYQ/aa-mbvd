package com.cookiewyq.aa_mbvd.util;

import java.util.HashMap;
import java.util.Map;

public class MapUtils {

    /**
     * 深拷贝HashMap
     * 注意：此方法假设值对象实现了Cloneable接口或有拷贝构造函数
     *
     * @param original 原始HashMap
     * @param <K> 键的类型
     * @param <V> 值的类型
     * @return 深拷贝后的HashMap
     */
    public static <K, V> HashMap<K, V> deepCopyHashMap(HashMap<K, V> original) {
        if (original == null) {
            return null;
        }

        HashMap<K, V> copiedMap = new HashMap<>();

        for (Map.Entry<K, V> entry : original.entrySet()) {
            K key = entry.getKey();
            V value = entry.getValue();

            // 处理键的深拷贝（如果需要）
            K copiedKey = copyObject(key);

            // 处理值的深拷贝
            V copiedValue = copyObject(value);

            copiedMap.put(copiedKey, copiedValue);
        }

        return copiedMap;
    }

    /**
     * 通用对象拷贝方法
     * 支持多种拷贝方式
     *
     * @param obj 要拷贝的对象
     * @param <T> 对象类型
     * @return 拷贝后的对象
     */
    @SuppressWarnings("unchecked")
    private static <T> T copyObject(T obj) {
        if (obj == null) {
            return null;
        }

        // 对于基本类型和String，直接返回（它们是不可变的）
        if (obj instanceof String || obj instanceof Number || obj instanceof Boolean ||
            obj instanceof Character || obj instanceof Enum) {
            return obj;
        }

        // 尝试使用clone方法
        if (obj instanceof Cloneable) {
            try {
                if (obj instanceof HashMap) {
                    // 递归处理嵌套的HashMap
                    return (T) deepCopyHashMap((HashMap<?, ?>) obj);
                } else {
                    // 调用clone方法
                    return (T) obj.getClass().getMethod("clone").invoke(obj);
                }
            } catch (Exception e) {
                // 如果clone失败，继续尝试其他方法
            }
        }

        // 尝试使用拷贝构造函数
        try {
            return (T) obj.getClass().getConstructor(obj.getClass()).newInstance(obj);
        } catch (Exception e) {
            // 如果拷贝构造函数不存在或失败
        }

        // 如果以上方法都失败，返回原对象（退化为浅拷贝）
        // 在实际应用中，你可能需要根据具体类型添加特定的拷贝逻辑
        return obj;
    }

    /**
     * 针对特定类型的深拷贝方法示例
     * 你可以根据需要扩展此方法以支持更多自定义类型
     *
     * @param original 原始HashMap
     * @param <K> 键的类型
     * @param <V> 值的类型
     * @return 深拷贝后的HashMap
     */
    public static <K, V> HashMap<K, V> deepCopyHashMapCustom(HashMap<K, V> original) {
        if (original == null) {
            return null;
        }

        HashMap<K, V> copiedMap = new HashMap<>();

        for (Map.Entry<K, V> entry : original.entrySet()) {
            K key = entry.getKey();
            V value = entry.getValue();

            // 根据具体类型进行深拷贝处理
            V copiedValue = deepCopyValue(value);
            K copiedKey = deepCopyKey(key);

            copiedMap.put(copiedKey, copiedValue);
        }

        return copiedMap;
    }

    /**
     * 根据值的具体类型进行深拷贝
     * 需要根据你的实际使用场景实现具体的拷贝逻辑
     *
     * @param value 要拷贝的值
     * @param <V> 值的类型
     * @return 拷贝后的值
     */
    @SuppressWarnings("unchecked")
    private static <V> V deepCopyValue(V value) {
        if (value == null) {
            return null;
        }

        // 添加你的自定义类型拷贝逻辑
        // 例如：
        /*
        if (value instanceof YourCustomClass) {
            return (V) ((YourCustomClass) value).copy();
        } else if (value instanceof ArrayList) {
            return (V) new ArrayList<>((ArrayList<?>) value);
        } else if (value instanceof HashMap) {
            return (V) deepCopyHashMap((HashMap<?, ?>) value);
        }
        */

        // 对于基本类型和不可变对象，直接返回
        if (value instanceof String || value instanceof Number || value instanceof Boolean ||
            value instanceof Character || value instanceof Enum) {
            return value;
        }

        // 默认情况下尝试使用clone
        try {
            if (value instanceof Cloneable) {
                return (V) value.getClass().getMethod("clone").invoke(value);
            }
        } catch (Exception e) {
            // 忽略异常
        }

        // 如果无法深拷贝，返回原对象（注意这会退化为浅拷贝）
        return value;
    }

    /**
     * 根据键的具体类型进行深拷贝
     * 通常键是不可变对象，如String等
     *
     * @param key 要拷贝的键
     * @param <K> 键的类型
     * @return 拷贝后的键
     */
    private static <K> K deepCopyKey(K key) {
        // 键通常不需要深拷贝，因为它们通常是不可变对象
        return key;
    }
}

