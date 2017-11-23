package com.igor.vetrov.photogallery.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class GalleryItem {

    @SerializedName("title")
    @Expose
    private String mCaption;
    @SerializedName("id")
    @Expose
    private String mId;
    @SerializedName("url_s")
    @Expose
    private String mUrl;
}
