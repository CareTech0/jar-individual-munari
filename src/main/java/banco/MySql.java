package banco;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class MySql extends Conexao {

    private String ambiente;

    public MySql() {
        ambiente = System.getenv("AMBIENTE");
        if(ambiente==null){
            super.usuario = "root";
            super.senha = "urubu100";
            super.host = "localhost";
            super.dataBase = "caretech";
            super.porta = "3306";
        }else {
            super.usuario = System.getenv("DB_USER");
            super.senha = System.getenv("DB_PASSWORD");
            super.host = System.getenv("DB_HOST");
            super.dataBase = System.getenv("DB_NAME");
            super.porta = System.getenv("DB_PORT");
        }
        super.url = "jdbc:mysql://%s:%s/%s".formatted(host, porta, dataBase);
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(usuario);
        dataSource.setPassword(senha);

        conexao = new JdbcTemplate(dataSource);
    }
}
