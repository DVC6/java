package br.com.devices.db;

import org.apache.commons.dbcp2.BasicDataSource;

public class Connection {

    private BasicDataSource dataSource;

    public Connection(String tipoBanco) {
        if (tipoBanco.equals("H2")) {
            dataSource = new BasicDataSource();
            dataSource.setDriverClassName("org.h2.Driver");
            dataSource.setUrl("jdbc:h2:file:./meu_banco");
            dataSource.setUsername("sa");
            dataSource.setPassword("");
        } else if (tipoBanco.equals("MySQL")) {
            dataSource = new BasicDataSource();
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            //Docker
            dataSource.setUrl("jdbc:mysql://db:3306/device?autoReconnect=true&useSSL=false&useTimezone=true&serverTimezone=UTC");
            dataSource.setUsername("root");
            //Local
//            dataSource.setUrl("jdbc:mysql://localhost:3306/devices_local?autoReconnect=true&useSSL=false&useTimezone=true&serverTimezone=UTC");
//            dataSource.setUsername("device");
            dataSource.setPassword("urubu100");
        } else if (tipoBanco.equals("Azure")) {
            dataSource = new BasicDataSource();
            dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            dataSource.setUrl("jdbc:sqlserver://projeto-device.database.windows.net:1433;database=bd-device;user=device-admin@projeto-device;password=d3v1c6-password;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;");
            dataSource.setUsername("device-admin");
            dataSource.setPassword("d3v1c6-password");
        }
    }

    public BasicDataSource getDataSource() {
        return dataSource;
    }

}
