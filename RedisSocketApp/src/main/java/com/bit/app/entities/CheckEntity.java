package com.bit.app.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class CheckEntity extends AbstractCheckEntity {

    private String sessionId;
    private String name;
    private Integer category;
}
