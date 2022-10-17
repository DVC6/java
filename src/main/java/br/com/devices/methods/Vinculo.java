
package br.com.devices.methods;

import br.com.devices.db.Conexao;
import br.com.devices.entities.HospitalEntity;
import java.util.List;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class Vinculo {
    public String Vincular(String cnpj, String id, String local) {
        Conexao conexao = new Conexao();
        JdbcTemplate connection = conexao.getConnection();
        List<HospitalEntity> hospital = 
                connection.query("SELECT id_hospital FROM [dbo].[hospital] WHERE cnpj = ?", 
                new BeanPropertyRowMapper<>(HospitalEntity.class), cnpj);
        try {
            String fkHospital = hospital.get(0).toString();
            connection.execute(String.format("INSERT INTO [dbo].[totem] "
                    + "(nome_maquina, localizacao, fkhospital) VALUES('%s', '%s', '%s')"
                    , id, local, fkHospital));
            return "Succeeded";
        } catch(RuntimeException e) {
            return "Failed";
        }
    }
    
    public Boolean isCamposValidos(String chave, String id, String local) {
        return !(chave.equals("") || id.equals("") || local.equals(""));
    }
}
