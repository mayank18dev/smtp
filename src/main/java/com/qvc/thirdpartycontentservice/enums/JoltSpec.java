package com.qvc.thirdpartycontentservice.enums;


import lombok.Getter;

@Getter
public enum JoltSpec {
    VIEW_HOME_CAROUSEL("HOME_EVENT_DATA","spec/CarouselResponse.json"),
    VIEW_HOME_STATIC("STATIC_EVENT_DATA","spec/StaticImageResponse.json");

    private final String joltSpecType;
    private final String joltSpecPath;

    JoltSpec(String joltSpecType, String joltSpecPath) {

        this.joltSpecType = joltSpecType;
        this.joltSpecPath = joltSpecPath;
    }

    public String getJoltSpecPath() {
        return this.joltSpecPath;
    }
}
