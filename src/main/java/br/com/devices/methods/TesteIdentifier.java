
package br.com.devices.methods;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.processador.Processador;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class TesteIdentifier {
    public static void main(String[] args) throws UnknownHostException, SocketException {
        InetAddress localHost = InetAddress.getLocalHost();
        NetworkInterface ni = NetworkInterface.getByInetAddress(localHost);
        byte[] hardwareAddress = ni.getHardwareAddress();

        Looca looca = new Looca();
        Processador processador = looca.getProcessador();

        String[] hexadecimal = new String[hardwareAddress.length];
        for (int i = 0; i < hardwareAddress.length; i++) {
            hexadecimal[i] = String.format("%02X", hardwareAddress[i]);
        }

        String macAddress = String.join("-", hexadecimal);

        System.out.println(macAddress + "-" + processador.getId());
    }
}
