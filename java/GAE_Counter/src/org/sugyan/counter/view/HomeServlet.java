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

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sugyan.counter.model.Counter;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * @author sugyan
 *
 */
@SuppressWarnings("serial")
public class HomeServlet extends HttpServlet {
    
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy/MM/dd");

    static {
        FORMAT.setTimeZone(TimeZone.getTimeZone("JST"));
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // JSPに渡すデータ
        List<Map<String, String>> counters = new ArrayList<Map<String, String>>();
        
        // userの取得
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        
        // queryの作成
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query(Counter.KIND)
            .addFilter(Counter.USER, FilterOperator.EQUAL, user)
            .addFilter(Counter.ACTIVE, FilterOperator.EQUAL, Boolean.TRUE);
        PreparedQuery preparedQuery = datastoreService.prepare(query);
        
        // データの抽出
        for (Entity entity : preparedQuery.asIterable()) {
            Map<String, String> counterData = new HashMap<String, String>();
            Counter counter = new Counter(entity);
            counterData.put("key", KeyFactory.keyToString(entity.getKey()));
            counterData.put("name", counter.getName());
            counterData.put("date", FORMAT.format(counter.getDate()));
            counterData.put("count", Long.valueOf(counter.getCount()).toString());
            counters.add(counterData);
        }
        
        // requestにデータを載せる
        Map<String, String> userBean = new HashMap<String, String>();
        userBean.put("name", user.getNickname());
        userBean.put("url", userService.createLogoutURL("/"));
        req.setAttribute("user", userBean);
        req.setAttribute("counters", counters);
        
        ServletContext context = getServletContext();
        RequestDispatcher dispatcher = context.getRequestDispatcher("/view/home.jsp");
        dispatcher.forward(req, resp);
    }

}
