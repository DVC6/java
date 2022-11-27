package app;

import br.com.devices.db.Connection;
import br.com.devices.entities.HospitalEntity;
import br.com.devices.entities.TotemEntity;
import br.com.devices.frames.FrameLogin;
import br.com.devices.methods.Insersor;
import br.com.devices.methods.Vinculo;
import com.github.britooo.looca.api.core.Looca;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class App {

    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner leitorNumerico = new Scanner(System.in);
        Scanner leitorTexto = new Scanner(System.in);
        Connection config = new Connection("Azure");
        JdbcTemplate connection = new JdbcTemplate(config.getDataSource());
        Looca looca = new Looca();
        Vinculo vinculo = new Vinculo();
        Integer idTotem = null;

        Logger logger = Logger.getLogger("Main");
        try {
            FileHandler fh = new FileHandler("../MainLog.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (SecurityException | IOException e) {
            logger.severe("Erro ao inicializar arquivo de log.");
        }

        System.out.println("\n"
                + " _____       _____   __          __   ___     ____       __\n"
                + "|  __ \\     /____ \\  \\ \\        / /  /   |   / ___\\     / /\n"
                + "| |  \\ \\      ___ /   \\ \\      / /  /_/| |  / /        / /__\n"
                + "| |   \\ \\    |___ \\    \\ \\    / /      | | | |        /  __ \\\n"
                + "| |   / /  __    \\ \\    \\ \\__/ /       | | | |       / /   \\ \\\n"
                + "| |__/ /   \\ \\___/ /     \\    /        | |  \\ \\____  \\ \\___/ /\n"
                + "|_____/     \\_____/       \\__/         |_|   \\____/   \\_____/ \n\n"
                + "Version: 1.1.5");

        Boolean menuLoop = true;
        while (menuLoop) {

            System.out.println("\nEscolha o modo de execucao:"
                    + "\n   1 - Padrao (Com GUI)"
                    + "\n   2 - Servidor (Sem GUI)"
                    + "\n   3 - Sair");

            Integer modoSelecionado = leitorNumerico.nextInt();
            Insersor insersor = new Insersor();

            switch (modoSelecionado) {
                case 1:
                    if(!vinculo.autoLogin()) {
                        new FrameLogin().setVisible(true);
                        menuLoop = false;
                    } 
                    break;
////////////////////////////////////////////////////////////////////////////////                    
                case 2:
//                    insersor.setAtivarSQL(true);

                    System.out.println("Validando Servidor...");
                    String nomeMaquina = null;
                    String localizacao = null;
                    String nomeHospital = null;
                    Integer idHospital = null;
                    String cnpj = null;
                    String idUnico = vinculo.getUniqueIdentifier();

                    try {
                        System.out.println("Totem: " + idUnico);
                        if (vinculo.isAlreadyVinculado() != null) {
                            System.out.println("\nTotem ja cadastrado..."
                                    + "\nRedirecionando...");
                            idTotem = vinculo.isAlreadyVinculado();
                        } else {
                            Boolean empresaConfirmada = false;
                            while (!empresaConfirmada) {
                                nomeHospital = null;
                                idHospital = null;
                                while (nomeHospital == null) {

                                    System.out.println("\nTotem ainda nao cadastrado!!!"
                                            + "\nDigite o CNPJ do seu Hospital:");
                                    cnpj = leitorTexto.nextLine();

                                    try {
                                        List<HospitalEntity> buscaHospital = connection.query("SELECT * FROM hospital WHERE cnpj = ?",
                                                new BeanPropertyRowMapper<>(HospitalEntity.class), cnpj);
                                        
                                        for (HospitalEntity hospital : buscaHospital) {
                                            nomeHospital = hospital.getNomeFantasia();
                                            idHospital = hospital.getIdHospital();
                                        }
                                        
                                        if (buscaHospital.isEmpty()) {
                                            System.out.println("\nCNPJ nao encontrado!!!");
                                        }
                                    } catch (Exception erro) {
                                        logger.severe(String.format("\nErro ao buscar chave de acesso para cadastro do totem: %s", erro));
                                    }
                                }
                                System.out.println("\nSeu hospital e: " + nomeHospital + "? [Y/N]");
                                String confirmacaoEmpresa = leitorTexto.nextLine();
                                if (confirmacaoEmpresa.equalsIgnoreCase("y")) {

                                    while (nomeMaquina == null || nomeMaquina.isBlank()) {   
                                        System.out.println("\nInsira um nome/identificador para este totem:");
                                        nomeMaquina = leitorTexto.nextLine();
                                    }
                                    
                                    while (localizacao == null || localizacao.isBlank()) { 
                                        System.out.println("\nInsira a localizacao do seu totem:");
                                        localizacao = leitorTexto.nextLine();
                                    }
                                    
                                    idTotem = vinculo.Vincular(cnpj, nomeMaquina, localizacao);
                                    
                                    if (idTotem != null){
                                        empresaConfirmada = true;
                                        System.out.println("\nTotem vinculado com sucesso!");
                                    } else {
                                        System.out.println("\nHouve um problema ao vincular este totem.");
                                    }
                                } else {
                                    System.out.println("\nChave digitada não corresponde ao seu Hospital...");
                                }
                            }
                        }

                    } catch (UnknownHostException ex) {
                        logger.severe(String.format("\nErro ao buscar dados do totem: %s", ex));
                    }

                    //////////Rodar D3V1C6cli
                    D3V1C6cli d3v1c6cli = new D3V1C6cli();
                    d3v1c6cli.rodarDevice(insersor, idTotem);

                    menuLoop = false;
                    break;
////////////////////////////////////////////////////////////////////////////////                    
                case 3:
                    System.out.println("\nObrigado por utilizar nosso produto");
                    System.exit(0);
                    break;
////////////////////////////////////////////////////////////////////////////////                    
                default:
                    System.out.println("\nOpção invalida! Digite o numero da opção deseja, de acordo com o menu:");
                    break;
            }
        }

    }

}
