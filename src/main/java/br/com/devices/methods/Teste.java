package br.com.devices.methods;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.DiscoGrupo;
import com.github.britooo.looca.api.group.processador.Processador;

import java.net.SocketException;
import java.net.UnknownHostException;

public class Teste {

    public static void main(String[] args) throws SocketException, UnknownHostException {

        Looca looca = new Looca();
        Processador p = looca.getProcessador();

        System.out.println(p.getIdentificador());

        System.out.println(p.getId());

    }
}
