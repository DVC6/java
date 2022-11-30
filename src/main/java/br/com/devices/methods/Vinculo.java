package br.com.devices.methods;

import br.com.devices.db.Conexao;
import br.com.devices.entities.ComponenteEntity;
import br.com.devices.entities.HospitalEntity;
import br.com.devices.entities.TotemEntity;
import br.com.devices.frames.FrameBemVindo;
import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.processador.Processador;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class Vinculo {

    public Integer Vincular(String cnpj, String nomeMaquina, String local) throws UnknownHostException, SocketException{

        Locale.setDefault(Locale.US);

        Looca looca = new Looca();
        Processador processador = looca.getProcessador();

        Conexao conexao = new Conexao();
        JdbcTemplate connectionAzure = conexao.getConnectionAzure();
        
        List<HospitalEntity> hospital
                = connectionAzure.query("SELECT * FROM [dbo].[hospital] WHERE cnpj = ?",
                        new BeanPropertyRowMapper<>(HospitalEntity.class), cnpj);
        
        try {
            String fkHospital = hospital.get(0).getIdHospital().toString();

            //Azure
            String insertSQLTotem = String.format("INSERT INTO totem "
                    + "(nome_maquina, localizacao, fkhospital, identificador_unico)"
                    + " VALUES('%s', '%s', '%s', '%s')",
                    nomeMaquina, local, fkHospital, getUniqueIdentifier());

            connectionAzure.execute(insertSQLTotem);

            List<TotemEntity> totemAzure
                    = connectionAzure.query("SELECT TOP 1 id_totem FROM totem ORDER BY id_totem DESC",
                    new BeanPropertyRowMapper<>(TotemEntity.class));

            Integer idTotemAzure = totemAzure.get(0).getIdTotem();

            String insertSQLComponentsAzure = String.format("INSERT INTO componente"
                    + "(total_componente, fktipocomponente, fktotem, modelo) VALUES"
                    + "(%.2f, %d, %d, '%s')"
                    + ",(%.2f, %d, %d, '%s')"
                    + ",(%.2f, %d, %d, '%s')",
                    Formatter.getTotalCpu().doubleValue(), 1, idTotemAzure, processador.getNome(),
                    Formatter.getTotalMemoria(), 2, idTotemAzure, "RAM",
                    Formatter.getTotalDiscos(), 3, idTotemAzure, "Disco");

            connectionAzure.execute(insertSQLComponentsAzure);

            //MySQL
            vincularMySql(idTotemAzure);

            System.out.println("Succeeded");
            
            return idTotemAzure;
        } catch (RuntimeException e) {
            System.out.println(e);
            return null;
        }
    }

    public static void vincularMySql(Integer idTotem) throws UnknownHostException, SocketException{

        Conexao conexao = new Conexao();
        JdbcTemplate connectionMySql = conexao.getConnectionMySQL();
        JdbcTemplate connectionAzure = conexao.getConnectionAzure();

        Looca looca = new Looca();
        Processador processador = looca.getProcessador();

        List<ComponenteEntity> compsMysql = connectionMySql.query(
                "SELECT * FROM componete WHERE fktotem = ?",
                new BeanPropertyRowMapper<>(ComponenteEntity.class), idTotem
        );

        if (compsMysql.size() == 0) {
            List<ComponenteEntity> componentes = connectionAzure.query(
                    "SELECT * FROM [dbo].[componente] WHERE fktotem = ? ORDER BY fktipocomponente",
                    new BeanPropertyRowMapper<>(ComponenteEntity.class), idTotem
            );

            connectionMySql.execute(String.format("INSERT INTO totem " +
                    "(id_totem, identificador_unico) VALUES" +
                    "(%d, '%s');", idTotem, getUniqueIdentifier()));

            List<TotemEntity> totemMySql
                    = connectionMySql.query("SELECT id_totem FROM totem ORDER BY id_totem DESC LIMIT 1;",
                    new BeanPropertyRowMapper<>(TotemEntity.class));

            Integer idTotemMySql = totemMySql.get(0).getIdTotem();

            String insertSQLComponentsMySql = String.format("INSERT INTO componente"
                            + "(id_componente, total_componente, fktipocomponente, fktotem, modelo) VALUES"
                            + "(%d, %.2f, %d, %d, '%s')"
                            + ",(%d, %.2f, %d, %d, '%s')"
                            + ",(%d, %.2f, %d, %d, '%s');",
                    componentes.get(0).getIdComponente(), Formatter.getTotalCpu().doubleValue(), 1, idTotemMySql, processador.getNome(),
                    componentes.get(1).getIdComponente(), Formatter.getTotalMemoria(), 2, idTotemMySql, "RAM",
                    componentes.get(2).getIdComponente(), Formatter.getTotalDiscos(), 3, idTotemMySql, "Disco");

            connectionMySql.execute(insertSQLComponentsMySql);
        } else {
            System.out.println("Totem e componentes já persistidos localmente.");
        }
    }

    public Boolean isCamposValidos(String chave, String id, String local) {
        return !(chave.equals("") || id.equals("") || local.equals(""));
    }
    
    private Boolean ativarSQL = true;

    public Boolean getAtivarSQL() {
        return ativarSQL;
    }

    public void setAtivarSQL(Boolean ativarSQL) {
        this.ativarSQL = ativarSQL;
    }
    
    Logger logger = Logger.getLogger("InicialLogger");
    
    private void exibirD3V1C6gui(Integer idTotem) throws InterruptedException, IOException {
        Thread.sleep(1000);

        FrameBemVindo telaD3V1C6gui = new FrameBemVindo(idTotem);
        telaD3V1C6gui.setAtivarSQL(ativarSQL);
        telaD3V1C6gui.setVisible(true);
    }
    
    public Boolean autoLogin() {
        Vinculo vinculo = new Vinculo();

        try {
            Integer idTotem = vinculo.isAlreadyVinculado();
            if (idTotem != null) {
                logger.info("Inicial - Totem encontrado no banco de dados. Iniciando coleta de dados.");
                atualizarComponentes(idTotem);
                exibirD3V1C6gui(idTotem);
                return true;
            }
            return false;
        } catch (UnknownHostException ex) {
            logger.severe(String.format("Inicial - Erro ao buscar hostname: %s", ex.toString()));
        } catch (Exception ex) {
            logger.severe("Inicial - Erro ao conectar com banco de dados");
        }
        return false;
    }

    public static Integer isAlreadyVinculado() throws UnknownHostException, SocketException {
        Conexao conexao = new Conexao();
        JdbcTemplate connection = conexao.getConnectionAzure();
        
        List<TotemEntity> totem
                = connection.query("SELECT * FROM [dbo].[totem] WHERE identificador_unico = ?",
                        new BeanPropertyRowMapper<>(TotemEntity.class), getUniqueIdentifier());  
        
        if (totem.size() != 0) {
            vincularMySql(totem.get(0).getIdTotem());
            return totem.get(0).getIdTotem();
        }
        else return null;
    }

    public static void atualizarComponentes(Integer idTotem) {
        Conexao conexao = new Conexao();
        JdbcTemplate connection = conexao.getConnectionAzure();
        Looca looca = new Looca();
        Processador p = looca.getProcessador();

        List<ComponenteEntity> componentes
                = connection.query(
                    "SELECT * FROM [dbo].[componente] WHERE fktotem = ? ORDER BY fktipocomponente",
                    new BeanPropertyRowMapper<>(ComponenteEntity.class), idTotem
                );

        if (
            !(componentes.get(0).getModelo().equals(p.getNome())) ||
            !(componentes.get(0).getTotalComponente().equals(Formatter.getTotalCpu().toString()))
        ) {
            connection.update(
                "UPDATE [dbo].[componente] SET modelo = ?, total_componente = ? WHERE fktotem = ? AND fktipocomponente = 1",
                p.getNome(), Formatter.getTotalCpu(), idTotem
            );
        }

        if (
            componentes.get(1).getTotalComponente().equals(Formatter.getTotalMemoria().toString())
        ) {
            connection.update(
                "UPDATE [dbo].[componente] SET total_componente = ? WHERE fktotem = ? AND fktipocomponente = 2",
                Formatter.getTotalMemoria(), idTotem
            );
        }

        if (
            componentes.get(0).getTotalComponente().equals(Formatter.getTotalDiscos().toString())
        ) {
            connection.update(
                "UPDATE [dbo].[componente] SET total_componente = ? WHERE fktotem = ? AND fktipocomponente = 3",
                Formatter.getTotalDiscos(), idTotem
            );
        }
    }

    
    public static String getUniqueIdentifier() throws UnknownHostException, SocketException {
        Looca looca = new Looca();
        Processador processador = looca.getProcessador();
        InetAddress localHost = InetAddress.getLocalHost();
        NetworkInterface ni = NetworkInterface.getByInetAddress(localHost);
        byte[] hardwareAddress = ni.getHardwareAddress();

        String[] hexadecimal = new String[hardwareAddress.length];
        for (int i = 0; i < hardwareAddress.length; i++) {
            hexadecimal[i] = String.format("%02X", hardwareAddress[i]);
        }

        return String.format(String.join("-", hexadecimal) + processador.getId());
    }

    public static String getNomeTotem() throws UnknownHostException, SocketException{
        Conexao conexao = new Conexao();
        JdbcTemplate connection = conexao.getConnectionAzure();

        List<TotemEntity> nomeTotem = connection.query(
                "SELECT * FROM [dbo].[totem] WHERE identificador_unico = ?",
                new BeanPropertyRowMapper<>(TotemEntity.class), getUniqueIdentifier()
            );

        return nomeTotem.get(0).getNomeMaquina();
    }
}
