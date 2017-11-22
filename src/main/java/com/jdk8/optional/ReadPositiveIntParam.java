package com.jdk8.optional;

import java.util.Optional;
import java.util.Properties;

/**
 * 实现需求:
 * 给定属性对应的值是一个代表正整数的字符串，就返回该整数值，任何其他的情况下都返回0
 * <p>
 * (1) 如果属性为a,readDuration方法返回5，因为该属性对应的字符串能映射到一个正数
 * (2) 如果属性为b,readDuration方法返回0，因为它对应的值不是一个数字
 * (3) 如果属性为c,readDuration方法返回0，因为它对应的值是一个负数
 * (4) 如果属性为d,readDuration方法返回0，因为不存在该名称对应的属性
 *
 * @author spuerKun
 * @date 17/11/22.
 */
public class ReadPositiveIntParam {

    private static Properties durationProp = new Properties();

    static {
        durationProp.setProperty("a", "5");
        durationProp.setProperty("b", "true");
        durationProp.setProperty("c", "-3");
    }

    /**
     * 命令式编程 (复杂而不具备可读性)
     *
     * @param props
     * @param name
     * @return
     */
    public static int readDuration(Properties props, String name) {
        String value = props.getProperty(name);
        if (value != null) {
            try {
                // 将String转换成int
                int i = Integer.parseInt(value);
                // 检查返回的数字是否为正数
                if (i > 0) {
                    return i;
                }
            } catch (NumberFormatException e) {
                //well, we dont care
            }
        }
        //如果前述的条件都不满足，则返回0
        return 0;
    }

    /**
     * (1) 如果访问的属性不存在,props.getProperty(name)方法返回的值是一个null
     * (2) 使用ofNullable工厂方法非常轻易地就能把该值转换为Optional对象
     * (3) 向flatMap方法传递代码 ReadPositiveIntParam.stringToInt 方法的引用
     * (4) 将Optional<String>转换为Optional<Integer>
     * (5) 过滤负数
     * <p>
     * 如果任何一个操作返回一个空的Optional对象,该方法都会返回orElse方法设置的默认值0
     * 否则就返回封装在Optional对象中的正整数
     *
     * @param props
     * @param name
     * @return
     */
    public static int readDurationByOptional(Properties props, String name) {
        return Optional.ofNullable(props.getProperty(name))
                .flatMap(ReadPositiveIntParam::stringToInt)
                .filter(i -> i > 0)
                .orElse(0);
    }

    /**
     * 出现异常的情况下，也可以使用空的Optional来代替
     *
     * @param str
     * @return
     */
    public static Optional<Integer> stringToInt(String str) {
        try {
            return Optional.of(Integer.parseInt(str));
        } catch (NumberFormatException e) {
            //返回一个空的optional对象
            return Optional.empty();
        }
    }

    public static void main(String[] args) {
        System.out.println(readDuration(durationProp, "a"));
        System.out.println(readDurationByOptional(durationProp, "a"));

        System.out.println(readDuration(durationProp, "b"));
        System.out.println(readDurationByOptional(durationProp, "b"));

        System.out.println(readDuration(durationProp, "c"));
        System.out.println(readDurationByOptional(durationProp, "c"));

        System.out.println(readDuration(durationProp, "d"));
        System.out.println(readDurationByOptional(durationProp, "d"));
    }


}
