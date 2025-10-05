package com.backend.domain.post.post.entity;

import com.backend.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Post extends BaseEntity {

    private String title;
    private String content;
}
