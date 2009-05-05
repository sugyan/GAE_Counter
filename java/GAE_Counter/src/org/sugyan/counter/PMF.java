/**
 * 
 */
package org.sugyan.counter;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

/**
 * @author sugyan
 *
 */
public final class PMF {
    private static final PersistenceManagerFactory pmfInstance =
        JDOHelper.getPersistenceManagerFactory("transactions-optional");
    
    private PMF() {
    }
    
    public static PersistenceManagerFactory get() {
        return pmfInstance;
    }
}
