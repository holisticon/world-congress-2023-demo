package de.holisticon.reactive.restclient;

import de.holisticon.reactive.config.NonReactiveProperties;
import de.holisticon.reactive.model.dto.CollectionItem;
import de.holisticon.reactive.model.dto.SearchResult;
import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

/**
 * <a href="https://images.nasa.gov/docs/images.nasa.gov_api_docs.pdf">API docs</a>
 */
@Service
@Slf4j
public class NasaApiClient {

    private final RestTemplate restTemplate;

    private final NonReactiveProperties properties;

    @Autowired
    public NasaApiClient(final @NonNull RestTemplateBuilder restTemplateBuilder, final @NonNull NonReactiveProperties properties) {
        this.restTemplate = restTemplateBuilder.build();
        this.properties = properties;
    }


    @PostConstruct
    public void init() {
        final var ceres = getSearchResults("ceres");
    }

    public ResponseEntity<SearchResult> getSearchResults(final String searchTerm) {
        return getSearchResults(searchTerm, null, null);
    }

    public ResponseEntity<SearchResult> getSearchResults(final String searchTerm, final Long page, final Long pageSize) {
        final var queryParams = new LinkedMultiValueMap<String, String>();
        queryParams.add("q", searchTerm);
        final var nullSafePage = Optional.ofNullable(page)
                .orElse(1L)
                .toString();
        queryParams.add("page", nullSafePage);
        final var nullSafePageSize = Optional.ofNullable(pageSize)
                .orElse(12L)
                .toString();
        queryParams.add("page_size", nullSafePageSize);
        //queryParams.add("api_key", properties.getApiKey());
        final var apiResponse = restTemplate.exchange(buildUri(queryParams), HttpMethod.GET, new HttpEntity<>(prepareHttpHeaders()), SearchResult.class);
        final var collection = apiResponse.getBody()
                .collection();
        final var size = collection
                .items()
                .size();
        log.info("response size: {} / page number {} / page size {} / has next page: {}", size, nullSafePage, nullSafePageSize, hasNextPage(collection));
        return apiResponse;
    }

    private boolean hasNextPage(CollectionItem collection) {
        final var hasNext = collection.links()
                .stream()
                .anyMatch(l -> "next".equals(l.rel()));
        return hasNext;
    }

    URI buildUri(final MultiValueMap<String, String> queryParams) {
        try {
            final var uri = new URI(properties.getNasaApiSchema(), properties.getNasaApiHostname(),
                    properties.getNasaApiSearchEndpoint(), null
            );
            return UriComponentsBuilder.fromUri(uri)
                    .queryParams(queryParams)
                    .build()
                    .toUri();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    HttpHeaders prepareHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.ACCEPT, "application/json");

        return httpHeaders;
    }
}
