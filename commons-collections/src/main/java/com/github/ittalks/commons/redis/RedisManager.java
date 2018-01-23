package com.github.ittalks.commons.redis;

import com.github.ittalks.commons.redis.queue.config.ConfigManager;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.util.Pool;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Created by 刘春龙 on 2017/3/3.
 * <p>
 * Redis连接池工具类
 */
public class RedisManager {

    public final static Logger logger = Logger.getLogger(RedisManager.class.getName());

    private static Pool<Jedis> pool;

    static {
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化连接池
     */
    private static void init() throws Exception {

        Properties props = ConfigManager.getRedisConn();
        logger.info("初始化Redis连接池。");

        if (props == null) {
            throw new RuntimeException("没有找到Redis配置文件");
        }

        //创建jedis池配置实例
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();

        //加载jedis池配置项
        Integer poolMaxTotal = Integer.valueOf(props.getProperty("redis.pool.maxTotal").trim());
        jedisPoolConfig.setMaxTotal(poolMaxTotal);

        Integer poolMaxIdle = Integer.valueOf(props.getProperty("redis.pool.maxIdle").trim());
        jedisPoolConfig.setMaxIdle(poolMaxIdle);

        Long poolMaxWaitMillis = Long.valueOf(props.getProperty("redis.pool.maxWaitMillis").trim());
        jedisPoolConfig.setMaxWaitMillis(poolMaxWaitMillis);

        logger.info(String.format("poolMaxTotal: %s, poolMaxIdle: %s", poolMaxTotal, poolMaxIdle));

        String connectMode = props.getProperty("redis.connectMode");
        logger.info(String.format("connectMode : %s.", connectMode));

        if ("single".equals(connectMode)) {
            //单机
            String host = props.getProperty("redis.pool.host");
            String port = props.getProperty("redis.pool.port");

            logger.info(String.format("hostPort - %s.", host + ":" + port));

            pool = new JedisPool(jedisPoolConfig, host, Integer.valueOf(port));
        } else if ("sentinel".equals(connectMode)) {
            // 集群
            String hostPort = props.getProperty("redis.hostPort");

            if (StringUtils.isEmpty(hostPort)) {
                throw new RuntimeException("redis配置文件未配置主机-端口集。");
            }

            logger.info(String.format("hostPort - %s.", hostPort));

            // 根据配置实例化jedis池
            String[] hostPortSet = hostPort.split(",");
            Set<String> sentinels = new HashSet<>();
            sentinels.addAll(Arrays.asList(hostPortSet));
            pool = new JedisSentinelPool("master", sentinels, jedisPoolConfig);
        }
    }

    /**
     * 获取Jedis对象
     * 使用完成后，必须调用returnResource归还到连接池中
     *
     * @return Jedis对象
     */
    public static synchronized Jedis getResource() {
        Jedis jedis = pool.getResource();
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
    public static synchronized Jedis getResource(int db) {
        Jedis jedis = pool.getResource();
        jedis.select(db);
        logger.info("获得Redis连接：" + jedis);
        return jedis;
    }

    /**
     * 归还Redis连接到连接池
     *
     * @param jedis Jedis对象
     */
    public static synchronized void returnResource(Jedis jedis) {
        if (jedis != null) {
//            pool.returnResource(jedis);
            // from Jedis 3.0
            jedis.close();
            logger.info("归还Redis连接到连接池：" + jedis);
        }
    }

    public static synchronized void destroy() throws Exception {
        pool.destroy();
    }
}
