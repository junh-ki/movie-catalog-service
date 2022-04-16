package io.msa.moviecatalogservice.resources;

import io.msa.moviecatalogservice.models.CatalogItem;
import io.msa.moviecatalogservice.models.Movie;
import io.msa.moviecatalogservice.models.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    private final RestTemplate restTemplate;
    private final WebClient.Builder webClientBuilder;

    @Autowired
    public MovieCatalogResource(RestTemplate restTemplate, WebClient.Builder webClientBuilder) {
        this.restTemplate = restTemplate;
        this.webClientBuilder = webClientBuilder;
    }

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {
        List<Rating> ratings = Arrays.asList(new Rating("1234", 4), new Rating("5678", 3));
        /*return ratings.stream().map(rating -> Optional.ofNullable(restTemplate.getForObject("http://localhost:8082/movies/"
                                + rating.getMovieId(), Movie.class))
                        .map(movie -> new CatalogItem(movie.getName(), "Description", rating.getRating()))
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());*/
        return ratings.stream().map(rating -> Optional.ofNullable(webClientBuilder.build().get()
                                .uri("http://localhost:8082/movies/" + rating.getMovieId())
                                .retrieve()
                                .bodyToMono(Movie.class)
                                .block())
                        .map(movie -> new CatalogItem(movie.getName(), "Description", rating.getRating()))
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}
