package banco;

import org.springframework.jdbc.core.JdbcTemplate;

public abstract class Conexao {

    protected String usuario;
    protected String senha;
    protected String host;
    protected String dataBase;
    protected String url;
    protected String porta;
    protected JdbcTemplate conexao;

    public JdbcTemplate getConexao() {
        return conexao;
    }
}
