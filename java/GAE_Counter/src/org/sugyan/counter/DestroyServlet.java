/**
 * 
 */
package org.sugyan.counter;

import java.io.IOException;
import java.util.logging.Logger;

import javax.jdo.JDOFatalUserException;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sugyan.counter.model.JavaAccessRecord;
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
                    Transaction transaction = pm.currentTransaction();
                    try {
                        transaction.begin();
                        for (JavaAccessRecord record : counter.getRecords()) {
                            pm.deletePersistent(record);
                        }
                        pm.deletePersistent(counter);
                        transaction.commit();
                    } finally {
                        if (transaction.isActive()) {
                            LOGGER.severe("transaction failed");
                            transaction.rollback();
                        }
                    }
                } else {
                    LOGGER.severe("invalid user");
                    resp.sendError(403);
                    return;
                }
            } else {
                LOGGER.severe("counter not found");
                resp.sendError(400);
                return;
            }
        } catch (NullPointerException e) {
            // requestにkeyが指定されていない場合
            LOGGER.severe(e.toString());
            resp.sendError(400);
            return;
        } catch (IllegalArgumentException e) {
            // keyの文字列が不正な場合
            LOGGER.severe(e.toString());
            resp.sendError(400);
            return;
        } catch (JDOFatalUserException e) {
            LOGGER.severe(e.toString());
            resp.sendError(400);
            return;
        } catch (JDOObjectNotFoundException e) {
            // 指定したkeyのカウンターが存在しなかった場合
            LOGGER.severe(e.toString());
            resp.sendError(400);
            return;
        } finally {
            pm.close();
        }
        
        resp.sendRedirect("/manage.jsp");
    }

}
