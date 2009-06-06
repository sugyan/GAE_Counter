/**
 * 
 */
package org.sugyan.counter.view;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * @author sugyan
 *
 */
@SuppressWarnings("serial")
public class IndexServlet extends HttpServlet {

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        
        // requestにデータを載せる
        Map<String, String> userBean = new HashMap<String, String>();
        if (user != null) {
            userBean.put("name", user.getNickname());
            userBean.put("url", userService.createLogoutURL("/"));
            userBean.put("action", "Sign out");
        } else {
            userBean.put("url", userService.createLoginURL("/"));
            userBean.put("action", "Sign in");
        }
        req.setAttribute("user", userBean);
        
        ServletContext context = getServletContext();
        RequestDispatcher dispatcher = context.getRequestDispatcher("/view/index.jsp");
        dispatcher.forward(req, resp);
    }

}
