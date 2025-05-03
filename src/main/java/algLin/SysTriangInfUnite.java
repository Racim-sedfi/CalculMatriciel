package algLin;

//Classe SysTriangInfUnite
/**
 * <p>SysTriangInfUnite class.</p>
 *
 * @author racim
 * @version $Id: $Id
 */
public class SysTriangInfUnite extends SysTriangInf {
	/**
	 * Constructeur de la classe
	 *
	 * @param matriceSystem a {@link algLin.Matrice} object
	 * @param secondMembre a {@link algLin.Vecteur} object
	 * @throws algLin.IrregularSysLinException Si le système est irrégulier ou si un pivot nul est détecté.
	 */
	public SysTriangInfUnite(Matrice matriceSystem, Vecteur secondMembre) throws IrregularSysLinException {
		super(matriceSystem, secondMembre);
	}

	/**
	 * {@inheritDoc}
	 *
	 * Methode qui donne la solution d'un système diagonale inf construit que de 1
	 */
	@Override
	public Vecteur resolution() {
		Vecteur solution = new Vecteur(super.getOrder());

		for (int i = 0; i < super.getOrder(); i++) { // Correction : on part de 0
			double sum = 0.0;
			for (int j = 0; j < i; j++) {
				sum += matriceSystem.getCoef(i, j) * solution.getValeur(j);
			}
			double valeur = secondMembre.getValeur(i) - sum; // Pas de division, car diagonale = 1
			solution.setValeur(i, valeur);
		}
		return solution;
	}

	/**
	 * <p>main.</p>
	 *
	 * @param args an array of {@link java.lang.String} objects
	 */
	public static void main(String[] args) {
		try {
			Matrice mat = new Matrice(new double[][] { { 1, 0, 0 }, { 1, 1, 0 }, { 4, 5, 1 } });
			Vecteur vec = new Vecteur(new double[] { 6, 12, 20 });
			SysTriangInfUnite sys = new SysTriangInfUnite(mat, vec);

			// Résolution du système
			Vecteur solution = sys.resolution();
			System.out.println("Solution du système triangulaire inférieur unité :\n" + solution);

			// Vérification de la solution
			Vecteur Ax = Matrice.produitVecteur(mat, solution);
			Vecteur difference = Vecteur.soustraction(Ax, vec);
			double norme = difference.normeL2();
			System.out.println("Norme L2 de (Ax - b) : " + norme);

			// Vérification avec EPSILON
			if (norme < Matrice.EPSILON) {
				System.out.println("La solution est correcte (norme < EPSILON).");
			} else {
				System.out.println("Attention : la solution pourrait être incorrecte (norme >= EPSILON).");
			}
		} catch (IrregularSysLinException e) {
			System.out.println(e);
		} catch (Exception e) {
			System.out.println("Erreur lors de la vérification de la solution : " + e.getMessage());
		}
	}

}
