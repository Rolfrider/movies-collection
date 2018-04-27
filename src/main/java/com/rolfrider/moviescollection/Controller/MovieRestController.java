package com.rolfrider.moviescollection.Controller;


import com.rolfrider.moviescollection.Entity.Movie;
import com.rolfrider.moviescollection.Exception.MovieNotFoundException;
import com.rolfrider.moviescollection.Repository.MoviesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieRestController {

    private MoviesRepository moviesRepository;


    @Autowired
    MovieRestController(MoviesRepository moviesRepository){
        this.moviesRepository = moviesRepository;
    }

    @RequestMapping(method =RequestMethod.GET)
    public List<Movie> readAllMovies(){
        return moviesRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{movieId}")
    public Movie readMovie(@PathVariable Long movieId){
        return moviesRepository.findById(movieId).orElseThrow(
                () -> new MovieNotFoundException(movieId));
    }

    @RequestMapping(method =RequestMethod.POST)
    public ResponseEntity<?> add(@RequestBody Movie movie){
        Movie addedMovie = moviesRepository.save(movie);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(addedMovie.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

}
