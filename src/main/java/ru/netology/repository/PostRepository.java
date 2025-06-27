package ru.netology.repository;

import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

public class PostRepository {
  private final List<Post> posts = new CopyOnWriteArrayList<>();
  private final AtomicLong idCounter = new AtomicLong(1);

  public List<Post> all() {
    return new ArrayList<>(posts);
  }

  public Optional<Post> getById(long id) {
    return posts.stream()
            .filter(p -> p.getId() == id)
            .findFirst();
  }

  public Post save(Post post) {
    if (post.getId() == 0) {
      long newId = idCounter.getAndIncrement();
      Post newPost = new Post(newId, post.getContent());
      posts.add(newPost);
      return newPost;
    } else {
      return posts.stream()
              .filter(p -> p.getId() == post.getId())
              .findFirst()
              .map(p -> {
                int index = posts.indexOf(p);
                Post updated = new Post(p.getId(), post.getContent());
                posts.set(index, updated);
                return updated;
              })
              .orElseThrow(() -> new NoSuchElementException("Post not found with id: " + post.getId()));
    }
  }

  public void removeById(long id) {
    posts.removeIf(p -> p.getId() == id);
  }
}