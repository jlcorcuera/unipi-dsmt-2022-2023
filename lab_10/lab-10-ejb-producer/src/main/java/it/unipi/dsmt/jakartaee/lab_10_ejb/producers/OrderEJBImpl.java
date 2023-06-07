package it.unipi.dsmt.jakartaee.lab_10_ejb.producers;

import it.unipi.dsmt.jakartaee.lab_10_ejb_interfaces.dto.OrderDTO;
import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.jms.JMSContext;
import jakarta.jms.Queue;

import java.util.UUID;

@Stateless
public class OrderEJBImpl {

    @Resource(lookup = "jms/OrdersQueue")
    private Queue queue;
    @Inject
    private JMSContext jmsContext;

    public String createOrder(OrderDTO orderDTO){
        //1. Insert OrderDTO into a database
        String id = UUID.randomUUID().toString();
        orderDTO.setId(id);
        /*
            2. Adding the orderDTO object to a queue for further processing.
            For instance: The warehouse will receive this order for preparing
            the products to be delivered.
        */
        jmsContext.createProducer().send(queue, orderDTO);
        return id;
    }

}
