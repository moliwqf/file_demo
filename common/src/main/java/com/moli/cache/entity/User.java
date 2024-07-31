package com.moli.cache.entity;

import lombok.Builder;
import lombok.Data;

/**
 * @author moli
 * @time 2024-07-30 17:55:48
 */
@Data
@Builder
public class User {

    private Long id;

    private String userName;

    private Long phoneNumber;

    private String email;

    private String description;
}
