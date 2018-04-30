package com.rolfrider.moviescollection.Rest;

import com.rolfrider.moviescollection.model.Movie;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

public class MovieResource extends ResourceSupport {

    private final Movie movie;

    public MovieResource(Movie movie){
        this.movie = movie;
        this.add(linkTo(MovieRestController.class).withRel("movies"));
        this.add(linkTo(methodOn(MovieRestController.class)
                .readMovie(movie.getId())).withSelfRel());
    }

    public Movie getMovie() {
        return movie;
    }
}
