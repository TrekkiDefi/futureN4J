package com.github.ittalks.commons.sdk.google.client.extensions.jdo;

import com.github.ittalks.fn.common.util.SpringAwareUtils;
import com.github.ittalks.fn.core.jdo.support.PersistenceManagerRetriever;

/**
 * Created by 刘春龙 on 2017/3/7.
 */
public class JdoDataStoreFactoryProxy {

    private static JdoDataStoreFactory dataStoreFactory;

    public static JdoDataStoreFactory getFactory() {
        if (dataStoreFactory == null) {
            PersistenceManagerRetriever retriever = SpringAwareUtils.getBean(PersistenceManagerRetriever.class);
            dataStoreFactory = new JdoDataStoreFactory(retriever.pmf());
        }
        return dataStoreFactory;
    }
}
