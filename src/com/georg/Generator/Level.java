package com.georg.Generator;

/**
 * Created by Georg on 30/05/16.
 */
public enum Level {
    ExtremelyEasy("Extremly Easy"),
    Easy ("Easy"),
    Medium ("Medium"),
    Hard ("Hard"),
    Evil ("Devil's playground");

    private String name;
    Level(String name){this.name = name;}
    public String getName(){return name;}
}
