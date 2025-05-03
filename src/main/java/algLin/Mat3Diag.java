package algLin;
/**
 * <p>Mat3Diag class.</p>
 *
 * @author azedi
 * @version $Id: $Id
 */
public class Mat3Diag extends Matrice {

	/**
	 * Constructeur qui crée une matrice de dimensions 3 x dim en s'assurant que
	 * dim1 == 3.
	 *
	 * @param dim1 Nombre de lignes (doit être 3)
	 * @param dim2 Nombre de colonnes (dimension du système)
	 * @throws algLin.IrregularSysLinException si dim1 ≠ 3
	 */
	public Mat3Diag(int dim1, int dim2) throws IrregularSysLinException {
		super(dim1, dim2);
		if (dim1 != 3) {
			throw new IrregularSysLinException("La première dimension doit être 3 pour une matrice tridiagonale.");
		}
	}

	/**
	 * Constructeur qui initialise une matrice tridiagonale à partir d'un tableau
	 * donné.
	 *
	 * @param tableau Tableau de coefficients (doit être de dimension 3 x n)
	 * @throws algLin.IrregularSysLinException si le tableau ne respecte pas ces dimensions
	 */
	public Mat3Diag(double[][] tableau) throws IrregularSysLinException {
		super(tableau);
		if (tableau.length != 3) {
			throw new IrregularSysLinException("Le tableau doit contenir exactement 3 lignes.");
		}
		int n = tableau[0].length;
		for (int i = 1; i < 3; i++) {
			if (tableau[i].length != n) {
				throw new IrregularSysLinException("Toutes les lignes du tableau doivent avoir la même taille.");
			}
		}
	}

	/**
	 * Constructeur spécifique aux matrices tridiagonales d'ordre n. Crée une
	 * matrice 3 x n contenant uniquement des zéros.
	 *
	 * @param dim Dimension du système (ordre n)
	 */
	public Mat3Diag(int dim) {
		super(3, dim); // 3 lignes (sous-diag, diag, sur-diag) et dim colonnes
	}

	/**
	 * Effectue le produit d'une matrice tridiagonale avec un vecteur. Optimisé pour
	 * éviter des calculs inutiles.
	 *
	 * @param mat  Matrice tridiagonale d'ordre n
	 * @param vect Vecteur de dimension n
	 * @return Le vecteur résultant du produit Mat3Diag * Vecteur
	 * @throws java.lang.Exception Si les dimensions sont incompatibles
	 */
	public static Vecteur produitVect(Mat3Diag mat, Vecteur vect) throws Exception {
		int n = mat.nbColonne();
		if (vect.taille() != n) {
			throw new Exception("Les dimensions de la matrice et du vecteur ne correspondent pas.");
		}

		Vecteur resultat = new Vecteur(n);

		// Produit spécifique pour une matrice tridiagonale
		for (int i = 0; i < n; i++) {
			double somme = 0.0;

			// Sous-diagonale
			if (i > 0) {
				somme += mat.getCoef(0, i) * vect.getValeur(i - 1);
			}
			// Diagonale principale
			somme += mat.getCoef(1, i) * vect.getValeur(i);
			// Sur-diagonale
			if (i < n - 1) {
				somme += mat.getCoef(2, i) * vect.getValeur(i + 1);
			}

			resultat.remplacecoef(i, 0, somme);
		}
		return resultat;
	}

 
}
