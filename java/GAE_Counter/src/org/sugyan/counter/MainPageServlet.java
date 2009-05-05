package org.sugyan.counter;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sugyan.counter.model.Counter;
import org.sugyan.counter.template.MainPageTemplate;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * @author sugyan
 *
 */
@SuppressWarnings("serial")
public class MainPageServlet extends HttpServlet {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(MainPageServlet.class.getName());

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        MainPageTemplate template = new MainPageTemplate();
        UserService userService = UserServiceFactory.getUserService();
        // templateに値をセット
        boolean userLoggedIn = userService.isUserLoggedIn();
        template.setUserLoggedIn(userLoggedIn);
        String linkUrl;
        if (userLoggedIn) {
            linkUrl = userService.createLogoutURL(req.getRequestURI());
            PersistenceManager pm = PMF.get().getPersistenceManager();
            String query = "SELECT FROM " + Counter.class.getName();
            template.setCounters((List<Counter>)pm.newQuery(query).execute());
        } else {
            linkUrl = userService.createLoginURL(req.getRequestURI());
        }
        template.setLinkUrl(linkUrl);
        template.setCurrentUser(userService.getCurrentUser());
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().println(template);
    }
    
}
