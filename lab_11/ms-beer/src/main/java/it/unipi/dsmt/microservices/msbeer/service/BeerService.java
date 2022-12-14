package it.unipi.dsmt.microservices.msbeer.service;

import it.unipi.dsmt.microservices.msbeer.dto.BeerDTO;
import it.unipi.dsmt.microservices.msbeer.dto.BeerFiltersDTO;
import it.unipi.dsmt.microservices.msbeer.dto.BeerSearchDTO;

import java.util.List;

public interface BeerService {
    List<BeerSearchDTO> search(BeerFiltersDTO beerFiltersDTO);
    BeerDTO loadById(Integer id);

    void delete(Integer id);

    void create(BeerDTO beerDTO);

    void update(BeerDTO beerDTO);
}
