package de.holisticon.worldcongressdemo.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
public class DataItemEntity {

    @Id
    private String nasaId;

    private List<String> uri;
}
