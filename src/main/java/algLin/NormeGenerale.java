package algLin;

/**
 * Interface définissant une méthode générale pour calculer une norme de matrice.
 *
 * @author racim
 * @version $Id: $Id
 */
public interface NormeGenerale {
    /**
     * Calcule la norme d'une matrice.
     *
     * @param m La matrice dont on veut calculer la norme.
     * @return La valeur de la norme choisie.
     */
    double norme(Matrice m);
}
