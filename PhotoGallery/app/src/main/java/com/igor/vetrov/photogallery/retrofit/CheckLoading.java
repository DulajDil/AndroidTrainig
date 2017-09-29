package com.igor.vetrov.photogallery.retrofit;


import lombok.Getter;
import lombok.Setter;

public class CheckLoading {

    @Getter
    @Setter
    private Boolean running;

    private CheckLoading(Boolean running) {
        this.running = running;
    }

    public static CheckLoading get() {
        return new CheckLoading(true);
    }
}
