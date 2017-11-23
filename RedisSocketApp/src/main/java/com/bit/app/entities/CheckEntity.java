package com.bit.app.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@AllArgsConstructor
@Data
@Builder
public class CheckEntity implements Serializable {

//    private String id;
    private String name;
//    private int grade;
}
