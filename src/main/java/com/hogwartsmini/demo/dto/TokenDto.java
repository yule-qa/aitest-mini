package com.hogwartsmini.demo.dto;

import lombok.Data;

@Data
public class TokenDto {

    //(userId/userName/defaultJenkinsId/token)
    private Integer userId;
    private String userName;
    private Integer defaultJenkinsId;
    private String token;

}
