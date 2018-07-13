package etapa1;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Arquivo {
	private int tamanhoPagina = 4096; 
	private String caminho = "C:/Users/bruno/eclipse-workspace/trabalhoGBD/DataBase/Arquivos/";
	private int pagina = 0;

	public void carregaDados(Catalogo c1, File dados) throws FileNotFoundException, IOException {
		String[] tipos = c1.getTipos();
		Scanner sc = null;
		String temp;

		String[] registro = new String[tipos.length + 1];
		ArrayList<String[]> Pagina = new ArrayList();
		try {
			sc = new Scanner(new FileReader(new File(c1.tabela + ".txt")));
			int cont = 0;
			int tamanhopg = 0;
			int tamanhoreg = 0;
			int numRegs = 0;
			while (sc.hasNext()) {
				sc.useDelimiter(";");
				temp = sc.next();
				if (temp.length() > 2 && temp.substring(0, 2).equals("\r\n")) {
					temp = temp.substring(2, temp.length());
				}
				registro[cont] = temp;
				System.out.println(registro[cont]);
				if (tipos[cont].equals("integer")) {
					tamanhoreg += 4;
				} else if (tipos[cont].equals("small_int")) {
					tamanhoreg += 2;
				} else if (tipos[cont].equals("char")) {
					tamanhoreg += 2;
				} else if (tipos[cont].equals("varchar")) {
					tamanhoreg += 2 + 2 * temp.length();
				}

				cont++;

				if (cont > c1.getNumAtributos() - 1) {
					System.out.println("reg " + tamanhoreg + "pg" + tamanhopg + "n" + numRegs);
					registro[cont] = "" + tamanhoreg;
					cont = 0;
					System.out.println("");
					if ((2 * (numRegs + 1)) + tamanhoreg + tamanhopg < tamanhoPagina) {
						tamanhopg += tamanhoreg;
						tamanhoreg = 0;
						numRegs += 1;
						Pagina.add(registro);
						registro = new String[tipos.length + 1];
						System.out.println("Pagina: " + tamanhopg);
					} else {
						DataOutputStream dos = new DataOutputStream(
								new FileOutputStream(caminho + c1.getTabela() + pagina + ".bin"));
						numRegs = Pagina.size();
						pagina++;
						String[] posRegs = new String[numRegs];
						int tamanhopagina = 0;
						int j = 0;
						for (String[] regs : Pagina) {

							posRegs[j] = "" + tamanhopagina;
							tamanhopagina += Integer.parseInt(regs[tipos.length]);
							for (int i = 0; i < tipos.length; i++) {
								if (tipos[i].equals("integer")) {
									dos.writeInt(Integer.parseInt(regs[i]));
								} else if (tipos[i].equals("small_int")) {
									dos.writeShort(Integer.parseInt(regs[i]));
								} else if (tipos[i].equals("char")) {
									dos.writeChar(regs[i].charAt(0));
								} else if (tipos[i].equals("varchar")) {
									dos.write(regs[i].length());
									dos.writeBytes(regs[i]);
								}
							}
							j++;
						}
						for (int i = 0; i < tamanhoPagina - (tamanhopagina + 2 * numRegs + 4); i++) {
							dos.writeInt(Integer.parseInt("0"));
						}
						for (int i = numRegs - 1; i >= 0; i--) {
							dos.writeInt(Integer.parseInt(posRegs[i]));
							dos.writeInt(Integer.parseInt("0"));
						}
						dos.writeInt(numRegs);
						dos.writeInt(tamanhopagina);
						dos.flush();
						dos.close();
						Pagina.clear();
						tamanhopg = tamanhoreg;
						tamanhoreg = 0;
						numRegs = 1;
						Pagina.add(registro);
					}
				}
			}
			c1.setPagina(pagina);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}