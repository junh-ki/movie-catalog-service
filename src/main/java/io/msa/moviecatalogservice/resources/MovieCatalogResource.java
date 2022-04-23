package io.msa.moviecatalogservice.resources;

import io.msa.moviecatalogservice.models.CatalogItem;
import io.msa.moviecatalogservice.services.MovieInfo;
import io.msa.moviecatalogservice.services.UserRatingInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    private final MovieInfo movieInfo;
    private final UserRatingInfo userRatingInfo;

    @Autowired
    public MovieCatalogResource(MovieInfo movieInfo, UserRatingInfo userRatingInfo) {
        this.movieInfo = movieInfo;
        this.userRatingInfo = userRatingInfo;
    }

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {
        return Objects.requireNonNull(userRatingInfo.getUserRating(userId)).getUserRatings().stream()
                .map(movieInfo::getCatalogItem)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}
