/**
 * 
 */
package org.sugyan.counter;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sugyan.counter.model.Counter;

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
        // TODO Auto-generated method stub
        // 必ずログイン済みであること
        UserService userService = UserServiceFactory.getUserService();
        if (!userService.isUserLoggedIn()) {
            LOGGER.severe("not signed in user");
            resp.sendRedirect("/main");
            return;
        }
        // request parameterからカウンター名を受け取る
        String name = req.getParameter("name");
        if (name == null || name.equals("")) {
            name = "No name";
        }
        
        // entityの生成
        Counter counter = new Counter();
        counter.setName(name);
        counter.setUser(userService.getCurrentUser());
        counter.setDate(new Date());
        counter.setCount(0);
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            pm.makePersistent(counter);
        } catch (Exception e) {
            LOGGER.severe(e.toString());
        } finally{
            pm.close();
        }
        LOGGER.info(counter.getUser().toString());
        
        resp.sendRedirect("/main");
    }
    
}
