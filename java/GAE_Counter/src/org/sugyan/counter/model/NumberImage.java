/**
 * 
 */
package org.sugyan.counter.model;

import com.google.appengine.api.datastore.Entity;

/**
 * @author sugyan
 *
 */
public class NumberImage {
    
    public static final String KIND = NumberImage.class.getSimpleName();
    
    private static final String NAME = "name";

    private Entity entity;

    /**
     * @param entity
     */
    public NumberImage(Entity entity) {
        this.entity = entity;
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
     * @param name
     */
    public void setName(String name) {
        entity.setProperty(NAME, name);
    }
}
