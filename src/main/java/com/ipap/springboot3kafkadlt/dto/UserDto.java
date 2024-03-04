package com.ipap.springboot3kafkadlt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class UserDto {

    private int id;
    private String firstname;
    private String lastName;
    private String email;
    private String gender;
    private String ipAddress;

}
