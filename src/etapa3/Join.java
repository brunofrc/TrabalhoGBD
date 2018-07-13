package etapa3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import etapa1.Catalogo;
import etapa2.Indice;
import etapa2.Operacoes;

public class Join {
    private Catalogo c1;
    private Catalogo c2;

    public Join(Catalogo c1, Catalogo c2) {
        this.c1=c1;
        this.c2=c2;
    }

    public void juncao() throws FileNotFoundException, IOException {
        Operacoes o = new Operacoes();
        List<Indice> lista = new ArrayList<Indice>();
        String result1 = "";
        String result2 = "";
        Scanner sc = null;
        String temp;
        sc = new Scanner(new FileReader(new File("C:/Users/bruno/eclipse-workspace/trabalhoGBD/DataBase/Indices/"+c1.getTabela()+".txt")));
        String[] atributos = c1.getAtributos();
        
        // foreach R
        for(int i=0;i<c1.getNumAtributos();i++)
            System.out.print(atributos[i]+ "   |   ");
        
        String[] atributos2 = c2.getAtributos();
        //foreach S
        for(int i=0;i<c2.getNumAtributos();i++)
            System.out.print(atributos2[i]+ "   |   ");
        System.out.println();
        //foreach s  S|ri = sj 
        while(sc.hasNext()){
            Indice ind = new Indice(c1.getTabela(), c1);
            sc.useDelimiter(" ");
            temp = sc.next(); //para posicao no arquivo de indices
            int chave = Integer.parseInt(sc.next()); //para chave
            ind.setPagina(sc.next()); //para pagina
            ind.setSlot(Integer.parseInt(sc.next())); //para slot
            result1 = o.busca(ind, c1);
            Indice ind2 = new Indice(c2.getTabela(), c2);
            lista = ind2.getIndice(chave);
            for(int i=0;i<lista.size();i++){               
                result2 = o.busca(lista.get(i), c2);
                //output<r,s>	
                System.out.println(result1+" "+result2);
            }
        } 
        sc.close();
    }
    
}
