/**
 * 
 */
package org.sugyan.counter.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sugyan.counter.model.NumberImage;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

/**
 * @author sugyan
 *
 */
@SuppressWarnings("serial")
public class CreateServlet extends HttpServlet {

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String name = req.getParameter("name");
        if (name == null) {
            name = "";
        }
        
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        NumberImage numberImage = new NumberImage(new Entity(NumberImage.KIND));
        numberImage.setName(name);
        datastoreService.put(numberImage.getEntity());
        
        resp.sendRedirect(req.getHeader("Referer"));
    }

}
