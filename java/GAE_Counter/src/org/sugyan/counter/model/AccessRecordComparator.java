/**
 * 
 */
package org.sugyan.counter.model;

import java.util.Comparator;

/**
 * @author sugyan
 *
 */
public class AccessRecordComparator {
    /**
     * @author sugyan
     * AccessRecordを日付順にsortするComparator
     */
    public static class DateComparator implements Comparator<JavaAccessRecord> {

        public int compare(JavaAccessRecord lhs, JavaAccessRecord rhs) {
            // TODO Auto-generated method stub
            return lhs.getDateTime().compareTo(rhs.getDateTime());
        }
        
    }
}
