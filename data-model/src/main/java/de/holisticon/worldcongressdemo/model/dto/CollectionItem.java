package de.holisticon.worldcongressdemo.model.dto;

import java.util.List;


public record CollectionItem(String href,
                             List<Item> items,
                             List<Link> links,
                             MetaData metadata,
                             String version) {

}
