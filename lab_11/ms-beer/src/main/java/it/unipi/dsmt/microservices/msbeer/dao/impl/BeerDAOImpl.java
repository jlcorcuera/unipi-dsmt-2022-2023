package it.unipi.dsmt.microservices.msbeer.dao.impl;

import it.unipi.dsmt.microservices.msbeer.dao.BeerDAO;
import it.unipi.dsmt.microservices.msbeer.dto.BeerFiltersDTO;
import it.unipi.dsmt.microservices.msbeer.dto.BeerSearchDTO;
import it.unipi.dsmt.microservices.msbeer.model.Beer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BeerDAOImpl implements BeerDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<BeerSearchDTO> list(BeerFiltersDTO beerFilters) {
        List<BeerSearchDTO> results = new ArrayList<>();
        Map<String, Object> queryParams = new HashMap<>();
        StringBuilder jpql = new StringBuilder();
        jpql.append("select b from Beer b where 1 = 1 ");
        if (beerFilters.getName() != null && !beerFilters.getName().isEmpty()){
            jpql.append(" and lower(b.name) like concat('%', lower(:name), '%') ");
            queryParams.put("name", beerFilters.getName());
        }
        if (beerFilters.getPriceStart() != null){
            jpql.append(" and b.price >= :priceStart ");
            queryParams.put("priceStart", beerFilters.getPriceStart());
        }
        if (beerFilters.getPriceEnd() != null){
            jpql.append(" and b.price >= :priceEnd ");
            queryParams.put("priceEnd", beerFilters.getPriceEnd());
        }
        Query query = entityManager.createQuery(jpql.toString());
        for(Map.Entry<String, Object> paramsKeyValue: queryParams.entrySet()) {
            query.setParameter(paramsKeyValue.getKey(), paramsKeyValue.getValue());
        }
        List<Beer> beers = query.getResultList();
        if (beers != null && !beers.isEmpty()) {
            beers.stream().forEach( beer -> {
                BeerSearchDTO dto = BeerSearchDTO.builder()
                        .id(beer.getId())
                        .name(beer.getName())
                        .imageUrl(beer.getImageUrl())
                        .shortName(beer.getShortName())
                        .price(beer.getPrice())
                        .build();
                results.add(dto);
            });
        }
        return results;
    }

    @Override
    public Beer findById(Integer id) {
        return entityManager.find(Beer.class, id);
    }

    @Override
    public void save(Beer beer) {
        entityManager.persist(beer);
    }

    @Override
    public void update(Beer beer) {
        entityManager.merge(beer);
    }

    @Override
    public void delete(Integer id) {
        Beer beer = entityManager.find(Beer.class, id);
        if (beer != null) {
            entityManager.remove(beer);
        }
    }
}
