/**
 * 
 */
package org.sugyan.counter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesServiceFactory;


/**
 * @author sugyan
 *
 */
@SuppressWarnings("serial")
public class CounterServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(CounterServlet.class.getName());

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        try {
            ServletContext servletContext = this.getServletContext();
            File file = new File(servletContext.getRealPath("images/0.png"));
            LOGGER.info(Long.toString(file.length()));
            FileInputStream fileInputStream = new FileInputStream(file);
            byte []buffer = new byte[(int)file.length()];
            fileInputStream.read(buffer);
            resp.setContentType("image/png");
            resp.getOutputStream().write(buffer);

            fileInputStream.close();
        } catch (FileNotFoundException e) {
            LOGGER.severe(e.toString());
        }
    }

}
