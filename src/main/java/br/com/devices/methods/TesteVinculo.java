
package br.com.devices.methods;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.DiscoGrupo;
import com.github.britooo.looca.api.group.memoria.Memoria;
import com.github.britooo.looca.api.group.processador.Processador;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TesteVinculo {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        Looca looca = new Looca();
        Memoria memoria = looca.getMemoria();
        Processador processador = looca.getProcessador();
        DiscoGrupo discosGroup = looca.getGrupoDeDiscos();
        
        Formatter fm = new Formatter();
        
        String sqlInsert = String.format("INSERT INTO [dbo].[componente]"
                + "(total_componente, fktipocomponente, fktotem, modelo) VALUES"
                + "('%d', '%d', '%s', '%s'),"
                + "('%.2f', '%d', '%s', '%s'),", 
                processador.getNumeroCpusFisicas(), 1, 1, processador.getNome(),
                fm.format(memoria.getTotal()), 2, 1, "Memoria Ram");

        for(int i = 0; i < discosGroup.getDiscos().size(); i++) {
            sqlInsert += String.format("('%.2f', '%d', '%s', '%s')",
                    fm.format(discosGroup.getDiscos().get(i).getTamanho()),
                    3, 1,
                    discosGroup.getDiscos().get(i).getModelo());
        }
        
        System.out.println(sqlInsert);
    }
}
