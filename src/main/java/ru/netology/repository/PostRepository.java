package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepository {
  private final Map<Long, Post> posts = new ConcurrentHashMap<>();

  private final AtomicLong nextId = new AtomicLong(1);

  public List<Post> all() {
    return new ArrayList<>(this.posts.values());
  }

  public Optional<Post> getById(long id) {
    Post post = this.posts.get(id);
    return Optional.ofNullable(post);
  }

  public Post save(Post post) throws NotFoundException {
    if (post.getId() == 0) {
      long newPostId = this.nextId.getAndIncrement();
      post.setId(newPostId);
      this.posts.put(newPostId, post);
    } else {
      Post existsPost = this.posts.get(post.getId());
      if (existsPost == null) {
        throw new NotFoundException();
      }
      existsPost.setContent(post.getContent());
    }
    return post;
  }

  public void removeById(long id) {
    this.posts.remove(id);
  }
}
