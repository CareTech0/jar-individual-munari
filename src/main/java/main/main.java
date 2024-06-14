package main;

import Threads.ThreadBloqueioDeSites;
import Threads.ThreadInsercao;
import banco.MySql;
import banco.SqlServer;
import infraestrutura.Computador;
import infraestrutura.SitesBloqueados;
import infraestrutura.hardware.*;

import java.util.List;
import java.util.Scanner;

public class main {
    public static void main(String[] args) throws InterruptedException {
        SqlServer sqlServer = new SqlServer();
        MySql mySql = new MySql();
        Computador computadorMySql = new Computador(mySql);
        Computador computadorSqlServer = new Computador(sqlServer);
        Cpu cpuMySql = new Cpu(mySql);
        Cpu cpuSql = new Cpu(sqlServer);
        Ram ramSql = new Ram(sqlServer);
        Ram ramMySql = new Ram(mySql);
        Disco discoSql = new Disco(sqlServer);
        Disco discoMySql = new Disco(mySql);
        Rede redeSql = new Rede(sqlServer);
        Rede redeMySql = new Rede(mySql);
        Scanner leitor = new Scanner(System.in);
        while(true){
            System.out.println("--------------------------------------------------------");
            System.out.println("||||||||||||||||     Login no Client     |||||||||||||||");
            System.out.println("--------------------------------------------------------");
            //Lendo os inputs de login e senha
            System.out.println("Login: ");
            String login = leitor.nextLine();
            System.out.println("Senha: ");
            String senha = leitor.nextLine();
            // Buscando na tabela computadores para autenticar com base no login e senha inseridos
            List<Computador> computador = computadorSqlServer.autenticarComputador(login, senha);
            // Computador autenticado
            if(computador.size()==1){
                //Adicionando os valores da query e reinstanciando o objeto computadorSql
                Computador computadorBusca = computador.get(0);
                computadorSqlServer = new Computador(
                        computadorBusca.getId_computador(),
                        computadorBusca.getEstacao_de_trabalho(),
                        computadorBusca.getLogin(),
                        computadorBusca.getSenha(),
                        computadorBusca.getFk_empresa(),
                        sqlServer);
                // Buscando na tabela computadores no MySQL para ver se a maquina está presente no mysql
                computadorMySql = computadorSqlServer.verificarSeComputadorJaCadastradoMySQL(login, senha);

                System.out.println("Login realizado com sucesso.");
                // verificando se o hardwere cpu da maquina esta presente na tabela hardware no SQL Server
                cpuSql = cpuSql.verificarSeHardwareJaCadastrado(computadorSqlServer.getId_computador());
                // verificando se o hardware cpu da maquina esta presente na tabela hardware no MySQL
                cpuMySql = cpuMySql.verificarSeHardwareJaCadastrado(computadorMySql.getId_computador());
                //Verifica se o hardware Ram esta presente na tabela hardware no Sql Server
                ramSql = ramSql.verificarSeHardwareJaCadastrado(computadorSqlServer.getId_computador());
                //Verificar se a Ram já está inserida no banco de MySQL Server
                ramMySql = ramMySql.verificarSeHardwareJaCadastrado(computadorMySql.getId_computador());
                // Verificar se o disco já esta inserido no SQL Server, se não estiver insere
                discoSql = discoSql.verificarSeHardwareJaCadastrado(computadorSqlServer.getId_computador());
                // Verificar se o disco já esta inserido no MySQL, se não estiver insere
                discoMySql = discoMySql.verificarSeHardwareJaCadastrado(computadorMySql.getId_computador());
                // Verificar se a rede já esta inserida no SQL Server, se não estiver insere
                redeSql = redeSql.verificarSeHardwareJaCadastrado(computadorSqlServer.getId_computador());
                // Verificar se a rede já esta inserida no MySQL, se não estiver insere
                redeMySql = redeMySql.verificarSeHardwareJaCadastrado(computadorMySql.getId_computador());
                //Inserir essas instâncias na lista de hardwere da instância de computador para o banco expcífico, ex: Hardwares redeSql, discoSql, cpuSql vao ser inseridos em uma lista do computadorSql, e o mesmo para o mySql
                computadorSqlServer.adicionarHardware(cpuSql, ramSql, discoSql, redeSql);
                computadorMySql.adicionarHardware(cpuMySql, ramMySql, discoMySql, redeMySql);
                SitesBloqueados sitesBloqueados = new SitesBloqueados();
                sitesBloqueados.setFk_empresa(computadorSqlServer.getFk_empresa());
                computadorSqlServer.adicionarSitesBloqueados(sitesBloqueados.getSitesBloqueados());
                break;
            }else {
                // computador não encontrado
                System.out.println("Usuário não encontrado.");
            }
        }
        ThreadInsercao persistenciaDeDados = new ThreadInsercao(computadorMySql, computadorSqlServer);
        ThreadBloqueioDeSites monitoramentoSitesBloqueados = new ThreadBloqueioDeSites(computadorSqlServer.getFk_empresa());
        persistenciaDeDados.start();
        monitoramentoSitesBloqueados.start();
    }

}
