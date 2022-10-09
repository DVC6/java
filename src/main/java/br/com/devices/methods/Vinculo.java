
package br.com.devices.methods;

import br.com.devices.db.Conexao;
import br.com.devices.entities.HospitalEntity;
import java.util.List;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class Vinculo {
    public String Vincular(String chave, String id, String local) {
        Conexao conexao = new Conexao();
        JdbcTemplate connection = conexao.getConnection();
        List<HospitalEntity> hospital = 
                connection.query("SELECT id_hospital FROM [dbo].[hospital] WHERE chave_acesso = ?", 
                new BeanPropertyRowMapper<>(HospitalEntity.class), chave);
        try {
            String fkHospital = hospital.get(0).toString();
            System.out.println(chave);
            System.out.println(id);
            System.out.println(local);
            connection.execute(String.format("INSERT INTO [dbo].[totem] "
                    + "(nome_maquina, localizacao, fkhospital) VALUES('%s', '%s', '%s')"
                    , id, local, fkHospital));
            return "Succeeded";
        } catch(RuntimeException e) {
            System.out.println(chave);
            System.out.println(id);
            System.out.println(local);
            return "Failed";
        }
    }
    
    public Boolean isCamposValidos(String chave, String id, String local) {
        return !(chave.equals("") || id.equals("") || local.equals(""));
    }
}
