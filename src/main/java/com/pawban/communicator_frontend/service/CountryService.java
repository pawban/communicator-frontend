package com.pawban.communicator_frontend.service;

import com.pawban.communicator_frontend.client.CountryClient;
import com.pawban.communicator_frontend.domain.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CountryService {

    private final CountryClient countryClient;

    @Autowired
    public CountryService(final CountryClient countryClient) {
        this.countryClient = countryClient;
    }

    public List<Country> getCountries() {
        return countryClient.getCountries().stream()
                .sorted(Comparator.comparing(Country::getName))
                .collect(Collectors.toList());
    }

}
