/**
 * 
 */
package org.sugyan.counter.model;

import java.util.Date;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Link;

/**
 * @author sugyan
 * Python版と区別するためにkindでJava版であることを明示する
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class JavaAccessRecord {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    
    @Persistent
    private long count;
    
    @Persistent
    private Date datetime;
    
    @Persistent
    private Link referer;
    
    @Persistent
    private String user_agent;
    
    @Persistent
    private String remote_addr;
    
    @Persistent
    private Counter counter;

    /**
     * @param count the count to set
     */
    public void setCount(long count) {
        this.count = count;
    }

    /**
     * @return the count
     */
    public long getCount() {
        return count;
    }

    /**
     * @param datetime the datetime to set
     */
    public void setDateTime(Date datetime) {
        this.datetime = datetime;
    }

    /**
     * @return the datetime
     */
    public Date getDateTime() {
        return datetime;
    }

    /**
     * @param referer the referer to set
     */
    public void setReferer(Link referer) {
        this.referer = referer;
    }

    /**
     * @return the referer
     */
    public Link getReferer() {
        return referer;
    }

    /**
     * @param user_agent the user_agent to set
     */
    public void setUserAgent(String user_agent) {
        this.user_agent = user_agent;
    }

    /**
     * @return the user_agent
     */
    public String getUserAgent() {
        return user_agent;
    }

    /**
     * @param remote_addr the remote_addr to set
     */
    public void setRemoteAddr(String remote_addr) {
        this.remote_addr = remote_addr;
    }

    /**
     * @return the remote_addr
     */
    public String getRemoteAddr() {
        return remote_addr;
    }

    /**
     * @param key the key to set
     */
    public void setKey(Key key) {
        this.key = key;
    }

    /**
     * @return the key
     */
    public Key getKey() {
        return key;
    }

    /**
     * @param counter the counter to set
     */
    public void setCounter(Counter counter) {
        this.counter = counter;
    }

    /**
     * @return the counter
     */
    public Counter getCounter() {
        return counter;
    }

}
