package br.com.devices.frames;

import br.com.devices.methods.Coletor;
import br.com.devices.methods.Insersor;
import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.DiscoGrupo;
import com.github.britooo.looca.api.group.memoria.Memoria;
import com.github.britooo.looca.api.group.processador.Processador;
import java.awt.Color;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import org.json.JSONObject;

public class FrameBemVindo extends javax.swing.JFrame {

    Integer contadorRAM = 0;
    Boolean contadorRAMRodando = false;
    Integer contadorCPU = 0;
    Boolean contadorCPURodando = false;
    Integer contadorDisco = 0;
    Boolean contadorDiscoRodando = false;

    JSONObject json = new JSONObject();

    Coletor coletor = new Coletor();
    Insersor insersor = new Insersor();

    Logger logger = Logger.getLogger("D3V1C6");
    FileHandler fh;

    private Boolean ativarSQL = false;

    public Boolean getAtivarSQL() {
        return ativarSQL;
    }

    public void setAtivarSQL(Boolean ativarSQL) {
        this.ativarSQL = ativarSQL;
    }

    public FrameBemVindo() throws IOException, InterruptedException {
        initComponents();
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        setAlwaysOnTop(true);

        try {
            fh = new FileHandler("../D3V1C6.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (SecurityException | IOException e) {
            logger.severe("Erro ao inicializar arquivo de log.");
        }

        try {
            lblNomeServidor.setText(InetAddress.getLocalHost().getHostName());
            this.setUpOs();
        } catch (UnknownHostException ex) {
            logger.severe(String.format("Erro no setUpOs: %s", ex));
        }

        //Loop de Execução
        int delay = 1000;   // tempo de espera antes da 1ª execução da tarefa.
        int interval = 5000;  // intervalo no qual a tarefa será executada.
        int intervalDisco = 60000;  // intervalo no qual a tarefa será executada.
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                rodarD3V1C6();
                exibicaoCLI();
                try {
                    contadorAlerta();
                } catch (IOException ex) {
                    logger.severe(String.format("Erro no contadorAlerta: %s", ex));
                } catch (InterruptedException ex) {
                    logger.severe(String.format("Erro no contadorAlerta: %s", ex));
                }

            }
        }, delay, interval);

        Timer timerDisco = new Timer();

        timerDisco.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                rodarD3V1C6disco();
            }
        }, delay, intervalDisco);

    }

    public void setUpOs() {
        Looca looca = new Looca();
        this.setBackground(Color.BLACK);
        lblInformacoesDoSistema.setText(looca.getSistema().toString());

        BigDecimal totalRAM = new BigDecimal(looca.getMemoria().getTotal().doubleValue() / 1073741824).setScale(2, RoundingMode.HALF_EVEN);
        lblTotalRAM.setText(totalRAM.toString() + " GB");
        BigDecimal totalCPU = new BigDecimal(looca.getProcessador().getFrequencia() / 1e+9).setScale(2, RoundingMode.HALF_EVEN);
        lblTotalCPU.setText(totalCPU.toString() + " GHz");
        BigDecimal totalDisco = new BigDecimal(looca.getGrupoDeDiscos().getTamanhoTotal().doubleValue() / 1e+9).setScale(0, RoundingMode.HALF_EVEN);
        lblTotalDisco.setText(totalDisco.toString() + " GB");

    }

    private void exibicaoCLI() {
        System.out.println(coletor);
    }

    private void contadorAlerta() throws IOException, InterruptedException {
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
//            Slack.enviarMensagem(json);
        }
        if (contadorCPU == 36) {
            json.put("text", "Alerta no servidor: " + nomeServidor
                    + "\nAlerta de CPU :heavy_exclamation_mark: // Possível Atividade Suspeita :heavy_exclamation_mark:");
//            Slack.enviarMensagem(json);
        }
        if (contadorDisco == 36) {
            json.put("text", "Alerta no servidor: " + nomeServidor
                    + "\nAlerta de Disco :heavy_exclamation_mark: // Uso de volume acima de 90% da capacidade total :heavy_exclamation_mark:");
//            Slack.enviarMensagem(json);
        }

    }

    private void rodarD3V1C6() {

        insersor.setAtivarSQL(ativarSQL);
        insersor.inserirRegistros(coletor.coletarRAM());
        insersor.inserirRegistros(coletor.coletarCPU());
        lblExibeRAM.setText(coletor.getAtualRAM() + "%");
        lblExibeCPU.setText(coletor.getAtualCPU() + "%");
        Double conversaoRAM = Double.valueOf(coletor.getAtualRAM());
        if (conversaoRAM > 90.0) {
            contadorRAMRodando = true;
            lblExibeRAM.setForeground(Color.red);
        } else if (conversaoRAM > 70.0) {
            lblExibeRAM.setForeground(Color.yellow);
        } else {
            contadorRAMRodando = false;
            lblExibeRAM.setForeground(Color.green);
        }
        Double conversaoCPU = Double.valueOf(coletor.getAtualCPU());
        if (conversaoCPU > 90.0) {
            contadorCPURodando = true;
            lblExibeCPU.setForeground(Color.red);
        } else if (conversaoCPU > 70.0) {
            lblExibeCPU.setForeground(Color.yellow);
        } else {
            contadorCPURodando = false;
            lblExibeCPU.setForeground(Color.green);
        }
    }

    private void rodarD3V1C6disco() {

        insersor.setAtivarSQL(ativarSQL);
        insersor.inserirRegistros(coletor.coletarDISCO());
        lblExibeDisco.setText(coletor.getAtualDisco() + "%");
        Double conversaoDisco = Double.valueOf(coletor.getAtualDisco());
        if (conversaoDisco > 90.0) {
            contadorDiscoRodando = true;
            lblExibeDisco.setForeground(Color.red);
        } else if (conversaoDisco > 70.0) {
            lblExibeDisco.setForeground(Color.yellow);
        } else {
            contadorDiscoRodando = false;
            lblExibeDisco.setForeground(Color.green);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblSistemaOperacional = new javax.swing.JLabel();
        lblExibeCPU = new javax.swing.JLabel();
        lblFabricante = new javax.swing.JLabel();
        lblArquitetura = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lblInformacoesDoSistema = new javax.swing.JTextArea();
        lblNomeServidor = new javax.swing.JLabel();
        txtRAM2 = new javax.swing.JLabel();
        lblTotalCPU = new javax.swing.JLabel();
        txtRAM6 = new javax.swing.JLabel();
        lblExibeRAM = new javax.swing.JLabel();
        txtRAM4 = new javax.swing.JLabel();
        lblTotalRAM = new javax.swing.JLabel();
        lblExibeDisco = new javax.swing.JLabel();
        txtRAM7 = new javax.swing.JLabel();
        lblTotalDisco = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(125, 4, 45));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Monospaced", 1, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Bem-Vindo(a)");

        lblSistemaOperacional.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        lblSistemaOperacional.setForeground(new java.awt.Color(255, 255, 255));
        lblSistemaOperacional.setText("CPU:");

        lblExibeCPU.setFont(new java.awt.Font("Monospaced", 0, 24)); // NOI18N
        lblExibeCPU.setForeground(new java.awt.Color(255, 255, 255));
        lblExibeCPU.setText("--");

        lblFabricante.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        lblFabricante.setForeground(new java.awt.Color(255, 255, 255));
        lblFabricante.setText("Memoria RAM:");

        lblArquitetura.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        lblArquitetura.setForeground(new java.awt.Color(255, 255, 255));
        lblArquitetura.setText("Disco:");

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Ola, espero que goste dos nossos serviços. Segue abaixo algumas informaçôes do seu hardware que já identificamos para você");

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/logo2.png"))); // NOI18N

        lblInformacoesDoSistema.setBackground(new java.awt.Color(0, 0, 0));
        lblInformacoesDoSistema.setColumns(20);
        lblInformacoesDoSistema.setFont(new java.awt.Font("Monospaced", 0, 14)); // NOI18N
        lblInformacoesDoSistema.setForeground(new java.awt.Color(84, 182, 243));
        lblInformacoesDoSistema.setRows(5);
        jScrollPane1.setViewportView(lblInformacoesDoSistema);

        lblNomeServidor.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        lblNomeServidor.setForeground(new java.awt.Color(255, 255, 255));
        lblNomeServidor.setText("NomeMaquina");

        txtRAM2.setFont(new java.awt.Font("Monospaced", 0, 24)); // NOI18N
        txtRAM2.setForeground(new java.awt.Color(255, 255, 255));
        txtRAM2.setText("de");

        lblTotalCPU.setFont(new java.awt.Font("Monospaced", 0, 24)); // NOI18N
        lblTotalCPU.setForeground(new java.awt.Color(255, 255, 255));
        lblTotalCPU.setText("--");

        txtRAM6.setFont(new java.awt.Font("Monospaced", 0, 24)); // NOI18N
        txtRAM6.setForeground(new java.awt.Color(255, 255, 255));

        lblExibeRAM.setFont(new java.awt.Font("Monospaced", 0, 24)); // NOI18N
        lblExibeRAM.setForeground(new java.awt.Color(255, 255, 255));
        lblExibeRAM.setText("--");

        txtRAM4.setFont(new java.awt.Font("Monospaced", 0, 24)); // NOI18N
        txtRAM4.setForeground(new java.awt.Color(255, 255, 255));
        txtRAM4.setText("de");

        lblTotalRAM.setFont(new java.awt.Font("Monospaced", 0, 24)); // NOI18N
        lblTotalRAM.setForeground(new java.awt.Color(255, 255, 255));
        lblTotalRAM.setText("--");

        lblExibeDisco.setFont(new java.awt.Font("Monospaced", 0, 24)); // NOI18N
        lblExibeDisco.setForeground(new java.awt.Color(255, 255, 255));
        lblExibeDisco.setText("--");

        txtRAM7.setFont(new java.awt.Font("Monospaced", 0, 24)); // NOI18N
        txtRAM7.setForeground(new java.awt.Color(255, 255, 255));
        txtRAM7.setText("de");

        lblTotalDisco.setFont(new java.awt.Font("Monospaced", 0, 24)); // NOI18N
        lblTotalDisco.setForeground(new java.awt.Color(255, 255, 255));
        lblTotalDisco.setText("--");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGap(6, 6, 6)
                                            .addComponent(lblExibeCPU, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(txtRAM2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(32, 32, 32)
                                            .addComponent(lblTotalCPU, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(lblExibeRAM, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(txtRAM4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(32, 32, 32)
                                            .addComponent(lblTotalRAM, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(txtRAM6, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGap(0, 0, 0))
                                .addComponent(lblFabricante, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblSistemaOperacional, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblArquitetura, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lblExibeDisco, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtRAM7, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(32, 32, 32)
                                .addComponent(lblTotalDisco, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                                .addComponent(lblNomeServidor, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel4)
                                .addGap(54, 54, 54))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane1)
                                .addContainerGap())))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(22, 22, 22))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(204, 204, 204))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(lblSistemaOperacional)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblExibeCPU)
                            .addComponent(txtRAM2)
                            .addComponent(lblTotalCPU))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lblFabricante)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(45, 45, 45)
                                        .addComponent(txtRAM6))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(lblExibeRAM)
                                            .addComponent(txtRAM4)
                                            .addComponent(lblTotalRAM))))
                                .addGap(32, 32, 32)
                                .addComponent(lblArquitetura)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblExibeDisco)
                                    .addComponent(txtRAM7)
                                    .addComponent(lblTotalDisco))
                                .addGap(12, 12, 12)
                                .addComponent(lblNomeServidor))
                            .addComponent(jLabel4)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrameBemVindo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrameBemVindo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrameBemVindo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrameBemVindo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new FrameBemVindo().setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(FrameBemVindo.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(FrameBemVindo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblArquitetura;
    private javax.swing.JLabel lblExibeCPU;
    private javax.swing.JLabel lblExibeDisco;
    private javax.swing.JLabel lblExibeRAM;
    private javax.swing.JLabel lblFabricante;
    private javax.swing.JTextArea lblInformacoesDoSistema;
    private javax.swing.JLabel lblNomeServidor;
    private javax.swing.JLabel lblSistemaOperacional;
    private javax.swing.JLabel lblTotalCPU;
    private javax.swing.JLabel lblTotalDisco;
    private javax.swing.JLabel lblTotalRAM;
    private javax.swing.JLabel txtRAM2;
    private javax.swing.JLabel txtRAM4;
    private javax.swing.JLabel txtRAM6;
    private javax.swing.JLabel txtRAM7;
    // End of variables declaration//GEN-END:variables
}
