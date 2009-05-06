/**
 * 
 */
package org.sugyan.counter.model;

import java.util.Date;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.users.User;

/**
 * @author sugyan
 *
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Counter {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
    private String encodedKey; 
    
    @Persistent
    private String name;

    @Persistent(defaultFetchGroup = "true")
    private User user;
    
    @Persistent
    private Date date;
    
    @Persistent
    private long count;

    /**
     * 
     */
    public void incrementCount() {
        this.count++;
    }
    
    /**
     * @return the count
     */
    public long getCount() {
        return count;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @return the encodedKey
     */
    public String getEncodedKey() {
        return encodedKey;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param count the count to set
     */
    public void setCount(long count) {
        this.count = count;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }
}
