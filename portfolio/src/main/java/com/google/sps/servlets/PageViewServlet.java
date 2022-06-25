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

/** Servlet that returns HTML that contains the page view count. */
@WebServlet("/page-views")
public class PageViewServlet extends HttpServlet {
  
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    Query<Entity> query = Query.newEntityQueryBuilder().setKind("pageViews").build();
    QueryResults<Entity> results = datastore.run(query);
    Entity entity = results.next();
    entity = Entity.newBuilder(entity).set("pageViews", entity.getLong("pageViews")+1).build();

    datastore.put(entity);

    response.setContentType("text/html;");
    response.getWriter().println("<h1>Page Views</h1>");
    response.getWriter().println("<p>This page has been viewed " + entity.getLong("pageViews") + " times.</p>");
  }
}
