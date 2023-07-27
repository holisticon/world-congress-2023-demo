package de.holisticon.worldcongressdemo.common;

import de.holisticon.worldcongressdemo.entity.DataItemEntity;
import de.holisticon.worldcongressdemo.model.dto.Item;
import de.holisticon.worldcongressdemo.model.dto.Link;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

@Slf4j
public abstract class AbstractService {


    protected DataItemEntity buildDataItemEntity(Item item, final String searchTerm) {
        final var dataItem = item.data().get(0);
        final var uriList = item.links().stream().map(Link::href).toList();
        logSearchResults(uriList, searchTerm);
        final DataItemEntity di = new DataItemEntity();
        di.setNasaId(dataItem.nasaId());
        di.setUri(uriList);
        return di;
    }

    protected void logSearchResults(final Collection<String> allLinks, final String searchTerm) {
        allLinks.forEach(link -> log.info("{} - {}", searchTerm, link));
    }
}
