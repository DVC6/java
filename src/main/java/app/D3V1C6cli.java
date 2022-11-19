package app;

import br.com.devices.methods.Coletor;
import br.com.devices.methods.Insersor;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import org.json.JSONObject;

public class D3V1C6cli {

    Integer contadorRAM = 0;
    Boolean contadorRAMRodando = false;
    Integer contadorCPU = 0;
    Boolean contadorCPURodando = false;
    Integer contadorDisco = 0;
    Boolean contadorDiscoRodando = false;

    Logger logger = Logger.getLogger("D3V1C6cli");
    FileHandler fh;

    public void rodarDevice(Insersor insersor) {

        try {
            fh = new FileHandler("../D3V1C6cliLog.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (SecurityException | IOException e) {
            logger.severe("Erro ao inicializar arquivo de log.");
        }

        JSONObject json = new JSONObject();

        Coletor coletor = new Coletor();

        int delay = 1000;   // tempo de espera antes da 1ª execução da tarefa.
        int interval = 5000;  // intervalo no qual a tarefa será executada.
        int intervalDisco = 60000;  // intervalo no qual a tarefa será executada.
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                insersor.inserirRegistros(coletor.coletarRAM());
                insersor.inserirRegistros(coletor.coletarCPU());

                // Exibição CLI
                System.out.println(coletor);

                Double conversaoRAM = Double.valueOf(coletor.getAtualRAM());
                if (conversaoRAM > 90.0) {
                    contadorRAMRodando = true;
                } else {
                    contadorRAMRodando = false;
                }
                Double conversaoCPU = Double.valueOf(coletor.getAtualCPU());
                if (conversaoCPU > 90.0) {
                    contadorCPURodando = true;
                } else {
                    contadorCPURodando = false;
                }

                try {
                    if (contadorRAMRodando) {
                        contadorRAM++;
                    } else {
                        contadorRAM = 0;
                    }
                    if (contadorCPURodando) {
                        contadorCPU++;
                    } else {
                        contadorCPU = 0;
                    }
                    if (contadorDiscoRodando) {
                        contadorDisco++;
                    } else {
                        contadorDisco = 0;
                    }

                    String nomeServidor = InetAddress.getLocalHost().getHostName();

                    if (contadorRAM == 36) {
                        json.put("text", "Alerta no servidor: " + nomeServidor
                                + "\nAlerta de RAM :heavy_exclamation_mark: // Possível Atividade Suspeita :heavy_exclamation_mark:");
//                        Slack.enviarMensagem(json);
                    }
                    if (contadorCPU == 36) {
                        json.put("text", "Alerta no servidor: " + nomeServidor
                                + "\nAlerta de CPU :heavy_exclamation_mark: // Possível Atividade Suspeita :heavy_exclamation_mark:");
//                        Slack.enviarMensagem(json);
                    }
                    if (contadorDisco == 36) {
                        json.put("text", "Alerta no servidor: " + nomeServidor
                                + "\nAlerta de Disco :heavy_exclamation_mark: // Uso de volume acima de 90% da capacidade total :heavy_exclamation_mark:");
//                        Slack.enviarMensagem(json);
                    }
                } catch (IOException ex) {
                    logger.severe("Erro ao verificar contadores.");
                }
//                } catch (InterruptedException ex) {
//                    logger.severe("Erro ao verificar contadores.");
//                }

            }
        }, delay, interval);

        Timer timerDisco = new Timer();

        timerDisco.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                insersor.inserirRegistros(coletor.coletarDISCO());
                Double conversaoDisco = Double.valueOf(coletor.getAtualDisco());
                if (conversaoDisco > 90.0) {
                    contadorDiscoRodando = true;
                } else {
                    contadorDiscoRodando = false;
                }
            }
        }, delay, intervalDisco);
    }

}
