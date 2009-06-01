/**
 * 
 */
package org.sugyan.counter.admin;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sugyan.counter.model.Counter;
import org.sugyan.counter.model.JavaAccessRecord;

import com.google.appengine.api.datastore.DatastoreFailureException;
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
public class DeleteCounterServlet extends HttpServlet {
    
    private static final Logger LOGGER = Logger.getLogger(DeleteCounterServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        
        // ActiveでなくなっているCounterを探す
        Query counterQuery = new Query(Counter.KIND)
            .addFilter(Counter.ACTIVE, FilterOperator.EQUAL, Boolean.FALSE);
        PreparedQuery counters = datastoreService.prepare(counterQuery);

        if (counters.countEntities() > 0) {
            // １つ目に現れたカウンターをparentとして持つAccessRecordを探す
            Entity entity = counters.asIterator().next();
            Query recordQuery = new Query(JavaAccessRecord.KIND, entity.getKey());
            PreparedQuery records = datastoreService.prepare(recordQuery);
            try {
                if (records.countEntities() > 0) {
                    // 最大で100件のAccessRecordを削除
                    Iterable<Entity> iterable = records.asIterable(Builder.withLimit(100));
                    for (Entity record : iterable) {
                        LOGGER.info("delete AccessRecord: " + record.getKey());
                        datastoreService.delete(record.getKey());
                    }
                } else {
                    // 既にAccessRecordが無ければカウンターそのものを削除
                    LOGGER.info("delete Counter:" + entity.getKey());
                    datastoreService.delete(entity.getKey());
                }
            } catch (DatastoreFailureException e) {
                LOGGER.log(Level.SEVERE, "", e);
            }
        }
    }

}
