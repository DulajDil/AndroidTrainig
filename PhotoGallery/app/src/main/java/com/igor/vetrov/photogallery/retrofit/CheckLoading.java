package com.igor.vetrov.photogallery.retrofit;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckLoading {

    private Boolean running;

    private CheckLoading(Boolean running) {
        this.running = running;
    }

    public static CheckLoading get() {
        return new CheckLoading(true);
    }
}
