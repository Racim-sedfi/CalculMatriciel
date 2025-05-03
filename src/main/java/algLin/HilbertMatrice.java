package algLin;

import java.util.Scanner;

public class HilbertMatrice extends Matrice {
  public HilbertMatrice(int n) {
    super(n, n);
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        this.remplacecoef(i, j, 1.0 / (i + j + 1));
      }
    }
  }

  public static void main(String[] args) throws Exception {
    /*Scanner sc = new Scanner(System.in);
    System.out.print("Entrez l'ordre de la matrice de Hilbert : ");
    int n = sc.nextInt();
    sc.close();

    HilbertMatrice H = new HilbertMatrice(n);
    System.out.println("Matrice de Hilbert H :\n" + H);

    double cond1 = H.cond_1();
    if (cond1 > 1e8) {
      System.out.println("! Attention : Conditionnement très élevé (" + cond1 + "), l'inversion peut être imprécise.");
    } else {
      Matrice H_inv = H.inverse();
      System.out.println("Inverse de la matrice de Hilbert H⁻¹ :\n" + H_inv);

      Matrice produit = Matrice.produit(H, H_inv);
      System.out.println("Produit H * H⁻¹ :\n" + produit);

      Matrice identite = matriceIdentite(n);

      Matrice erreur = Matrice.verif_produit(H, H_inv);
      double normeErreur = erreur.norme_1();
      System.out.println("Norme de l'erreur ||H * H⁻¹ - I||_1 : " + normeErreur);

      double condInf = H.cond_inf();
      System.out.println("Conditionnement norme-1 de H : " + cond1);
      System.out.println("Conditionnement norme-inf de H : " + condInf);
    }
    */
    System.out.println("\nTests des matrices de Hilbert de n = 3 à 15");
    for (int k = 3; k <= 15; k++) {
        HilbertMatrice Hk = new HilbertMatrice(k);
        double cond1_k = 0;
        double condInf_k = 0;

        try {
            // Calcul du conditionnement norme-1
            cond1_k = Hk.cond_1();
            // Calcul du conditionnement norme-inf
            condInf_k = Hk.cond_inf();
        } catch (Exception e) {
            System.out.println("\nOrdre " + k + " :");
            System.out.println("! Attention : Conditionnement trop élevé ou matrice singulière.");
            continue;
        }

        System.out.println("\nOrdre " + k + " :");
        System.out.println("Conditionnement norme-1 : " + cond1_k);
        System.out.println("Conditionnement norme-inf : " + condInf_k);

        if (cond1_k > 1e8 || condInf_k > 1e8) {
            System.out.println("! Attention : Conditionnement très élevé, l'inversion peut être imprécise.");
        }

    }
  }
}
