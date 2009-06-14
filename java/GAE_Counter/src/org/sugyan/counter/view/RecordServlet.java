/**
 * 
 */
package org.sugyan.counter.view;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sugyan.counter.model.Counter;
import org.sugyan.counter.model.AccessRecord;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.FetchOptions.Builder;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * @author sugyan
 *
 */
@SuppressWarnings("serial")
public class RecordServlet extends HttpServlet {

    private static final int LIMIT_LENGTH = 40;
    private static final Logger LOGGER = Logger.getLogger(RecordServlet.class.getName());
    private static final Pattern PATTERN = Pattern.compile("^/record/([\\p{Alnum}-_]+)$");
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    static {
        FORMAT.setTimeZone(TimeZone.getTimeZone("JST"));
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();

        Matcher matcher = PATTERN.matcher(req.getRequestURI());
        if (!matcher.find()) {
            LOGGER.warning("invalid path");
            resp.sendError(404);
            return;
        }
        String keyString = matcher.group(1);

        // JSPに渡すデータ
        Map<String, String> counterData = new HashMap<String, String>();
        List<Map<String, String>> records = new ArrayList<Map<String,String>>();
        
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        try {
            Key key = KeyFactory.stringToKey(keyString);
            Counter counter = new Counter(datastoreService.get(key));
            if (!counter.isActive()) {
                LOGGER.warning("no longer exists");
                resp.sendError(404);
                return;
            }
            if (!counter.getUser().equals(user)) {
                LOGGER.warning("invalid user");
                resp.sendError(403);
                return;
            }
            counterData.put("name", counter.getName());
            
            Query query = new Query(AccessRecord.KIND, key)
                .addSort(AccessRecord.DATETIME, SortDirection.DESCENDING);
            PreparedQuery recordQuery = datastoreService.prepare(query);
            for (Entity entity : recordQuery.asIterable(Builder.withLimit(1000))) {
                Map<String, String> recordData = new HashMap<String, String>();
                AccessRecord record = new AccessRecord(entity);
                recordData.put("count", Long.valueOf(record.getCount()).toString());
                recordData.put("date", FORMAT.format(record.getDateTime()));
                recordData.put("remote", record.getRemoteAddr());
                String userAgent = record.getUserAgent();
                if (userAgent.length() > LIMIT_LENGTH) {
                    userAgent = userAgent.substring(0, LIMIT_LENGTH) + "...";
                }
                recordData.put("agent", userAgent);
                String referer = record.getReferer().getValue();
                recordData.put("referer_full", referer);
                if (referer.length() > LIMIT_LENGTH) {
                    referer = referer.substring(0, LIMIT_LENGTH);
                }
                recordData.put("referer", referer);
                records.add(recordData);
            }
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "", e);
            resp.sendError(404);
            return;
        } catch (EntityNotFoundException e) {
            LOGGER.log(Level.WARNING, "", e);
            resp.sendError(404);
            return;
        }
        
        // requestにデータを載せる
        Map<String, String> userBean = new HashMap<String, String>();
        userBean.put("name", user.getNickname());
        userBean.put("url", userService.createLogoutURL("/"));
        req.setAttribute("user", userBean);
        req.setAttribute("counter", counterData);
        req.setAttribute("records", records);
        
        ServletContext context = getServletContext();
        RequestDispatcher dispatcher = context.getRequestDispatcher("/view/record.jsp");
        dispatcher.forward(req, resp);
    }

}
