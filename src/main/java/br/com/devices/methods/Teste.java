
package br.com.devices.methods;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.DiscoGrupo;
import com.github.britooo.looca.api.group.processador.Processador;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Teste {
    public static void main(String[] args) {
        
        System.out.println(Formatter.getTotalDiscos());
        System.out.println(Formatter.getTotalCpu());
        System.out.println(Formatter.getTotalMemoria());
        
        Looca looca = new Looca();
        Processador proc = looca.getProcessador();
        DiscoGrupo dg = looca.getGrupoDeDiscos();
        
        System.out.println(dg.getDiscos().get(0).getModelo());
    }
}
