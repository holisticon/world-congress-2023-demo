package de.holisticon.reactive.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MetaData(@JsonProperty("total_hits")
                       Long totalHits) {

}
