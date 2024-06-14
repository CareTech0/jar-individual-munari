package infraestrutura.hardware;

import banco.Conexao;
import infraestrutura.Computador;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.List;

public class Cpu extends Hardware{

    public Cpu() {
        nome_hardware = "cpu";
    }

    public Cpu(Integer idHardwere, Double capacidadeTotal, Integer fkComputador, Conexao con){
        this.id_hardware = idHardwere;
        this.nome_hardware = "cpu";
        this.capacidade_total = capacidadeTotal;
        this.fk_computador = fkComputador;
        this.con = con;
    }

    public Cpu(Conexao con) {
        super(con);
        this.nome_hardware = "cpu";
    }

    @Override
    public List<Cpu> autenticarHardware(Integer fk_computador) {
         return con.getConexao().query(
                "SELECT * FROM hardware WHERE nome_hardware = 'cpu' AND fk_computador = '%d'".formatted(fk_computador),
                new BeanPropertyRowMapper<>(Cpu.class)
        );
    }

    @Override
    public Cpu verificarSeHardwareJaCadastrado(Integer idComputador) {

        List<Cpu> cpuAutenticacao = autenticarHardware(idComputador);
        Cpu cpu = new Cpu(con);
        if(cpuAutenticacao.size()==1){
            //Se estiver presente no banco do SQL Server instancia o objeto novamente passando iniciando os atributos conforme no  banco
            cpu = new Cpu(cpuAutenticacao.get(0).getId_hardware(), cpuAutenticacao.get(0).getCapacidade_total(), cpuAutenticacao.get(0).getFk_computador(), con);
        } else if (cpuAutenticacao.isEmpty()) {
            //Se não estiver presente insere no banco SQL Server e instancia o objeto
            cpu.inserirHardwere(idComputador);
            System.out.println("INSERINDO HARDWARE CPU");
            cpuAutenticacao = cpu.autenticarHardware(idComputador);
            cpu = new Cpu(cpuAutenticacao.get(0).getId_hardware(), cpuAutenticacao.get(0).getCapacidade_total(), cpuAutenticacao.get(0).getFk_computador(), con);
        }
        return cpu;
    }

    @Override
    public void inserirHardwere(Integer fkComputador) {
        buscarTotalDisponivel();
        capacidade_total.toString().replace(',', '.');
        con.getConexao().execute(
                "INSERT INTO hardware (nome_hardware, capacidade_total, fk_computador) VALUES ('cpu', %s, %d)".formatted( capacidade_total, fkComputador)
        );
    }

    @Override
    public void buscarTotalDisponivel() {
        this.capacidade_total = 100.0;
    }

    @Override
    public Double buscarUsoCapacidade() {
        uso_capacidade = looca.getProcessador().getUso();
        return uso_capacidade;
    }
}
