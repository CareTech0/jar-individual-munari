package Threads;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.janelas.Janela;
import com.github.britooo.looca.api.group.sistema.Sistema;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import infraestrutura.SitesBloqueados;

import java.util.List;

public class ThreadBloqueioDeSites extends Thread{

    private Integer fk_empresa;

    public ThreadBloqueioDeSites(Integer fk_empresa) {
        this.fk_empresa = fk_empresa;
    }

    @Override
    public void run(){
        SitesBloqueados sitesBloqueados = new SitesBloqueados();
        sitesBloqueados.setFk_empresa(fk_empresa);
        Looca looca = new Looca();
        Sistema sistema = looca.getSistema();
        System.out.println("Iniciando monitoramento de sites acessados");
        while(true){
            List<Janela> listaProcessos = looca.getGrupoDeJanelas().getJanelas();
            List<SitesBloqueados> listaSitesBloqueados = sitesBloqueados.getSitesBloqueados();
            for(Janela janelaDaVez : listaProcessos){
                for(SitesBloqueados siteBloqueadoDaVez : listaSitesBloqueados){
                    if(janelaDaVez.getTitulo().toLowerCase().contains(siteBloqueadoDaVez.getNome().toLowerCase())){
                        System.out.println("Você não tem permissão para acessar o site %s, portanto estamos fechando seu navegador".formatted(siteBloqueadoDaVez.getNome()));
                        Long pidJanela = janelaDaVez.getPid();
                        PowerShellResponse response;
                        if (sistema.getSistemaOperacional().equalsIgnoreCase("Windows")) {
                            response = PowerShell.executeSingleCommand("taskkill /PID %d".formatted(pidJanela));
                        } else {
                            response = PowerShell.executeSingleCommand("kill %d".formatted(pidJanela));
                        }
                        break;
                    }
                }
            }

        }
    }

}
