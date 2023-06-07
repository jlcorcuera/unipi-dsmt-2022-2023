package it.unipi.dsmt.jakartaee.lab_10_ejb_interfaces.interfaces;

import it.unipi.dsmt.jakartaee.lab_10_ejb_interfaces.dto.BeerDTO;
import jakarta.ejb.Remote;

import java.util.List;

@Remote
public interface ShoppingCartEJB {

    public void addProduct(String productId, String name);
    public List<BeerDTO> getBeerDTOList();
    public int getTotalNumberProducts();
    public String checkout();
}
