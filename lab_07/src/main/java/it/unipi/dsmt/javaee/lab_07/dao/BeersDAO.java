package it.unipi.dsmt.javaee.lab_07.dao;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unipi.dsmt.javaee.lab_07.entity.Beer;
import it.unipi.dsmt.javaee.lab_07.dto.BeerDTO;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BeersDAO {

    private static List<Beer> beerList = new ArrayList<Beer>();

    public static void init() {
        try {
            String beersJSONFile = "data/beers.json";
            ClassLoader classLoader = BeersDAO.class.getClassLoader();
            URL resource = classLoader.getResource(beersJSONFile);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(resource);

            JsonNode searchResultNode = jsonNode.get("search_results");
            Iterator<JsonNode> iterator = searchResultNode.iterator();
            while (iterator.hasNext()) {
                JsonNode beerJSON = iterator.next();
                Beer beer = new Beer();
                beer.setId(UUID.randomUUID().toString());
                beer.setName(beerJSON.get("title").asText());
                beer.setAsin(beerJSON.get("asin").asText());
                beer.setLink(beerJSON.get("link").asText());
                beer.setImage(beerJSON.get("image").asText());
                beer.setRating(beerJSON.get("rating").asDouble());
                beer.setRatingsTotal(beerJSON.get("ratings_total").asDouble());
                beerList.add(beer);
            }
            System.out.println("Loaded " + beerList.size() + " beers.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<BeerDTO> search(String keyword){
        return beerList.stream()
                .filter(beer -> beer.getName().toLowerCase().contains(keyword.toLowerCase()))
                .map(beer -> new BeerDTO(beer.getName(), beer.getLink(), beer.getImage(), beer.getRating()))
                .collect(Collectors.toList());
    }

}