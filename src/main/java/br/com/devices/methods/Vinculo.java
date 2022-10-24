package br.com.devices.methods;

import br.com.devices.db.Conexao;
import br.com.devices.entities.HospitalEntity;
import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.DiscoGrupo;
import com.github.britooo.looca.api.group.memoria.Memoria;
import com.github.britooo.looca.api.group.processador.Processador;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class Vinculo {

    public String Vincular(String cnpj, String id, String local) throws FileNotFoundException, IOException {
        Looca looca = new Looca();
        Memoria memoria = looca.getMemoria();
        Processador processador = looca.getProcessador();
        DiscoGrupo discosGroup = looca.getGrupoDeDiscos();
        Conexao conexao = new Conexao();
        JdbcTemplate connection = conexao.getConnection();
        List<HospitalEntity> hospital
                = connection.query("SELECT id_hospital FROM [dbo].[hospital] WHERE cnpj = ?",
                        new BeanPropertyRowMapper<>(HospitalEntity.class), cnpj);
        try {
            String fkHospital = hospital.get(0).toString();
            connection.execute(String.format("INSERT INTO [dbo].[totem] "
                    + "(nome_maquina, localizacao, fkhospital) VALUES('%s', '%s', '%s')",
                    id, local, fkHospital));

            List<Map<String, Object>> totem
                    = (connection.queryForList("SELECT TOP 1 id_totem FROM [dbo].[totem] ORDER BY id_totem DESC"));
            String idTotem = totem.get(0).get("id_totem").toString();
            
            connection.execute(String.format("INSERT INTO [dbo].[componente]"
                    + "(total_componente, fktipocomponente, fktotem) VALUES"
                    + "('%s', %d, %s),"
                    + "('%s', %d, %s),"
                    + "('%s', %d, %s)", processador.getNome(), 1, idTotem,
                    memoria.getTotal(), 2, idTotem,
                    discosGroup.getTamanhoTotal(), 3, idTotem));
            
            String filePath = String.format("C:\\Users\\%s\\AppData\\Roaming\\D3V1C6\\cache.dat", System.getProperty("user.name"));
            FileOutputStream arq = new FileOutputStream(filePath);
            DataOutputStream gravarArq = new DataOutputStream(arq);

            gravarArq.writeUTF(idTotem);
            arq.close();

            return "Succeeded";
        } catch (RuntimeException e) {
            return "Failed";
        }
    }

    public Boolean isCamposValidos(String chave, String id, String local) {
        return !(chave.equals("") || id.equals("") || local.equals(""));
    }

    public Boolean isAlreadyVinculado() throws FileNotFoundException, IOException {
        String filePath = String.format("C:\\Users\\%s\\AppData\\Roaming\\D3V1C6\\cache.dat", System.getProperty("user.name"));
        FileInputStream arqRead = new FileInputStream(filePath);
        DataInputStream lerArq = new DataInputStream(arqRead);
        
        String fkTotem = lerArq.readUTF();
        
        if (fkTotem != null){
            return true;
        }
        return false;
    }
}
