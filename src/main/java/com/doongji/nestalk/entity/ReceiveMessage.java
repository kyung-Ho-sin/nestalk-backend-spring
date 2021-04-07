package com.doongji.nestalk.entity;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ReceiveMessage {

    private String userId;
    private String message;
    private String profileImage;

}
