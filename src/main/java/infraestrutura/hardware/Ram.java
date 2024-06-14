package infraestrutura.hardware;

import banco.Conexao;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.List;

public class Ram extends Hardware{

    public Ram() {
        nome_hardware = "ram";
    }

    public Ram(Integer idHardwere, Double capacidadeTotal, Integer fkComputador, Conexao con){
        this.id_hardware = idHardwere;
        this.nome_hardware = "ram";
        this.capacidade_total = capacidadeTotal;
        this.fk_computador = fkComputador;
        this.con = con;
    }

    public Ram(Conexao con) {
        super(con);
    }

    @Override
    public List<Ram> autenticarHardware(Integer fk_computador) {
        return con.getConexao().query(
                "SELECT * FROM hardware WHERE nome_hardware = 'ram' AND fk_computador = '%d'".formatted(fk_computador),
                new BeanPropertyRowMapper<>(Ram.class)
        );
    }

    @Override
    public Ram verificarSeHardwareJaCadastrado(Integer idComputador) {
        List<Ram> ramAutenticacao = autenticarHardware(idComputador);
        Ram ram = new Ram(con);
        if(ramAutenticacao.size()==1){
            //Se estiver presente no banco de dados, instancia o objeto novamente passando iniciando os atributos conforme no  banco
            ram = new Ram(ramAutenticacao.get(0).getId_hardware(), ramAutenticacao.get(0).getCapacidade_total(), ramAutenticacao.get(0).getFk_computador(), con);
        } else if (ramAutenticacao.isEmpty()) {
            //Se não estiver presente insere no banco dedados e instancia o objeto
            ram.inserirHardwere(idComputador);
            System.out.println("Inserindo RAM no banco");
            ramAutenticacao = ram.autenticarHardware(idComputador);
            ram = new Ram(ramAutenticacao.get(0).getId_hardware(), ramAutenticacao.get(0).getCapacidade_total(), ramAutenticacao.get(0).getFk_computador(), con);
        }
        return ram;
    }

    @Override
    public void inserirHardwere(Integer fkComputador) {
        buscarTotalDisponivel();
        capacidade_total.toString().replace(',', '.');
        con.getConexao().execute(
                "INSERT INTO hardware (nome_hardware, capacidade_total, fk_computador) VALUES ('ram', '%s', %d)".formatted( capacidade_total, fkComputador)
        );
    }

    @Override
    public Double buscarUsoCapacidade() {
        uso_capacidade = looca.getMemoria().getEmUso()/(Math.pow(1024.0, 3));
        return uso_capacidade;
    }

    @Override
    public void buscarTotalDisponivel() {
        capacidade_total = looca.getMemoria().getTotal() / (Math.pow(1024.0, 3));
    }
}
