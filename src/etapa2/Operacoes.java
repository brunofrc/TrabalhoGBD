package etapa2;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import etapa1.Catalogo;

public class Operacoes {

	public String select(int chave, Catalogo c) throws FileNotFoundException, IOException {
		String result = "";
		// Buscando o índice do registro
		List<Indice> lista = new ArrayList<Indice>();
		Indice ind = new Indice(c.getTabela(), c);
		lista = ind.getIndice(chave);
		String[] atributos = c.getAtributos();
		for (int i = 0; i < c.getNumAtributos(); i++)
			System.out.print(atributos[i] + "   |   ");
		System.out.println();
		if (lista.isEmpty())
			System.out.println("Nao foi encontrado nenhum registro para consulta.");
		else {
			for (int i = 0; i < lista.size(); i++) {
				result = result + busca(lista.get(i), c) + "\n";
			}
		}
		return result;
	}

	public String busca(Indice ind, Catalogo c) throws FileNotFoundException, IOException {
		FileInputStream i = new FileInputStream("C:/Users/bruno/eclipse-workspace/trabalhoGBD/DataBase/Arquivos/" + ind.getPagina());
		BufferedInputStream in = new BufferedInputStream(i);
		String[] tipos = c.getTipos();
		int x = 0;
		String n = null;
		String resultado = "";
		for (int k = 0; k < ind.getSlot(); k++) {
			x = in.read();
		}
		for (int j = 0; j < c.getNumAtributos(); j++) {
			if (tipos[j].equals("integer") && j == 0) {
				x = in.read();
				n = "" + Integer.toHexString(x);
				x = in.read();
				n = n + Integer.toHexString(x);
				x = in.read();
				n = n + Integer.toHexString(x);
				x = in.read();
				n = n + Integer.toHexString(x);
				int chave = Integer.parseInt(n, 16);
				resultado = resultado + chave + "       ";
			} else if (tipos[j].equals("small_int")) {
				x = in.read();
				n = "" + Integer.toHexString(x);
				x = in.read();
				n = n + Integer.toHexString(x);
				int r = Integer.parseInt(n, 16);
				resultado = resultado + r + "       ";
			} else if (tipos[j].equals("char")) {
				byte[] vetor = new byte[1];
				in.read();
				vetor[0] = (byte) in.read();
				String s = new String(vetor);
				resultado = resultado + s + "       ";
			} else if (tipos[j].equals("varchar")) {
				x = in.read();
				n = "" + Integer.toHexString(x);
				int v = Integer.parseInt(n, 16);
				byte[] vetor = new byte[v];
				for (int k = 0; k < v; k++) {
					vetor[k] = (byte) in.read();
				}
				String s = new String(vetor);
				resultado = resultado + s + "       ";
			}
		}
		// resultado = resultado + "\n";
		return resultado;
	}

	public void insert(List<String> item, Catalogo c1) throws IOException {
		String[] tipos = c1.getTipos();
		DataOutputStream dos = new DataOutputStream(
				new FileOutputStream("C:/Users/bruno/eclipse-workspace/trabalhoGBD/DataBase/Arquivos/" + c1.getTabela() + c1.getPagina() + ".bin"));
		FileInputStream i = new FileInputStream("C:/Users/bruno/eclipse-workspace/trabalhoGBD/DataBase/Arquivos/" + c1.getTabela() + c1.getPagina() + ".bin");
		BufferedInputStream in = new BufferedInputStream(i);
		byte[] saida = new byte[4098];
		int x, j = 0;
		while ((x = in.read()) != -1) {
			saida[j] = (byte) x;
			j++;
		}
		for (int a = 0; a < j; a++)
			dos.write(saida[a]);
		// inserindo o novo registro
		for (int cont = 0; cont < c1.getNumAtributos(); cont++) {
			if (tipos[cont].equals("integer")) {
				dos.writeInt(Integer.parseInt(item.get(cont)));
			} else if (tipos[cont].equals("small_int")) {
				dos.writeShort(Integer.parseInt(item.get(cont)));
			} else if (tipos[cont].equals("char")) {
				dos.writeChar(item.get(cont).charAt(0));
			} else if (tipos[cont].equals("varchar")) {
				dos.write(item.get(cont).length());
				dos.writeBytes(item.get(cont));
			}
		}
		in.close();
		Indice i1 = new Indice(c1.getTabela(), c1);
		i1.criaIndice();
		System.out.println("\nRegistro inserido com sucesso.");
	}

	public void delete(int chave, Catalogo c) throws FileNotFoundException, IOException {
		List<Indice> lista = new ArrayList<Indice>();
		Indice ind = new Indice(c.getTabela(), c);
		lista = ind.getIndice(chave);
		if (lista.isEmpty())
			System.out.println("Nao existe registro com essa chave.");
		else {
			while (!lista.isEmpty()) {
				remove(lista.get(0), c);
				lista = ind.getIndice(chave);
			}
			System.out.println("Registro removido com sucesso.");
		}
	}

	private void remove(Indice ind, Catalogo c) throws FileNotFoundException, IOException {
		String[] tipos = c.getTipos();
		File fileOrigin = new File("C:/Users/bruno/eclipse-workspace/trabalhoGBD/DataBase/Arquivos/" + ind.getPagina());
		FileInputStream i = new FileInputStream(fileOrigin);
		BufferedInputStream in = new BufferedInputStream(i);
		byte[] saida = new byte[4098];
		int x, j = 0;
		for (int k = 0; k < ind.getSlot(); k++) {
			x = in.read();
			saida[j] = (byte) x;
			j++;
		}
		for (int k = 0; k < c.getNumAtributos(); k++) {
			if (tipos[k].equals("integer") && k == 0) {
				in.read();
				in.read();
				in.read();
				in.read();
			} else if (tipos[k].equals("small_int")) {
				in.read();
				in.read();
			} else if (tipos[k].equals("char")) {
				in.read();
				in.read();
			} else if (tipos[k].equals("varchar")) {
				String n = "" + Integer.toHexString(in.read());
				int v = Integer.parseInt(n, 16);
				byte[] vetor = new byte[v];
				for (int y = 0; y < v; y++)
					in.read();
			}
		}
		while ((x = in.read()) != -1) {
			saida[j] = (byte) x;
			j++;
		}
		DataOutputStream dos = new DataOutputStream(new FileOutputStream("C:/Users/bruno/eclipse-workspace/trabalhoGBD/DataBase/Arquivos/" + ind.getPagina()));
		for (int h = 0; h < j; h++)
			dos.write(saida[h]);
		i.close();
		in.close();
		dos.close();
		Indice i1 = new Indice(c.getTabela(), c);
		i1.criaIndice();
	}
}
