/**
 * 
 */
package org.sugyan.counter.model;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

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


    public Counter(String name) {
        this.setName(name);
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the encodedKey
     */
    public String getEncodedKey() {
        return encodedKey;
    }
}
