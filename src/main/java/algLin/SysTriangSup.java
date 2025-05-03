package algLin;

//Classe SysTriangSup
/**
 * <p>SysTriangSup class.</p>
 *
 * @author racim
 * @version $Id: $Id
 */
public class SysTriangSup extends SysLin {
	/**
	 * Constructeur de la classe
	 *
	 * @param matriceSystem a {@link algLin.Matrice} object
	 * @param secondMembre a {@link algLin.Vecteur} object
	 * @throws algLin.IrregularSysLinException Si le système est irrégulier ou si un pivot nul est détecté.
	 */
	public SysTriangSup(Matrice matriceSystem, Vecteur secondMembre) throws IrregularSysLinException {
		super(matriceSystem, secondMembre);
	}

	/**
	 * {@inheritDoc}
	 *
	 * Methode qui resout un système diagonale superieure
	 */
	@Override
	public Vecteur resolution() throws IrregularSysLinException {
		Vecteur solution = new Vecteur(super.getOrder());

		for (int i = super.getOrder() - 1; i >= 0; i--) {
			double sum = 0.0;
			for (int j = i + 1; j < super.getOrder(); j++) {
				sum += matriceSystem.getCoef(i, j) * solution.getValeur(j);
			}
			double diagCoef = matriceSystem.getCoef(i, i);
			if (diagCoef == 0) {
				throw new IrregularSysLinException("Élément diagonal nul à l'indice " + i);
			}
			double valeur = (secondMembre.getValeur(i) - sum) / diagCoef;
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
			Matrice mat = new Matrice(new double[][] { { 2, 1, 4 }, { 0, 3, 5 }, { 0, 0, 6 } });
			Vecteur vec = new Vecteur(new double[] { 6, 12, 20 });
			SysTriangSup sys = new SysTriangSup(mat, vec);

			// Résolution du système
			Vecteur solution = sys.resolution();
			System.out.println("Solution du système triangulaire supérieur :\n" + solution);

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
