package com.jdk8.lambda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author spuerKun
 * @date 17/12/10.
 */
public class Sorting {

    public static void main(String[] args) {

        // step1
        List<Apple> inventory = new ArrayList<>();
        inventory.addAll(Arrays.asList(new Apple(80, "green"), new Apple(155, "green"), new Apple(120, "red")));

        // step2
        // [Apple{color='green', weight=80}, Apple{color='red', weight=120}, Apple{color='green', weight=155}]
        inventory.sort(new AppleComparator());
        System.out.println(inventory);

        // reshuffling things a little
        inventory.set(1, new Apple(30, "green"));

        // 第二步: 使用匿名类
        inventory.sort(new Comparator<Apple>() {
            @Override
            public int compare(Apple o1, Apple o2) {
                return o1.getWeight().compareTo(o2.getWeight());
            }
        });
        System.out.println(inventory);

        // reshuffling things a little
        inventory.set(1, new Apple(20, "red"));
        System.out.println(inventory);

        // 第三步: 使用lambda
        inventory.sort((Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()));
        System.out.println(inventory);

        // 第四步: 使用lambda在编译器中可以根据上下文来推断lambda表达式的参数类型
        inventory.sort((a1, a2) -> a1.getWeight().compareTo(a2.getWeight()));
        System.out.println(inventory);

        // 第五步: Comparator具有一个叫作comparing的静态辅助方法, 它可以接受一个Function来提取Comparable键值,并生成一个Comparator对象
        inventory.sort(Comparator.comparing((a) -> a.getWeight()));
        System.out.println(inventory);

        // 第六步: 使用方法引用,代替那些转发参数的Lambda表达式的语法糖
        inventory.sort(Comparator.comparing(Apple::getWeight));
        System.out.println(inventory);
    }

    public static class Apple {
        private Integer weight = 0;
        private String color = "";

        public Apple(Integer weight, String color) {
            this.weight = weight;
            this.color = color;
        }

        public Integer getWeight() {
            return weight;
        }

        public void setWeight(Integer weight) {
            this.weight = weight;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        @Override
        public String toString() {
            return "Apple{" + "color='" + color + '\'' + ", weight=" + weight + '}';
        }
    }

    /**
     * 第一步：传递代码
     */
    public static class AppleComparator implements Comparator<Apple> {
        @Override
        public int compare(Apple o1, Apple o2) {
            return o1.getWeight().compareTo(o2.getWeight());
        }
    }

}
