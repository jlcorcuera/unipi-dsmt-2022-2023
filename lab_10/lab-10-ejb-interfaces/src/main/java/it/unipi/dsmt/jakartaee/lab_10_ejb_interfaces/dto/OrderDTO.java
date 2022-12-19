package it.unipi.dsmt.jakartaee.lab_10_ejb_interfaces.dto;

import java.io.Serializable;
import java.util.List;

public class OrderDTO implements Serializable {
    private String id;
    private List<BeerDTO> beers;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<BeerDTO> getBeers() {
        return beers;
    }

    public void setBeers(List<BeerDTO> beers) {
        this.beers = beers;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "id='" + id + '\'' +
                ", beers=" + beers +
                '}';
    }
}
