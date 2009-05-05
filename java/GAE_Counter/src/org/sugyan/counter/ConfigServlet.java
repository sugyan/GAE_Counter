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
import org.sugyan.counter.template.ConfigTemplate;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * @author sugyan
 *
 */
@SuppressWarnings("serial")
public class ConfigServlet extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(ConfigServlet.class.getName());

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        ConfigTemplate template = new ConfigTemplate();
        UserService userService = UserServiceFactory.getUserService();
        // ログインチェック
        if (!userService.isUserLoggedIn()) {
            LOG.severe("not signed in user");
            resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
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
                    template.setCounter(counter);
                    resp.setCharacterEncoding("UTF-8");
                    resp.getWriter().println(template);
                } else {
                    LOG.severe("invalid user");
                }
            } else {
                LOG.severe("counter not found");
            }
        } catch (NullPointerException e) {
            // requestにkeyが指定されていない場合
            LOG.severe(e.toString());
        } catch (IllegalArgumentException e) {
            // keyの文字列が不正な場合
            LOG.severe(e.toString());
        } catch (JDOFatalUserException e) {
            LOG.severe(e.toString());
        } catch (JDOObjectNotFoundException e) {
            // 指定したkeyのカウンターが存在しなかった場合
            LOG.severe(e.toString());
        } finally {
            pm.close();
        }

    }

}