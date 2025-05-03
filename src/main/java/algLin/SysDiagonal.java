package algLin;

/**
 * <p>SysDiagonal class.</p>
 *
 * @author racim
 * @version $Id: $Id
 */
public class SysDiagonal extends SysLin {
	/**
	 * <p>Constructor for SysDiagonal.</p>
	 *
	 * @param matrice a {@link algLin.Matrice} object
	 * @param vecteur a {@link algLin.Vecteur} object
	 * @throws algLin.IrregularSysLinException if any.
	 */
	public SysDiagonal(Matrice matrice, Vecteur vecteur) throws IrregularSysLinException {
		super(matrice, vecteur);
	}

	/** {@inheritDoc} */
	@Override
	public Vecteur resolution() throws IrregularSysLinException {
		Vecteur solution = new Vecteur(super.getOrder());

		for (int i = 0; i < super.getOrder(); i++) {
			double diagCoef = matriceSystem.getCoef(i, i);
			if (diagCoef == 0) {
				throw new IrregularSysLinException("Élément diagonal nul à l'indice " + i);
			}
			double valeur = secondMembre.getValeur(i) / diagCoef;
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
			Matrice mat = new Matrice(new double[][] { { 2, 0, 0 }, { 0, 3, 0 }, { 0, 0, 4 } });
			Vecteur vec = new Vecteur(new double[] { 6, 12, 20 });
			SysDiagonal sys = new SysDiagonal(mat, vec);

			// Résolution du système
			Vecteur solution = sys.resolution();
			System.out.println("Solution du système diagonal :\n" + solution);

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
