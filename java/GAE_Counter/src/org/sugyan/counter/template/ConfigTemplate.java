/**
 * 
 */
package org.sugyan.counter.template;

import org.sugyan.counter.model.Counter;

/**
 * @author sugyan
 *
 */
public class ConfigTemplate extends BaseTemplate {
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
               "    <td align=\"right\">作成日時：</td>\n" +
               "    <td>" + counter.getDate() + "</td>\n" +
               "  </tr>\n" +
               "  <tr>\n" +
               "    <td align=\"right\">アクセス数：</td>\n" +
               "    <td>" + counter.getCount() + "</td>\n" +
               "  </tr>\n" +
               "  <tr>\n" +
               "    <td align=\"right\">URL：</td>\n" +
               "    <td>\n" +
               "      <a href=\"/counter/" + counter.getEncodedKey() + ".png\">\n" +
               "        http://gae-counter.appspot.com/counter/" + counter.getEncodedKey() + ".png\n" +
               "      </a><br />\n" +
               "      <a href=\"/counter/" + counter.getEncodedKey() + ".jpg\">\n" +
               "        http://gae-counter.appspot.com/counter/" + counter.getEncodedKey() + ".jpg\n" +
               "      </a><br />\n" +
               "    </td>\n" +
               "  </tr>\n" +
               "</table>\n" +
               "<form method=\"POST\" action=\"/destroy\">\n" +
               "  <input type=\"hidden\" name=\"key\" value=\"" + counter.getEncodedKey() + "\" />\n" +
               "  <input type=\"submit\" value=\"削除する\"\n" +
               "            onclick=\"javascript:return confirm('本当に削除しますか？')?true:false\"/>\n" +
               "</form>\n";
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
