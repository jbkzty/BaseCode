package com.jdk8.optional;

import java.util.Optional;

/**
 * (1) 变量存在时，Optional类只是对这个类简单的封装，变量不存在的时候，缺失的值会被建模成一个空的Optional对象，由方法Optional.empty()返回
 * Optional.empty()方法是一个静态工厂方法，返回Optional类的特定单一实例
 * <p>
 * (2) 使用Optional,可以非常清晰的界定出变量值的缺失是结构上的问题，还是代码上的缺陷
 * <p>
 * (3) 创建Optional对象
 * - 声明一个空的Optional: Optional<Car> optCar = Optional.empty()
 * - 依据一个非空值创建Optional: Optional<Car> optCar = Optional.of(car);  如果对象是car是一个null,那么代码会立即抛出一个NullPointException
 * - 可接受null的Optional : Optional<Car> optCar = Optional.ofNullable(car);
 *
 * @author spuerKun
 * @date 17/11/20.
 */
public class OptionalMain {

    /**
     * flatMap: 接受一个函数作为参数，这个函数的返回值是另外一个流（由方法生成的每个流都会被扁平化为一个单一的流）
     * <p>
     * 使用Optional<Person>对象,我们可以结合使用map和flatmap方法，从Person中解析引出Car,从Car中解析出Insurance,从Insurance对象中引出包含insurance公司名称的字符串
     * <p>
     * step1:
     * 某个Function作为参数，被传递给由Optional封装的Person对象，对其进行转换.
     * 这个例子中的表现是一个方法的引用,即对Person对象的getCar的引用,由于该方法返回一个Optional<Car>类型的对象,Optional内的Person也被转换成了这种对象的实例,结果就是一个两层的Optional对象,最终它们会被flagMap操作合并
     *
     * step2:
     * 与第一步大同小异
     *
     * step3:
     * 将Optional<Insurance>转化为Optional<String>对象,由于Insurance.getName() 方法的返回类型为String,这里就不再需要进行flapMap操作了
     *
     *
     * @param person
     * @return
     */
    public String getCarInsuranceName(Optional<Person> person) {
        return person.flatMap(Person::getCar)
                .flatMap(Car::getInsurance)
                .map(Insurance::getName)
                .orElse("Unknown");
    }

}
