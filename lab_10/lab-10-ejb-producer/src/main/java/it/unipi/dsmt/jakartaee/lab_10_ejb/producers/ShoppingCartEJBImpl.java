package it.unipi.dsmt.jakartaee.lab_10_ejb.producers;

import it.unipi.dsmt.jakartaee.lab_10_ejb_interfaces.dto.BeerDTO;
import it.unipi.dsmt.jakartaee.lab_10_ejb_interfaces.dto.OrderDTO;
import it.unipi.dsmt.jakartaee.lab_10_ejb_interfaces.interfaces.ShoppingCartEJB;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateful;

import java.util.ArrayList;
import java.util.List;

@Stateful
public class ShoppingCartEJBImpl implements ShoppingCartEJB {

    @EJB
    private OrderEJBImpl orderEJB;

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

    public String checkout(){
        //1. Creating OrderDTO object
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBeers(beerDTOList);
        //2. Creating Order by calling the OrderEJB.createOrder method
        String orderId = orderEJB.createOrder(orderDTO);
        //3. Since we created an order, we have to remove all products in our shopping cart
        beerDTOList = new ArrayList<>();
        //4. Returning the orderId generated
        return orderId;
    }

}
