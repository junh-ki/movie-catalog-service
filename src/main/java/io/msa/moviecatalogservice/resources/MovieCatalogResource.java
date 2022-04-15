package io.msa.moviecatalogservice.resources;

import io.msa.moviecatalogservice.models.CatalogItem;
import io.msa.moviecatalogservice.models.Movie;
import io.msa.moviecatalogservice.models.Rating;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {
        RestTemplate restTemplate = new RestTemplate();
        List<Rating> ratings = Arrays.asList(new Rating("1234", 4), new Rating("5678", 3));
        return ratings.stream().map(rating -> Optional.ofNullable(restTemplate.getForObject("http://localhost:8082/movies/"
                                + rating.getMovieId(), Movie.class))
                        .map(movie -> new CatalogItem(movie.getName(), "Description", rating.getRating()))
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}
