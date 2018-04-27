package com.rolfrider.moviescollection.Repository;


import com.rolfrider.moviescollection.Entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoviesRepository extends JpaRepository<Movie, Long> {
}
