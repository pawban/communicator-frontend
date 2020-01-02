package com.pawban.communicator_frontend.client;

import com.pawban.communicator_frontend.config.BackendConfig;
import com.pawban.communicator_frontend.domain.Country;
import com.pawban.communicator_frontend.dto.CountryDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CountryClient {

    private final static Logger LOGGER = LoggerFactory.getLogger(CountryClient.class);

    private final RestTemplate restTemplate;
    private final BackendConfig backendConfig;

    @Autowired
    public CountryClient(final RestTemplate restTemplate, final BackendConfig backendConfig) {
        this.restTemplate = restTemplate;
        this.backendConfig = backendConfig;
    }

    public List<Country> getCountries() {
        URI url = UriComponentsBuilder.fromHttpUrl(backendConfig.getCountriesEndpoint())
                .build(true)
                .toUri();
        try {
            CountryDto[] countriesResponse = restTemplate.getForObject(url, CountryDto[].class);
            return Arrays.stream(Optional.ofNullable(countriesResponse).orElse(new CountryDto[0]))
                    .map(Country::new)
                    .collect(Collectors.toList());
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

}
