package com.backend.domain.post.post.service;

import com.backend.domain.post.post.entity.Post;
import com.backend.domain.post.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Post write(String title, String content){

        Post post = new Post(title, content);
        return postRepository.save(post);
    }

    public long count(){
        return postRepository.count();
    }
}
