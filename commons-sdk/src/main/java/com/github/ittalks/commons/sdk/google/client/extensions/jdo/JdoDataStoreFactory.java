package com.github.ittalks.commons.sdk.google.client.extensions.jdo;

import com.google.api.client.util.Lists;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.Sets;
import com.google.api.client.util.store.AbstractDataStore;
import com.google.api.client.util.store.AbstractDataStoreFactory;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.DataStoreUtils;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

/**
 * Created by 刘春龙 on 2017/2/23.
 */
public class JdoDataStoreFactory extends AbstractDataStoreFactory {
    private final PersistenceManagerFactory persistenceManagerFactory;

    public JdoDataStoreFactory(PersistenceManagerFactory persistenceManagerFactory) {
        this.persistenceManagerFactory = (PersistenceManagerFactory)Preconditions.checkNotNull(persistenceManagerFactory);
    }

    protected <V extends Serializable> DataStore<V> createDataStore(String id) throws IOException {
        return new JdoDataStoreFactory.JdoDataStore(this, this.persistenceManagerFactory, id);
    }

    static class PrivateUtils {
        PrivateUtils() {
        }

        public static class ComposedIdKey implements Serializable {
            private static final long serialVersionUID = 1L;
            public String key;
            public String id;

            public ComposedIdKey() {
            }

            public ComposedIdKey(String value) {
                StringTokenizer token = new StringTokenizer(value, "::");
                token.nextToken();
                this.key = token.nextToken();
                this.id = token.nextToken();
            }

            public boolean equals(Object obj) {
                if(obj == this) {
                    return true;
                } else if(!(obj instanceof JdoDataStoreFactory.PrivateUtils.ComposedIdKey)) {
                    return false;
                } else {
                    JdoDataStoreFactory.PrivateUtils.ComposedIdKey other = (JdoDataStoreFactory.PrivateUtils.ComposedIdKey)obj;
                    return this.key.equals(other.key) && this.id.equals(other.id);
                }
            }

            public int hashCode() {
                return this.key.hashCode() ^ this.id.hashCode();
            }

            public String toString() {
                return this.getClass().getName() + "::" + this.key + "::" + this.id;
            }
        }
    }

    static class JdoDataStore<V extends Serializable> extends AbstractDataStore<V> {
        private final Lock lock = new ReentrantLock();
        private final PersistenceManagerFactory persistenceManagerFactory;

        JdoDataStore(JdoDataStoreFactory dataStore, PersistenceManagerFactory persistenceManagerFactory, String id) {
            super(dataStore, id);
            this.persistenceManagerFactory = persistenceManagerFactory;
        }

        public Set<String> keySet() throws IOException {
            this.lock.lock();

            try {
                PersistenceManager persistenceManager = this.persistenceManagerFactory.getPersistenceManager();

                try {
                    Query query = this.newAllKeysQuery(persistenceManager);

                    try {
                        HashSet result = Sets.newHashSet();
                        Iterator i$ = this.executeAllKeysQuery(query).iterator();

                        while(i$.hasNext()) {
                            JdoValue jdoValue = (JdoValue)i$.next();
                            result.add(jdoValue.getKey());
                        }

                        Set i$1 = Collections.unmodifiableSet(result);
                        return i$1;
                    } finally {
                        query.closeAll();
                    }
                } finally {
                    persistenceManager.close();
                }
            } finally {
                this.lock.unlock();
            }
        }

        public Collection<V> values() throws IOException {
            this.lock.lock();

            try {
                PersistenceManager persistenceManager = this.persistenceManagerFactory.getPersistenceManager();

                try {
                    Query query = this.newAllKeysQuery(persistenceManager);

                    try {
                        ArrayList result = Lists.newArrayList();
                        Iterator i$ = this.executeAllKeysQuery(query).iterator();

                        while(i$.hasNext()) {
                            JdoValue jdoValue = (JdoValue)i$.next();
                            result.add(jdoValue.deserialize());
                        }

                        List i$1 = Collections.unmodifiableList(result);
                        return i$1;
                    } finally {
                        query.closeAll();
                    }
                } finally {
                    persistenceManager.close();
                }
            } finally {
                this.lock.unlock();
            }
        }

        public V  get(String key) throws IOException {
            if(key == null) {
                return null;
            } else {
                this.lock.lock();

                Serializable var5;
                try {
                    PersistenceManager persistenceManager = this.persistenceManagerFactory.getPersistenceManager();

                    try {
                        Query query = this.newKeyQuery(persistenceManager);

                        try {
                            JdoValue jdoValue = this.executeKeyQuery(query, key);
                            var5 = jdoValue == null?null:jdoValue.deserialize();
                        } finally {
                            query.closeAll();
                        }
                    } finally {
                        persistenceManager.close();
                    }
                } finally {
                    this.lock.unlock();
                }

                return (V) var5;
            }
        }

        public JdoDataStoreFactory.JdoDataStore<V> set(String key, V value) throws IOException {
            Preconditions.checkNotNull(key);
            Preconditions.checkNotNull(value);
            this.lock.lock();

            try {
                PersistenceManager persistenceManager = this.persistenceManagerFactory.getPersistenceManager();

                try {
                    Query query = this.newKeyQuery(persistenceManager);

                    try {
                        JdoValue jdoValue = this.executeKeyQuery(query, key);
                        if(jdoValue != null) {
                            jdoValue.serialize(value);
                        } else {
                            jdoValue = new JdoValue(this.getId(), key, value);
                            persistenceManager.makePersistent(jdoValue);
                        }
                    } finally {
                        query.closeAll();
                    }
                } finally {
                    persistenceManager.close();
                }
            } finally {
                this.lock.unlock();
            }

            return this;
        }

        public DataStore<V> delete(String key) throws IOException {
            if(key == null) {
                return this;
            } else {
                this.lock.lock();

                try {
                    PersistenceManager persistenceManager = this.persistenceManagerFactory.getPersistenceManager();

                    try {
                        Query query = this.newKeyQuery(persistenceManager);

                        try {
                            JdoValue jdoValue = this.executeKeyQuery(query, key);
                            if(jdoValue != null) {
                                persistenceManager.deletePersistent(jdoValue);
                            }
                        } finally {
                            query.closeAll();
                        }
                    } finally {
                        persistenceManager.close();
                    }
                } finally {
                    this.lock.unlock();
                }

                return this;
            }
        }

        public JdoDataStoreFactory.JdoDataStore<V> clear() throws IOException {
            this.lock.lock();

            try {
                PersistenceManager persistenceManager = this.persistenceManagerFactory.getPersistenceManager();

                try {
                    Query query = this.newAllKeysQuery(persistenceManager);

                    try {
                        persistenceManager.deletePersistentAll(this.executeAllKeysQuery(query));
                    } finally {
                        query.closeAll();
                    }
                } finally {
                    persistenceManager.close();
                }
            } finally {
                this.lock.unlock();
            }

            return this;
        }

        public JdoDataStoreFactory getDataStoreFactory() {
            return (JdoDataStoreFactory)super.getDataStoreFactory();
        }

        public String toString() {
            return DataStoreUtils.toString(this);
        }

        Query newAllKeysQuery(PersistenceManager persistenceManager) {
            Query query = persistenceManager.newQuery(JdoValue.class);
            query.setFilter("id == idParam");
            query.declareParameters("String idParam");
            return query;
        }

        Collection<JdoValue> executeAllKeysQuery(Query allKeysQuery) {
            return (Collection)allKeysQuery.execute(this.getId());
        }

        Query newKeyQuery(PersistenceManager persistenceManager) {
            Query query = persistenceManager.newQuery(JdoValue.class);
            query.setFilter("id == idParam && key == keyParam");
            query.declareParameters("String idParam, String keyParam");
            return query;
        }

        JdoValue executeKeyQuery(Query keyQuery, String key) {
            Collection queryResult = (Collection)keyQuery.execute(this.getId(), key);
            return queryResult.isEmpty()?null:(JdoValue)queryResult.iterator().next();
        }
    }
}

