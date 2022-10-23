
package br.com.devices.methods;

import java.io.FileNotFoundException;
import java.io.IOException;

public class TesteVinculo {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        Vinculo vinculo = new Vinculo();
        vinculo.Vincular("123456789010101112", "Machine-01-test", "Recepcao 1");
    }
}
