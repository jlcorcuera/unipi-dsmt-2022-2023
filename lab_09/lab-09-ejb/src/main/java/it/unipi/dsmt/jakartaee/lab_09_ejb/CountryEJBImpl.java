package it.unipi.dsmt.jakartaee.lab_09_ejb;

import it.unipi.dsmt.jakartaee.lab_09_ejb_interfaces.dto.CountryDTO;
import it.unipi.dsmt.jakartaee.lab_09_ejb_interfaces.interfaces.CountryEJB;
import jakarta.ejb.Stateless;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import jakarta.annotation.Resource;
import javax.sql.DataSource;

@Stateless
public class CountryEJBImpl implements CountryEJB {

    @Resource(lookup = "jdbc/WorldPool")
    private DataSource dataSource;

    @Override
    public List<CountryDTO> list() {
        Connection connection = null;
        ResultSet rs = null;
        List<CountryDTO> result = new ArrayList<>();
        try{
            connection = dataSource.getConnection();
            String sql = "select name from country";
            rs = connection.prepareStatement(sql).executeQuery();
            while (rs.next()){
                CountryDTO object = new CountryDTO();
                object.setName(rs.getString(1));
                result.add(object);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                }
            }
        }
        return result;
    }
}
