/**
 * 
 */
package org.sugyan.counter.model;

import java.util.Date;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Link;

/**
 * @author sugyan
 * Python版と区別するためにkindでJava版であることを明示する
 */
public class JavaAccessRecord {
    
    public static final String KIND = JavaAccessRecord.class.getSimpleName();
    public static final String DATETIME   = "datetime";
    
    private static final String COUNT      = "count";
    private static final String REFERER    = "referer";
    private static final String USERAGENT  = "user_agent";
    private static final String REMOTEADDR = "remote_addr";
    
    private Entity entity;
    
    /**
     * @param entity
     */
    public JavaAccessRecord(Entity entity) {
        this.entity = entity;
    }

    /**
     * @return
     */
    public long getCount() {
        return (Long)entity.getProperty(COUNT);
    }
    
    /**
     * @return
     */
    public Date getDateTime() {
        return (Date)entity.getProperty(DATETIME);
    }
    
    /**
     * @return
     */
    public Entity getEntity() {
        return entity;
    }
    
    /**
     * @return
     */
    public Link getReferer() {
        return (Link)entity.getProperty(REFERER);
    }
    
    /**
     * @return
     */
    public String getRemoteAddr() {
        return (String)entity.getProperty(REMOTEADDR);
    }
    
    /**
     * @return
     */
    public String getUserAgent() {
        return (String)entity.getProperty(USERAGENT);
    }
    
    /**
     * @param count
     */
    public void setCount(long count) {
        entity.setProperty(COUNT, count);
    }

    /**
     * @param date
     */
    public void setDateTime(Date date) {
        entity.setProperty(DATETIME, date);
    }
    
    /**
     * @param referer
     */
    public void setReferer(Link referer) {
        if (referer.getValue() != null) {
            entity.setProperty(REFERER, referer);
        } else {
            entity.setProperty(REFERER, new Link(""));
        }
    }

    /**
     * @param remoteAddr
     */
    public void setRemoteAddr(String remoteAddr) {
        entity.setProperty(REMOTEADDR, remoteAddr);
    }
    
    /**
     * @param userAgent
     */
    public void setUserAgent(String userAgent) {
        entity.setProperty(USERAGENT, userAgent);
    }
}
