package com.rolfrider.moviescollection;

import com.rolfrider.moviescollection.model.Movie;
import com.rolfrider.moviescollection.model.MoviesRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MoviesCollectionApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoviesCollectionApplication.class, args);
    }

    @Bean
    CommandLineRunner init(MoviesRepository moviesRepository){
        return (evt) -> {
            moviesRepository.save(new Movie("Lord of the Rings", "A great movie"));
            moviesRepository.save(new Movie("Where is Nemo?", "A cartoon movie"));
            moviesRepository.save(new Movie("Kill Bill", "A bloody movie"));
        };
    }
}
