package Threads;

import infraestrutura.Computador;
import infraestrutura.hardware.Hardware;

import java.util.List;

public class ThreadInsercao extends Thread{
    private Computador computadorMySql;
    private Computador computadorSqlServer;

    public ThreadInsercao(Computador computadorMySql, Computador computadorSqlServer) {
        this.computadorMySql = computadorMySql;
        this.computadorSqlServer = computadorSqlServer;
    }

    @Override
    public void run() {
        while(true){
            try {
                computadorSqlServer.buscarUsosCapacidade();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            List<Hardware> usoCapacidadeHardwares = computadorSqlServer.getHardwares();
            try{
                for(int i=0; i< usoCapacidadeHardwares.size(); i++){
                    computadorSqlServer.inserirRegistro(usoCapacidadeHardwares.get(i).getUso_capacidade(), computadorSqlServer.getHardwares().get(i).getId_hardware());
                }
                System.out.println("Dados inseridos na núvem com sucesso");
            }catch(RuntimeException e){
                throw new RuntimeException(e);
            }
            try{
                for(int i=0; i<usoCapacidadeHardwares.size(); i++){
                    computadorMySql.inserirRegistro(usoCapacidadeHardwares.get(i).getUso_capacidade(), computadorMySql.getHardwares().get(i).getId_hardware());
                }
                System.out.println("Dados inseridos localmente com sucesso");
            }catch(RuntimeException e){
                throw new RuntimeException(e);
            }

        }
    }

    public Computador getComputadorMySql() {
        return computadorMySql;
    }

    public Computador getComputadorSqlServer() {
        return computadorSqlServer;
    }
}
