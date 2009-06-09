/**
 * 
 */
package org.sugyan.counter.model;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Entity;

/**
 * @author sugyan
 *
 */
public class NumberImage {
    
    public static final String KIND = NumberImage.class.getSimpleName();
    
    private static final String NAME  = "name";

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
     * @param num
     * @return
     */
    public Blob getImage(String num) {
        return (Blob)entity.getProperty(num);
    }
    
    /**
     * @return
     */
    public String getName() {
        return (String)entity.getProperty(NAME);
    }

    /**
     * @param num
     * @param blob
     */
    public void setImage(String num, Blob blob) {
        entity.setProperty(num, blob);
    }
    
    /**
     * @param name
     */
    public void setName(String name) {
        entity.setProperty(NAME, name);
    }
}
