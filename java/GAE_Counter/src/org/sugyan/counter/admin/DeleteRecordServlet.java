/**
 * 
 */
package org.sugyan.counter.admin;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sugyan.counter.model.AccessRecord;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions.Builder;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;

/**
 * @author sugyan
 *
 */
@SuppressWarnings("serial")
public class DeleteRecordServlet extends HttpServlet {
    
    private static final Logger LOGGER = Logger.getLogger(DeleteRecordServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        
        // 30日以上前のものは削除する
        Date threshold = new Date(System.currentTimeMillis() - 30 * 24 * 60 * 60 * 1000);
        // threshold以前のAccessRecordを探す
        Query query = new Query(AccessRecord.KIND)
            .addFilter(AccessRecord.DATETIME, FilterOperator.LESS_THAN, threshold);
        PreparedQuery records = datastoreService.prepare(query);
        // 最大100件を削除
        for (Entity record : records.asIterable(Builder.withLimit(100))) {
            LOGGER.info("delete AccessRecord: " + record.getKey());
            datastoreService.delete(record.getKey());
        }
    }

}
