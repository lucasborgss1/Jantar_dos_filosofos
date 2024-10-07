package entidades;

public class Mesa {
	final static int PENSANDO = 1;
	final static int COMENDO = 2;
	final static int FOME = 3;
	final static int NR_FILOSOFOS = 5;
	final static int PRIMEIRO_FILOSOFO = 0;
	final static int ULTIMO_FILOSOFO = NR_FILOSOFOS - 1;
	boolean[] garfos = new boolean[NR_FILOSOFOS];
	int[] filosofos = new int[NR_FILOSOFOS];
	int[] tentativas = new int[NR_FILOSOFOS];
	int[] quantidadeComidas = new int[NR_FILOSOFOS];

	public Mesa() {
		for (int i = 0; i < 5; i++) {
			garfos[i] = true;
			filosofos[i] = PENSANDO;
			tentativas[i] = 0;
			quantidadeComidas[i] = 0;
		}
	}

	public synchronized void pegarGarfos(int filosofo) {
		filosofos[filosofo] = FOME;
		while (filosofos[aEsquerda(filosofo)] == COMENDO || filosofos[aDireita(filosofo)] == COMENDO) {
			try {
				tentativas[filosofo]++;
				wait();
				if (tentativas[filosofo] > 10) {
					System.out.println("O Filósofo " + filosofo + " morreu devido a starvation.");
				}
			} catch (InterruptedException e) {
			}
		}
		
		tentativas[filosofo] = 0;
		garfos[garfoEsquerdo(filosofo)] = false;
		garfos[garfoDireito(filosofo)] = false;
		filosofos[filosofo] = COMENDO;
		imprimeEstadosFilosofos();
		imprimeGarfos();
		imprimeTentativas();
		imprimeQuantidadeComidas();
	}

	public synchronized void returningGarfos(int filosofo) {
		garfos[garfoEsquerdo(filosofo)] = true;
		garfos[garfoDireito(filosofo)] = true;
		quantidadeComidas[filosofo]++;
		if (filosofos[aEsquerda(filosofo)] == FOME || filosofos[aDireita(filosofo)] == FOME) {
			notifyAll();
		}
		filosofos[filosofo] = PENSANDO;
	}
	
	public void imprimeQuantidadeComidas() {
	      System.out.print("Quantidade de refeições = [ ");
	      for (int i = 0; i < NR_FILOSOFOS; i++) {
	         System.out.print(quantidadeComidas[i] + " ");
	      }
	      System.out.println("]");
	   }

	public int aDireita(int filosofo) {
		int direito;
		if (filosofo == ULTIMO_FILOSOFO) {
			direito = PRIMEIRO_FILOSOFO;
		} else {
			direito = filosofo + 1;
		}
		return direito;
	}

	public int aEsquerda(int filosofo) {
		int esquerdo;
		if (filosofo == PRIMEIRO_FILOSOFO) {
			esquerdo = ULTIMO_FILOSOFO;
		} else {
			esquerdo = filosofo - 1;
		}
		return esquerdo;
	}

	public int garfoEsquerdo(int filosofo) {
		int garfoEsquerdo = filosofo;
		return garfoEsquerdo;
	}

	public int garfoDireito(int filosofo) {
		int garfoDireito;
		if (filosofo == ULTIMO_FILOSOFO) {
			garfoDireito = 0;
		} else {
			garfoDireito = filosofo + 1;
		}
		return garfoDireito;
	}

	public void imprimeEstadosFilosofos() {
		String texto = "*";
		System.out.print("Filósofos = [ ");
		for (int i = 0; i < NR_FILOSOFOS; i++) {
			switch (filosofos[i]) {
			case PENSANDO:
				texto = "PENSANDO";
				break;
			case FOME:
				texto = "FOME";
				break;
			case COMENDO:
				texto = "COMENDO";
				break;
			}
			System.out.print(texto + " ");
		}
		System.out.println("]");
	}

	public void imprimeGarfos() {
		String garfo = "*";
		System.out.print("Garfos = [ ");
		for (int i = 0; i < NR_FILOSOFOS; i++) {
			if (garfos[i]) {
				garfo = "LIVRE";
			} else {
				garfo = "OCUPADO";
			}
			System.out.print(garfo + " ");
		}
		System.out.println("]");
	}

	public void imprimeTentativas() {
		System.out.print("Tentou comer = [ ");
		for (int i = 0; i < NR_FILOSOFOS; i++) {
			System.out.print(tentativas[i] + " ");
		}
		System.out.println("]");
	}
}
