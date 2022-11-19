
package br.com.devices.methods;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.DiscoGrupo;
import com.github.britooo.looca.api.group.memoria.Memoria;
import com.github.britooo.looca.api.group.processador.Processador;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

public class Formatter {
    
    public static Integer getTotalCpu() {
        Looca looca = new Looca();
        Processador processador = looca.getProcessador();
        return processador.getNumeroCpusFisicas();
    }
    
    public static Double getTotalMemoria() {
        Looca looca = new Looca();
        Memoria memoria = looca.getMemoria();
        return new BigDecimal(
            memoria.getTotal().doubleValue() / 1073741824
        ).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
    }
    
    public static Double getTotalDiscos() {
        Looca looca = new Looca();
        DiscoGrupo discos = looca.getGrupoDeDiscos();
        return discos.getDiscos().stream().mapToDouble(d ->
                new BigDecimal(d.getTamanho().doubleValue() / 1e+9)
                .setScale(2, RoundingMode.HALF_EVEN).doubleValue()
        ).sum();
    }
}
