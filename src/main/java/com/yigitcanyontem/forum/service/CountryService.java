package com.yigitcanyontem.forum.service;

import com.yigitcanyontem.forum.entity.Country;
import com.yigitcanyontem.forum.exceptions.SearchNotFoundException;
import com.yigitcanyontem.forum.repository.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService {

    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }


    public Country singleCountry(Integer id){
        return countryRepository.findById(id).orElseThrow(() -> new SearchNotFoundException("Country With id " +id +" doesn't exist"));
    }
    public List<Country> allCountries(){
        return countryRepository.findAll();
    }
}
