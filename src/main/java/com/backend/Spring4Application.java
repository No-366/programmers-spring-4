package com.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing //JPA 엔티티의 생성/수정 시간 같은 "감사(Auditing)" 정보를 자동으로 관리해주는 기능을 활성화하는 애너테이션
public class Spring4Application {

    public static void main(String[] args) {
        SpringApplication.run(Spring4Application.class, args);
    }

}
