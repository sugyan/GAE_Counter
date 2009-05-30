/**
 * 
 */
package org.sugyan.counter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sugyan.counter.model.JavaAccessRecord;
import org.sugyan.counter.model.Counter;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.datastore.Transaction;
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
            LOGGER.warning("invalid path");
            resp.sendError(404);
            return;
        }
        long count;
        
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        try {
            // カウントのインクリメントと、アクセスの記録を同時に行う
            Key key = KeyFactory.stringToKey(strings[0]);
            
            // transaction開始
            Transaction transaction = datastoreService.beginTransaction();
            
            // カウンターの取得
            Counter counter = new Counter(datastoreService.get(transaction, key));
            count = counter.getCount() + 1;
            // アクセス記録
            JavaAccessRecord record = 
                new JavaAccessRecord(new Entity(JavaAccessRecord.KIND, key));
            record.setCount(count);
            record.setDateTime(new Date());
            record.setReferer(new Link(req.getHeader("Referer")));
            record.setUserAgent(req.getHeader("User-Agent"));
            record.setRemoteAddr(req.getRemoteAddr());
            datastoreService.put(transaction, record.getEntity());
            // カウンターの変更
            counter.setCount(count);
            datastoreService.put(transaction, counter.getEntity());
            
            // transaction終了
            transaction.commit();
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "", e);
            resp.sendError(404);
            return;
        } catch (EntityNotFoundException e) {
            LOGGER.log(Level.WARNING, "", e);
            resp.sendError(404);
            return;
        } finally {
            Transaction transaction = datastoreService.getCurrentTransaction(null);
            if (transaction != null) {
                transaction.rollback();
            }
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
