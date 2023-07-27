package de.holisticon.worldcongressdemo.model.dto;

import java.util.List;


public record Item(List<DataItem> data,
                   String href,
                   List<Link> links) {

}
