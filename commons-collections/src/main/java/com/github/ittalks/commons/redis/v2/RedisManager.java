package com.github.ittalks.commons.redis.v2;

import com.github.ittalks.fn.common.util.SpringAwareUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.logging.Logger;

/**
 * Created by 刘春龙 on 2017/3/3.
 * <p>
 * Redis连接池工具类
 */
public class RedisManager {

    public final static Logger logger = Logger.getLogger(RedisManager.class.getName());

    private static final JedisPool jedisPool = (JedisPool) SpringAwareUtils.getBean("jedisPool");

    /**
     * 获取Jedis对象
     * 使用完成后，必须调用returnResource归还到连接池中
     *
     * @return Jedis对象
     */
    public static Jedis getResource() {
        Jedis jedis = jedisPool.getResource();
        logger.info("获得Redis连接：" + jedis);
        return jedis;
    }

    /**
     * 获取Jedis对象
     * 使用完成后，必须调用returnResource归还到连接池中
     *
     * @param db Redis数据库序号
     * @return Jedis对象
     */
    public static Jedis getResource(int db) {
        Jedis jedis = jedisPool.getResource();
        jedis.select(db);
        logger.info("获得Redis连接：" + jedis);
        return jedis;
    }

    /**
     * 归还Redis连接到连接池
     *
     * @param jedis Jedis对象
     */
    public static void returnResource(Jedis jedis) {
        if (jedis != null) {
            jedisPool.returnResource(jedis);
            logger.info("归还Redis连接到连接池：" + jedis);
        }
    }

    public static void destroy() throws Exception {
        jedisPool.destroy();
    }
}
