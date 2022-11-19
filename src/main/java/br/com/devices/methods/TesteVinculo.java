
package br.com.devices.methods;

import java.io.FileNotFoundException;
import java.io.IOException;

public class TesteVinculo {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        Vinculo vinculo = new Vinculo();
        vinculo.Vincular("12312312312312", "Machine-01-test", "Recepcao 1");
        
        System.out.println(vinculo.isAlreadyVinculado()); 
    }
}
