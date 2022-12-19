package it.unipi.dsmt.jakartaee.lab_10_ejb.producers;


import it.unipi.dsmt.jakartaee.lab_10_ejb_interfaces.dto.BeerDTO;
import it.unipi.dsmt.jakartaee.lab_10_ejb_interfaces.interfaces.BeerEJB;
import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Stateless(mappedName = "BeerEJBFromMySQL")
public class BeerEJBFromMySQLImpl implements BeerEJB {

    private static final Integer BEER_PRODUCT_TYPE = 2;

    @Resource(lookup = "jdbc/BeerPool")
    private DataSource dataSource;

    @Override
    public List<BeerDTO> search(String keyword){
        List<BeerDTO> beers = new ArrayList<>();
        try(Connection connection = dataSource.getConnection()) {
            StringBuilder sql = new StringBuilder();
            sql.append("select ");
            sql.append("    ep.id, ");//1
            sql.append("    ep.name, ");//2
            sql.append("    image_url as link, ");//3
            sql.append("    image_url as image, ");//4
            sql.append("    1 as rating ");//5
            sql.append("from ");
            sql.append("    ec_product ep ");
            sql.append("    inner join ec_beer eb on ep.id = eb.product_id ");
            sql.append("where ");
            sql.append(" ep.type = ").append(BEER_PRODUCT_TYPE);
            if (keyword != null && !keyword.isEmpty()){
                sql.append("  and lower(ep.name) like concat('%', lower(?), '%') ");
            }
            sql.append(" order by lower(ep.name)");
            try(PreparedStatement pstmt = connection.prepareStatement(sql.toString())){
                if (keyword != null && !keyword.isEmpty()){
                    pstmt.setString(1, keyword);
                }
                try(ResultSet rs = pstmt.executeQuery()){
                    while (rs.next()){
                        BeerDTO dto = new BeerDTO();
                        dto.setId(rs.getString(1));
                        dto.setName(rs.getString(2));
                        dto.setLink(rs.getString(3));
                        dto.setImageUrl(rs.getString(4));
                        dto.setRating(rs.getDouble(5));
                        beers.add(dto);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beers;
    }
}
