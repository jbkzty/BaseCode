package com.jdk8.chap8.proxy.demo1;

/**
 * @author spuerKun
 * @date 18/1/5.
 */
public class Validator {

    private final ValidatioStrategy strategy;

    public Validator(ValidatioStrategy strategy) {
        this.strategy = strategy;
    }

    public boolean validate(String str) {
        return strategy.execute(str);
    }

    public static void main(String[] args) {
        Validator numericValidator = new Validator(new IsNumeric());
        boolean b1 = numericValidator.validate("aaa");

        Validator lowerValidator = new Validator(new IsAllLowerCase());
        boolean b2 = lowerValidator.validate("bbb");

        System.out.println(b1);
        System.out.println(b2);

        /**
         * 其实，ValidatioStrategy是一个函数接口
         * (与Predicate<String>具有相同的函数描述,这意味着我们不需要声明新的类来实现不同的策略,可以直接传递Lambda表达式)
         */
        Validator numericValidator2 = new Validator((String s) -> s.matches("[a-z]+"));
        boolean b11 = numericValidator2.validate("aaa");

        Validator lowerValidator2 = new Validator((String s) -> s.matches("\\d+"));
        boolean b22 = lowerValidator2.validate("bbb");

        System.out.println(b11);
        System.out.println(b22);
    }
}
