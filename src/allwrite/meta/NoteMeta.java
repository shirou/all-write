package allwrite.meta;

//@javax.annotation.Generated(value = { "slim3-gen", "@VERSION@" }, date = "2010-09-20 22:59:18")
/** */
public final class NoteMeta extends org.slim3.datastore.ModelMeta<allwrite.model.Note> {

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<allwrite.model.Note, java.util.Date> createdDate = new org.slim3.datastore.CoreAttributeMeta<allwrite.model.Note, java.util.Date>(this, "createdDate", "createdDate", java.util.Date.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<allwrite.model.Note, com.google.appengine.api.datastore.Key> indexedEntityKey = new org.slim3.datastore.CoreAttributeMeta<allwrite.model.Note, com.google.appengine.api.datastore.Key>(this, "indexedEntityKey", "indexedEntityKey", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<allwrite.model.Note, com.google.appengine.api.datastore.Key> key = new org.slim3.datastore.CoreAttributeMeta<allwrite.model.Note, com.google.appengine.api.datastore.Key>(this, "__key__", "key", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<allwrite.model.Note, java.util.Date> lastmodifiedDate = new org.slim3.datastore.CoreAttributeMeta<allwrite.model.Note, java.util.Date>(this, "lastmodifiedDate", "lastmodifiedDate", java.util.Date.class);

    /** */
    public final org.slim3.datastore.StringAttributeMeta<allwrite.model.Note> mailAddress = new org.slim3.datastore.StringAttributeMeta<allwrite.model.Note>(this, "mailAddress", "mailAddress");

    /** */
    public final org.slim3.datastore.StringAttributeMeta<allwrite.model.Note> noteId = new org.slim3.datastore.StringAttributeMeta<allwrite.model.Note>(this, "noteId", "noteId");

    /** */
    public final org.slim3.datastore.StringAttributeMeta<allwrite.model.Note> title = new org.slim3.datastore.StringAttributeMeta<allwrite.model.Note>(this, "title", "title");

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<allwrite.model.Note, java.lang.Long> version = new org.slim3.datastore.CoreAttributeMeta<allwrite.model.Note, java.lang.Long>(this, "version", "version", java.lang.Long.class);

    private static final NoteMeta slim3_singleton = new NoteMeta();

    /**
     * @return the singleton
     */
    public static NoteMeta get() {
       return slim3_singleton;
    }

    /** */
    public NoteMeta() {
        super("Note", allwrite.model.Note.class);
    }

    @Override
    public allwrite.model.Note entityToModel(com.google.appengine.api.datastore.Entity entity) {
        allwrite.model.Note model = new allwrite.model.Note();
        model.setContentsText((com.google.appengine.api.datastore.Text) entity.getProperty("contentsText"));
        model.setCreatedDate((java.util.Date) entity.getProperty("createdDate"));
        model.setIndexedEntityKey((com.google.appengine.api.datastore.Key) entity.getProperty("indexedEntityKey"));
        model.setKey(entity.getKey());
        model.setLastmodifiedDate((java.util.Date) entity.getProperty("lastmodifiedDate"));
        model.setMailAddress((java.lang.String) entity.getProperty("mailAddress"));
        model.setNoteId((java.lang.String) entity.getProperty("noteId"));
        model.setTitle((java.lang.String) entity.getProperty("title"));
        model.setVersion((java.lang.Long) entity.getProperty("version"));
        return model;
    }

    @Override
    public com.google.appengine.api.datastore.Entity modelToEntity(java.lang.Object model) {
        allwrite.model.Note m = (allwrite.model.Note) model;
        com.google.appengine.api.datastore.Entity entity = null;
        if (m.getKey() != null) {
            entity = new com.google.appengine.api.datastore.Entity(m.getKey());
        } else {
            entity = new com.google.appengine.api.datastore.Entity(kind);
        }
        entity.setUnindexedProperty("contentsText", m.getContentsText());
        entity.setProperty("createdDate", m.getCreatedDate());
        entity.setProperty("indexedEntityKey", m.getIndexedEntityKey());
        entity.setProperty("lastmodifiedDate", m.getLastmodifiedDate());
        entity.setProperty("mailAddress", m.getMailAddress());
        entity.setProperty("noteId", m.getNoteId());
        entity.setProperty("title", m.getTitle());
        entity.setProperty("version", m.getVersion());
        entity.setProperty("slim3.schemaVersion", 1);
        return entity;
    }

    @Override
    protected com.google.appengine.api.datastore.Key getKey(Object model) {
        allwrite.model.Note m = (allwrite.model.Note) model;
        return m.getKey();
    }

    @Override
    protected void setKey(Object model, com.google.appengine.api.datastore.Key key) {
        validateKey(key);
        allwrite.model.Note m = (allwrite.model.Note) model;
        m.setKey(key);
    }

    @Override
    protected long getVersion(Object model) {
        allwrite.model.Note m = (allwrite.model.Note) model;
        return m.getVersion() != null ? m.getVersion().longValue() : 0L;
    }

    @Override
    protected void incrementVersion(Object model) {
        allwrite.model.Note m = (allwrite.model.Note) model;
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