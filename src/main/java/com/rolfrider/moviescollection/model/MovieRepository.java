package com.rolfrider.moviescollection.model;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    Collection<Movie> findByAccountUsername(String username);
}
