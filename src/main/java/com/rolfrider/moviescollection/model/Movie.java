package com.rolfrider.moviescollection.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Movie {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private String description;

    @JsonIgnore
    @ManyToOne
    private Account account;

    public Movie(){}

    public Movie( final Account account, final String title, final String description) {
        this.account = account;
        this.title = title;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public Account getAccount() {
        return account;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
