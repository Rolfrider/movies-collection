package com.rolfrider.moviescollection;

import com.rolfrider.moviescollection.Entity.Movie;
import com.rolfrider.moviescollection.Repository.MoviesRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MoviesCollectionApplication.class)
@WebAppConfiguration
public class MoviesCollectionApplicationTests {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private List<Movie> movieList = new ArrayList<>();

    @Autowired
    private MoviesRepository moviesRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    void setConverts(HttpMessageConverter<?> [] converters){
        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter( hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);
        assertNotNull("the JSON message converter must not be null",
        this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception{
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        this.moviesRepository.deleteAllInBatch();

        this.movieList.add(moviesRepository.save(new Movie("Shrek", "A cartoon movie")));
        this.movieList.add(moviesRepository.save(new Movie("Lobster", "A weird movie")));

    }

    @Test
    public void movieNotFound() throws Exception{
        mockMvc.perform(get("/movies/3"))
                .andExpect(status().isNotFound());

    }

    @Test
    public void readSingleMovie() throws Exception {
        mockMvc.perform(get("/movies/" + this.movieList.get(0).getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is(this.movieList.get(0).getId().intValue())))
                .andExpect(jsonPath("$.title", is("Shrek")))
                .andExpect(jsonPath("$.description", is("A cartoon movie")));
    }

    @Test
    public void readMovies() throws Exception {
        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(this.movieList.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].title", is("Shrek")))
                .andExpect(jsonPath("$[0].description", is("A cartoon movie")))
                .andExpect(jsonPath("$[1].id", is(this.movieList.get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].title", is("Lobster")))
                .andExpect(jsonPath("$[1].description", is("A weird movie")));
    }

    @Test
    public void createMovie() throws Exception{
        String movieJson = json(new Movie("Kill Bill", "A Bloody movie"));

        this.mockMvc.perform(post("/movies")
                                .contentType(contentType)
                                .content(movieJson))
                .andExpect(status().isCreated());
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

}
