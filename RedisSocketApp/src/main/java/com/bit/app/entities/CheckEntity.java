package com.bit.app.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@AllArgsConstructor
@Data
public class CheckEntity implements Serializable {

    private String id;
    private String name;
    private int grade;
}
