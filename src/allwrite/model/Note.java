package allwrite.model;

import java.io.Serializable;
import java.util.Date;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

@Model(schemaVersion = 1)
public class Note implements Serializable {
    private static final int MAX_TITLE_LENGTH = 10;
    private static final long serialVersionUID = 1L;

    @Attribute(primaryKey = true)
    private Key key;

    @Attribute(version = true)
    private Long version;

    private Text contentsText = new Text("");
    private String noteId;
    private Date createdDate = new Date();
    private Date lastmodifiedDate = new Date();
    private String title;
    private String mailAddress;
    private Key indexedEntityKey;
    /**
     * Returns the key.
     *
     * @return the key
     */
    public Key getKey() {
        return key;
    }

    /**
     * Sets the key.
     *
     * @param key
     *            the key
     */
    public void setKey(Key key) {
        this.key = key;
    }

    /**
     * Returns the version.
     *
     * @return the version
     */
    public Long getVersion() {
        return version;
    }

    /**
     * Sets the version.
     *
     * @param version
     *            the version
     */
    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Note other = (Note) obj;
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }
        return true;
    }
    
    public Text getContentsText(){
        return contentsText;
    }
    public void setContentsText(Text text){
        this.contentsText = text;
    }
    /**
     * contentsTextÇÃÉâÉbÉpÅ[
     * Textå^ÇÕÇªÇÃÇ‹Ç‹Ç≈ÇÕàµÇ¶Ç»Ç¢ÇΩÇﬂ
     * @param contents
     */
    public void setText(String contents){
        if (contents.length() < MAX_TITLE_LENGTH)
            this.title = contents;
        else{
            int end = contents.indexOf(System.getProperty("line.separator"));
            if (end < MAX_TITLE_LENGTH){
                if (end < 0){ // not includes separator
                    this.title = contents;
                }else{
                    this.title = contents.substring(0, end);
                }
            }else{
                this.title = contents.substring(0, MAX_TITLE_LENGTH);
            }
        }
        setContentsText(new Text(contents));
    }
    public String getText(){
        return contentsText.getValue();
    }
    public Date getCreatedDate(){
        return this.createdDate;
    }
    public void setCreatedDate(Date date){
        this.createdDate = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setLastmodifiedDate(Date lastmodifiedDate) {
        this.lastmodifiedDate = lastmodifiedDate;
    }

    public Date getLastmodifiedDate() {
        return lastmodifiedDate;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setIndexedEntityKey(Key indexedEntityKey) {
        this.indexedEntityKey = indexedEntityKey;
    }

    public Key getIndexedEntityKey() {
        return indexedEntityKey;
    }
}
