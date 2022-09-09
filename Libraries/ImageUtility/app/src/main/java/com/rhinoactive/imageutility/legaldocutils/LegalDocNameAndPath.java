package com.rhinoactive.imageutility.legaldocutils;

/**
 * Created by hunter on 2017-09-12.
 */

public class LegalDocNameAndPath {

    private String name;
    private String path;

    public LegalDocNameAndPath(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }
}
