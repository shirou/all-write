package allwrite.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.google.appengine.api.datastore.Key;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

@Model(schemaVersion = 1)
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute(primaryKey = true)
    private Key key;

    @Attribute(version = true)
    private Long version;
    
    private String mailAddress;
    private String hashedPassword; // SHA1
    
    private List<String> tokens;
    private Date lastTokenUsedTime;
    
    private Key lastIndexKey;
    private List<Key> indexes;

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
        UserInfo other = (UserInfo) obj;
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }
        return true;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }
    
    public void setPassword(String password){
        // FIXME: hash
        setHashedPassword(password);
    }
    public String getPassword(){
        return this.hashedPassword;
    }

    public void setLastIndexKey(Key lastIndexKey) {
        this.lastIndexKey = lastIndexKey;
    }

    public Key getLastIndexKey() {
        return lastIndexKey;
    }
    
    public void setLastTokenUsedTime(Date lastTokenUsedTime) {
        this.lastTokenUsedTime = lastTokenUsedTime;
    }

    public Date getLastTokenUsedTime() {
        return lastTokenUsedTime;
    }

    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }

    public List<String> getTokens() {
        return tokens;
    }

    public void setIndexes(List<Key> indexes) {
        this.indexes = indexes;
    }

    public List<Key> getIndexes() {
        return indexes;
    }

    public void addIndex(Key mKey) {
        this.indexes.add(mKey);
    }
    
}
