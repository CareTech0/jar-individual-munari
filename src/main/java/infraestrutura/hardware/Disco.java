package infraestrutura.hardware;

import banco.Conexao;
import com.github.britooo.looca.api.group.discos.DiscoGrupo;
import com.github.britooo.looca.api.group.discos.Volume;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.List;

public class Disco extends Hardware{

    public Disco() {
        nome_hardware = "disco";
    }

    public Disco(Conexao con) {
        super(con);
        nome_hardware = "disco";
    }

    public Disco(Integer idHardwere, Double capacidadeTotal, Integer fkComputador, Conexao con){
        this.id_hardware = idHardwere;
        this.nome_hardware = "disco";
        this.capacidade_total = capacidadeTotal;
        this.fk_computador = fkComputador;
        this.con = con;
    }

    @Override
    public List<Disco> autenticarHardware(Integer fkComputador) {
        return con.getConexao().query(
                "SELECT * FROM hardware WHERE nome_hardware = 'disco' AND fk_computador = '%d'".formatted(fkComputador),
                new BeanPropertyRowMapper<>(Disco.class)
        );
    }

    @Override
    public void inserirHardwere(Integer fkComputador) {
        buscarTotalDisponivel();
        capacidade_total.toString().replace(',', '.');
        con.getConexao().execute(
                "INSERT INTO hardware (nome_hardware, capacidade_total, fk_computador) VALUES ('disco', '%s', %d)".formatted( capacidade_total, fkComputador)
        );
    }

    @Override
    public void buscarTotalDisponivel() {
        DiscoGrupo grupoDeDisco = new DiscoGrupo();
        List<Volume> listaDeVolume = grupoDeDisco.getVolumes();
        capacidade_total = listaDeVolume.get(0).getTotal()/(Math.pow(1024.0, 3));
    }

    @Override
    public Double buscarUsoCapacidade() {
        DiscoGrupo grupoDeDiscos = new DiscoGrupo();
        List<Volume> listaDeVolumes = grupoDeDiscos.getVolumes();

        Double espacoLivre = listaDeVolumes.get(0).getDisponivel()/Math.pow(1024.0, 3);
        uso_capacidade = capacidade_total - espacoLivre;
        return uso_capacidade;
    }

    @Override
    public Disco verificarSeHardwareJaCadastrado(Integer idComputador) {
        List<Disco> discoAutenticacao = autenticarHardware(idComputador);
        Disco disco = new Disco(con);
        if(discoAutenticacao.size()==1){
            //Se estiver presente no banco de dados, instancia o objeto novamente passando iniciando os atributos conforme no  banco
            disco = new Disco(discoAutenticacao.get(0).getId_hardware(), discoAutenticacao.get(0).getCapacidade_total(), discoAutenticacao.get(0).getFk_computador(), con);
        } else if (discoAutenticacao.isEmpty()) {
            //Se não estiver presente insere no banco dedados e instancia o objeto
            disco.inserirHardwere(idComputador);
            System.out.println("Inserindo Disco no banco");
            discoAutenticacao = disco.autenticarHardware(idComputador);
            disco = new Disco(discoAutenticacao.get(0).getId_hardware(), discoAutenticacao.get(0).getCapacidade_total(), discoAutenticacao.get(0).getFk_computador(), con);
        }
        return disco;
    }
}
