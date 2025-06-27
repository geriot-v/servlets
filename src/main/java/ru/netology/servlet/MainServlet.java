package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {
  private PostController controller;

  @Override
  public void init() {
    final var repository = new PostRepository();
    final var service = new PostService(repository);
    controller = new PostController(service);
  }


  private static final String POSTS_PATH = "/api/posts";
  private static final String POST_ID_REGEX = "/api/posts/\\d+";

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    try {
      final var path = req.getRequestURI();
      final var method = req.getMethod();

      switch (method) {
        case "GET":
          handleGet(path, req, resp);
          break;
        case "POST":
          controller.save(req.getReader(), resp);
          break;
        case "DELETE":
          handleDelete(path, resp);
          break;
        default:
          resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
      }
    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  private void handleGet(String path, HttpServletRequest req, HttpServletResponse resp) throws IOException {
    if (path.equals(POSTS_PATH)) {
      controller.all(resp);
    } else if (path.matches(POST_ID_REGEX)) {
      final var id = extractIdFromPath(path);
      controller.getById(id, resp);
    } else {
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
  }

  private void handleDelete(String path, HttpServletResponse resp) {
    if (path.matches(POST_ID_REGEX)) {
      final var id = extractIdFromPath(path);
      controller.removeById(id, resp);
    } else {
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
  }

  private long extractIdFromPath(String path) {
    return Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
  }
}