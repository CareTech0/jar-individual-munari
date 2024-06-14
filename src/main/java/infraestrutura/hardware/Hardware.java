package infraestrutura.hardware;

import banco.Conexao;
import com.github.britooo.looca.api.core.Looca;

import java.util.List;

public abstract class Hardware <T> {
    protected Integer id_hardware;
    protected String nome_hardware;
    protected Double capacidade_total;
    protected Integer fk_computador;
    protected Looca looca = new Looca();
    protected Conexao con;
    protected Double uso_capacidade;

    public Hardware() {
    }

    public Hardware(Conexao con) {
        this.con = con;
    }

    public abstract List<T> autenticarHardware(Integer fk_computador);
    public abstract void inserirHardwere(Integer fkComputador);
    public abstract void buscarTotalDisponivel();
    public abstract Double buscarUsoCapacidade() throws InterruptedException;
    public abstract T verificarSeHardwareJaCadastrado(Integer idComputador);
    public Integer getId_hardware() {
        return id_hardware;
    }

    public void setId_hardware(Integer id_hardware) {
        this.id_hardware = id_hardware;
    }

    public String getNome_hardwere() {
        return nome_hardware;
    }

    public void setNome_hardwere(String nome_hardwere) {
        this.nome_hardware = nome_hardwere;
    }

    public Double getCapacidade_total() {
        return capacidade_total;
    }

    public void setCapacidade_total(Double capacidade_total) {
        this.capacidade_total = capacidade_total;
    }

    public Integer getFk_computador() {
        return fk_computador;
    }

    public void setFk_computador(Integer fk_computador) {
        this.fk_computador = fk_computador;
    }

    public Looca getLooca() {
        return looca;
    }

    public void setLooca(Looca looca) {
        this.looca = looca;
    }

    public Conexao getCon() {
        return con;
    }

    public Double getUso_capacidade() {
        return uso_capacidade;
    }

    public void setUso_capacidade(Double uso_capacidade) {
        this.uso_capacidade = uso_capacidade;
    }

    @Override
    public String toString() {
        return "Hardware{" +
                "id_hardware=" + id_hardware +
                ", nome_hardware='" + nome_hardware + '\'' +
                ", capacidade_total='" + capacidade_total + '\'' +
                ", fk_computador=" + fk_computador +
                ", looca=" + looca +
                ", con=" + con +
                ", uso_capacidade=" + uso_capacidade +
                '}';
    }
}
