/**
 * 
 */
package org.sugyan.counter.admin;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sugyan.counter.model.NumberImage;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * @author sugyan
 *
 */
@SuppressWarnings("serial")
public class ViewServlet extends HttpServlet {
    
    private static final Logger LOGGER = Logger.getLogger(ViewServlet.class.getName());
    private static final Pattern PATTERN = Pattern.compile("^/admin/view/(\\p{Alnum}+)/(\\p{Digit})$");

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Matcher matcher = PATTERN.matcher(req.getRequestURI());
        if (!matcher.find()) {
            resp.sendError(404);
        }
        
        String keyString = matcher.group(1);
        String numString = matcher.group(2);
        
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        try {
            Entity entity = datastoreService.get(KeyFactory.stringToKey(keyString));
            NumberImage numberImage = new NumberImage(entity);
            Blob image = numberImage.getImage(numString);
            if (image != null) {
                ServletOutputStream outputStream = resp.getOutputStream();
                outputStream.write(image.getBytes());
            }
        } catch (EntityNotFoundException e) {
            LOGGER.log(Level.WARNING, "", e);
        }
    }

}
