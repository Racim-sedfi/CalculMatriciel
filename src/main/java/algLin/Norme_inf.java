package algLin;

/**
 * Implémentation de la norme infinie pour une matrice.
 * La norme infinie est définie comme le maximum des sommes des valeurs absolues des lignes de la matrice.
 *
 * @author racim
 * @version $Id: $Id
 */
public class Norme_inf implements NormeGenerale {
    
    /**
     * {@inheritDoc}
     *
     * Calcule la norme infinie d'une matrice donnée.
     */
    @Override
    public double norme(Matrice a) {
        double maxSum = 0;
        for (int i = 0; i < a.nbLigne(); i++) {
            double rowSum = 0;
            for (int j = 0; j < a.nbColonne(); j++) {
                rowSum += Math.abs(a.getCoef(i, j));
            }
            maxSum = Math.max(maxSum, rowSum);
        }
        return maxSum;
    }
}
