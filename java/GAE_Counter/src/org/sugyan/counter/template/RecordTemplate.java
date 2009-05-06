/**
 * 
 */
package org.sugyan.counter.template;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

import org.sugyan.counter.model.JavaAccessRecord;
import org.sugyan.counter.model.AccessRecordComparator;
import org.sugyan.counter.model.Counter;

/**
 * @author sugyan
 *
 */
public class RecordTemplate extends BaseTemplate {
    private Counter counter = null;

    /* (non-Javadoc)
     * @see org.sugyan.counter.template.BaseTemplate#content()
     */
    @Override
    protected String content() {
        // TODO Auto-generated method stub
        return "<h2>" + counter.getName() + "</h2>\n" +
               "<table>\n" +
               "  <tr>\n" +
               "    <th>Count</th>\n" +
               "    <th>Date Time</th>\n" +
               "    <th>IP Address</th>\n" +
               "    <th>User Agent</th>\n" +
               "    <th>Referer</th>\n" +
               "  </tr>\n" +
               recordList() + 
               "</table>";
    }

    private String recordList() {
        // TODO Auto-generated method stub
        if (counter == null) {
            return "";
        }
        
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TimeZone zone = TimeZone.getTimeZone("JST");
        format.setTimeZone(zone);
        List<JavaAccessRecord> records = counter.getRecords();
        // 日付の新しい順にsortする
        Collections.sort(records,
                Collections.reverseOrder(new AccessRecordComparator.DateComparator()));
        StringBuilder stringBuilder = new StringBuilder();
        for (JavaAccessRecord record : records) {
            int limitLength = 40;
            String userAgent = record.getUserAgent();
            if (userAgent.length() > limitLength) {
                userAgent = userAgent.substring(0, limitLength) + "...";
            }
            String referer = record.getReferer().toString();
            if (referer.length() > limitLength) {
                referer = referer.substring(0, limitLength) + "...";
            }
            stringBuilder
                .append("  <tr>\n")
                .append("    <td align=\"right\">" + record.getCount() + "</td>\n")
                .append("    <td align=\"left\">" + format.format(record.getDateTime()) + "</td>\n")
                .append("    <td align=\"right\">" + record.getRemoteAddr() + "</td>\n")
                .append("    <td align=\"left\">" + userAgent + "</td>\n")
                .append("    <td align=\"left\"><a href=\"" + record.getReferer() + "\">")
                        .append(referer + "</a>" + "</td>\n")
                .append("  </tr>\n");
        }
        
        return stringBuilder.toString();
    }

    /* (non-Javadoc)
     * @see org.sugyan.counter.template.BaseTemplate#header()
     */
    @Override
    protected String header() {
        // TODO Auto-generated method stub
        return "<link href=\"/css/style.css\" rel=\"stylesheet\" type=\"text/css\" />\n";
    }

    /**
     * @param counter the counter to set
     */
    public void setCounter(Counter counter) {
        this.counter = counter;
    }
    
}
