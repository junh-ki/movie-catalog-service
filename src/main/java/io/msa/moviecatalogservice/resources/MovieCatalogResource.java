package io.msa.moviecatalogservice.resources;

import io.msa.moviecatalogservice.models.CatalogItem;
import io.msa.moviecatalogservice.models.Movie;
import io.msa.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    private static final String MOVIE_INFO_SERVICE_URL = "http://movie-info-service/movies/";
    private static final String RATINGS_DATA_SERVICE_URL = "http://ratings-data-service/ratingsdata/users/";
    private final RestTemplate restTemplate;
    private final WebClient.Builder webClientBuilder;

    @Autowired
    public MovieCatalogResource(RestTemplate restTemplate, WebClient.Builder webClientBuilder) {
        this.restTemplate = restTemplate;
        this.webClientBuilder = webClientBuilder;
    }

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {
        /*return ratings.stream().map(rating -> Optional.ofNullable(webClientBuilder.build().get()
                                .uri(MOVIE_INFO_SERVICE_URL + rating.getMovieId())
                                .retrieve()
                                .bodyToMono(Movie.class)
                                .block())
                        .map(movie -> new CatalogItem(movie.getName(), "Description", rating.getRating()))
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());*/
        return restTemplate.getForObject(RATINGS_DATA_SERVICE_URL + userId, UserRating.class)
                .getUserRatings().stream().map(rating -> Optional.ofNullable(restTemplate.getForObject(MOVIE_INFO_SERVICE_URL
                                + rating.getMovieId(), Movie.class))
                        .map(movie -> new CatalogItem(movie.getName(), movie.getDesc(), rating.getRating()))
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}
