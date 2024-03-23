package example;


import client.ClientService;
import db.init.InitDb;

import java.sql.Connection;
import java.sql.DriverManager;

public class Main {

    public static void main(String[] args) throws Exception {
        Connection connection;
        ClientService clientService;
        String connectionUrl = "jdbc:h2:./test";
        new InitDb().initDb(connectionUrl);

        connection = DriverManager.getConnection(connectionUrl);

        clientService = new ClientService(connection);
        long id = clientService.create("name");

        String result = clientService.getById(3);

        System.out.println(result);

        clientService.setName(3, "another name");
        System.out.println(clientService.getById(3));

        System.out.println(clientService.listAll());

        clientService.deleteById(1);
        System.out.println(clientService.listAll());





    }
}