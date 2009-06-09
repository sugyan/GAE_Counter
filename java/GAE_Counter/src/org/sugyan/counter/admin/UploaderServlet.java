/**
 * 
 */
package org.sugyan.counter.admin;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.sugyan.counter.model.NumberImage;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

/**
 * @author sugyan
 *
 */
@SuppressWarnings("serial")
public class UploaderServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(UploaderServlet.class.getName());
    
    public enum Action {
        CREATE,
        DELETE,
        UPLOAD;
    };
    
    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // JSPに渡すデータ
        List<Map<String, String>> images = new ArrayList<Map<String, String>>();
        
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query(NumberImage.KIND);
        PreparedQuery preparedQuery = datastoreService.prepare(query);
        for (Entity entity : preparedQuery.asIterable()) {
            Map<String, String> imageInfo = new HashMap<String, String>();
            NumberImage numberImage = new NumberImage(entity);
            imageInfo.put("key", KeyFactory.keyToString(entity.getKey()));
            imageInfo.put("name", numberImage.getName());
            images.add(imageInfo);
        }
        
        // requestにデータを載せる
        req.setAttribute("images", images);
        
        ServletContext context = getServletContext();
        RequestDispatcher dispatcher = context.getRequestDispatcher("/view/admin/uploader.jsp");
        dispatcher.forward(req, resp);
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Action action = Action.valueOf(req.getParameter("action"));
        LOGGER.info(action.name());
        switch (action) {
        case CREATE:
            create(req, resp);
            break;
        case DELETE:
            delete(req, resp);
            break;
        case UPLOAD:
            upload(req, resp);
            break;
        default:
            break;
        }

        resp.sendRedirect(req.getHeader("Referer"));
    }
    
    /**
     * @param req
     * @param resp
     */
    private void create(HttpServletRequest req, HttpServletResponse resp) {
        String name = req.getParameter("name");
        if (name == null) {
            name = "";
        }

        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        NumberImage numberImage = new NumberImage(new Entity(NumberImage.KIND));
        numberImage.setName(name);
        datastoreService.put(numberImage.getEntity());
    }
    
    /**
     * @param req
     * @param resp
     */
    private void delete(HttpServletRequest req, HttpServletResponse resp) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        for (String keyString : req.getParameterValues("delete")) {
            Key key = KeyFactory.stringToKey(keyString);
            datastoreService.delete(key);
        }
    }

    /**
     * @param req
     * @param resp
     * @throws IOException
     */
    private void upload(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!ServletFileUpload.isMultipartContent(req)) {
            return;
        }
        
        ServletFileUpload fileUpload = new ServletFileUpload();
        try {
            String value = null;
            String image = null;
            Blob blob = null;
            FileItemIterator itemIterator = fileUpload.getItemIterator(req);
            while (itemIterator.hasNext()) {
                FileItemStream itemStream = itemIterator.next();
                InputStream inputStream = itemStream.openStream();
                if (itemStream.isFormField()) {
                    // numパラメータの取得
                    InputStreamReader streamReader = new InputStreamReader(inputStream);
                    BufferedReader reader = new BufferedReader(streamReader);
                    if (itemStream.getFieldName().equals("num")) {
                        value = reader.readLine();
                    }
                    if (itemStream.getFieldName().equals("image")) {
                        image = reader.readLine();
                    }
                } else {
                    // ファイル内容を取得
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    int len;
                    byte []buffer = new byte[1024];
                    while ((len = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, len);
                    }
                    blob = new Blob(outputStream.toByteArray());
                }
            }
            LOGGER.info(image);
            LOGGER.info(value);
            LOGGER.info(blob.toString());
            DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
            Entity entity = datastoreService.get(KeyFactory.stringToKey(image));
            NumberImage numberImage = new NumberImage(entity);
            numberImage.setImage(value, blob);
            datastoreService.put(numberImage.getEntity());
        } catch (FileUploadException e) {
            LOGGER.log(Level.SEVERE, "", e);
        } catch (EntityNotFoundException e) {
            LOGGER.log(Level.SEVERE, "", e);
        }
    }

}
