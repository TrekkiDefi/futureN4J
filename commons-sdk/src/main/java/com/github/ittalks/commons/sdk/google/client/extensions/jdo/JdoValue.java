package com.github.ittalks.commons.sdk.google.client.extensions.jdo;

import com.google.api.client.util.IOUtils;

import javax.jdo.PersistenceManager;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.spi.JDOImplHelper;
import javax.jdo.spi.StateManager;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by 刘春龙 on 2017/2/23.
 */
@PersistenceCapable(
        objectIdClass = JdoDataStoreFactory.PrivateUtils.ComposedIdKey.class
)
public class JdoValue implements javax.jdo.spi.PersistenceCapable {
    @PrimaryKey
    @Persistent
    private String key;
    @PrimaryKey
    @Persistent
    private String id;
    @Persistent
    private byte[] bytes;
    protected transient StateManager jdoStateManager;
    protected transient byte jdoFlags;
    private static final byte[] jdoFieldFlags = __jdoFieldFlagsInit();
    private static final Class jdoPersistenceCapableSuperclass = __jdoPersistenceCapableSuperclassInit();
    private static final Class[] jdoFieldTypes = __jdoFieldTypesInit();
    private static final String[] jdoFieldNames = __jdoFieldNamesInit();
    private static final int jdoInheritedFieldCount = __jdoGetInheritedFieldCount();

    JdoValue() {
    }

    <V extends Serializable> JdoValue(String id, String key, V value) throws IOException {
        this.id = id;
        this.key = key;
        this.serialize(value);
    }

    <V extends Serializable> void serialize(V value) throws IOException {
        jdoSetbytes(this, IOUtils.serialize(value));
    }

    <V extends Serializable> V deserialize() throws IOException {
        return IOUtils.deserialize(jdoGetbytes(this));
    }

    String getKey() {
        return jdoGetkey(this);
    }

    static {
        JDOImplHelper.registerClass(___jdo$loadClass("com.google.api.client.extensions.jdo.JdoDataStoreFactory$JdoValue"), jdoFieldNames, jdoFieldTypes, jdoFieldFlags, jdoPersistenceCapableSuperclass, new JdoValue());
    }

    public void jdoCopyKeyFieldsFromObjectId(ObjectIdFieldConsumer fc, Object oid) {
        if(fc == null) {
            throw new IllegalArgumentException("ObjectIdFieldConsumer is null");
        } else if(!(oid instanceof JdoDataStoreFactory.PrivateUtils.ComposedIdKey)) {
            throw new ClassCastException("oid is not instanceof com.google.api.client.extensions.jdo.JdoDataStoreFactory$PrivateUtils$ComposedIdKey");
        } else {
            JdoDataStoreFactory.PrivateUtils.ComposedIdKey o = (JdoDataStoreFactory.PrivateUtils.ComposedIdKey)oid;

            try {
                fc.storeStringField(1, o.id);
                fc.storeStringField(2, o.key);
            } catch (Exception var5) {
                ;
            }

        }
    }

    protected void jdoCopyKeyFieldsFromObjectId(Object oid) {
        if(!(oid instanceof JdoDataStoreFactory.PrivateUtils.ComposedIdKey)) {
            throw new ClassCastException("key class is not com.google.api.client.extensions.jdo.JdoDataStoreFactory$PrivateUtils$ComposedIdKey or null");
        } else {
            JdoDataStoreFactory.PrivateUtils.ComposedIdKey o = (JdoDataStoreFactory.PrivateUtils.ComposedIdKey)oid;

            try {
                this.id = o.id;
                this.key = o.key;
            } catch (Exception var4) {
                ;
            }

        }
    }

    public final void jdoCopyKeyFieldsToObjectId(Object oid) {
        if(!(oid instanceof JdoDataStoreFactory.PrivateUtils.ComposedIdKey)) {
            throw new ClassCastException("key class is not com.google.api.client.extensions.jdo.JdoDataStoreFactory$PrivateUtils$ComposedIdKey or null");
        } else {
            JdoDataStoreFactory.PrivateUtils.ComposedIdKey o = (JdoDataStoreFactory.PrivateUtils.ComposedIdKey)oid;

            try {
                o.id = this.id;
                o.key = this.key;
            } catch (Exception var4) {
                ;
            }

        }
    }

    public final void jdoCopyKeyFieldsToObjectId(ObjectIdFieldSupplier fs, Object oid) {
        if(fs == null) {
            throw new IllegalArgumentException("ObjectIdFieldSupplier is null");
        } else if(!(oid instanceof JdoDataStoreFactory.PrivateUtils.ComposedIdKey)) {
            throw new ClassCastException("oid is not instanceof com.google.api.client.extensions.jdo.JdoDataStoreFactory$PrivateUtils$ComposedIdKey");
        } else {
            JdoDataStoreFactory.PrivateUtils.ComposedIdKey o = (JdoDataStoreFactory.PrivateUtils.ComposedIdKey)oid;

            try {
                o.id = fs.fetchStringField(1);
                o.key = fs.fetchStringField(2);
            } catch (Exception var5) {
                ;
            }

        }
    }

    public final Object jdoGetObjectId() {
        return this.jdoStateManager != null?this.jdoStateManager.getObjectId(this):null;
    }

    public final Object jdoGetVersion() {
        return this.jdoStateManager != null?this.jdoStateManager.getVersion(this):null;
    }

    protected final void jdoPreSerialize() {
        if(this.jdoStateManager != null) {
            this.jdoStateManager.preSerialize(this);
        }

    }

    public final PersistenceManager jdoGetPersistenceManager() {
        return this.jdoStateManager != null?this.jdoStateManager.getPersistenceManager(this):null;
    }

    public final Object jdoGetTransactionalObjectId() {
        return this.jdoStateManager != null?this.jdoStateManager.getTransactionalObjectId(this):null;
    }

    public final boolean jdoIsDeleted() {
        return this.jdoStateManager != null?this.jdoStateManager.isDeleted(this):false;
    }

    public final boolean jdoIsDirty() {
        return this.jdoStateManager != null?this.jdoStateManager.isDirty(this):false;
    }

    public final boolean jdoIsNew() {
        return this.jdoStateManager != null?this.jdoStateManager.isNew(this):false;
    }

    public final boolean jdoIsPersistent() {
        return this.jdoStateManager != null?this.jdoStateManager.isPersistent(this):false;
    }

    public final boolean jdoIsTransactional() {
        return this.jdoStateManager != null?this.jdoStateManager.isTransactional(this):false;
    }

    public void jdoMakeDirty(String fieldName) {
        if(this.jdoStateManager != null) {
            this.jdoStateManager.makeDirty(this, fieldName);
        }

    }

    public final Object jdoNewObjectIdInstance() {
        return new JdoDataStoreFactory.PrivateUtils.ComposedIdKey();
    }

    public final Object jdoNewObjectIdInstance(Object key) {
        return new JdoDataStoreFactory.PrivateUtils.ComposedIdKey((String)key);
    }

    public final void jdoProvideFields(int[] indices) {
        if(indices == null) {
            throw new IllegalArgumentException("argment is null");
        } else {
            int i = indices.length - 1;
            if(i >= 0) {
                do {
                    this.jdoProvideField(indices[i]);
                    --i;
                } while(i >= 0);
            }

        }
    }

    public final void jdoReplaceFields(int[] indices) {
        if(indices == null) {
            throw new IllegalArgumentException("argument is null");
        } else {
            int i = indices.length;
            if(i > 0) {
                int j = 0;

                do {
                    this.jdoReplaceField(indices[j]);
                    ++j;
                } while(j < i);
            }

        }
    }

    public final void jdoReplaceFlags() {
        if(this.jdoStateManager != null) {
            this.jdoFlags = this.jdoStateManager.replacingFlags(this);
        }

    }

    public final synchronized void jdoReplaceStateManager(StateManager sm) {
        if(this.jdoStateManager != null) {
            this.jdoStateManager = this.jdoStateManager.replacingStateManager(this, sm);
        } else {
            JDOImplHelper.checkAuthorizedStateManager(sm);
            this.jdoStateManager = sm;
            this.jdoFlags = 1;
        }

    }

    public boolean jdoIsDetached() {
        return false;
    }

    public javax.jdo.spi.PersistenceCapable jdoNewInstance(StateManager sm) {
        JdoValue result = new JdoValue();
        result.jdoFlags = 1;
        result.jdoStateManager = sm;
        return result;
    }

    public javax.jdo.spi.PersistenceCapable jdoNewInstance(StateManager sm, Object obj) {
        JdoValue result = new JdoValue();
        result.jdoFlags = 1;
        result.jdoStateManager = sm;
        result.jdoCopyKeyFieldsFromObjectId(obj);
        return result;
    }

    public void jdoReplaceField(int index) {
        if(this.jdoStateManager == null) {
            throw new IllegalStateException("state manager is null");
        } else {
            switch(index) {
                case 0:
                    this.bytes = (byte[])this.jdoStateManager.replacingObjectField(this, index);
                    break;
                case 1:
                    this.id = this.jdoStateManager.replacingStringField(this, index);
                    break;
                case 2:
                    this.key = this.jdoStateManager.replacingStringField(this, index);
                    break;
                default:
                    throw new IllegalArgumentException("out of field index :" + index);
            }

        }
    }

    public void jdoProvideField(int index) {
        if(this.jdoStateManager == null) {
            throw new IllegalStateException("state manager is null");
        } else {
            switch(index) {
                case 0:
                    this.jdoStateManager.providedObjectField(this, index, this.bytes);
                    break;
                case 1:
                    this.jdoStateManager.providedStringField(this, index, this.id);
                    break;
                case 2:
                    this.jdoStateManager.providedStringField(this, index, this.key);
                    break;
                default:
                    throw new IllegalArgumentException("out of field index :" + index);
            }

        }
    }

    protected final void jdoCopyField(JdoValue obj, int index) {
        switch(index) {
            case 0:
                this.bytes = obj.bytes;
                break;
            case 1:
                this.id = obj.id;
                break;
            case 2:
                this.key = obj.key;
                break;
            default:
                throw new IllegalArgumentException("out of field index :" + index);
        }

    }

    public void jdoCopyFields(Object obj, int[] indices) {
        if(this.jdoStateManager == null) {
            throw new IllegalStateException("state manager is null");
        } else if(indices == null) {
            throw new IllegalStateException("fieldNumbers is null");
        } else if(!(obj instanceof JdoValue)) {
            throw new IllegalArgumentException("object is not an object of type com.google.api.client.extensions.jdo.JdoDataStoreFactory$JdoValue");
        } else {
            JdoValue other = (JdoValue)obj;
            if(this.jdoStateManager != other.jdoStateManager) {
                throw new IllegalArgumentException("state managers do not match");
            } else {
                int i = indices.length - 1;
                if(i >= 0) {
                    do {
                        this.jdoCopyField(other, indices[i]);
                        --i;
                    } while(i >= 0);
                }

            }
        }
    }

    private static final String[] __jdoFieldNamesInit() {
        return new String[]{"bytes", "id", "key"};
    }

    private static final Class[] __jdoFieldTypesInit() {
        return new Class[]{___jdo$loadClass("[B"), ___jdo$loadClass("java.lang.String"), ___jdo$loadClass("java.lang.String")};
    }

    private static final byte[] __jdoFieldFlagsInit() {
        return new byte[]{26, 24, 24};
    }

    protected static int __jdoGetInheritedFieldCount() {
        return 0;
    }

    protected static int jdoGetManagedFieldCount() {
        return 3;
    }

    private static Class __jdoPersistenceCapableSuperclassInit() {
        return null;
    }

    public static Class ___jdo$loadClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException var2) {
            throw new NoClassDefFoundError(var2.getMessage());
        }
    }

    private Object jdoSuperClone() throws CloneNotSupportedException {
        JdoValue o = (JdoValue)super.clone();
        o.jdoFlags = 0;
        o.jdoStateManager = null;
        return o;
    }

    private static byte[] jdoGetbytes(JdoValue objPC) {
        return objPC.jdoStateManager != null && !objPC.jdoStateManager.isLoaded(objPC, 0)?(byte[])objPC.jdoStateManager.getObjectField(objPC, 0, objPC.bytes):objPC.bytes;
    }

    private static void jdoSetbytes(JdoValue objPC, byte[] val) {
        if(objPC.jdoStateManager == null) {
            objPC.bytes = val;
        } else {
            objPC.jdoStateManager.setObjectField(objPC, 0, objPC.bytes, val);
        }

    }

    private static String jdoGetid(JdoValue objPC) {
        return objPC.id;
    }

    private static void jdoSetid(JdoValue objPC, String val) {
        if(objPC.jdoStateManager == null) {
            objPC.id = val;
        } else {
            objPC.jdoStateManager.setStringField(objPC, 1, objPC.id, val);
        }

    }

    private static String jdoGetkey(JdoValue objPC) {
        return objPC.key;
    }

    private static void jdoSetkey(JdoValue objPC, String val) {
        if(objPC.jdoStateManager == null) {
            objPC.key = val;
        } else {
            objPC.jdoStateManager.setStringField(objPC, 2, objPC.key, val);
        }

    }
}
