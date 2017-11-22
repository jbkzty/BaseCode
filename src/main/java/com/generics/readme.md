
**泛型不是协变的(covariant)**   

          java中的数组是协变的(covariant),也就是说，如果Integer扩展了Number,那么不仅Integer是Number,而且Integer[]也是Number[],
       在要求Number[]的地方完全可以赋值或者传递Integer[],更正式的说，如果Number是Integer的父类,那么Number[]也是Integer的父类
       
          List<Number> 是 List<Integer>的超类型，那么是不是在需要  List<Number> 的时候也可以传入 List<Integer> ?
          
          不可以这样做有一个很充分的原因：**这样做将破坏要提供的类型安全泛型**
          
          如果能将List<Integer>赋值给List<Number>中，那么也可以将非Integer的值放入List<Integer>中
          
          List<Integer> li = new ArrayList<Integer>();
          List<Number> ln = li;  // illegal
          ln.add(new Float(3.1345));
          
          上面的代码,就可以将非Integer类型的内容放入到List<Integer>
  