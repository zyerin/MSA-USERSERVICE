package com.example.user_service.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@RedisHash(value = "redismember", timeToLive = 600)//단위는 초임
@AllArgsConstructor
@NoArgsConstructor
public class RedisMember {
    @Id
    private String id;

    private String idx; //사용자 idx

    private String username; // 사용자 이름

    public RedisMember(String idx, String username) {
        this.idx = idx;
        this.username = username;
    }
}

