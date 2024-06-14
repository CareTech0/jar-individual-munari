package infraestrutura.hardware;

import banco.Conexao;
import com.github.britooo.looca.api.group.discos.DiscoGrupo;
import com.github.britooo.looca.api.group.discos.Volume;
import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.List;

public class Rede extends Hardware{

    public Rede() {
        nome_hardware="rede";
    }

    public Rede(Conexao con) {
        super(con);
        nome_hardware="Rede";
    }

    public Rede(Integer idHardwere, Double capacidadeTotal, Integer fkComputador, Conexao con){
        this.id_hardware = idHardwere;
        this.nome_hardware = "rede";
        this.capacidade_total = capacidadeTotal;
        this.fk_computador = fkComputador;
        this.con = con;
    }

    @Override
    public void inserirHardwere(Integer fkComputador) {
        buscarTotalDisponivel();
        capacidade_total.toString().replace(',', '.');
        con.getConexao().execute(
                "INSERT INTO hardware (nome_hardware, capacidade_total, fk_computador) VALUES ('rede', '%s', %d)".formatted( capacidade_total, fkComputador)
        );
    }

    @Override
    public List<Rede> autenticarHardware(Integer fkComputador) {
        return con.getConexao().query(
                "SELECT * FROM hardware WHERE nome_hardware = 'rede' AND fk_computador = '%d'".formatted(fkComputador),
                new BeanPropertyRowMapper<>(Rede.class)
        );
    }

    @Override
    public void buscarTotalDisponivel() {
        capacidade_total = 0.0;
    }

    @Override
    public Double buscarUsoCapacidade() throws InterruptedException {
        // Crie um objeto SpeedTestSocket
        SpeedTestSocket speedTestSocket = new SpeedTestSocket();

        // Variável para armazenar a velocidade de download em Mbps
        double downloadSpeedMbps = 0;

        // Adicione um listener para capturar eventos de teste de velocidade
        speedTestSocket.addSpeedTestListener(new ISpeedTestListener() {

            @Override
            public void onCompletion(SpeedTestReport report) {
                // Quando o teste for concluído, obtenha a velocidade de download em bits/s
                double downloadSpeed = report.getTransferRateBit().doubleValue();

                // Converta a velocidade de bits por segundo para megabits por segundo (Mbps)
                double downloadSpeedMbps = downloadSpeed / 1_000_000.0;
                uso_capacidade = downloadSpeedMbps;
                //System.out.println(downloadSpeedMbps);
            }

            @Override
            public void onError(SpeedTestError speedTestError, String errorMessage) {
                System.err.println("Erro: " + errorMessage);
            }

            @Override
            public void onProgress(float percent, SpeedTestReport report) {
                // Progresso do teste de velocidade (opcional)
            }
        });

        // Inicie o teste de download com um arquivo de teste
        String fileUrl = "https://link.testfile.org/PDF10MB"; // URL do arquivo de teste
        int timeout = 10000; // Tempo limite de conexão em milissegundos
        speedTestSocket.startDownload(fileUrl, timeout);
        Thread.sleep(10000);
        return uso_capacidade;
    }

    @Override
    public Rede verificarSeHardwareJaCadastrado(Integer idComputador) {
        List<Rede> redeAutenticacao = autenticarHardware(idComputador);
        Rede rede = new Rede(con);
        if(redeAutenticacao.size()==1){
            //Se estiver presente no banco de dados, instancia o objeto novamente passando iniciando os atributos conforme no  banco
            rede = new Rede(redeAutenticacao.get(0).getId_hardware(), redeAutenticacao.get(0).getCapacidade_total(), redeAutenticacao.get(0).getFk_computador(), con);
        } else if (redeAutenticacao.isEmpty()) {
            //Se não estiver presente insere no banco dedados e instancia o objeto
            rede.inserirHardwere(idComputador);
            System.out.println("Inserindo Rede no banco");
            redeAutenticacao = rede.autenticarHardware(idComputador);
            rede = new Rede(redeAutenticacao.get(0).getId_hardware(), redeAutenticacao.get(0).getCapacidade_total(), redeAutenticacao.get(0).getFk_computador(), con);
        }
        return rede;
    }
}
