package br.com.devices.methods;

import java.net.SocketException;
import java.net.UnknownHostException;

public class Teste {

    public static void main(String[] args) {

        try {
            System.out.println(Vinculo.getNomeTotem());
        } catch (RuntimeException e) {
            System.out.println(e);
        } catch (SocketException e) {
            System.out.println(e);
        } catch (UnknownHostException e) {
            System.out.println(e);
        }
    }
}
