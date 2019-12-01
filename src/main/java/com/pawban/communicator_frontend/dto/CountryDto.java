package com.pawban.communicator_frontend.dto;

import com.pawban.communicator_frontend.domain.Country;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CountryDto {

    private String name;
    private String countryCode;
    private String flagUrl;

    public CountryDto(final Country country) {
        this(
                country.getName(),
                country.getCountryCode(),
                country.getFlagUrl()
        );
    }

}
