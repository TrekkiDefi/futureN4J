## 概述

- JMS消息服务；
- 基于消息驱动的POJO；
- 使用基于消息的RPC；
    将JMS导出为RPC远程调用

## @Async/@Transational相关说明

```java
@Component
public class JmsConsumer implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = Logger.getLogger(JmsConsumer.class.getName());

    private final JmsOperations template;

    @Autowired
    public JmsConsumer(JmsOperations template) {
        this.template = template;
    }

    private void consume() {
        while (true) {
            ObjectMessage message = (ObjectMessage) template.receive("barrage.queue");
            try {
                Msg msg = (Msg) message.getObject();
                // [FnExecutor-1], Received Msg：[Msg{msg='"hello fnpac"'}]
                logger.info("[" + Thread.currentThread().getName() + "], Received Msg：[" + msg + "]");
            } catch (JMSException e) {
                throw JmsUtils.convertJmsAccessException(e);
            }
        }
    }
    
    @Async
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            consume();
        }
    }
}
```

通过上面的代码我们发现，`@Async`标注在`onApplicationEvent`方法上。标注在`consume`方法上是没有作用的。

为什么？

**_在同一个类中，一个方法调用另外一个有注解（比如@Async，@Transational）的方法，注解失效的原因和解决方法_**

在同一个类中，一个方法调用另外一个有注解（比如@Async，@Transational）的方法，注解是不会生效的。

比如，下面代码例子中，有两方法，一个有@Transational注解，一个没有。
如果调用了有注解的`addPerson()`方法，会启动一个Transaction；
如果调用`updatePersonByPhoneNo()`，因为它内部调用了有注解的`addPerson()`，如果你以为系统也会为它启动一个Transaction，那就错了，实际上是没有的。

```java
@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    PersonDao personDao;

    @Override
    @Transactional
    public boolean addPerson(Person person) {
        boolean result = personDao.insertPerson(person) > 0 ? true : false;
        return result;
    }

    @Override
    public boolean updatePersonByPhoneNo(Person person) {
        boolean result = personDao.updatePersonByPhoneNo(person) > 0 ? true : false;
        addPerson(person); //测试同一个类中@Transactional是否起作用
        return result;
    }
}
```

**_如何查看是否启动了Transaction？_**

设置log leve为debug，可以查看是否有下面这个log，判断是否启动了Transaction：

```text
DEBUG org.springframework.jdbc.datasource.DataSourceTransactionManager - Creating new transaction with name...
```

同样地，`@Async`等其他注解也有这样的问题。

**_原因：_**

spring 在扫描bean的时候会扫描方法上是否包含`@Transactional`注解，
如果包含，spring会为这个bean动态地生成一个子类（即代理类，proxy），代理类是继承原来那个bean的。
此时，当这个有注解的方法被调用的时候，实际上是由代理类来调用的，代理类在调用之前就会启动transaction。
然而，如果这个**有注解的方法**是被同一个类中的其他方法调用的，那么该方法的调用并没有通过代理类，而是直接通过原来的那个bean，
所以就不会启动transaction，我们看到的现象就是`@Transactional`注解无效。

为什么一个方法`a()`调用同一个类中另外一个方法`b()`的时候，`b()`不是通过代理类来调用的呢？

可以看下面的例子（为了简化，用伪代码表示）：

```text
@Service
class A{
    @Transactinal
    method b(){...}
    
    method a(){    //标记1
        b();
    }
}

//Spring扫描注解后，创建了另外一个代理类，并为有注解的方法插入一个startTransaction()方法：
class proxy$A{
    A objectA = new A();
    method b(){    //标记2
        startTransaction();
        objectA.b();
    }

    method a(){    //标记3
        objectA.a();    //由于a()没有注解，所以不会启动transaction，而是直接调用A的实例的a()方法
    }
}
```

当我们调用`A`的bean的`a()`方法的时候，也是被`proxy$A`拦截，执行`proxy$A.a()`（标记3），
然而，由以上代码可知，这时候它调用的是`objectA.a()`，也就是由原来的bean来调用`a()`方法了，所以代码跑到了“标记1”。
由此可见，“标记2”并没有被执行到，所以`startTransaction()`方法也没有运行。

了解了失效的原因，解决的方法就简单了（两种）：

1. 把这两个方法分开到不同的类中；
2. 把注解加到类名上面；

参考：
<http://stackoverflow.com/questions/18590170/transactional-does-not-work-on-method-level>


