
package br.com.devices.db;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class Conexao {

    private JdbcTemplate connectionAzure;
    private JdbcTemplate connectionMySQL;

    public Conexao() {

        // Conexão SQL Server Azure

        BasicDataSource dataSourceAzure = new BasicDataSource();

        dataSourceAzure.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSourceAzure.setUrl("jdbc:sqlserver://projeto-device.database.windows.net:1433;database=bd-device;user=device-admin@projeto-device;password=d3v1c6-password;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;");
        dataSourceAzure.setUsername("device-admin");
        dataSourceAzure.setPassword("d3v1c6-password");

        connectionAzure = new JdbcTemplate(dataSourceAzure);

        // Conexão MYSQL para Docker

        BasicDataSource dataSourceMySQL = new BasicDataSource();

        dataSourceMySQL.setDriverClassName("com.mysql.cj.jdbc.Driver");
//        Docker
        dataSourceMySQL.setUrl("jdbc:mysql://db:3306/device?autoReconnect=true&useSSL=false&useTimezone=true&serverTimezone=UTC");
        dataSourceMySQL.setUsername("device_user");
//        //Local
//        dataSourceMySQL.setUrl("jdbc:mysql://localhost:3306/devices_local?autoReconnect=true&useSSL=false&useTimezone=true&serverTimezone=UTC");
//        dataSourceMySQL.setUsername("device");
        dataSourceMySQL.setPassword("urubu100");

        connectionMySQL = new JdbcTemplate(dataSourceMySQL);

    }

    public JdbcTemplate getConnectionAzure() {
        return connectionAzure;
    }
    public JdbcTemplate getConnectionMySQL() {
        return connectionMySQL;
    }
}
