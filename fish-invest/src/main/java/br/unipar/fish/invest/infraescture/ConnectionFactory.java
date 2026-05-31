package br.unipar.fish.invest.infraescture;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectionFactory {
    
    public Connection getConnection() throws SQLException{
        return DriverManager.getConnection(
            "jdbc:postgresql://localhost:6670/FishInvest",
            "postgres",
            "6670*");
    }
}
