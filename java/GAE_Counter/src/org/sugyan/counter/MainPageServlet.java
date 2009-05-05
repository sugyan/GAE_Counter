package org.sugyan.counter;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sugyan.counter.template.BaseTemplate;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * @author sugyan
 *
 */
@SuppressWarnings("serial")
public class MainPageServlet extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(MainPageServlet.class.getName());

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        BaseTemplate template = new BaseTemplate();
        UserService userService = UserServiceFactory.getUserService();
        // templateに値をセット
        boolean userLoggedIn = userService.isUserLoggedIn();
        template.setUserLoggedIn(userLoggedIn);
        String linkUrl;
        if (userLoggedIn) {
            linkUrl = userService.createLogoutURL(req.getRequestURI());
        } else {
            linkUrl = userService.createLoginURL(req.getRequestURI());
        }
        LOG.info(linkUrl);
        template.setLinkUrl(linkUrl);
        template.setCurrentUser(userService.getCurrentUser());
        resp.getWriter().println(template);
    }
    
}
