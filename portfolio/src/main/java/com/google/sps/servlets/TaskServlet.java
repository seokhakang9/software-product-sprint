package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.OrderBy;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import com.google.gson.Gson;
import com.google.sps.servlets.data.Task;
import java.util.ArrayList;
import java.util.List;
import com.google.cloud.datastore.Key;

@WebServlet("/tasks")
public class TaskServlet extends HttpServlet{
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
      // Sanitize user input to remove HTML tags and JavaScript.
      String title = Jsoup.clean(request.getParameter("title"), Whitelist.none());
      long timestamp = System.currentTimeMillis();
  
      Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
      KeyFactory keyFactory = datastore.newKeyFactory().setKind("Task");
      FullEntity taskEntity =
          Entity.newBuilder(keyFactory.newKey())
              .set("title", title)
              .set("timestamp", timestamp)
              .build();
      datastore.put(taskEntity);
      //response.sendRedirect("/index.html");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        Query<Entity> query =
            Query.newEntityQueryBuilder().setKind("Task").setOrderBy(OrderBy.desc("timestamp")).build();
        QueryResults<Entity> results = datastore.run(query);
    
        List<Task> tasks = new ArrayList<>();
        
        while (results.hasNext()) {
          Entity entity = results.next();
    
          long id = entity.getKey().getId();
          String title = entity.getString("title");
          long timestamp = entity.getLong("timestamp");
    
          Task task = new Task(id, title, timestamp);
          tasks.add(task);
        }
    
        Gson gson = new Gson();
    
        response.setContentType("application/json;");
        response.getWriter().println(gson.toJson(tasks));
      }

      @Override
      protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long task_id = Long.parseLong(request.getParameter("task_id"));
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        KeyFactory keyFactory = datastore.newKeyFactory().setKind("Task");
        Key taskEntityKey = keyFactory.newKey(task_id);
        datastore.delete(taskEntityKey);
      }
}
