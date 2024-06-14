package infraestrutura;

import banco.Conexao;
import banco.MySql;
import infraestrutura.hardware.Hardware;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.ArrayList;
import java.util.List;

public class Computador {
    private Integer id_computador;
    private String estacao_de_trabalho;
    private String login;
    private String senha;
    private Integer fk_empresa;
    private Conexao con;
    private List<Hardware> hardwares;
    private List<SitesBloqueados> sitesBloqueados;

    public Computador(Integer id_computador, String estacao_de_trabalho, String login, String senha, Integer fk_empresa, Conexao con) {
        this.id_computador = id_computador;
        this.estacao_de_trabalho = estacao_de_trabalho;
        this.login = login;
        this.senha = senha;
        this.fk_empresa = fk_empresa;
        this.hardwares = new ArrayList<>();
        this.con = con;
    }

    public Computador(Conexao conexao) {
        this.con = conexao;
    }

    public Computador() {
    }

    public List<Computador> autenticarComputador(String login, String senha){

        return con.getConexao().query(
                "SELECT * FROM computador WHERE login = '%s' AND senha = '%s'".formatted(login, senha),
                new BeanPropertyRowMapper<>(Computador.class)
        );
    }
    public Computador verificarSeComputadorJaCadastradoMySQL(String login, String senha){
        MySql mySql = new MySql();
        Computador computadorMySql = new Computador(mySql);
        List<Computador> autenticacaoComputadorMySql = computadorMySql.autenticarComputador(login, senha);

        if(autenticacaoComputadorMySql.isEmpty()){
            computadorMySql.inserirComputador(
                    estacao_de_trabalho,
                    this.login,
                    this.senha,
                    this.fk_empresa);
            System.out.println("Não encontramos esse computador no MySQL portanto inserimos ao banco MySQL local");
            autenticacaoComputadorMySql = computadorMySql.autenticarComputador(login, senha);
            Computador computadorBuscaMySql = autenticacaoComputadorMySql.get(0);
            computadorMySql = new Computador(
                    computadorBuscaMySql.getId_computador(),
                    computadorBuscaMySql.getEstacao_de_trabalho(),
                    computadorBuscaMySql.getLogin(),
                    computadorBuscaMySql.getSenha(),
                    computadorBuscaMySql.getFk_empresa(),
                    mySql);
            System.out.println(computadorMySql);
        }else {
            Computador computadorBuscaMySql = autenticacaoComputadorMySql.get(0);
            computadorMySql = new Computador(
                    computadorBuscaMySql.getId_computador(),
                    computadorBuscaMySql.getEstacao_de_trabalho(),
                    computadorBuscaMySql.getLogin(),
                    computadorBuscaMySql.getSenha(),
                    computadorBuscaMySql.getFk_empresa(),
                    mySql);
        }
        return computadorMySql;
    }

    public void inserirComputador(String estacao, String login, String senha, Integer fk_empresa){
        con.getConexao().execute(
                "INSERT INTO computador (estacao_de_trabalho, login, senha, fk_empresa) VALUES ('%s', '%s', '%s', %d)".formatted(estacao, login, senha, fk_empresa)
        );
    }

    public void buscarUsosCapacidade() throws InterruptedException {

        for(Hardware hardwareDaVez : hardwares){
            Double usoCapacidade = hardwareDaVez.buscarUsoCapacidade();
            Integer fkHardware = hardwareDaVez.getId_hardware();
        }
    }

    public void inserirRegistro(Double usoCapacidade, Integer fkHardware){

        usoCapacidade.toString().replace(',', '.');
        con.getConexao().execute("INSERT INTO registros (uso_capacidade, fk_hardware) VALUES (%s, %d)".formatted(usoCapacidade, fkHardware));
    }

    public void adicionarSitesBloqueados(List<SitesBloqueados> sitesBloqueados){
        this.sitesBloqueados = sitesBloqueados;
    }

    public void adicionarHardware(Hardware cpu, Hardware ram, Hardware disco, Hardware rede){
        hardwares.add(cpu);
        hardwares.add(ram);
        hardwares.add(disco);
        hardwares.add(rede);
    }


    public Integer getId_computador() {
        return id_computador;
    }

    public void setId_computador(Integer id_computador) {
        this.id_computador = id_computador;
    }

    public String getEstacao_de_trabalho() {
        return estacao_de_trabalho;
    }

    public void setEstacao_de_trabalho(String estacao_de_trabalho) {
        this.estacao_de_trabalho = estacao_de_trabalho;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Integer getFk_empresa() {
        return fk_empresa;
    }

    public void setFk_empresa(Integer fk_empresa) {
        this.fk_empresa = fk_empresa;
    }

    public List<Hardware> getHardwares() {
        return hardwares;
    }

    public void setHardwares(List<Hardware> hardwares) {
        this.hardwares = hardwares;
    }

    public List<SitesBloqueados> getSitesBloqueados() {
        return sitesBloqueados;
    }

    public void setSitesBloqueados(List<SitesBloqueados> sitesBloqueados) {
        this.sitesBloqueados = sitesBloqueados;
    }

    @Override
    public String toString() {
        return "Computador{" +
                "id_computador=" + id_computador +
                ", estacao_de_trabalho='" + estacao_de_trabalho + '\'' +
                ", login='" + login + '\'' +
                ", senha='" + senha + '\'' +
                ", fk_empresa=" + fk_empresa +
                ", con=" + con +
                ", hardwares=" + hardwares +
                ", sitesBloqueados=" + sitesBloqueados +
                '}';
    }
}
