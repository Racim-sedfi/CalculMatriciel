package algLin;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Scanner;

public class Matrice {
  final static double EPSILON = 1.0E-06;
  /** Définir ici les attributs de la classe **/
  protected double coefficient[][];

  /** Définir ici les constructeur de la classe **/
  Matrice(int nbligne, int nbcolonne) {
    this.coefficient = new double[nbligne][nbcolonne];
  }

  Matrice(double[][] tableau) {
    coefficient = tableau;
  }

  Matrice(String fichier) {
    try {
      Scanner sc = new Scanner(new File(fichier));
      int ligne = sc.nextInt();
      int colonne = sc.nextInt();
      this.coefficient = new double[ligne][colonne];
      for (int i = 0; i < ligne; i++)
        for (int j = 0; j < colonne; j++)
          this.coefficient[i][j] = sc.nextDouble();
      sc.close();

    } catch (FileNotFoundException e) {
      System.out.println("Fichier absent");
    }
  }

  /** Definir ici les autres methodes */

  public void recopie(Matrice arecopier) {
    int ligne, colonne;
    ligne = arecopier.nbLigne();
    colonne = arecopier.nbColonne();
    this.coefficient = new double[ligne][colonne];
    for (int i = 0; i < ligne; i++)
      for (int j = 0; j < colonne; j++)
        this.coefficient[i][j] = arecopier.coefficient[i][j];
  }

  public int nbLigne() {
    return this.coefficient.length;
  }

  public int nbColonne() {
    return this.coefficient[0].length;
  }

  public double getCoef(int ligne, int colonne) {
    return this.coefficient[ligne][colonne];
  }

  public void remplacecoef(int ligne, int colonne, double value) {
    this.coefficient[ligne][colonne] = value;
  }

  public String toString() {
    int ligne = this.nbLigne();
    int colonne = this.nbColonne();
    String matr = "";
    for (int i = 0; i < ligne; i++) {
      for (int j = 0; j < colonne; j++) {
        if (j == 0) {
          matr += this.getCoef(i, j);
        } else {
          matr += " " + this.getCoef(i, j);
        }
      }
      matr += "\n";
    }
    return matr;
  }

  public Matrice produit(double scalaire) {
    int ligne = this.nbLigne();
    int colonne = this.nbColonne();
    for (int i = 0; i < ligne; i++)
      for (int j = 0; j < colonne; j++)
        this.coefficient[i][j] *= scalaire;
    return this;
  }

  static Matrice addition(Matrice a, Matrice b) {
    int ligne = a.nbLigne();
    int colonne = a.nbColonne();
    Matrice mat = new Matrice(ligne, colonne);
    for (int i = 0; i < ligne; i++)
      for (int j = 0; j < colonne; j++)
        mat.coefficient[i][j] = a.coefficient[i][j] + b.coefficient[i][j];
    return mat;
  }

  static Matrice verif_addition(Matrice a, Matrice b) throws Exception {
    if ((a.nbLigne() == b.nbLigne()) && (a.nbColonne() == b.nbColonne())) {
      int ligne = a.nbLigne();
      int colonne = a.nbColonne();
      Matrice mat = new Matrice(ligne, colonne);
      for (int i = 0; i < ligne; i++)
        for (int j = 0; j < colonne; j++)
          mat.coefficient[i][j] = a.coefficient[i][j] + b.coefficient[i][j];
      return mat;
    } else {
      throw new Exception("Les deux matrices n'ont pas les mêmes dimensions !!!");
    }
  }

  static Matrice produit(Matrice a, Matrice b) {
    int ligne, colonne;
    ligne = a.nbLigne();
    colonne = b.nbColonne();
    Matrice mat = new Matrice(ligne, colonne);
    for (int i = 0; i < ligne; i++)
      for (int j = 0; j < colonne; j++) {
        mat.coefficient[i][j] = 0;
        for (int k = 0; k < a.nbColonne(); k++)
          mat.coefficient[i][j] += a.coefficient[i][k] * b.coefficient[k][j];
      }
    return mat;
  }

  static Matrice verif_produit(Matrice a, Matrice b) throws Exception {
    int ligne = 0;
    int colonne = 0;
    if (a.nbColonne() == b.nbLigne()) {
      ligne = a.nbLigne();
      colonne = b.nbColonne();
    } else {
      throw new Exception("Dimensions des matrices à multiplier incorrectes");
    }

    Matrice mat = new Matrice(ligne, colonne);
    for (int i = 0; i < ligne; i++)
      for (int j = 0; j < colonne; j++) {
        mat.coefficient[i][j] = 0;
        for (int k = 0; k < a.nbColonne(); k++)
          mat.coefficient[i][j] += a.coefficient[i][k] * b.coefficient[k][j];
      }
    return mat;
  }

  public static Vecteur produitVecteur(Matrice mat, Vecteur vec) throws Exception {
    if (mat.nbColonne() != vec.taille()) {
      throw new Exception("Dimensions incompatibles pour le produit matrice-vecteur.");
    }
    Vecteur resultat = new Vecteur(mat.nbLigne());
    for (int i = 0; i < mat.nbLigne(); i++) {
      double sum = 0.0;
      for (int j = 0; j < mat.nbColonne(); j++) {
        sum += mat.getCoef(i, j) * vec.getCoef(j, 0);
      }
      if (Math.abs(sum) < Matrice.EPSILON) {
        sum = 0.0;
      }
      resultat.remplacecoef(i, 0, sum);
    }
    return resultat;
  }

  // SUITE TP 3
  public Matrice inverse() throws IrregularSysLinException {
    if (Math.abs(this.determinant()) == 0) {
      throw new IrregularSysLinException("La matrice est singulière et ne peut pas être inversée.");
    }

    int n = this.nbLigne();
    Matrice identite = Matrice.matriceIdentite(n);
    Matrice inverse = new Matrice(n, n);

    // Utilisation d'une copie de la matrice pour éviter sa modification
    Matrice copieA = this.copie();
    Helder systeme = new Helder(copieA, new Vecteur(n));
    systeme.factorLDR();

    for (int j = 0; j < n; j++) {
      Vecteur colonneIdentite = new Vecteur(n);
      colonneIdentite.remplacecoef(j, 0, 1.0);

      systeme.setSecondMembre(colonneIdentite);
      Vecteur solution = systeme.resolutionPartielle();

      for (int i = 0; i < n; i++) {
        inverse.remplacecoef(i, j, solution.getCoef(i, 0));
      }
    }

    return inverse;
  }

  public double determinant() throws IrregularSysLinException {
    if (this.nbLigne() != this.nbColonne()) {
      throw new IrregularSysLinException("La matrice doit être carrée pour calculer le déterminant.");
    }

    int n = this.nbLigne();
    if (n == 1) {
      return this.getCoef(0, 0);
    }
    double det = 0;
    for (int j = 0; j < n; j++) {
      Matrice sousMatrice = this.sousMatrice(0, j);
      det += Math.pow(-1, j) * this.getCoef(0, j) * sousMatrice.determinant();
    }
    return det;
  }

  public Matrice sousMatrice(int ligne, int colonne) {
    int n = this.nbLigne();
    Matrice sousMat = new Matrice(n - 1, n - 1);

    int r = 0;
    for (int i = 0; i < n; i++) {
      if (i == ligne)
        continue;
      int c = 0;
      for (int j = 0; j < n; j++) {
        if (j == colonne)
          continue;
        sousMat.remplacecoef(r, c, this.getCoef(i, j));
        c++;
      }
      r++;
    }

    return sousMat;
  }

  public static Matrice matriceIdentite(int n) {
    Matrice identite = new Matrice(n, n);

    for (int i = 0; i < n; i++) {
      identite.coefficient[i][i] = 1.0;
    }

    return identite;
  }

  public Matrice copie() {
    int ligne = this.nbLigne();
    int colonne = this.nbColonne();
    double[][] copieCoeffs = new double[ligne][colonne];

    for (int i = 0; i < ligne; i++) {
      for (int j = 0; j < colonne; j++) {
        copieCoeffs[i][j] = this.coefficient[i][j];
      }
    }

    return new Matrice(copieCoeffs);
  }

  public static Matrice transpose(Matrice m) {
    int lignes = m.nbLigne();
    int colonnes = m.nbColonne();

    // Création d'une nouvelle matrice de dimensions inversées
    Matrice transposée = new Matrice(colonnes, lignes);

    // Remplissage de la matrice transposée
    for (int i = 0; i < lignes; i++) {
      for (int j = 0; j < colonnes; j++) {
        transposée.remplacecoef(j, i, m.getCoef(i, j)); // Inverse les indices
      }
    }

    return transposée;
  }

  public static Matrice soustraction(Matrice a, Matrice b) throws Exception {
    if (a.nbLigne() != b.nbLigne() || a.nbColonne() != b.nbColonne()) {
      throw new Exception("Les deux matrices doivent avoir les mêmes dimensions pour effectuer la soustraction.");
    }

    int ligne = a.nbLigne();
    int colonne = a.nbColonne();
    Matrice resultat = new Matrice(ligne, colonne);

    for (int i = 0; i < ligne; i++) {
      for (int j = 0; j < colonne; j++) {
        resultat.coefficient[i][j] = a.getCoef(i, j) - b.getCoef(i, j);
      }
    }

    return resultat;
  }

  public double norme_1() {
    double max = 0.0;
    for (int j = 0; j < this.nbColonne(); j++) {
      double somme = 0.0;
      for (int i = 0; i < this.nbLigne(); i++) {
        somme += Math.abs(this.getCoef(i, j));
      }
      max = Math.max(somme, max);
    }
    return max;
  }

  public double norme_inf() {
    double max = 0.0;
    for (int i = 0; i < this.nbLigne(); i++) {
      double somme = 0.0;
      for (int j = 0; j < this.nbColonne(); j++) {
        somme += Math.abs(this.getCoef(i, j));
      }
      max = Math.max(max, somme);
    }
    return max;
  }

  public double cond_1() throws IrregularSysLinException {
    Matrice inverseMatrice = this.inverse();
    double norme1A = this.norme_1();
    double norme1AInv = inverseMatrice.norme_1();

    System.out.println("Vérification norme-1 : ||A||_1 = " + norme1A + ", ||A⁻¹||_1 = " + norme1AInv);
    return norme1A * norme1AInv;
  }

  public double cond_inf() throws IrregularSysLinException {
    Matrice inverseMatrice = this.inverse();
    double normeInfA = this.norme_inf();
    double normeInfAInv = inverseMatrice.norme_inf();

    System.out.println("Vérification norme-inf : ||A||_∞ = " + normeInfA + ", ||A⁻¹||_∞ = " + normeInfAInv);
    return normeInfA * normeInfAInv;
  }

  public static void main(String[] args) throws Exception {
    /*
     * // Construction d'une matrice par affectation d'un tableau double mat[][] = {
     * { 2, 1 }, { 0, 1 } }; Matrice a = new Matrice(mat); System.out.
     * println("Construction d'une matrice par affectation d'un tableau :\n" + a);
     * 
     * // Lecture d'un fichier situé dans src/main/resources String fileName =
     * "matrice1.txt"; ClassLoader classLoader =
     * Thread.currentThread().getContextClassLoader(); File fichier = null;
     * 
     * try { URL fileURL = classLoader.getResource(fileName); if (fileURL == null) {
     * throw new FileNotFoundException("Le fichier " + fileName +
     * " est introuvable dans les ressources."); } fichier = new
     * File(fileURL.getFile()); } catch (FileNotFoundException e) {
     * System.err.println("Erreur : " + e.getMessage()); }
     * 
     * // Construction d'une matrice à partir du fichier Matrice b = new
     * Matrice(fichier.getPath()); if (b.coefficient == null) { System.err.
     * println("Erreur lors de la création de la matrice à partir du fichier."); }
     * System.out.println("Construction d'une matrice par lecture d'un fichier :\n"
     * + b);
     * 
     * // Recopie de la matrice b Matrice c = new Matrice(b.nbLigne(),
     * b.nbColonne()); c.recopie(b);
     * System.out.println("Recopie de la matrice b :\n" + c);
     * 
     * // Affichage du nombre de lignes et colonnes de la matrice
     * System.out.println("Nombre de lignes et colonnes de la matrice c : " +
     * c.nbLigne() + ", " + c.nbColonne());
     * 
     * // Accès et modification d'un coefficient
     * System.out.println("Coefficient (2,2) de la matrice b : " + b.getCoef(1, 1));
     * System.out.println("Nouvelle valeur de ce coefficient : 8");
     * b.remplacecoef(1, 1, 8);
     * System.out.println("Vérification de la modification du coefficient");
     * System.out.println("Coefficient (2,2) de la matrice b : " + b.getCoef(1, 1));
     * 
     * // Addition de deux matrices System.out.
     * println("Addition de 2 matrices : affichage des 2 matrices puis de leur addition"
     * ); System.out.println("Matrice 1 :\n" + a + "Matrice 2 :\n" + b + "Somme :\n"
     * + Matrice.addition(a, b));
     * 
     * // Produit de deux matrices System.out.
     * println("Produit de 2 matrices : affichage des 2 matrices puis de leur produit"
     * ); System.out.println("Matrice 1 :\n" + a + "Matrice 2 :\n" + b +
     * "Produit :\n" + Matrice.produit(a, b));
     */
    // TEST INVERSE MATRICE
    try {
      // Créer une matrice carrée
      double[][] matriceData = { { 2, 1 }, { 0, 1 } };
      Matrice A = new Matrice(matriceData);

      System.out.println("Matrice originale : \n" + A);

      // Calcul de l'inverse de la matrice A
      Matrice inverseA = A.inverse();
      System.out.println("Matrice inverse : \n" + inverseA);

      // Calcul du produit A * A^(-1)
      Matrice produitAAinv = Matrice.produit(A, inverseA);
      System.out.println("Résultat A * A^(-1) : \n" + produitAAinv);

      // Génération de la matrice identité correspondante
      Matrice matriceIdentiteAttendue = Matrice.matriceIdentite(A.nbLigne());
      System.out.println("Matrice identité attendue : \n" + matriceIdentiteAttendue);

      // Calcul de la différence entre A * A^(-1) et la matrice identité
      Matrice difference = Matrice.soustraction(produitAAinv, matriceIdentiteAttendue);

      // Calcul de la norme_1 et norme_inf de la différence
      double norme1Difference = difference.norme_1();
      double normeInfDifference = difference.norme_inf();

      System.out.println("Norme_1 de la différence (A * A^(-1) - I) : " + norme1Difference);
      System.out.println("Norme_inf de la différence (A * A^(-1) - I) : " + normeInfDifference);

      // Vérification que la norme est proche de zéro (avec une tolérance EPSILON)
      if (norme1Difference < Matrice.EPSILON && normeInfDifference < Matrice.EPSILON) {
        System.out.println("La matrice inverse est correcte.");
      } else {
        System.out.println("! Attention : La matrice inverse n'est pas exacte.");
      }

      // Calcul et affichage du conditionnement de la matrice A
      double cond1 = A.cond_1();
      double condInf = A.cond_inf();

      System.out.println("Conditionnement de la matrice A selon la norme_1 : " + cond1);
      System.out.println("Conditionnement de la matrice A selon la norme_inf : " + condInf);

    } catch (Exception e) {
      System.err.println("Erreur : " + e.getMessage());
    }
  }
}