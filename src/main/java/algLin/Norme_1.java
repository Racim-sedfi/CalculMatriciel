package algLin;

/**
 * Implémentation de la norme 1 pour une matrice.
 * La norme 1 est définie comme le maximum des sommes des valeurs absolues des colonnes de la matrice.
 *
 * @author racim
 * @version $Id: $Id
 */
public class Norme_1 implements NormeGenerale {
    
        /**
         * {@inheritDoc}
         *
         * Calcule la norme 1 d'une matrice donnée.
         */
    @Override
        public double norme(Matrice A) {
            double maxSum = 0;
            for (int j = 0; j < A.nbColonne(); j++) {
                double colSum = 0;
                for (int i = 0; i < A.nbLigne(); i++) {
                    colSum += Math.abs(A.getCoef(i, j));
                }
                maxSum = Math.max(maxSum, colSum);
            }
            return maxSum;
        }
}
