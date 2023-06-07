package it.unipi.dsmt.jakartaee.lab_09_ejb.ejb;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unipi.dsmt.jakartaee.lab_09_ejb.model.Beer;
import it.unipi.dsmt.jakartaee.lab_09_ejb_interfaces.dto.BeerDTO;
import it.unipi.dsmt.jakartaee.lab_09_ejb_interfaces.interfaces.BeerEJB;
import jakarta.ejb.Stateless;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Stateless(mappedName = "BeerEJBFromJSON")
public class BeerEJBFromJSONImpl implements BeerEJB {

    private static List<Beer> beerList = new ArrayList<Beer>();

    public BeerEJBFromJSONImpl(){
        init();
    }

    public static void init() {
        try {
            String beersJSONFile = "data/beers.json";
            ClassLoader classLoader = BeerEJBFromJSONImpl.class.getClassLoader();
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

    @Override
    public List<BeerDTO> search(String keyword){
        return beerList.stream()
                .filter(beer -> beer.getName().toLowerCase().contains(keyword.toLowerCase()))
                .map(beer -> new BeerDTO(beer.getId(), beer.getName(), beer.getLink(), beer.getImage(), beer.getRating()))
                .collect(Collectors.toList());
    }
}
