/**
 * 
 */
package org.sugyan.counter.model;

import java.util.Date;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.users.User;

/**
 * @author sugyan
 *
 */
public class Counter {

    public static final String KIND = Counter.class.getSimpleName();
    public static final String USER   = "user";
    public static final String ACTIVE = "active";
    
    private static final String NAME  = "name";
    private static final String DATE  = "date";
    private static final String COUNT = "count";
    
    private Entity entity;

    /**
     * @param entity
     */
    public Counter(Entity entity) {
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
    public Date getDate() {
        return (Date)entity.getProperty(DATE);
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
    public String getName() {
        return (String)entity.getProperty(NAME);
    }

    /**
     * @return
     */
    public User getUser() {
        return (User)entity.getProperty(USER);
    }
    
    /**
     * @return
     */
    public boolean isActive() {
        return (Boolean)entity.getProperty(ACTIVE);
    }

    /**
     * @param isActive
     */
    public void setActive(boolean isActive) {
        entity.setProperty(ACTIVE, isActive);
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
    public void setDate(Date date) {
        entity.setProperty(DATE, date);
    }
    
    /**
     * @param name
     */
    public void setName(String name) {
        entity.setProperty(NAME, name);
    }
    
    /**
     * @param user
     */
    public void setUser(User user) {
        entity.setProperty(USER, user);
    }
}
