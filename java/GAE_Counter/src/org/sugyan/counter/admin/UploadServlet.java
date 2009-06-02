/**
 * 
 */
package org.sugyan.counter.admin;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.appengine.api.datastore.Blob;

/**
 * @author sugyan
 *
 */
@SuppressWarnings("serial")
public class UploadServlet extends HttpServlet {
    
    private static final Logger LOGGER = Logger.getLogger(UploadServlet.class.getName());

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        if (ServletFileUpload.isMultipartContent(req)) {
            ServletFileUpload fileUpload = new ServletFileUpload();
            try {
                String value = null;
                Blob blob = null;
                FileItemIterator itemIterator = fileUpload.getItemIterator(req);
                while (itemIterator.hasNext()) {
                    FileItemStream itemStream = itemIterator.next();
                    InputStream inputStream = itemStream.openStream();
                    if (itemStream.isFormField()) {
                        // numパラメータの取得
                        if (itemStream.getFieldName().equals("num")) {
                            InputStreamReader streamReader = new InputStreamReader(inputStream);
                            BufferedReader reader = new BufferedReader(streamReader);
                            value = reader.readLine();
                        }
                    } else {
                        // ファイル内容を取得
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        int read;
                        byte []buffer = new byte[1024];
                        while ((read = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, read);
                        }
                        blob = new Blob(outputStream.toByteArray());
                    }
                }
                LOGGER.info(value);
                LOGGER.info(blob.toString());
            } catch (FileUploadException e) {
                LOGGER.log(Level.SEVERE, "", e);
            }
        } else {
            LOGGER.severe("not multipart request");
            resp.sendError(400);
        }

//        resp.sendRedirect(req.getHeader("Referer"));
    }

}
