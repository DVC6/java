package br.com.devices.methods;

import br.com.devices.db.Conexao;
import br.com.devices.entities.HospitalEntity;
import br.com.devices.entities.TotemEntity;
import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.processador.Processador;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Locale;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class Vinculo {

    public Boolean Vincular(String cnpj, String nomeMaquina, String local) throws UnknownHostException, SocketException{

        Locale.setDefault(Locale.US);

        Looca looca = new Looca();
        Processador processador = looca.getProcessador();

        Conexao conexao = new Conexao();
        JdbcTemplate connectionAzure = conexao.getConnectionAzure();
        JdbcTemplate connectionMySql = conexao.getConnectionMySQL();

        
        List<HospitalEntity> hospital
                = connectionAzure.query("SELECT * FROM [dbo].[hospital] WHERE cnpj = ?",
                        new BeanPropertyRowMapper<>(HospitalEntity.class), cnpj);
        
        try {
            String fkHospital = hospital.get(0).getIdHospital().toString();

            //Azure
            String insertSQLTotem = String.format("INSERT INTO totem "
                    + "(nome_maquina, localizacao, fkhospital, identificador_unico)"
                    + " VALUES('%s', '%s', '%s', '%s')",
                    nomeMaquina, local, fkHospital, getUniqueIdentifier());

            connectionAzure.execute(insertSQLTotem);

            List<TotemEntity> totemAzure
                    = connectionAzure.query("SELECT TOP 1 id_totem FROM totem ORDER BY id_totem DESC",
                    new BeanPropertyRowMapper<>(TotemEntity.class));

            Integer idTotemAzure = totemAzure.get(0).getIdTotem();

            String insertSQLComponentsAzure = String.format("INSERT INTO componente"
                    + "(total_componente, fktipocomponente, fktotem, modelo) VALUES"
                    + "(%.2f, %d, %d, '%s')"
                    + ",(%.2f, %d, %d, '%s')"
                    + ",(%.2f, %d, %d, '%s')",
                    Formatter.getTotalCpu().doubleValue(), 1, idTotemAzure, processador.getNome(),
                    Formatter.getTotalMemoria(), 2, idTotemAzure, "RAM",
                    Formatter.getTotalDiscos(), 3, idTotemAzure, "Disco");

            connectionAzure.execute(insertSQLComponentsAzure);

            //MySQL
            connectionMySql.execute(String.format("INSERT INTO totem " +
                    "(identificador_unico) VALUES" +
                    "('%s');", getUniqueIdentifier()));

            List<TotemEntity> totemMySql
                    = connectionMySql.query("SELECT id_totem FROM totem ORDER BY id_totem DESC LIMIT 1;",
                    new BeanPropertyRowMapper<>(TotemEntity.class));

            Integer idTotemMySql = totemMySql.get(0).getIdTotem();

            String insertSQLComponentsMySql = String.format("INSERT INTO componente"
                            + "(total_componente, fktipocomponente, fktotem, modelo) VALUES"
                            + "(%.2f, %d, %d, '%s')"
                            + ",(%.2f, %d, %d, '%s')"
                            + ",(%.2f, %d, %d, '%s');",
                    Formatter.getTotalCpu().doubleValue(), 1, idTotemMySql, processador.getNome(),
                    Formatter.getTotalMemoria(), 2, idTotemMySql, "RAM",
                    Formatter.getTotalDiscos(), 3, idTotemMySql, "Disco");

            connectionMySql.execute(insertSQLComponentsMySql);

            System.out.println("Succeeded");
            
            return true;
        } catch (RuntimeException e) {
            System.out.println("Failed");
            return false;
        }
    }

    public Boolean isCamposValidos(String chave, String id, String local) {
        return !(chave.equals("") || id.equals("") || local.equals(""));
    }

    public Boolean isAlreadyVinculado() throws UnknownHostException, SocketException {
        Conexao conexao = new Conexao();
        JdbcTemplate connection = conexao.getConnectionAzure();
        
        List<TotemEntity> totem
                = connection.query("SELECT * FROM [dbo].[totem] WHERE identificador_unico = ?",
                        new BeanPropertyRowMapper<>(TotemEntity.class), getUniqueIdentifier());  
        
        if (totem.size() != 0) return true;
        else return false;
    }
    
    public String getUniqueIdentifier() throws UnknownHostException, SocketException {
        Looca looca = new Looca();
        Processador processador = looca.getProcessador();
        InetAddress localHost = InetAddress.getLocalHost();
        NetworkInterface ni = NetworkInterface.getByInetAddress(localHost);
        byte[] hardwareAddress = ni.getHardwareAddress();

        String[] hexadecimal = new String[hardwareAddress.length];
        for (int i = 0; i < hardwareAddress.length; i++) {
            hexadecimal[i] = String.format("%02X", hardwareAddress[i]);
        }

        return String.format(String.join("-", hexadecimal) + processador.getId());
    }
}
