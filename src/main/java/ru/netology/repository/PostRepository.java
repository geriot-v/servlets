package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepository {
  private final Map<Long, Post> posts = new ConcurrentHashMap<>();
  private final AtomicLong idCounter = new AtomicLong(1);

  public List<Post> all() {
    return new ArrayList<>(posts.values());
  }

  public Optional<Post> getById(long id) {
    return Optional.ofNullable(posts.get(id));
  }

  public Post save(Post post) {
    long id = post.getId();
    if (id == 0) {
      long newId = idCounter.getAndIncrement();
      Post newPost = new Post(newId, post.getContent());
      posts.put(newId, newPost);
      return newPost;
    } else {
      if (posts.containsKey(id)) {
        Post updated = new Post(id, post.getContent());
        posts.put(id, updated);
        return updated;
      }
      throw new NoSuchElementException("Post not found with id: " + id);
    }
  }

  public void removeById(long id) {
    posts.remove(id);
  }
}