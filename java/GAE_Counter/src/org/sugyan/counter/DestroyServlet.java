/**
 * 
 */
package org.sugyan.counter;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sugyan.counter.model.Counter;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.EntityNotFoundException;
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
        
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        // requestのkeyからカウンターを引き当てる
        try {
            Key key = KeyFactory.stringToKey(req.getParameter("key"));
            Counter counter = new Counter(datastoreService.get(key));
            if (counter != null) {
                // 現在のユーザーと関連づけられているか否か
                if (counter.getUser().equals(userService.getCurrentUser())) {
                    /*
                     * TODO recordの削除は別の場所で行う
                    for (JavaAccessRecord record : counter.getRecords()) {
                        pm.deletePersistent(record);
                    }
                    */
                    datastoreService.delete(counter.getEntity().getKey());
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
            LOGGER.log(Level.WARNING, "", e);
            resp.sendError(400);
            return;
        } catch (IllegalArgumentException e) {
            // keyの文字列が不正な場合
            LOGGER.log(Level.WARNING, "", e);
            resp.sendError(400);
            return;
        } catch (EntityNotFoundException e) {
            // entityが見つからなかった場合
            LOGGER.log(Level.WARNING, "", e);
            resp.sendError(400);
            return;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "", e);
            resp.sendError(500);
            return;
        }
        
        resp.sendRedirect("/manage.jsp");
    }

}
