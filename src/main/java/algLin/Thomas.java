package algLin;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * <p>Thomas class.</p>
 *
 * @author azedi
 * @version $Id: $Id
 */
public class Thomas extends SysLin {

    private Mat3Diag matTridiag;

    /**
     * <p>Constructor for Thomas.</p>
     *
     * @param m a {@link algLin.Mat3Diag} object
     * @param secondMembre a {@link algLin.Vecteur} object
     * @throws algLin.IrregularSysLinException if any.
     */
    public Thomas(Mat3Diag m, Vecteur secondMembre) throws IrregularSysLinException {
        // Appel explicite au constructeur de SysLin en première instruction
        super(new Matrice(secondMembre.taille(), secondMembre.taille()), secondMembre);
        this.matTridiag = m;
        this.ordre = secondMembre.taille();
    }

    /**
     * {@inheritDoc}
     *
     * Méthode standard en double utilisant des objets Vecteur pour stocker p, q et x.
     */
    @Override
    public Vecteur resolution() throws IrregularSysLinException {
        int n = this.ordre;
        Vecteur p = new Vecteur(n); // Stocke la suite p
        Vecteur q = new Vecteur(n); // Stocke la suite q
        Vecteur x = new Vecteur(n); // Stocke la solution

        // Initialisation : équation 1 (indice 0)
        double b0 = this.matTridiag.coefficient[1][0];
        if (Math.abs(b0) < Matrice.EPSILON) {
            throw new IrregularSysLinException("Division par zéro détectée à l'indice 0.");
        }
        p.setValeur(0, - this.matTridiag.coefficient[2][0] / b0);
        q.setValeur(0, this.getSecondMembre().getValeur(0) / b0);

        // Forward sweep : pour i de 1 à n-2
        for (int i = 1; i < n - 1; i++) {
            double a_i = this.matTridiag.coefficient[0][i]; // a_(i+1)
            double b_i = this.matTridiag.coefficient[1][i]; // b_(i+1)
            double c_i = this.matTridiag.coefficient[2][i]; // c_(i+1)
            double beta = a_i * p.getValeur(i - 1) + b_i;
            if (Math.abs(beta) < Matrice.EPSILON) {
                throw new IrregularSysLinException("Division par zéro détectée à l'indice " + i);
            }
            p.setValeur(i, - c_i / beta);
            q.setValeur(i, (this.getSecondMembre().getValeur(i) - a_i * q.getValeur(i - 1)) / beta);
        }

        // Dernière équation : i = n-1
        int i = n - 1;
        double a_i = this.matTridiag.coefficient[0][i];
        double b_i = this.matTridiag.coefficient[1][i];
        double beta = a_i * p.getValeur(i - 1) + b_i;
        if (Math.abs(beta) < Matrice.EPSILON) {
            throw new IrregularSysLinException("Division par zéro détectée lors du calcul de x[" + i + "].");
        }
        x.setValeur(i, (this.getSecondMembre().getValeur(i) - a_i * q.getValeur(i - 1)) / beta);

        // Back substitution : pour i de n-2 à 0
        for (i = n - 2; i >= 0; i--) {
            x.setValeur(i, p.getValeur(i) * x.getValeur(i + 1) + q.getValeur(i));
        }

        return x;
    }

    /**
     * Méthode utilisant BigDecimal pour une résolution à haute précision.
     * Les calculs intermédiaires se font avec BigDecimal, puis le résultat est converti en Vecteur.
     *
     * @return a {@link algLin.Vecteur} object
     * @throws algLin.IrregularSysLinException if any.
     */
    public Vecteur resolutionPrecise() throws IrregularSysLinException {
        int n = this.ordre;
        BigDecimal[] p = new BigDecimal[n];
        BigDecimal[] q = new BigDecimal[n];
        BigDecimal[] x = new BigDecimal[n];
        int scale = 20; // Précision en décimales
        RoundingMode rm = RoundingMode.HALF_UP;

        // Initialisation pour i = 0
        BigDecimal b0 = BigDecimal.valueOf(this.matTridiag.coefficient[1][0]);
        if (b0.abs().compareTo(BigDecimal.valueOf(Matrice.EPSILON)) < 0) {
            throw new IrregularSysLinException("Division par zéro détectée à l'indice 0.");
        }
        BigDecimal c0 = BigDecimal.valueOf(this.matTridiag.coefficient[2][0]);
        p[0] = c0.negate().divide(b0, scale, rm);  // p0 = - c0/b0
        BigDecimal d0 = BigDecimal.valueOf(this.getSecondMembre().getValeur(0));
        q[0] = d0.divide(b0, scale, rm);

        // Forward sweep pour i de 1 à n-2
        for (int i = 1; i < n - 1; i++) {
            BigDecimal a_i = BigDecimal.valueOf(this.matTridiag.coefficient[0][i]);
            BigDecimal b_i = BigDecimal.valueOf(this.matTridiag.coefficient[1][i]);
            BigDecimal c_i = BigDecimal.valueOf(this.matTridiag.coefficient[2][i]);
            BigDecimal beta = a_i.multiply(p[i - 1]).add(b_i);
            if (beta.abs().compareTo(BigDecimal.valueOf(Matrice.EPSILON)) < 0) {
                throw new IrregularSysLinException("Division par zéro détectée à l'indice " + i);
            }
            p[i] = c_i.negate().divide(beta, scale, rm);
            BigDecimal d_i = BigDecimal.valueOf(this.getSecondMembre().getValeur(i));
            q[i] = d_i.subtract(a_i.multiply(q[i - 1])).divide(beta, scale, rm);
        }

        // Dernière équation
        int iFinal = n - 1;
        BigDecimal a_i = BigDecimal.valueOf(this.matTridiag.coefficient[0][iFinal]);
        BigDecimal b_i = BigDecimal.valueOf(this.matTridiag.coefficient[1][iFinal]);
        BigDecimal beta = a_i.multiply(p[iFinal - 1]).add(b_i);
        if (beta.abs().compareTo(BigDecimal.valueOf(Matrice.EPSILON)) < 0) {
            throw new IrregularSysLinException("Division par zéro détectée lors du calcul de x[" + iFinal + "].");
        }
        BigDecimal d_i = BigDecimal.valueOf(this.getSecondMembre().getValeur(iFinal));
        x[iFinal] = d_i.subtract(a_i.multiply(q[iFinal - 1])).divide(beta, scale, rm);

        // Back substitution pour i de n-2 à 0
        for (iFinal = n - 2; iFinal >= 0; iFinal--) {
            x[iFinal] = p[iFinal].multiply(x[iFinal + 1]).add(q[iFinal]);
        }

        // Conversion du résultat en double
        double[] result = new double[n];
        for (int i = 0; i < n; i++) {
            result[i] = x[i].doubleValue();
        }
        return new Vecteur(result);
    }

    // Méthode main pour tester l'exemple du TD2 avec vérifications complémentaires
    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects
     */
    public static void main(String[] args) {
        try {
            int n = 4;  // Système de 4 équations
            // Création d'une matrice tridiagonale : tableau de 3 lignes et n colonnes
            Mat3Diag m = new Mat3Diag(n);
            // Affectation des coefficients pour chaque équation
            // Équation 1 : a₁ = 0, b₁ = 2, c₁ = -1
            m.coefficient[0][0] = 0;
            m.coefficient[1][0] = 2;
            m.coefficient[2][0] = -1;

            // Équation 2 : a₂ = -1, b₂ = 2, c₂ = -1
            m.coefficient[0][1] = -1;
            m.coefficient[1][1] = 2;
            m.coefficient[2][1] = -1;

            // Équation 3 : a₃ = -1, b₃ = 2, c₃ = -1
            m.coefficient[0][2] = -1;
            m.coefficient[1][2] = 2;
            m.coefficient[2][2] = -1;

            // Équation 4 : a₄ = -1, b₄ = 2, c₄ = 0
            m.coefficient[0][3] = -1;
            m.coefficient[1][3] = 2;
            m.coefficient[2][3] = 0;

            // Second membre d = (-2, -2, -2, 23)
            double[] d = { -2, -2, -2, 23 };
            Vecteur secondMembre = new Vecteur(d);

            // Création du système avec l'algorithme de Thomas
            Thomas systeme = new Thomas(m, secondMembre);

            // Résolution standard (double) avec utilisation de Vecteur pour p, q et x
            Vecteur solutionStandard = systeme.resolution();
            System.out.println("Solution standard (double) (attendue : 1, 4, 9, 16) :");
            System.out.println(solutionStandard);

            // Résolution à haute précision avec BigDecimal
            Vecteur solutionPrecise = systeme.resolutionPrecise();
            System.out.println("Solution haute précision (BigDecimal) :");
            System.out.println(solutionPrecise);

            // Vérification 1 : Calcul du produit de la matrice tridiagonale par la solution haute précision
            Vecteur produit = Mat3Diag.produitVect(m, solutionPrecise);
            double diffNorme = 0.0;
            for (int i = 0; i < n; i++) {
                double diff = produit.getValeur(i) - secondMembre.getValeur(i);
                diffNorme += diff * diff;
            }
            diffNorme = Math.sqrt(diffNorme);
            System.out.println("Norme L2 du résiduel (M*x - d) : " + diffNorme);

            // Vérification 2 : Comparaison avec la solution attendue : (1, 4, 9, 16)
            double[] solutionAttendue = {1, 4, 9, 16};
            boolean correct = true;
            for (int i = 0; i < n; i++) {
                double erreur = Math.abs(solutionPrecise.getValeur(i) - solutionAttendue[i]);
                System.out.println("Erreur à l'indice " + i + " : " + erreur);
                if (erreur > Matrice.EPSILON) {
                    correct = false;
                }
            }
            if (correct) {
                System.out.println("La solution haute précision correspond à la solution attendue.");
            } else {
                System.out.println("La solution haute précision diffère de la solution attendue.");
            }
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }
}
