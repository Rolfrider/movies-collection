package com.rolfrider.moviescollection;

import com.rolfrider.moviescollection.model.Account;
import com.rolfrider.moviescollection.model.AccountRepository;
import com.rolfrider.moviescollection.model.Movie;
import com.rolfrider.moviescollection.model.MovieRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class MoviesCollectionApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoviesCollectionApplication.class, args);
    }

    @Bean
    CommandLineRunner init(MovieRepository movieRepository, AccountRepository accountRepository){
        return args ->
                Arrays.asList("rob,bob,jeff,jenifer,angela,pam,michael,alex".split(","))
                        .forEach(a -> {
                            Account account = accountRepository.save(new Account(a, "password"));
                            movieRepository.save(new Movie(account, "Shrek", "A " + a + "'s favorite movie"));
                            movieRepository.save(new Movie(account, "Shrek 2", "A " + a + "'s second favorite movie"));
                        });
    }

}

