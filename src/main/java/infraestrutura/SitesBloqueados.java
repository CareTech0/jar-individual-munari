package infraestrutura;

import banco.Conexao;
import banco.SqlServer;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class SitesBloqueados {
    private Integer id_sites;
    private String nome;
    private String url;
    private Integer fk_empresa;

    public SitesBloqueados() {

    }

    public List<SitesBloqueados> getSitesBloqueados(){
        
        SqlServer sqlServer = new SqlServer();
        JdbcTemplate con = sqlServer.getConexao();

        List<SitesBloqueados> sitesBloqueados = con.query(
                "SELECT * FROM sites_bloqueados WHERE fk_empresa = %d".formatted(fk_empresa),
                new BeanPropertyRowMapper<>(SitesBloqueados.class)
        );
        return sitesBloqueados;
    }

    public Integer getId_sites() {
        return id_sites;
    }

    public String getNome() {
        return nome;
    }

    public String getUrl() {
        return url;
    }

    public Integer getFk_empresa() {
        return fk_empresa;
    }

    public void setId_sites(Integer id_sites) {
        this.id_sites = id_sites;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setFk_empresa(Integer fk_empresa) {
        this.fk_empresa = fk_empresa;
    }
}
