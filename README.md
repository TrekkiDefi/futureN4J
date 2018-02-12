# futureN4J

[![Build Status](https://travis-ci.org/fnpac/futureN4J.svg?branch=master)](https://travis-ci.org/fnpac/futureN4J)

## 版本更新

- 「20180211」 调整项目代码结构

    1. 将分布式队列独立出单独的项目[KMQueue](https://github.com/fnpac/KMQueue) TODO 备份队列监控
    2. 优化基于Java Config的配置
    3. 优化分布式会话存储配置

## 一、项目管理

1. 项目管理 ：Maven
2. 版本控制 ：Git

## 二、技术栈

1. ioc容器：Spring
2. web框架：SpringMVC
3. druid数据源、druid日志监控
4. 基于Redis分布式队列
5. 线程池
6. WebService：Apache CXF
7. Log日志：logback
8. HTTP Client：Retrofit封装
9. 基于Redis分布式锁
10. xml迁移为Java Config搭建方式
11. 基于redis的分布式会话存储 - spring redis session
12. Spring Data JPA

## 三、项目Module

### 1、Google SDK

#### #1 google oauth2

#### #2 google calendar

**_设计_**

使用**基于Redis的分布式队列**实现数据的拉取与处理。

---

### 2、Commons Collections

#### #1 基于Redis的分布式队列

抽离到单独的项目[KMQueue](https://github.com/fnpac/KMQueue)，具体的设计和使用请参阅该项目[README.md](https://github.com/fnpac/KMQueue/blob/master/README.md)

#### #2 基于Redis的分布式锁

```java
GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
final JedisPool pool = new JedisPool(poolConfig, host, port, timeout, password);
Jedis jedis = pool.getResource();

RedisLock redisLock = new RedisLock(jedis, lockKey, timeoutMsecs, expireMsecs);

try {
    if (redisLock.acquire()) { // 启用锁
        //执行业务逻辑
    } else {
        logger.info("The time wait for lock more than [{}] ms ", timeoutMsecs);
    }
} catch (Throwable t) {
    // 分布式锁异常
    logger.warn(t.getMessage(), t);
} finally {
    if (redisLock != null) {
        try {
            redisLock.release();// 则解锁
        } catch (Exception e) {
        }
    }
    if (jedis != null) {
        try {
            pool.returnResource(jedis);// 还到连接池里
        } catch (Exception e) {
        }
    }
}
```

也可以使用Spring提供的RedisTemplate.

```java
//获取Redis的连接
RedisConnection redisConnection = redisTemplate.getConnectionFactory().getConnection();
Jedis jedis = (Jedis) redisConnection.getNativeConnection();
//创建锁对象
RedisLock redisLock = new RedisLock(jedis, RedisKeyConstant.KEY_REMINDS_GEN_DISTRIBUTE_LOCK);

try {
    if (redisLock.acquire()) { // 加锁成功
        //执行业务逻辑   ------------- start -------------------
        ...
        //执行业务逻辑   ------------- end -------------------
    } else {    //加锁失败
        logger.error("请求分布式锁超时,超时时间 [{}] ms ", redisLock.getAcquireTimeoutInMillis());
        continue;
    }
} catch (Throwable t) {
    // 分布式锁异常
    logger.warn(t.getMessage(), t);
} finally {
    if (redisLock != null) {
        try {
            redisLock.release();// 则解锁
        } catch (Exception e) {
        }
    }
    if (redisConnection != null) {
        try {
            redisConnection.close();// 还到连接池里
        } catch (Exception e) {
        }
    }
}
```

## 四、开发规范

响应返回规范化：

1. 响应成功：

    ![](./static/response.png)

2. 响应失败：

    ![](./static/response2.png)
