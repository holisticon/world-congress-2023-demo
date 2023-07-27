package de.holisticon.worldcongressdemo.model.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;


public record DataItem(String center,
                       @JsonProperty("date_created")
                       Instant dateCreated,
                       String description,
                       List<String> keywords,
                       @JsonProperty("media_type")
                       String mediaType,
                       @JsonProperty("nasa_id")
                       String nasaId,
                       String title) {

}
