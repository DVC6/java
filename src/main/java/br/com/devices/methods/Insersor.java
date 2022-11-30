package br.com.devices.methods;

import br.com.devices.db.Conexao;
import br.com.devices.db.Connection;
import br.com.devices.entities.LeituraEntity;
import org.springframework.jdbc.core.JdbcTemplate;

public class Insersor {

    // CONEXÃO BANCO DE DADOS
    Connection config = new Connection("Azure");
    JdbcTemplate template = new JdbcTemplate(config.getDataSource());

    public Insersor() {
    }
    
    private Boolean ativarSQL = true;

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

    Conexao conexao = new Conexao();
    JdbcTemplate connectionMySql = conexao.getConnectionMySQL();

    public void adicionarSQL(LeituraEntity registro) {

        String insertStatement = "INSERT INTO leitura (data_hora_atual, consumo, fkcomponente, consumo_qtd) VALUES (?, ?, ?, ?)";

        try {
            connectionMySql.update(insertStatement,
                    registro.getDataHoraAtual().toString(),
                    registro.getConsumo(),
                    registro.getFkComponente(),
                    registro.getConsumoQtd());
        } catch (RuntimeException e) {
            System.out.println(e);
        }
    }
}
