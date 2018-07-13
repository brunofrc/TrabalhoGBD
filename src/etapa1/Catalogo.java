package etapa1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 
 * @author bruno
 *
 */
public class Catalogo {
    protected String tabela;
    private int numAtributos;
    private String caminho = "C:/Users/bruno/eclipse-workspace/trabalhoGBD/DataBase/Catalogos/";
    private String[] atributos;
    private String[] tipos;
    private int pagina; //contador de paginas

    public String[] getAtributos() {
        return atributos;
    }

    public int getPagina() {
        return pagina;
    }

    public void setPagina(int pagina) {
        this.pagina = pagina;
    }

    public String getCaminho() {
        return caminho;
    }

    public int getNumAtributos() {
        return numAtributos;
    }

    public String getTabela() {
        return tabela;
    }

    public String[] getTipos() {
        return tipos;
    }

    public Catalogo(String tabela, int numAtributos, String[] atributos, String[] tipos){
        this.tabela = tabela;
        this.numAtributos = numAtributos;
        this.atributos = atributos;
        this.tipos = tipos;
    }

    public void criaCatalogo() throws IOException{
        FileWriter saida = new FileWriter(new File(caminho+tabela+".catalogo"),false);
        saida.write(numAtributos+"\n");
        for(int i=0;i<numAtributos;i++)
            saida.write(atributos[i]+" "+tipos[i]+"\n");
        saida.close();
    }
}
