package app;

import br.com.devices.db.Connection;
import br.com.devices.entities.HospitalEntity;
import br.com.devices.entities.TotemEntity;
import br.com.devices.frames.FrameLogin;
import br.com.devices.methods.Insersor;
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
        JdbcTemplate template = new JdbcTemplate(config.getDataSource());
        Looca looca = new Looca();

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
                + "|_____/     \\_____/       \\__/         |_|   \\____/   \\_____/");

        Boolean menuLoop = true;
        while (menuLoop) {

            System.out.println("\nEscolha o modo de execução:"
                    + "\n   1 - Padrão (Com GUI)"
                    + "\n   2 - Servidor (Sem GUI)"
                    + "\n   3 - Completo (Com GUI + MySQL)"
                    + "\n   4 - Sair");

            Integer modoSelecionado = leitorNumerico.nextInt();
            Insersor insersor = new Insersor();

            switch (modoSelecionado) {
                case 1:
                    new FrameLogin().setVisible(true);
                    menuLoop = false;
                    break;
////////////////////////////////////////////////////////////////////////////////                    
                case 2:
//                    insersor.setAtivarSQL(true);

                    System.out.println("Validando Servidor...");
                    String nomeMaquina = null;
                    String nomeHospital = null;
                    String idHospital = null;

                    try {
                        nomeMaquina = InetAddress.getLocalHost().getHostName();
                        System.out.println("Totem: " + nomeMaquina);
                        List buscarNomeTotem = template.queryForList("SELECT * FROM totem "
                                + "WHERE nome_maquina = '" + nomeMaquina + "'");
                        if (!buscarNomeTotem.isEmpty()) {
                            System.out.println("Totem já cadastrado..."
                                    + "\nRedirecionando...");

                        } else {
                            Boolean empresaConfirmada = false;
                            while (!empresaConfirmada) {
                                nomeHospital = null;
                                idHospital = null;
                                while (nomeHospital == null) {

                                    System.out.println("\nTotem ainda não cadastrado!!!"
                                            + "\nDigite o CNPJ do seu Hospital:");
                                    String chaveDigitada = leitorTexto.nextLine();

                                    try {
                                        HospitalEntity hospitalTeste = new HospitalEntity();
                                        List<HospitalEntity> buscaHospital = template.query("SELECT * FROM empresa WHERE chave_acesso = ?",
                                                new BeanPropertyRowMapper<>(HospitalEntity.class), chaveDigitada);
                                        for (HospitalEntity hospital : buscaHospital) {
                                            nomeHospital = hospital.getNomeFantasia();
                                            idHospital = hospital.getIdHospital().toString();
                                        }
                                        if (buscaHospital.isEmpty()) {
                                            System.out.println("CNPJ não encontrado!!!");
                                        }
                                    } catch (Exception erro) {
                                        logger.severe(String.format("Erro ao buscar chave de acesso para cadastro do totem: %s", erro));
                                    }
                                }
                                System.out.println("\nSeu hospital é: " + nomeHospital + "? [Y/N]");
                                String confirmacaoEmpresa = leitorTexto.nextLine();
                                if (confirmacaoEmpresa.equalsIgnoreCase("y")) {

                                    String insertStatement = "INSERT INTO totem VALUES (?, ?, ?, ?)";
                                    template.update(insertStatement,
                                            nomeMaquina,
                                            "OK",
                                            idHospital,
                                            "Ala-A");
                                    TotemEntity servidorTeste = new TotemEntity();
                                    List<TotemEntity> buscaTotem = template.query("SELECT * FROM totem WHERE nome_maquina = ?",
                                            new BeanPropertyRowMapper<>(TotemEntity.class), nomeMaquina);
                                    for (TotemEntity totem : buscaTotem) {
                                        String insertStatement2 = "INSERT INTO componente VALUES (?, ?, ?, ?)";
                                        template.update(insertStatement2,
                                                "total?",
                                                1,
                                                totem.getIdTotem(),
                                                1);
                                        template.update(insertStatement2,
                                                "total?",
                                                2,
                                                totem.getIdTotem(),
                                                1);
                                        template.update(insertStatement2,
                                                "total?",
                                                3,
                                                totem.getIdTotem(),
                                                1);
                                    }
                                    empresaConfirmada = true;
                                } else {
                                    System.out.println("Chave digitada não corresponde ao seu Hospital...");
                                }
                            }
                        }

                    } catch (UnknownHostException ex) {
                        logger.severe(String.format("Erro ao buscar dados do totem: %s", ex));
                    }

                    Boolean usuarioLogado = false;
                    while (!usuarioLogado) {
                        System.out.println("Email:");
                        String usuarioDigitado = leitorTexto.nextLine();
                        System.out.println("Senha:");
                        String senhaDigitada = leitorTexto.nextLine();

                        System.out.println("Verificando Dados...");

                        try {
                            List buscarUsuario = template.queryForList(
                                    "SELECT * FROM funcionario "
                                    + "WHERE email = '" + usuarioDigitado
                                    + "' AND senha = '" + senhaDigitada + "'");
                            if (buscarUsuario.isEmpty()) {
                                System.out.println("Credenciais Incorretas");

                            } else {
                                System.out.println("Usuário Logado com sucesso!"
                                        + "\nBem-vindo!!!");
                                usuarioLogado = true;
                            }

                        } catch (Exception erro) {
                            logger.severe(String.format("Erro ao tentar logar o usuário: %s Erro: %s", usuarioDigitado, erro));
                        }
                    }

                    //////////Rodar CodeSafe
                    D3V1C6cli d3v1c6cli = new D3V1C6cli();
                    d3v1c6cli.rodarCodeSafe(insersor);

                    menuLoop = false;
                    break;
////////////////////////////////////////////////////////////////////////////////

                case 3:
                    insersor.setAtivarSQL(true);
                    FrameLogin telaInicial = new FrameLogin();
                    telaInicial.setAtivarSQL(true);
                    telaInicial.setVisible(true);
//                    new TelaInicial().setVisible(true);
                    menuLoop = false;
                    break;
////////////////////////////////////////////////////////////////////////////////                    
                case 4:
                    System.out.println("Obrigado por utilizar nosso produto");
                    System.exit(0);
                    break;
////////////////////////////////////////////////////////////////////////////////                    
                default:
                    System.out.println("Opção inválida! Digite o número da opção deseja, de acordo com o menu:");
                    break;
            }
        }

    }

}
