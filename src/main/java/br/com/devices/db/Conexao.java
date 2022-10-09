/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.devices.db;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author gabri
 */
public class Conexao {
    
    private JdbcTemplate connection;
    
    public Conexao() {
        BasicDataSource datasource = new BasicDataSource();
        
        datasource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        datasource.setUrl("jdbc:sqlserver://projeto-device.database.windows.net/bd-device");
        datasource.setUsername("device-admin");
        datasource.setPassword("d3v1c6-password");
        
        connection = new JdbcTemplate(datasource);
    }
    
}
