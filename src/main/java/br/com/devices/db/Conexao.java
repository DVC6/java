/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.devices.db;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author gabri
 */
public class Conexao {

    private JdbcTemplate connectionAzure;
    private Statement connectionMySQL;
//    private JdbcTemplate connectionMySQL;

    public Conexao() {

        // Conexão SQL Server Azure

        BasicDataSource dataSourceAzure = new BasicDataSource();

        dataSourceAzure.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSourceAzure.setUrl("jdbc:sqlserver://projeto-device.database.windows.net:1433;database=bd-device;user=device-admin@projeto-device;password=d3v1c6-password;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;");
        dataSourceAzure.setUsername("device-admin");
        dataSourceAzure.setPassword("d3v1c6-password");

        connectionAzure = new JdbcTemplate(dataSourceAzure);

        // Conexão MYSQL para Docker

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/device?autoReconnect=true&useSSL=false&useTimezone=true&serverTimezone=UTC",
                    "root",
                    "urubu100");

            connectionMySQL = con.createStatement();

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }







//        BasicDataSource dataSourceMySQL = new BasicDataSource();
//
//        dataSourceMySQL.setDriverClassName("com.mysql.cj.jdbc.Driver");
//        dataSourceMySQL.setUrl("jdbc:mysql://localhost:3306/device?autoReconnect=true&useSSL=false&useTimezone=true&serverTimezone=UTC");
//        dataSourceMySQL.setUsername("root");
//        dataSourceMySQL.setPassword("urubu100");
//
//        connectionMySQL = new JdbcTemplate(dataSourceMySQL);


    }

    public JdbcTemplate getConnectionAzure() {
        return connectionAzure;
    }
    public Statement getConnectionMySQL() {
        return connectionMySQL;
    }
}
