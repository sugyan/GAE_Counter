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
import org.sugyan.counter.model.NumberImage;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * @author sugyan
 *
 */
@SuppressWarnings("serial")
public class ConfigServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ConfigServlet.class.getName());
    private static final Pattern PATTERN = Pattern.compile("^/config/([\\p{Alnum}-_]+)$");
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
        Map<String, String> counterInfo = new HashMap<String, String>();
        List<Map<String, String>> images = new ArrayList<Map<String,String>>();
        
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
            counterInfo.put("name", counter.getName());
            counterInfo.put("date", FORMAT.format(counter.getDate()));
            counterInfo.put("size", counter.getSize().toString());
            counterInfo.put("count", Long.valueOf(counter.getCount()).toString());
            Key imageKey = counter.getImage();
            if (imageKey != null) {
                Entity entity = datastoreService.get(imageKey);
                counterInfo.put("image", new NumberImage(entity).getName());
            }
            counterInfo.put("key", keyString);
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "", e);
            resp.sendError(404);
            return;
        } catch (EntityNotFoundException e) {
            LOGGER.log(Level.WARNING, "", e);
            resp.sendError(404);
            return;
        }
        
        Query query = new Query(NumberImage.KIND);
        PreparedQuery preparedQuery = datastoreService.prepare(query);
        for (Entity entity : preparedQuery.asIterable()) {
            HashMap<String, String> imageInfo = new HashMap<String, String>();
            NumberImage numberImage = new NumberImage(entity);
            imageInfo.put("name", numberImage.getName());
            imageInfo.put("key", KeyFactory.keyToString(entity.getKey()));
            images.add(imageInfo);
        }
        
        // requestにデータを載せる
        Map<String, String> userBean = new HashMap<String, String>();
        userBean.put("name", user.getNickname());
        userBean.put("url", userService.createLogoutURL("/"));
        req.setAttribute("user", userBean);
        req.setAttribute("counter", counterInfo);
        req.setAttribute("images", images);
        
        ServletContext context = getServletContext();
        RequestDispatcher dispatcher = context.getRequestDispatcher("/view/config.jsp");
        dispatcher.forward(req, resp);
    }

}
