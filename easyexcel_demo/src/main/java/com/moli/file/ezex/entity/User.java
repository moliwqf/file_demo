package com.moli.file.ezex.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * @author moli
 * @time 2024-07-30 17:55:48
 */
@Builder
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    /**
     * user id.
     *
     */
    @ExcelProperty("ID")
    private Long id;

    /**
     * username.
     */
    @ExcelProperty("Name")
    private String userName;

    /**
     * email.
     */
    @ExcelProperty("Email")
    private String email;

    /**
     * phoneNumber.
     */
    @ExcelProperty("Phone")
    private long phoneNumber;

    /**
     * description.
     */
    @ExcelProperty("Description")
    private String description;
}
