package com.rolfrider.moviescollection;

import com.rolfrider.moviescollection.model.Account;
import com.rolfrider.moviescollection.model.AccountRepository;
import com.rolfrider.moviescollection.model.Movie;
import com.rolfrider.moviescollection.model.MovieRepository;
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
public class MoviesCollectionApplicationTests{


    private MediaType contentType = new MediaType("application", "hal+json", Charset.forName("UTF-8"));;

    private MockMvc mockMvc;

    private String userName = "bdussault";

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private Account account;

    private List<Movie> movieList = new ArrayList<>();

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        this.movieRepository.deleteAllInBatch();
        this.accountRepository.deleteAllInBatch();

        this.account = accountRepository.save(new Account(userName, "password"));
        this.movieList.add(movieRepository.save(new Movie(account, "Lion King", "A " + userName + "description")));
        this.movieList.add(movieRepository.save(new Movie(account, "Lion King 2" , "A "+ userName + "description")));
    }

    @Test
    public void userNotFound() throws Exception {
        mockMvc.perform(post("/george/movies/")
                .content(this.json(new Movie(null, null, null)))
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }

    @Test
    public void readSingleMovie() throws Exception {
        mockMvc.perform(get("/" + userName + "/movies/"
                + this.movieList.get(0).getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.movie.id", is(this.movieList.get(0).getId().intValue())))
                .andExpect(jsonPath("$.movie.title", is("Lion King")))
                .andExpect(jsonPath("$.movie.description", is("A " + userName + "description")))
                .andExpect(jsonPath("$._links.self.href", containsString("/" + userName + "/movies/"
                        + this.movieList.get(0).getId())));
    }

    @Test
    public void readMovies() throws Exception {
        mockMvc.perform(get("/" + userName + "/movies"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$._embedded.movieResourceList", hasSize(2)))
                .andExpect(jsonPath("$._embedded.movieResourceList[0].movie.id", is(this.movieList.get(0).getId().intValue())))
                .andExpect(jsonPath("$._embedded.movieResourceList[0].movie.title", is("Lion King")))
                .andExpect(jsonPath("$._embedded.movieResourceList[0].movie.description", is("A " + userName + "description")))
                .andExpect(jsonPath("$._embedded.movieResourceList[1].movie.id", is(this.movieList.get(1).getId().intValue())))
                .andExpect(jsonPath("$._embedded.movieResourceList[1].movie.title", is("Lion King 2")))
                .andExpect(jsonPath("$._embedded.movieResourceList[1].movie.description", is("A " + userName + "description")));
    }

    @Test
    public void createMovie() throws Exception {
        String movieJson = json(new Movie(
                this.account, "Kill Bill", "A Bloody movie"));

        this.mockMvc.perform(post("/" + userName + "/movies")
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
