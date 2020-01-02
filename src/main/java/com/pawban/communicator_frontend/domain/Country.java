package com.pawban.communicator_frontend.domain;

import com.pawban.communicator_frontend.dto.CountryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Country {

    private String name;
    private String countryCode;
    private String flagUrl;

    public Country(final CountryDto countryDto) {
        this(countryDto.getName(), countryDto.getCountryCode(), countryDto.getFlagUrl());
    }

}
