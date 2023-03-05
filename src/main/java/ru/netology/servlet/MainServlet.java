package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.exception.NotFoundException;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
  private PostController controller;

  private final String getPostsRoute = "/api/posts";
  private final String storeUpdatePostsRoute = "/api/posts";
  private final String getPostRoute = "/api/posts/\\d+";
  private final String deletePostRoute = "/api/posts/\\d+";

  @Override
  public void init() {
    final var repository = new PostRepository();
    final var service = new PostService(repository);
    controller = new PostController(service);
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    try {
      final var path = req.getRequestURI();
      final var method = req.getMethod();

      if (method.equals("GET") && path.equals(this.getPostsRoute)) {
        controller.all(resp);
        return;
      }
      if (method.equals("GET") && path.matches(this.getPostRoute)) {
        final var id = this.getIdFromPath(path);
        controller.getById(id, resp);
        return;
      }
      if (method.equals("POST") && path.equals(this.storeUpdatePostsRoute)) {
        controller.save(req.getReader(), resp);
        return;
      }
      if (method.equals("DELETE") && path.matches(this.deletePostRoute)) {
        final var id = this.getIdFromPath(path);
        controller.removeById(id, resp);
        return;
      }
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (NotFoundException e){
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  private long getIdFromPath(String path) {
    return Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
  }
}

