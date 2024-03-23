package client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ClientService {

    private PreparedStatement createSt;
    private PreparedStatement getByIdSt;
    private PreparedStatement selectMaxIdSt;
    private PreparedStatement getAllSt;
    private PreparedStatement updateSt;
    private PreparedStatement deleteByIdSt;


    public ClientService(Connection connection) throws SQLException {
        createSt = connection.prepareStatement(
                "INSERT INTO client (name) VALUES (?)"
        );

        getByIdSt = connection.prepareStatement(
                "SELECT name FROM client WHERE id = ?"
        );

        selectMaxIdSt = connection.prepareStatement(
                "SELECT max(id) AS maxId FROM client"
        );

        getAllSt = connection.prepareStatement(
                "SELECT id, name FROM client"
        );

        updateSt = connection.prepareStatement(
                "UPDATE client SET name = ? WHERE id = ?"
        );

        deleteByIdSt = connection.prepareStatement(
                "DELETE FROM client WHERE id = ?"
        );


    }

    public long create(String name) throws Exception {
        if (checkName(name)) {
            throw new Exception("invalid name");
        }

        createSt.setString(1, name);
        createSt.executeUpdate();

        long id;
        try (ResultSet rs = selectMaxIdSt.executeQuery()) {
            rs.next();
            id = rs.getLong("maxId");
        }


        return id;

    }

    public String getById(long id) throws Exception {
        if (checkId(id)) {
            throw new Exception("invalid id");
        }

        getByIdSt.setLong(1, id);

        try (ResultSet rs = getByIdSt.executeQuery()) {
            if (!rs.next()) {
                return null;
            }
            Client result = new Client();
            result.setId(id);
            result.setName(rs.getString("name"));

            return result.getName();

        }
    }

    public void setName(long id, String name) throws Exception {
        if (checkId(id)) {
            throw new Exception("invalid id");
        }
        if (checkName(name)) {
            throw new Exception("invalid name");
        }
        updateSt.setString(1, name);
        updateSt.setLong(2, id);
        updateSt.executeUpdate();

    }

    public void deleteById(long id) throws Exception {
        if (checkId(id)) {
            throw new Exception("invalid id");
        }
        deleteByIdSt.setLong(1, id);
        deleteByIdSt.executeUpdate();
    }


    public List<Client> listAll() throws SQLException {

        try (ResultSet rs = getAllSt.executeQuery()) {
            List<Client> result = new ArrayList<>();

            while (rs.next()) {
                Client client = new Client();
                client.setId(rs.getLong("id"));
                client.setName(rs.getString("name"));

                result.add(client);
            }
            return result;

        }
    }

    public boolean checkName(String name) {
        return name.length() > 20 || name.length() < 2;
    }

    public boolean checkId(long id) {
        long maxIdInTable = 0;
        try (ResultSet rs = selectMaxIdSt.executeQuery()) {
            rs.next();
            maxIdInTable = rs.getLong("maxId");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id <= 0 || id > maxIdInTable;
    }

}
