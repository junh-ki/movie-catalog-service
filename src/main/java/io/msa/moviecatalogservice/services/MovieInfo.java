package io.msa.moviecatalogservice.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import io.msa.moviecatalogservice.models.CatalogItem;
import io.msa.moviecatalogservice.models.Movie;
import io.msa.moviecatalogservice.models.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class MovieInfo {

    private static final String MOVIE_INFO_SERVICE_URL = "http://movie-info-service/movies/";
    private final RestTemplate restTemplate;

    @Autowired
    public MovieInfo(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @HystrixCommand(fallbackMethod = "getFallbackCatalogItem",
            threadPoolKey = "movieInfoPool",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "20"),
                    @HystrixProperty(name = "maxQueueSize", value = "10")
            })
    public CatalogItem getCatalogItem(Rating rating) {
        return Optional.ofNullable(restTemplate.getForObject(MOVIE_INFO_SERVICE_URL
                        + rating.getMovieId(), Movie.class))
                .map(movie -> new CatalogItem(movie.getName(), movie.getDesc(), rating.getRating()))
                .orElse(null);
    }

    private CatalogItem getFallbackCatalogItem(Rating rating) {
        return new CatalogItem("Movie name not found", "", rating.getRating());
    }

}
