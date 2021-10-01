package com.example.demo.controllers;

public class Movie {
    private int id;
    private String title;
    private int year;
    private int length;
    private String subject;
    private int popularity;
    private String awards;

    @Override
    public String toString() {
        return "<h1 style=\"text-align: center\"> Movie: " + title + "</h1>" +
                "<p style=\"text-align: center\">id: " + id +
                ", Year: " + year +
                ", Length: " + length +
                ", Subject: " + subject +
                ", Popularity: " + popularity +
                ", Awards: " + awards + "</p>";
    }



    public Movie(int id, String title, int year, int length, String subject, int popularity, String awards) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.length = length;
        this.subject = subject;
        this.popularity = popularity;
        this.awards = awards;
    }

    public int getPopularity() {
        return popularity;
    }
}

