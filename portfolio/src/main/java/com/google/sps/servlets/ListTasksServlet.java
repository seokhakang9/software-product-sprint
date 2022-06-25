package com.google.sps.servlets;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.OrderBy;
import com.google.gson.Gson;

import com.google.sps.servlets.data.Task;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sound.sampled.SourceDataLine;

/** Servlet responsible for listing tasks. */
@WebServlet("/list-tasks")
public class ListTasksServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    Query<Entity> query =
        Query.newEntityQueryBuilder().setKind("Task").setOrderBy(OrderBy.desc("timestamp")).build();
    QueryResults<Entity> results = datastore.run(query);

    List<Task> tasks = new ArrayList<>();
    
    while (results.hasNext()) {
      Entity entity = results.next();
      System.out.println(entity);

      long id = entity.getKey().getId();
      System.out.println("entity.getKey(): " + entity.getKey());
      System.out.println("entity.getKey().getId(): " + entity.getKey().getId());
      String title = entity.getString("title");
      System.out.println("title: " + title);
      long timestamp = entity.getLong("timestamp");
      System.out.println("timestamp: " + timestamp);

      Task task = new Task(id, title, timestamp);
      tasks.add(task);
    }

    Gson gson = new Gson();

    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(tasks));
  }
}
