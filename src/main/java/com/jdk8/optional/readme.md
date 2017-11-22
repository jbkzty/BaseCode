(1) null引用在历史上被引入到程序设计语言中,目的是为了表示变量值的缺失。

(2) 引入了一个新的类java.util.Optional<T>,对存在或者缺失对象的建模


API:

      empty   :  返回一个空的Optional实例
      
      filter  :  如果值存在并且满足提供的谓词,就返回包含该值的 Optional 对象;否则返回一个空的Optional对象
 
      flatMap :  如果值存在,就对该值执行提供的mapping函数调用,返回一个Optional类型的值,否则就返回一个空的Optional对象
      
      get     :  如果该值存在,将该值用Optional封装返回,否则抛出一个NoSuchElementException异常
      
      ifPresent :  如果值存在,就执行使用该值的方法调用,否则什么也不做
      
      isPresent :  如果值存在就返回true,否则返回false
      
      map : 如果值存在,就对该值执行提供的 mapping 函数调用
      
      of : 将指定值用Optional封装之后返回,如果该值为null,则抛出一个NullPointerException异常
      
      ofNullable : 将指定值用Optional封装之后返回,如果该值为null,则返回一个空的Optional对象
      
      orElse : 如果有值则将其返回,否则返回一个默认值
      
      orElseGet : 如果有值则将其返回,否则返回一个由指定的Supplier接口生成的值
      
      orElseThrow : 如果有值则将其返回,否则抛出一个由指定的Supplier接口生成的异常