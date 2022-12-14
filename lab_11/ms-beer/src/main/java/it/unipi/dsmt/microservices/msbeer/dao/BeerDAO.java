package it.unipi.dsmt.microservices.msbeer.dao;

import it.unipi.dsmt.microservices.msbeer.dto.BeerDTO;
import it.unipi.dsmt.microservices.msbeer.dto.BeerFiltersDTO;
import it.unipi.dsmt.microservices.msbeer.dto.BeerSearchDTO;
import it.unipi.dsmt.microservices.msbeer.model.Beer;

import java.util.List;

public interface BeerDAO {

    List<BeerSearchDTO> list(BeerFiltersDTO beerFilters);
    Beer findById(Integer id);
    void save(Beer beer);
    void update(Beer beer);
    void delete(Integer id);

}
