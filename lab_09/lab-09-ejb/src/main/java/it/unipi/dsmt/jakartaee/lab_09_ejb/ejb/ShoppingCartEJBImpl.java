package it.unipi.dsmt.jakartaee.lab_09_ejb.ejb;

import it.unipi.dsmt.jakartaee.lab_09_ejb_interfaces.dto.BeerDTO;
import it.unipi.dsmt.jakartaee.lab_09_ejb_interfaces.interfaces.ShoppingCartEJB;
import jakarta.ejb.Stateful;

import java.util.ArrayList;
import java.util.List;

@Stateful
public class ShoppingCartEJBImpl implements ShoppingCartEJB {

    private List<BeerDTO> beerDTOList;

    public ShoppingCartEJBImpl(){
        beerDTOList = new ArrayList<>();
    }

    public void addProduct(String productId, String name){
        BeerDTO found = null;
        for(BeerDTO beerDTO: beerDTOList) {
            if (beerDTO.getId().equals(productId)) {
                found = beerDTO;
                break;
            }
        }
        if (found != null) {
            found.setQuantity(found.getQuantity() + 1);
        } else {
            found = new BeerDTO(productId, name, 1);
            beerDTOList.add(found);
        }
    }

    public List<BeerDTO> getBeerDTOList() {
        return beerDTOList;
    }

    public int getTotalNumberProducts() {
        return beerDTOList.stream()
                .map(beerDTO -> beerDTO.getQuantity())
                .reduce(0, Integer::sum);
    }

}
