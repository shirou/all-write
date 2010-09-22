package allwrite.meta;

//@javax.annotation.Generated(value = { "slim3-gen", "@VERSION@" }, date = "2010-09-14 01:07:39")
/** */
public final class IndexMeta extends org.slim3.datastore.ModelMeta<allwrite.model.Index> {

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<allwrite.model.Index, com.google.appengine.api.datastore.Key> key = new org.slim3.datastore.CoreAttributeMeta<allwrite.model.Index, com.google.appengine.api.datastore.Key>(this, "__key__", "key", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.StringAttributeMeta<allwrite.model.Index> mailAddress = new org.slim3.datastore.StringAttributeMeta<allwrite.model.Index>(this, "mailAddress", "mailAddress");

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<allwrite.model.Index, java.lang.Long> version = new org.slim3.datastore.CoreAttributeMeta<allwrite.model.Index, java.lang.Long>(this, "version", "version", java.lang.Long.class);

    private static final IndexMeta slim3_singleton = new IndexMeta();

    /**
     * @return the singleton
     */
    public static IndexMeta get() {
       return slim3_singleton;
    }

    /** */
    public IndexMeta() {
        super("Index", allwrite.model.Index.class);
    }

    @Override
    public allwrite.model.Index entityToModel(com.google.appengine.api.datastore.Entity entity) {
        allwrite.model.Index model = new allwrite.model.Index();
        model.setIndexBlob((com.google.appengine.api.datastore.Blob) entity.getProperty("indexBlob"));
        model.setKey(entity.getKey());
        model.setMailAddress((java.lang.String) entity.getProperty("mailAddress"));
        model.setVersion((java.lang.Long) entity.getProperty("version"));
        return model;
    }

    @Override
    public com.google.appengine.api.datastore.Entity modelToEntity(java.lang.Object model) {
        allwrite.model.Index m = (allwrite.model.Index) model;
        com.google.appengine.api.datastore.Entity entity = null;
        if (m.getKey() != null) {
            entity = new com.google.appengine.api.datastore.Entity(m.getKey());
        } else {
            entity = new com.google.appengine.api.datastore.Entity(kind);
        }
        entity.setProperty("indexBlob", m.getIndexBlob());
        entity.setProperty("mailAddress", m.getMailAddress());
        entity.setProperty("version", m.getVersion());
        entity.setProperty("slim3.schemaVersion", 1);
        return entity;
    }

    @Override
    protected com.google.appengine.api.datastore.Key getKey(Object model) {
        allwrite.model.Index m = (allwrite.model.Index) model;
        return m.getKey();
    }

    @Override
    protected void setKey(Object model, com.google.appengine.api.datastore.Key key) {
        validateKey(key);
        allwrite.model.Index m = (allwrite.model.Index) model;
        m.setKey(key);
    }

    @Override
    protected long getVersion(Object model) {
        allwrite.model.Index m = (allwrite.model.Index) model;
        return m.getVersion() != null ? m.getVersion().longValue() : 0L;
    }

    @Override
    protected void incrementVersion(Object model) {
        allwrite.model.Index m = (allwrite.model.Index) model;
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