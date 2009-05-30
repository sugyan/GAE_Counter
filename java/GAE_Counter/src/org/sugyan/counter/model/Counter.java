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
    public static final String USER  = "user";
    
    private static final String NAME  = "name";
    private static final String DATE  = "date";
    private static final String COUNT = "count";
    
    private Entity entity;

    public Counter(Entity entity) {
        this.entity = entity;
    }
    
    public Entity getEntity() {
        return entity;
    }
    
    public long getCount() {
        return (Long)entity.getProperty(COUNT);
    }
    
    public Date getDate() {
        return (Date)entity.getProperty(DATE);
    }
    
    public String getName() {
        return (String)entity.getProperty(NAME);
    }

    public User getUser() {
        return (User)entity.getProperty(USER);
    }

    public void setCount(long count) {
        entity.setProperty(COUNT, count);
    }
    
    public void setDate(Date date) {
        entity.setProperty(DATE, date);
    }
    
    public void setName(String name) {
        entity.setProperty(NAME, name);
    }
    
    public void setUser(User user) {
        entity.setProperty(USER, user);
    }
}
