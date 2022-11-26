package br.com.devices.methods;

import br.com.devices.db.Connection;
import br.com.devices.entities.LeituraEntity;
import org.springframework.jdbc.core.JdbcTemplate;

public class Insersor {

    // CONEXÃO BANCO DE DADOS
    Connection config = new Connection("Azure");
    JdbcTemplate template = new JdbcTemplate(config.getDataSource());

    public Insersor() {
    }
    
    private Boolean ativarSQL = false;

    public Boolean getAtivarSQL() {
        return ativarSQL;
    }

    public void setAtivarSQL(Boolean ativarSQL) {
        this.ativarSQL = ativarSQL;
    }

    public void inserirRegistros(LeituraEntity registro) {
        String insertStatement = "INSERT INTO leitura VALUES (?, ?, ?, ?)";
        template.update(insertStatement,
                registro.getDataHoraAtual(),
                registro.getConsumo(),
                registro.getFkComponente(),
                registro.getConsumoQtd()
        );
        
        if(ativarSQL){
            adicionarSQL(registro);
        }
    }

    Connection configSQL = new Connection("MySQL");
    JdbcTemplate templateSQL = new JdbcTemplate(configSQL.getDataSource());

    public void adicionarSQL(LeituraEntity registro) {

        String insertStatement = "INSERT INTO leitura VALUES (null, ?, ?, ?, ?)";
        templateSQL.update(insertStatement,
                registro.getDataHoraAtual(),
                registro.getConsumo(),
                registro.getFkComponente(),
                registro.getConsumoQtd());
    }
}
