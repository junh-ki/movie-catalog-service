package io.msa.moviecatalogservice.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.msa.moviecatalogservice.models.Rating;
import io.msa.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class UserRatingInfo {

    private static final String RATINGS_DATA_SERVICE_URL = "http://ratings-data-service/ratingsdata/users/";
    private final RestTemplate restTemplate;

    @Autowired
    public UserRatingInfo(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @HystrixCommand(fallbackMethod = "getFallbackUserRating")
    public UserRating getUserRating(String userId) {
        return restTemplate.getForObject(RATINGS_DATA_SERVICE_URL + userId, UserRating.class);
    }

    private UserRating getFallbackUserRating(String userId) {
        UserRating userRating = new UserRating();
        userRating.setUserId(userId);
        userRating.setUserRatings(List.of(new Rating("0", 0)));
        return userRating;
    }

}
