package br.com.devices.methods;

import br.com.devices.db.Conexao;
import br.com.devices.entities.HospitalEntity;
import br.com.devices.entities.TotemEntity;
import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.DiscoGrupo;
import com.github.britooo.looca.api.group.memoria.Memoria;
import com.github.britooo.looca.api.group.processador.Processador;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class Vinculo {
    
    Conexao conexao = new Conexao();
    JdbcTemplate connection = conexao.getConnection();

    public String Vincular(String cnpj, String id, String local) throws FileNotFoundException, IOException {
        
        Looca looca = new Looca();
        Memoria memoria = looca.getMemoria();
        Processador processador = looca.getProcessador();
        DiscoGrupo discosGroup = looca.getGrupoDeDiscos();
        
        Formatter fm = new Formatter();
        
        List<HospitalEntity> hospital
                = connection.query("SELECT * FROM [dbo].[hospital] WHERE cnpj = ?",
                        new BeanPropertyRowMapper<>(HospitalEntity.class), cnpj);
        
        try {
            
            String fkHospital = hospital.get(0).getIdHospital().toString();
            connection.execute(String.format("INSERT INTO [dbo].[totem] "
                    + "(nome_maquina, localizacao, fkhospital, identificador_unico) "
                    + "VALUES('%s', '%s', '%s', '%s')",
                    id, local, fkHospital, getUniqueIdentifier()));

            List<Map<String, Object>> totem
                    = (connection.queryForList("SELECT TOP 1 id_totem "
                            + "FROM [dbo].[totem] ORDER BY id_totem DESC"));
            String idTotem = totem.get(0).get("id_totem").toString();
            
            String sqlInsert = String.format("INSERT INTO [dbo].[componente]"
                    + "(total_componente, fktipocomponente, fktotem, modelo) VALUES"
                    + "('%d', '%d', '%s', '%s')"
                    + ",('%.2f', '%d', '%s', '%s')", 
                    processador.getNumeroCpusFisicas(), 1, idTotem, processador.getNome(),
                    fm.format(memoria.getTotal()), 2, idTotem, "Memoria Ram");
            
            for(int i = 0; i < discosGroup.getDiscos().size(); i++) {
                sqlInsert += String.format(",('%.2f', '%d', '%s', '%s')",
                        fm.format(discosGroup.getDiscos().get(i).getTamanho()),
                        3, idTotem,
                        discosGroup.getDiscos().get(i).getModelo());
            }
            
            connection.execute(sqlInsert);

            return "Succeeded";
        } catch (RuntimeException e) {
            return "Failed";
        }
    }

    public Boolean isCamposValidos(String chave, String id, String local) {
        return !(chave.equals("") || id.equals("") || local.equals(""));
    }

    public Boolean isAlreadyVinculado() throws UnknownHostException, SocketException{
        
        List<TotemEntity> totens = connection.query("SELECT * FROM [dbo].[componente] "
                + "WHERE identificador_unico = %s",
                new BeanPropertyRowMapper<>(TotemEntity.class)
                , getUniqueIdentifier());
        
        Integer idTotem = totens.get(0).getIdTotem();
        
        if (idTotem != null) return true;
        return false;
    }
    
    private String getUniqueIdentifier() throws UnknownHostException, SocketException {
        
            InetAddress localHost = InetAddress.getLocalHost();
            NetworkInterface ni = NetworkInterface.getByInetAddress(localHost);
            byte[] hardwareAddress = ni.getHardwareAddress();
            
            Looca looca = new Looca();
            Processador processador = looca.getProcessador();
            
            String[] hexadecimal = new String[hardwareAddress.length];
            for (int i = 0; i < hardwareAddress.length; i++) {
                hexadecimal[i] = String.format("%02X", hardwareAddress[i]);
            }
            
            String macAddress = String.join("-", hexadecimal);
            
            return macAddress + "-" + processador.getId();
    }
}
