package com.hogwartsmini.demo.common;

import lombok.Data;

@Data
public class HogwartsToken {
    private String token;
    private String deadlineTime; // 过期时间

}
