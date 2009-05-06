/**
 * 
 */
package org.sugyan.counter.template;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import org.sugyan.counter.model.Counter;

/**
 * @author sugyan
 *
 */
public class MainPageTemplate extends BaseTemplate {

   private List<Counter> counters = null;
    
    /* (non-Javadoc)
     * @see org.sugyan.counter.template.BaseTemplate#content()
     */
    @Override
    protected String content() {
        // TODO Auto-generated method stub
        if (isUserLoggedIn()) {
            return "カウンターは３つまで作れます\n" +
                   "<table>\n" +
                   "  <tr>\n" +
                   "    <th>カウンター名</th>\n" +
                   "    <th>作成日</th>\n" +
                   "    <th>アクセス数</th>\n" +
                   "    <th colspan=\"2\"></th>\n" +
                   "  </tr>\n" +
                   counterList() +
                   "</table>\n" + ( counters.size() < 3 ?
                   "<hr />\n" +
                   "<h3>新規作成</h3>\n" +
                   "<form method=\"POST\" action=\"/create\">\n" +
                   "  カウンター名：<input name=\"name\" />\n" +
                   "  <input type=\"submit\" value=\"作成する\" />\n" +
                   "</form>\n" : "");   
        } else {
            return "<h1>GAE Counter (Java version)</h1>\n" +
                   "<img src=\"http://2.latest.gae-counter.appspot.com/counter/agtnYWUtY291bnRlcnIOCxIHQ291bnRlchj0Lgw.png\"><br />\n" +
                   "<a href=\"http://gae-counter.appspot.com\">Python版はこちら</a>" +
                   "<h2>説明</h2>\n" +
                   "<ul>\n" +
                   "  <li>Google App Engine(Java版)で動くアクセスカウンターです。</li>\n" +
                   "  <li>アクセスされるたびに数字が増加する画像データを提供します。</li>\n" +
                   "  <li>アクセス記録を取って簡単なアクセス解析もできます。</li>\n" +
                   "  <li>Googleアカウントで<a href=\"" + getLinkUrl() + "\">ログイン</a>すると使えます。</li>" +
                   "</ul>\n" + 
                   "<h3>ソース</h3>\n" +
                   "GitHubで公開しています。<br />\n" +
                   "<a href=\"http://github.com/sugyan/GAE_Counter/tree/master\">\n" +
                   "  http://github.com/sugyan/GAE_Counter/tree/master\n" +
                   "</a>\n" +
                   "<h4>作者</h4>\n" +
                   "すぎゃーん<br />\n" +
                   "<a href=\"http://d.hatena.ne.jp/sugyan/\">http://d.hatena.ne.jp/sugyan/</a><br />\n" +
                   "<a href=\"http://twitter.com/sugyan\">http://twitter.com/sugyan</a><br />\n";
        }
    }

    /* (non-Javadoc)
     * @see org.sugyan.counter.template.BaseTemplate#header()
     */
    @Override
    protected String header() {
        // TODO Auto-generated method stub
        return "<link href=\"/css/style.css\" rel=\"stylesheet\" type=\"text/css\" />\n";
    }

    private String counterList() {
        if (counters == null) {
            return "";
        }
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("JST"));
        StringBuilder stringBuilder = new StringBuilder();
        for (Counter counter : counters) {
            stringBuilder
                .append("  <tr>\n")
                .append("    <td>" + counter.getName() + "</td>\n")
                .append("    <td>" + dateFormat.format(counter.getDate()) + "</td>\n")
                .append("    <td align=\"right\">" + counter.getCount() + "</td>\n")
                .append("    <td><a href=\"/config?key=" + counter.getEncodedKey() + "\">設定</a></td>\n")
                .append("    <td><a href=\"/record?key=" + counter.getEncodedKey() + "\">記録</a></td>\n")
                .append("  </tr>\n"); 
        }
        return stringBuilder.toString();
    }
    
    /**
     * @param counters the counters to set
     */
    public void setCounters(List<Counter> counters) {
        this.counters = (List<Counter>)counters;
    }

    /**
     * @return the counters
     */
    public List<Counter> getCounters() {
        return counters;
    }

}
