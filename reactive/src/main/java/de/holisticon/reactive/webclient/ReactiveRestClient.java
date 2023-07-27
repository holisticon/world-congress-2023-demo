package de.holisticon.reactive.webclient;

import de.holisticon.reactive.config.ReactiveProperties;
import de.holisticon.worldcongressdemo.model.dto.CollectionItem;
import de.holisticon.worldcongressdemo.model.dto.Item;
import de.holisticon.worldcongressdemo.model.dto.SearchResult;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@Service
@Slf4j
public class ReactiveRestClient {

    private final @NonNull WebClient.Builder webBuilder;
    private final @NonNull ReactiveProperties properties;

    private final @NonNull WebClient webClient;

    public ReactiveRestClient(WebClient.Builder webBuilder, ReactiveProperties properties) {
        this.webBuilder = webBuilder;
        this.properties = properties;
        this.webClient = WebClient.builder()
                .build();
    }

    public Flux<Item> getSearchResults(final String searchTerm) {
        return getSearchResults(searchTerm, null, null)
                .map(item-> {
                    if (item.data().get(0).nasaId().contains("GS")){
                        throw new IllegalStateException("error");
                    }
                    return item;
                });
    }

    public Flux<Item> getSearchResults(final String searchTerm, final Long page, final Long pageSize) {
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
        return webClient
                .get()
                .uri(buildUri(queryParams))
                .headers(h -> h.addAll(prepareHttpHeaders()))
                .retrieve()
                .bodyToFlux(SearchResult.class)
                .doOnNext(searchResult -> {
                    final var collection = searchResult
                            .collection();
                    final var size = collection
                            .items()
                            .size();
                    log.debug("response size: {} / page number {} / page size {} / has next page: {}", size, nullSafePage, nullSafePageSize, hasNextPage(collection));

                })
                .flatMapIterable(searchResult -> searchResult.collection().items())
                .onBackpressureBuffer();
    }

    private boolean hasNextPage(CollectionItem collection) {
        return collection.links()
                .stream()
                .anyMatch(l -> "next".equals(l.rel()));
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
