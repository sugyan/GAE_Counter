/**
 * 
 */
package org.sugyan.counter;

import java.io.IOException;
import java.util.logging.Logger;

import javax.jdo.JDOFatalUserException;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sugyan.counter.model.Counter;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * @author sugyan
 *
 */
@SuppressWarnings("serial")
public class DestroyServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(DestroyServlet.class.getName());

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
        
        // requestのkeyからカウンターを引き当てる
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            Key key = KeyFactory.stringToKey(req.getParameter("key"));
            Counter counter = pm.getObjectById(Counter.class, key);
            if (counter != null) {
                // 現在のユーザーと関連づけられているか否か
                if (counter.getUser().equals(userService.getCurrentUser())) {
                    pm.deletePersistent(counter);
                } else {
                    LOGGER.severe("invalid user");
                }
            } else {
                LOGGER.severe("counter not found");
            }
        } catch (NullPointerException e) {
            // requestにkeyが指定されていない場合
            LOGGER.severe(e.toString());
            return;
        } catch (IllegalArgumentException e) {
            // keyの文字列が不正な場合
            LOGGER.severe(e.toString());
            return;
        } catch (JDOFatalUserException e) {
            LOGGER.severe(e.toString());
            return;
        } catch (JDOObjectNotFoundException e) {
            // 指定したkeyのカウンターが存在しなかった場合
            LOGGER.severe(e.toString());
            return;
        } finally {
            pm.close();
        }
        
        resp.sendRedirect("/main");
    }

}
