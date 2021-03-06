/**
 * 
 */
package org.sugyan.counter;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sugyan.counter.model.Counter;
import org.sugyan.counter.model.NumberImage;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * @author sugyan
 *
 */
@SuppressWarnings("serial")
public class CreateServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(CreateServlet.class.getName());

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 必ずログイン済みであること
        UserService userService = UserServiceFactory.getUserService();
        if (!userService.isUserLoggedIn()) {
            LOGGER.severe("not signed in user");
            resp.sendRedirect("/");
            return;
        }
        // request parameterからカウンター名を受け取る
        String name = req.getParameter("name");
        if (name == null || name.equals("")) {
            name = "No name";
        }
        if (name.length() > 100) {
            name = name.substring(0, 100);
        }
        
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        // デフォルトのフォントを選択
        Query query = new Query(NumberImage.KIND);
        PreparedQuery prepare = datastoreService.prepare(query);
        Entity image = prepare.asIterator().next();
        Key key = image.getKey();
        
        // entityの生成
        Counter counter = new Counter(new Entity(Counter.KIND));
        counter.setActive(true);
        counter.setName(name);
        counter.setUser(userService.getCurrentUser());
        counter.setDate(new Date());
        counter.setImage(key);
        counter.setSize(100L);
        counter.setCount(0);
        try {
            datastoreService.put(counter.getEntity());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "", e);
        }
        
        resp.sendRedirect("/home");
    }
    
}
