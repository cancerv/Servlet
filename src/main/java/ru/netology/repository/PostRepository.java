package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class PostRepository {
  private final List<Post> posts = Collections.synchronizedList(new ArrayList<>());

  private final AtomicLong nextId = new AtomicLong(1);

  public List<Post> all() {
    return this.posts;
  }

  private Post getPostById(long id) {
    for (Post post : this.posts) {
      if (post.getId() == id) {
        return post;
      }
    }
    return null;
  }

  public Optional<Post> getById(long id) {
    Post post = this.getPostById(id);
    return Optional.ofNullable(post);
  }

  public Post save(Post post) throws NotFoundException {
    if (post.getId() == 0) {
      post.setId(this.nextId.getAndIncrement());
      this.posts.add(post);
    } else {
      Post existsPost = this.getPostById(post.getId());
      if (existsPost == null) {
        throw new NotFoundException();
      }
      existsPost.setContent(post.getContent());
    }
    return post;
  }

  public void removeById(long id) {
    Post post = this.getPostById(id);
    if (post != null) {
      this.posts.remove(post);
    }
  }
}
