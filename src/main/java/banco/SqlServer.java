package banco;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class SqlServer extends Conexao{

    public SqlServer() {
        super.usuario = "sa";
        super.senha = "urubu100";
        super.host = "54.85.6.232";
        super.dataBase = "caretech";
        super.porta = "1433";
        super.url = "jdbc:sqlserver://%s:%s;databaseName=%s".formatted(host, porta, dataBase);
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(usuario);
        dataSource.setPassword(senha);

        conexao = new JdbcTemplate(dataSource);
    }
}
