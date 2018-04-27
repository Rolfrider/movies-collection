package com.rolfrider.moviescollection.Exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MovieNotFoundException extends RuntimeException {

    public MovieNotFoundException(Long movieId){
        super("Could not found movie " + movieId + ".");
    }
}
