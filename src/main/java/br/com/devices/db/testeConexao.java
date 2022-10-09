/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.devices.db;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author gabri
 */
public class testeConexao {

    public static void main(String[] args) {
        Conexao conexao = new Conexao();
        JdbcTemplate conn = conexao.getConnection();

        String insertTotem = "INSERT INTO dbo.totem VALUES ('SP', 'MQ-2', 'UP', 1);";
        conn.update(insertTotem);

        List totem = conn.queryForList("SELECT * FROM dbo.totem;");

        System.out.println(totem);
    }
}
