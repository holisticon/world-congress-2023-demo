package de.holisticon.nonreactive.service;

import de.holisticon.nonreactive.data.DataItemRepository;
import de.holisticon.nonreactive.restclient.NonReactiveRestClient;
import de.holisticon.worldcongressdemo.common.AbstractService;
import de.holisticon.worldcongressdemo.entity.DataItemEntity;
import de.holisticon.worldcongressdemo.model.dto.Item;
import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Shortcuts:
 *  - run:   control+option+R
 *  - debug: control+option+D
 *  - run console: command+4
 *  - debug console: command+5
 *  - font size: shift+control + . ,
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class NonReactiveService extends AbstractService {

    private final @NonNull NonReactiveRestClient nonReactiveRestClient;

    private final @NonNull DataItemRepository nonReactiveRepo;

    @PostConstruct
    public void init() {
        this.callNasaApi("ceres");
        this.callNasaApi("saturn");
        this.callNasaApi("earth");
    }

    public void callNasaApi(final String searchterm) {

        final List<DataItemEntity> dataItemEntities = new ArrayList<>();

        try {
            final List<Item> items = this.nonReactiveRestClient.getSearchResults(searchterm);

            for (final Item item : items) {
                if (Objects.nonNull(item.links())) {
                    final var di = buildDataItemEntity(item, searchterm);
                    dataItemEntities.add(di);
                }
            }
        } catch (final IllegalStateException ex) {
            log.error("There has been an exception.", ex);
        }

        nonReactiveRepo.saveAll(dataItemEntities);
        log.info("{} entries have been saved to the database", dataItemEntities.size());
    }

}
