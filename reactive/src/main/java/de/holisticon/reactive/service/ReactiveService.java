package de.holisticon.reactive.service;

import de.holisticon.reactive.data.DataItemReactiveRepository;
import de.holisticon.reactive.webclient.ReactiveRestClient;
import de.holisticon.worldcongressdemo.common.AbstractService;
import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * Shortcuts:
 * - run:   control+option+R
 * - debug: control+option+D
 * - run console: command+4
 * - debug console: command+5
 * - font size: shift+control + . ,
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class ReactiveService extends AbstractService {

    private final @NonNull ReactiveRestClient reactiveRestClient;

    private final @NonNull DataItemReactiveRepository reactiveRepo;

    @PostConstruct
    public void init() {
        this.callNasaApi("ceres");
        this.callNasaApi("saturn");
        this.callNasaApi("earth");
    }

    public void callNasaApi(final String searchterm) {
        this.reactiveRestClient.getSearchResults(searchterm)
                .filter(item -> Objects.nonNull(item.links()))
                .onErrorResume(throwable -> {
                    log.error("There was an error searching for {}. Skipping the item.", searchterm);
                    return Mono.empty();
                })
                .map(item -> buildDataItemEntity(item, searchterm))
                .flatMap(reactiveRepo::save)
                .collectList()
                .subscribe(dataItemEntities -> log.info("{} entries have been saved to the database", dataItemEntities.size()),
                        throwable -> log.error("error", throwable));
    }


}
