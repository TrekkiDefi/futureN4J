package com.github.ittalks.fn.core.jdo.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.jdo.LocalPersistenceManagerFactoryBean;
import org.springframework.stereotype.Component;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;
import java.util.logging.Logger;

@Component
public class PersistenceManagerRetriever extends ThreadLocal {

    private static final Logger logger = Logger.getLogger(PersistenceManagerRetriever.class.getName());

    /**
     * 代码规范：该类的功能使用必须传入`localPersistenceManagerFactoryBean`，故使用构造方法注入，并且不提供无参构造方法。
     * @param localPersistenceManagerFactoryBean
     */
    @Autowired
    public PersistenceManagerRetriever(@Qualifier("pmf") LocalPersistenceManagerFactoryBean localPersistenceManagerFactoryBean) {
        this.localPersistenceManagerFactoryBean = localPersistenceManagerFactoryBean;
        logger.info(this.localPersistenceManagerFactoryBean.getObject().toString());
    }

    private LocalPersistenceManagerFactoryBean localPersistenceManagerFactoryBean;

    private PersistenceManagerFactory pmf;

    /**
     * 获取相关的PersistenceManagerFactory
     *
     * @return PersistenceManagerFactory实例
     */
    public PersistenceManagerFactory pmf() {
        if (pmf == null) {
            pmf = localPersistenceManagerFactoryBean.getObject();
        }
        return pmf;
    }

    /**
     * 获取一个与当前线程相关的PersistenceManager
     *
     * @return PersistenceManager实例
     */
    public PersistenceManager pm() {
        return (PersistenceManager) get();
    }

    public Object get() {

        PersistenceManager pm = (PersistenceManager) super.get();

        if (pm == null || pm.isClosed()) {
            pm = pmf().getPersistenceManager();
            set(pm);
            logger.info("retrieved new PM: " + pm);
        }
        return pm;
    }

    /**
     * 释放所有与本线程相关的JDO资源
     */
    public void cleanup() {
        PersistenceManager pm = (PersistenceManager) super.get();

        if (pm == null) return;

        try {
            if (!pm.isClosed()) {
                Transaction ts = pm.currentTransaction();
                if (ts != null && ts.isActive()) {
                    logger.info("发现一个未完成的Transaction [" + pmf.getConnectionURL() + "]！" + ts);
                    ts.rollback();
                }
                pm.close();
            }
        } catch (Exception e) {
            logger.info("释放JDO资源时出错：" + e);
        } finally {
            set(null);
        }
    }
}