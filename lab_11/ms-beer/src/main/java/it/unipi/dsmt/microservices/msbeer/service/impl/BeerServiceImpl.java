package it.unipi.dsmt.microservices.msbeer.service.impl;

import it.unipi.dsmt.microservices.msbeer.dao.BeerDAO;
import it.unipi.dsmt.microservices.msbeer.dto.BeerDTO;
import it.unipi.dsmt.microservices.msbeer.dto.BeerFiltersDTO;
import it.unipi.dsmt.microservices.msbeer.dto.BeerSearchDTO;
import it.unipi.dsmt.microservices.msbeer.model.Beer;
import it.unipi.dsmt.microservices.msbeer.service.BeerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BeerServiceImpl implements BeerService {

    @Autowired
    private BeerDAO beerDAO;

    @Override
    public List<BeerSearchDTO> search(BeerFiltersDTO beerFiltersDTO) {
        List<BeerSearchDTO> results = beerDAO.list(beerFiltersDTO);
        return results;
    }

    @Override
    public BeerDTO loadById(Integer id) {
        Beer beer = beerDAO.findById(id);
        BeerDTO dto = BeerDTO.builder()
                .brand(beer.getBrand())
                .description(beer.getDescription())
                .name(beer.getName())
                .price(beer.getPrice())
                .stock(beer.getStock())
                .alcoholPercentage(beer.getAlcoholPercentage())
                .imageUrl(beer.getImageUrl())
                .liquidVolumeInML(beer.getLiquidVolumeInML())
                .shortName(beer.getShortName())
                .id(beer.getId())
                .build();
        return dto;
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Integer id) {
        beerDAO.delete(id);
    }

    @Override
    @Transactional(readOnly = false)
    public void create(BeerDTO beerDTO) {
        Beer beer = new Beer();
        beer.setBrand(beerDTO.getBrand());
        beer.setImageUrl(beerDTO.getImageUrl());
        beer.setName(beerDTO.getName());
        beer.setPrice(beerDTO.getPrice());
        beer.setShortName(beerDTO.getShortName());
        beer.setStock(beerDTO.getStock());
        beer.setDescription(beerDTO.getDescription());
        beer.setAlcoholPercentage(beerDTO.getAlcoholPercentage());
        beer.setLiquidVolumeInML(beerDTO.getLiquidVolumeInML());
        beerDAO.save(beer);
    }

    @Override
    @Transactional(readOnly = false)
    public void update(BeerDTO beerDTO) {
        Beer beer = beerDAO.findById(beerDTO.getId());
        beer.setBrand(beerDTO.getBrand());
        beer.setImageUrl(beerDTO.getImageUrl());
        beer.setName(beerDTO.getName());
        beer.setPrice(beerDTO.getPrice());
        beer.setShortName(beerDTO.getShortName());
        beer.setStock(beerDTO.getStock());
        beer.setDescription(beerDTO.getDescription());
        beer.setAlcoholPercentage(beerDTO.getAlcoholPercentage());
        beer.setLiquidVolumeInML(beerDTO.getLiquidVolumeInML());
        beerDAO.update(beer);
    }
}
