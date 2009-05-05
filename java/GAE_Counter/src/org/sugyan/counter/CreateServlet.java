/**
 * 
 */
package org.sugyan.counter;

import java.io.IOException;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sugyan.counter.model.Counter;

import com.google.appengine.api.users.UserServiceFactory;

/**
 * @author sugyan
 *
 */
@SuppressWarnings("serial")
public class CreateServlet extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(CreateServlet.class.getName());

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        if (!UserServiceFactory.getUserService().isUserLoggedIn()) {
            LOG.severe("not signed in user");
            resp.sendRedirect("/");
            return;
        }
        String name = req.getParameter("name");
        if (name == null || name.equals("")) {
            name = "No name";
        }
        
        Counter counter = new Counter(name);
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            pm.makePersistent(counter);
        } finally{
            pm.close();
        }
        
    }
    
}
