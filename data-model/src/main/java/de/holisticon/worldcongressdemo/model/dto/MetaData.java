package de.holisticon.worldcongressdemo.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MetaData(@JsonProperty("total_hits")
                       Long totalHits) {

}
