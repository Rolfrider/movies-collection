package com.rolfrider.moviescollection.Rest;


import com.rolfrider.moviescollection.model.Movie;
import com.rolfrider.moviescollection.model.MoviesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/movies")
public class MovieRestController {

    private MoviesRepository moviesRepository;


    @Autowired
    MovieRestController(MoviesRepository moviesRepository){
        this.moviesRepository = moviesRepository;
    }

    @RequestMapping(method =RequestMethod.GET)
    public Resources<MovieResource> readAllMovies(){

        List<MovieResource> movieResourceList = moviesRepository
                .findAll().stream().map(MovieResource::new)
                .collect(Collectors.toList());

        return new Resources<>(movieResourceList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{movieId}")
    public MovieResource readMovie(@PathVariable Long movieId){
        return new MovieResource(this.moviesRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException(movieId)));
    }

    @RequestMapping(method =RequestMethod.POST)
    public ResponseEntity<?> add(@RequestBody Movie movie){
        Movie addedMovie = moviesRepository.save(movie);

        Link forOneMovie = new MovieResource(movie).getLink("self");

        return ResponseEntity.created(URI.create(forOneMovie.getHref())).build();
    }

}
