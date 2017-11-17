package com.bit.app.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HelloMessage {

    private String name;
}
