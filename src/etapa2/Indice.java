package etapa2;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import etapa1.Catalogo;

public class Indice {
    protected String tabela; 
    protected Catalogo c;
    protected int buckets=100; 
    protected int slot; 
    protected String pagina; 

    public Indice(String tabela, Catalogo c) {
        this.tabela = tabela;
        this.c = c;
    }

    public int getBuckets() {
        return buckets;
    }

    public void setBuckets(int buckets) {
        this.buckets = buckets;
    }

    public Catalogo getC() {
        return c;
    }

    public void setC(Catalogo c) {
        this.c = c;
    }

    public String getPagina() {
        return pagina;
    }

    public void setPagina(String pagina) {
        this.pagina = pagina;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public String getTabela() {
        return tabela;
    }

    public void setTabela(String tabela) {
        this.tabela = tabela;
    }

    public void criaIndice() throws FileNotFoundException, IOException {
       File diretorio = new File("C:/Users/bruno/eclipse-workspace/trabalhoGBD/DataBase/Arquivos");
       File[] flist = diretorio.listFiles();
       File indice = new File("C:/Users/bruno/eclipse-workspace/trabalhoGBD/DataBase/Indices/"+tabela+".txt");
       FileWriter saida = new FileWriter(indice,false); //criação do índice    
       List<ItemIndice> lista = new ArrayList<ItemIndice>();
       
       for(int arq=0;arq<flist.length;arq++){ 
           String arquivo = flist[arq].getName();
           pagina = arquivo;
           if(arquivo.substring(0, tabela.length()).equals(tabela)){//seleciona apenas os arquivos binarios da tabela
               FileInputStream i = new FileInputStream("C:/Users/bruno/eclipse-workspace/trabalhoGBD/DataBase/Arquivos/"+arquivo);  
               BufferedInputStream in = new BufferedInputStream(i); 
               String[] tipos = c.getTipos();
               int x,j=0;
               slot=0;
               while((x = in.read()) != -1){
                   String n = null;
                   if(tipos[j].equals("integer") && j==0){                      
                       n = ""+Integer.toHexString(x);
                       x = in.read();
                       n = n+Integer.toHexString(x);
                       x = in.read();
                       n = n+Integer.toHexString(x);
                       x = in.read();
                       n = n+Integer.toHexString(x);
                       
                       int chave = Integer.parseInt(n, 16);
                       System.out.println(chave); 
                       //-------------------------------------------------------
                       Hash h = new Hash(buckets);
                       int pos = h.funcaoHash(chave);
                       ItemIndice item = new ItemIndice(pos, chave, pagina, slot);
                       lista.add(item);                       
                       //-------------------------------------------------------
                       slot = slot + 4;
                   }else
                   if(tipos[j].equals("char")||tipos[j].equals("small_int")){
                       x = in.read();
                       slot = slot + 2;
                   }else
                   if(tipos[j].equals("varchar")){
                       n = ""+Integer.toHexString(x);
                       int v = Integer.parseInt(n, 16);
                       for(int k=0;k<v;k++){
                           x = in.read();
                           slot++;
                       }
                       slot++; 
                   }                  
                   j++;
                   if(j>=c.getNumAtributos())
                       j=0;
               }  
               in.close();
           }
       }
       
       //ordenando a lista de ItemIndice
       try {  
          Collections.sort(lista, new Comparator<ItemIndice>() {  
             @Override  
             public int compare(ItemIndice pessoa1, ItemIndice pessoa2) {  
                if (pessoa1.compareTo(pessoa2) == -1) {  
                   return -1;  
                } else if (pessoa1.compareTo(pessoa2) == 0) {  
                   return 0;  
                }   
                return 1;  
             }  
          });     
       } catch (Exception e) {  
          e.getMessage();  
       }
       
       for(int i=0;i<lista.size();i++){
          saida.write(lista.get(i).posicao+" ");
          saida.write(lista.get(i).chave+" ");
          saida.write(lista.get(i).pagina+" ");
          saida.write(lista.get(i).slot+" ");
       }
       saida.close();
    }

    public List<Indice> getIndice(int chave) throws FileNotFoundException {
        List<Indice> lista = new ArrayList<Indice>();        
        Hash h = new Hash(buckets);
        int pos = h.funcaoHash(chave);
        Scanner sc = null;
        String temp;
        sc = new Scanner(new FileReader(new File("C:/Users/bruno/eclipse-workspace/trabalhoGBD/DataBase/Indices/"+c.getTabela()+".txt")));
        while(sc.hasNext()){
            Indice ind = new Indice(tabela, c);
            sc.useDelimiter(" ");
            temp = sc.next(); //para posicao no arquivo de indices
            if(Integer.parseInt(temp)==pos){
                temp = sc.next(); //para chave
                int chaveb = Integer.parseInt(temp);
                if(chaveb==chave){
                    ind.setPagina(sc.next());
                    ind.setSlot(Integer.parseInt(sc.next()));                    
                    lista.add(ind);
                }else{
                    temp = sc.next(); //para pagina
                    temp = sc.next(); //para slot
                }
            }else{
                temp = sc.next(); //para chave
                temp = sc.next(); //para pagina
                temp = sc.next(); //para slot
            }           
        } 
        sc.close();
        return lista;
    }
}
