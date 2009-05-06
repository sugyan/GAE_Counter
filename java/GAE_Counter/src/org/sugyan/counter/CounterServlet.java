/**
 * 
 */
package org.sugyan.counter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Logger;

import javax.jdo.JDOFatalUserException;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sugyan.counter.model.JavaAccessRecord;
import org.sugyan.counter.model.Counter;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.images.Composite;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ImagesService.OutputEncoding;

/**
 * @author sugyan
 * ローカル環境で実行するとJPEGの画像がおかしくなるが、本番環境では問題なく表示される模様。
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
        // パスの解析
        String uri = req.getRequestURI();
        String[] strings = uri.split("/", 3)[2].split("\\.", 2);
        if (strings.length < 2) {
            LOGGER.severe("invalid path");
            resp.sendError(404);
            return;
        }
        OutputEncoding encoding;
        if (strings[1].equals("png")) {
            encoding = ImagesService.OutputEncoding.PNG;
            resp.setContentType("image/png");
        } else if (strings[1].equals("jpg")) {
            encoding = ImagesService.OutputEncoding.JPEG;
            resp.setContentType("image/jpeg");
        } else {
            LOGGER.severe("invalid path");
            resp.sendError(404);
            return;
        }
        long count;
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            Key key = KeyFactory.stringToKey(strings[0]);
            // カウントのインクリメントと、アクセスの記録を同時に行う
            // transaction
            Transaction transaction = pm.currentTransaction();
            try {
                // transaction開始
                transaction.begin();
                // カウンターの取得
                Counter counter = pm.getObjectById(Counter.class, key);
                count = counter.getCount() + 1;
                // アクセス記録
                JavaAccessRecord record = new JavaAccessRecord();
                record.setCount(count);
                record.setDateTime(new Date());
                String referer = req.getHeader("Referer");
                if (referer != null) {
                    record.setReferer(new Link(referer));
                } else {
                    record.setReferer(new Link(""));
                }
                record.setUserAgent(req.getHeader("User-Agent"));
                record.setRemoteAddr(req.getRemoteAddr());
                record.setCounter(counter);
                JavaAccessRecord result = pm.makePersistent(record);
                // カウンターの変更
                counter.incrementCount();
                counter.getRecords().add(result);
                pm.makePersistent(counter);
                // transaction終了
                transaction.commit();
            } finally {
                if (transaction.isActive()) {
                    LOGGER.warning("transaction failed");
                    transaction.rollback();
                }
            }
            
        } catch (IllegalArgumentException e) {
            LOGGER.severe(e.toString());
            resp.sendError(404);
            return;
        } catch (JDOFatalUserException e) {
            LOGGER.severe(e.toString());
            resp.sendError(404);
            return;
        } catch (JDOObjectNotFoundException e) {
            LOGGER.severe(e.toString());
            resp.sendError(404);
            return;
        } finally {
            pm.close();
        }
        
        // カウントを桁で区切る
        LOGGER.info(Long.toString(count));
        LinkedList<Integer> digits = new LinkedList<Integer>();
        while (count / 10 != 0) {
            digits.addFirst(Integer.valueOf((int) (count % 10)));
            count /= 10;
        }
        digits.addFirst(Integer.valueOf((int) count));
        
        // それぞれの桁に対応する画像データを取得し、imageListにCompositeを追加
        Image[] images = new Image[10];     // データキャッシュ用
        LinkedList<Composite> imageList = new LinkedList<Composite>();
        ServletContext servletContext = this.getServletContext();
        int xOffset = 0;
        for (Integer digit : digits) {
            if (images[digit] == null) {
                File file = new File(servletContext.getRealPath("images/" + digit + ".png"));
                byte[] imageData = new byte[(int)file.length()];
                FileInputStream fileInputStream = new FileInputStream(file);
                fileInputStream.read(imageData);
                Image image = ImagesServiceFactory.makeImage(imageData);
                images[digit] = image;
            }
            Composite composite = ImagesServiceFactory.makeComposite(
                    images[digit], xOffset, 0, 1.0F, Composite.Anchor.TOP_LEFT);
            imageList.add(composite);
            xOffset += images[digit].getWidth();
        }

        // imageListのCompositeデータを用いて画像を合成、出力
        int width = xOffset;
        int height = 128;
        long color = 0x00000000L;
        ImagesService imagesService = ImagesServiceFactory.getImagesService();
        Image image = imagesService.composite(imageList, width, height, color, encoding);
        LOGGER.info(image.getFormat().toString());
        resp.getOutputStream().write(image.getImageData());
        return;
    }

}
