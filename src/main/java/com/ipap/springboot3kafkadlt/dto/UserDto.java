package com.ipap.springboot3kafkadlt.dto;

public record User(
        Integer id,
        String firstname,
        String lastName,
        String email,
        String gender,
        String ipAddress
) {
}
