package etapa2;

import java.util.Collections;
import java.util.List;

public class ItemIndice implements Comparable<ItemIndice> {
	public int posicao;
	public int chave;
	public String pagina;
	public int slot;

	public int getChave() {
		return chave;
	}

	public void setChave(int chave) {
		this.chave = chave;
	}

	public String getPagina() {
		return pagina;
	}

	public void setPagina(String pagina) {
		this.pagina = pagina;
	}

	public int getPosicao() {
		return posicao;
	}

	public void setPosicao(int posicao) {
		this.posicao = posicao;
	}

	public int getSlot() {
		return slot;
	}

	public void setSlot(int slot) {
		this.slot = slot;
	}

	public ItemIndice(int posicao, int chave, String pagina, int slot) {
		this.posicao = posicao;
		this.chave = chave;
		this.pagina = pagina;
		this.slot = slot;
	}

	void sort(List<ItemIndice> lista) {
		Collections.sort(lista, null);
		System.out.println(lista);
	}

	@Override
	public int compareTo(ItemIndice item) {
		if (this.getPosicao() < item.getPosicao()) {
			return -1;
		} else if (this.getPosicao() == item.getPosicao()) {
			return 0;
		}
		return 1;
	}
}
