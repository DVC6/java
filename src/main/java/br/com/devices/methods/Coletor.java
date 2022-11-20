package br.com.devices.methods;

import br.com.devices.db.Connection;
import br.com.devices.entities.ComponenteEntity;
import br.com.devices.entities.LeituraEntity;
import br.com.devices.entities.TotemEntity;
import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.Disco;
import com.github.britooo.looca.api.group.discos.DiscoGrupo;
import com.github.britooo.looca.api.group.discos.Volume;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class Coletor {

    Connection config = new Connection("Azure");
    JdbcTemplate template = new JdbcTemplate(config.getDataSource());
    // CONEXÃO LOOCA
    Looca looca = new Looca();
    // Gerenciar Grupo de Discos
    DiscoGrupo grupoDeDiscos = looca.getGrupoDeDiscos();
    List<com.github.britooo.looca.api.group.discos.Disco> discos = grupoDeDiscos.getDiscos();
    
    Logger logger = Logger.getLogger("Coletor");
    FileHandler fh;

    // Atributos para serem chamados na exibição de CodeSafe.java
    private String atualRAM;
    private String atualCPU;
    private String atualDisco;
    private String atualRAMQtd;
    private String atualCPUQtd;
    private String atualDiscoQtd;
    private String serverRAM;
    private String serverCPU;
    private String serverDisco;

    public Coletor() {
        try {  
            fh = new FileHandler("../ColetorLog.log");  
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);  
        } catch (SecurityException | IOException e) {  
            logger.severe("Erro ao inicializar arquivo de log.");
        }
    }

    // RAM
    public LeituraEntity coletarRAM() {
        LeituraEntity registroRAM = new LeituraEntity(identificarComponente(2));
        BigDecimal consumoRAM = new BigDecimal(looca.getMemoria().getEmUso().doubleValue() / 1073741824).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal totalRAM = new BigDecimal(looca.getMemoria().getTotal().doubleValue() / 1073741824).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal percentualConsumoRAM = new BigDecimal((consumoRAM.doubleValue() * 100) / totalRAM.doubleValue()).setScale(2, RoundingMode.HALF_EVEN);
        registroRAM.setConsumo(percentualConsumoRAM.doubleValue());
        registroRAM.setConsumoQtd(Formatter.getConsumoMemoria());
        this.atualRAM = (percentualConsumoRAM).toString();
        this.atualRAMQtd = Formatter.getConsumoMemoria().toString();
        this.serverRAM = (totalRAM).toString();
        return registroRAM;
    }

    ;
    
    // CPU
    public LeituraEntity coletarCPU() {
        LeituraEntity registroCPU = new LeituraEntity(identificarComponente(1));
        BigDecimal totalCPU = new BigDecimal(looca.getProcessador().getFrequencia() / 1e+9).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal percentualCPU = new BigDecimal(looca.getProcessador().getUso()).setScale(2, RoundingMode.HALF_EVEN);
        registroCPU.setConsumo(percentualCPU.doubleValue());
        registroCPU.setConsumoQtd(null);
        this.atualCPU = (percentualCPU).toString();
        this.serverCPU = (totalCPU).toString();
        return registroCPU;
    }

    ;
    
    // DISCO
    List<Volume> volumes = grupoDeDiscos.getVolumes();

    public LeituraEntity coletarDISCO() {
        Double discoDisponivel = 0.0;
        Double discoTotal = 0.0;
        Double discoConsumido = 0.0;
        for (Volume volume : volumes) {
            discoDisponivel += volume.getDisponivel().doubleValue();
            discoTotal += volume.getTotal().doubleValue();
            discoConsumido += (volume.getTotal().doubleValue() - volume.getDisponivel().doubleValue());
        }
//        System.out.println(discoDisponivel);
//        System.out.println(discoTotal);
//        System.out.println(discoConsumido);
        LeituraEntity registroDISCO = new LeituraEntity(identificarComponente(3));
        BigDecimal totalVolume = new BigDecimal(discoTotal.doubleValue() / 1e+9).setScale(0, RoundingMode.HALF_EVEN);
        BigDecimal consumoDisco = new BigDecimal(discoConsumido / 1e+9).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal percentualDisco = new BigDecimal((consumoDisco.doubleValue() * 100) / totalVolume.doubleValue()).setScale(0, RoundingMode.HALF_EVEN);
        registroDISCO.setConsumo(percentualDisco.doubleValue());
        registroDISCO.setConsumoQtd(Formatter.getConsumoDiscos());
        this.atualDisco = (percentualDisco).toString();
        this.atualDiscoQtd = Formatter.getConsumoDiscos().toString();
        this.serverDisco = (totalVolume).toString();
        return registroDISCO;
//        BigDecimal totalDisco = new BigDecimal(looca.getGrupoDeDiscos().getTamanhoTotal().doubleValue() / 1e+9).setScale(0, RoundingMode.HALF_EVEN);
//        registroDISCO.setTotal(totalDisco.doubleValue());
//        this.totalDisco = (totalDisco).toString();
    }

    public Integer identificarComponente(Integer tipoComponente) {
        Integer idComponenteCorreto = null;

        try {
            TotemEntity servidorTeste = new TotemEntity();
            List<TotemEntity> buscaServidor = template.query("SELECT * FROM totem WHERE nome_maquina = ?",
                    new BeanPropertyRowMapper<>(TotemEntity.class), InetAddress.getLocalHost().getHostName());
            for (TotemEntity totem : buscaServidor) {
                ComponenteEntity componenteTeste = new ComponenteEntity();
                List<ComponenteEntity> buscaComponente = template.query("SELECT * FROM componente WHERE fkTotem = ? AND fktipocomponente = ?",
                        new BeanPropertyRowMapper<>(ComponenteEntity.class), totem.getIdTotem(), tipoComponente);
                for (ComponenteEntity componente : buscaComponente) {
                    idComponenteCorreto = componente.getIdComponente();
                }
            }
        } catch (UnknownHostException ex) {
            logger.severe(String.format("Erro ao identificar componente: %s", ex));
        }
        return idComponenteCorreto;
    }

    // GET e SET
    public List<Disco> getDiscos() {
        return discos;
    }

    public void setDiscos(List<Disco> discos) {
        this.discos = discos;
    }

    public String getAtualRAM() {
        return atualRAM;
    }

    public void setAtualRAM(String atualRAM) {
        this.atualRAM = atualRAM;
    }

    public String getAtualCPU() {
        return atualCPU;
    }

    public void setAtualCPU(String atualCPU) {
        this.atualCPU = atualCPU;
    }

    public String getAtualDisco() {
        return atualDisco;
    }

    public void setAtualDisco(String atualDisco) {
        this.atualDisco = atualDisco;
    }

    @Override
    public String toString() {
        return String.format("\n"
                + "__________________________________\n"
                + "|Componente |  Total    |  Uso    |\n"
                + "|RAM:       |  %s GB  |  %s%% |\n"
                + "|CPU:       |  %s GHz |  %s%% |\n"
                + "|Disco:     |   %s GB  |  %s%%    |",
                serverRAM, atualRAM, serverCPU, atualCPU, serverDisco, atualDisco);
    }

}
