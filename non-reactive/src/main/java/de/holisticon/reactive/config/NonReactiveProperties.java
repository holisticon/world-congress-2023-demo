package de.holisticon.reactive.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "word-congress-demo.non-reactive")
@Component
@Data
public class NonReactiveProperties {

    private String nasaApiSchema;
    private String nasaApiHostname;
    private String nasaApiSearchEndpoint;
    private String nasaApiAssetEndpoint;
    private String nasaApiCaptionsEndpoint;
    private String nasaApiAlbumEndpoint;
    private String apiKey;

}
