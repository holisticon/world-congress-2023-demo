package de.holisticon.reactive.restclient;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.holisticon.reactive.model.dto.SearchResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(MockitoExtension.class)
class NasaApiClientTest {


    @Test
    void testDataModel() throws IOException {
        // given
        final String testData = getTestdata("searchResultItem.json");

        // when
        final var searchResult = objectMapper().readerFor(SearchResult.class)
                .readValue(testData);

        // then

        assertNotNull(searchResult);

    }

    ObjectMapper objectMapper() {
        return new ObjectMapper()
                .findAndRegisterModules()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
                .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
                .setPropertyNamingStrategy(new PropertyNamingStrategy() {
                    @Override
                    public String nameForGetterMethod(final MapperConfig<?> config, final AnnotatedMethod method, final String defaultName) {
                        if ((method.getRawReturnType() == Boolean.class || method.getRawReturnType() == boolean.class)) {
                            return defaultName;
                        }
                        return super.nameForGetterMethod(config, method, defaultName);
                    }
                });
    }

    static String getTestdata(final String fileName) throws IOException {
        final File file = new File(ClassLoader.getSystemClassLoader()
                .getResource(fileName)
                .getFile());
        final BufferedReader br = new BufferedReader(new FileReader(file));
        final StringBuilder stringBuilder = new StringBuilder();
        String st;
        while ((st = br.readLine()) != null) {
            stringBuilder.append(st);
        }
        return stringBuilder.toString();
    }

}