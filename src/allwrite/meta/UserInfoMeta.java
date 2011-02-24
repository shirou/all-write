package allwrite.meta;

//@javax.annotation.Generated(value = { "slim3-gen", "@VERSION@" }, date = "2011-02-05 00:49:18")
/** */
public final class UserInfoMeta extends org.slim3.datastore.ModelMeta<allwrite.model.UserInfo> {

    /** */
    public final org.slim3.datastore.StringAttributeMeta<allwrite.model.UserInfo> hashedPassword = new org.slim3.datastore.StringAttributeMeta<allwrite.model.UserInfo>(this, "hashedPassword", "hashedPassword");

    /** */
    public final org.slim3.datastore.CollectionAttributeMeta<allwrite.model.UserInfo, java.util.List<com.google.appengine.api.datastore.Key>, com.google.appengine.api.datastore.Key> indexes = new org.slim3.datastore.CollectionAttributeMeta<allwrite.model.UserInfo, java.util.List<com.google.appengine.api.datastore.Key>, com.google.appengine.api.datastore.Key>(this, "indexes", "indexes", java.util.List.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<allwrite.model.UserInfo, com.google.appengine.api.datastore.Key> key = new org.slim3.datastore.CoreAttributeMeta<allwrite.model.UserInfo, com.google.appengine.api.datastore.Key>(this, "__key__", "key", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<allwrite.model.UserInfo, com.google.appengine.api.datastore.Key> lastIndexKey = new org.slim3.datastore.CoreAttributeMeta<allwrite.model.UserInfo, com.google.appengine.api.datastore.Key>(this, "lastIndexKey", "lastIndexKey", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<allwrite.model.UserInfo, java.util.Date> lastTokenUsedTime = new org.slim3.datastore.CoreAttributeMeta<allwrite.model.UserInfo, java.util.Date>(this, "lastTokenUsedTime", "lastTokenUsedTime", java.util.Date.class);

    /** */
    public final org.slim3.datastore.StringAttributeMeta<allwrite.model.UserInfo> mailAddress = new org.slim3.datastore.StringAttributeMeta<allwrite.model.UserInfo>(this, "mailAddress", "mailAddress");

    /** */
    public final org.slim3.datastore.StringCollectionAttributeMeta<allwrite.model.UserInfo, java.util.List<java.lang.String>> tokens = new org.slim3.datastore.StringCollectionAttributeMeta<allwrite.model.UserInfo, java.util.List<java.lang.String>>(this, "tokens", "tokens", java.util.List.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<allwrite.model.UserInfo, java.lang.Long> version = new org.slim3.datastore.CoreAttributeMeta<allwrite.model.UserInfo, java.lang.Long>(this, "version", "version", java.lang.Long.class);

    private static final UserInfoMeta slim3_singleton = new UserInfoMeta();

    /**
     * @return the singleton
     */
    public static UserInfoMeta get() {
       return slim3_singleton;
    }

    /** */
    public UserInfoMeta() {
        super("UserInfo", allwrite.model.UserInfo.class);
    }

    @Override
    public allwrite.model.UserInfo entityToModel(com.google.appengine.api.datastore.Entity entity) {
        allwrite.model.UserInfo model = new allwrite.model.UserInfo();
        model.setHashedPassword((java.lang.String) entity.getProperty("hashedPassword"));
        model.setIndexes(toList(com.google.appengine.api.datastore.Key.class, entity.getProperty("indexes")));
        model.setKey(entity.getKey());
        model.setLastIndexKey((com.google.appengine.api.datastore.Key) entity.getProperty("lastIndexKey"));
        model.setLastTokenUsedTime((java.util.Date) entity.getProperty("lastTokenUsedTime"));
        model.setMailAddress((java.lang.String) entity.getProperty("mailAddress"));
        model.setTokens(toList(java.lang.String.class, entity.getProperty("tokens")));
        model.setVersion((java.lang.Long) entity.getProperty("version"));
        return model;
    }

    @Override
    public com.google.appengine.api.datastore.Entity modelToEntity(java.lang.Object model) {
        allwrite.model.UserInfo m = (allwrite.model.UserInfo) model;
        com.google.appengine.api.datastore.Entity entity = null;
        if (m.getKey() != null) {
            entity = new com.google.appengine.api.datastore.Entity(m.getKey());
        } else {
            entity = new com.google.appengine.api.datastore.Entity(kind);
        }
        entity.setProperty("hashedPassword", m.getHashedPassword());
        entity.setProperty("indexes", m.getIndexes());
        entity.setProperty("lastIndexKey", m.getLastIndexKey());
        entity.setProperty("lastTokenUsedTime", m.getLastTokenUsedTime());
        entity.setProperty("mailAddress", m.getMailAddress());
        entity.setProperty("tokens", m.getTokens());
        entity.setProperty("version", m.getVersion());
        entity.setProperty("slim3.schemaVersion", 1);
        return entity;
    }

    @Override
    protected com.google.appengine.api.datastore.Key getKey(Object model) {
        allwrite.model.UserInfo m = (allwrite.model.UserInfo) model;
        return m.getKey();
    }

    @Override
    protected void setKey(Object model, com.google.appengine.api.datastore.Key key) {
        validateKey(key);
        allwrite.model.UserInfo m = (allwrite.model.UserInfo) model;
        m.setKey(key);
    }

    @Override
    protected long getVersion(Object model) {
        allwrite.model.UserInfo m = (allwrite.model.UserInfo) model;
        return m.getVersion() != null ? m.getVersion().longValue() : 0L;
    }

    @Override
    protected void incrementVersion(Object model) {
        allwrite.model.UserInfo m = (allwrite.model.UserInfo) model;
        long version = m.getVersion() != null ? m.getVersion().longValue() : 0L;
        m.setVersion(Long.valueOf(version + 1L));
    }

    @Override
    protected void prePut(Object model) {
        assignKeyIfNecessary(model);
        incrementVersion(model);
    }

    @Override
    public String getSchemaVersionName() {
        return "slim3.schemaVersion";
    }

    @Override
    public String getClassHierarchyListName() {
        return "slim3.classHierarchyList";
    }

}