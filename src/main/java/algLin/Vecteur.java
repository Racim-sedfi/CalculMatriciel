package algLin;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Scanner;

/**
 * <p>
 * Vecteur class.
 * </p>
 *
 * @author racim
 * @version $Id: $Id
 */
public class Vecteur extends Matrice {

	/**
	 * Constructeur qui crée un vecteur avec une seule colonne.
	 *
	 * @param nbLignes a int
	 */
	public Vecteur(int nbLignes) {
		super(nbLignes, 1);
	}

	/**
	 * Constructeur qui crée un vecteur avec une seule colonne, initialisé avec les
	 * coefficients du tableau passé en paramètre.
	 *
	 * @param tableau an array of {@link double} objects
	 */
	public Vecteur(double[] tableau) {
		super(tableau.length, 1);

		for (int i = 0; i < tableau.length; i++) {
			coefficient[i][0] = tableau[i];
		}
	}

	/**
	 * Constructeur qui crée un vecteur à partir d'un fichier.
	 *
	 * @param nomFichier a {@link java.lang.String} object
	 */
	public Vecteur(String nomFichier) {
		super(0, 0);
		try {
			Scanner sc = new Scanner(new File(nomFichier));
			int ligne = sc.nextInt();
			this.coefficient = new double[ligne][1];
			for (int i = 0; i < ligne; i++)
				this.coefficient[i][0] = sc.nextDouble();
			sc.close();

		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Le fichier spécifié est introuvable : " + nomFichier, e);
		}
	}

	/**
	 * Méthode qui renvoie la taille du vecteur.
	 *
	 * @return la taille du vecteur
	 */
	public int taille() {
		return coefficient.length;
	}

	/**
	 * Méthode qui renvoie le coefficient selon l'indice passé en paramètre.
	 *
	 * @param indice a int
	 * @return le coefficient
	 */
	public double getValeur(int indice) {
		return coefficient[indice][0];
	}

	/**
	 * Méthode qui change la valeur du coefficient selon l'indice.
	 *
	 * @param indice a int
	 * @param valeur a double
	 */
	public void setValeur(int indice, double valeur) {
		coefficient[indice][0] = valeur;
	}

	/**
	 * {@inheritDoc}
	 *
	 * Convertir l'objet vecteur en chaîne de caractères.
	 */
	@Override
	public String toString() {
		StringBuilder chaine = new StringBuilder("[");
		for (int i = 0; i < taille(); i++) {
			chaine.append(getValeur(i));
			if (i < taille() - 1) {
				chaine.append(", ");
			}
		}
		chaine.append("]");
		return chaine.toString();
	}

	/**
	 * Méthode statique qui calcule le produit scalaire des deux vecteurs sans
	 * vérification.
	 *
	 * @param vec1 a {@link algLin.Vecteur} object
	 * @param vec2 a {@link algLin.Vecteur} object
	 * @return le produit scalaire
	 */
	public static double produitScalaireSansVerif(Vecteur vec1, Vecteur vec2) {
		double produits = 0;
		for (int i = 0; i < vec1.taille(); ++i) {
			produits += vec1.getValeur(i) * vec2.getValeur(i);
		}
		return produits;
	}

	/**
	 * Méthode statique qui calcule le produit scalaire des deux vecteurs avec
	 * vérification.
	 *
	 * @param vec1 a {@link algLin.Vecteur} object
	 * @param vec2 a {@link algLin.Vecteur} object
	 * @return le produit scalaire
	 */
	public static double produitScalaire(Vecteur vec1, Vecteur vec2) {
		if (vec1.taille() != vec2.taille()) {
			throw new IllegalArgumentException("Les deux vecteurs n'ont pas la même taille.");
		}
		double produits = 0;
		for (int i = 0; i < vec1.taille(); ++i) {
			produits += vec1.getValeur(i) * vec2.getValeur(i);
		}
		return produits;
	}

	/**
	 * Calcule la norme L1 du vecteur.
	 *
	 * @return la norme L1
	 */
	public double normeL1() {
		double somme = 0;
		for (int i = 0; i < taille(); i++) {
			somme += Math.abs(getValeur(i));
		}
		return somme;
	}

	/**
	 * Calcule la norme L2 du vecteur.
	 *
	 * @return la norme L2
	 */
	public double normeL2() {
		double somme = 0;
		for (int i = 0; i < taille(); i++) {
			somme += Math.pow(getValeur(i), 2);
		}
		return Math.sqrt(somme);
	}

	/**
	 * Calcule la norme Linfini du vecteur.
	 *
	 * @return la norme Linfini
	 */
	public double normeLinfini() {
		double max = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < taille(); i++) {
			max = Math.max(max, Math.abs(getValeur(i)));
		}
		return max;
	}

	/**
	 * <p>soustraction.</p>
	 *
	 * @param v1 a {@link algLin.Vecteur} object
	 * @param v2 a {@link algLin.Vecteur} object
	 * @return a {@link algLin.Vecteur} object
	 * @throws java.lang.Exception if any.
	 */
	public static Vecteur soustraction(Vecteur v1, Vecteur v2) throws Exception {
		if (v1.taille() != v2.taille()) {
			throw new Exception("Les vecteurs doivent avoir la même taille pour la soustraction.");
		}
		Vecteur resultat = new Vecteur(v1.taille());
		for (int i = 0; i < v1.taille(); i++) {
			resultat.remplacecoef(i, 0, v1.getCoef(i, 0) - v2.getCoef(i, 0));
		}
		return resultat;
	}

	/**
	 * <p>
	 * main.
	 * </p>
	 *
	 * @param args an array of {@link java.lang.String} objects
	 */
	public static void main(String[] args) {
		// Création d'un vecteur vide
		Vecteur vec1 = new Vecteur(4);

		// Création d'un vecteur avec un tableau
		double[] tableau = { 1.0, 2.0, 3.0, 4.0, 5.0 };
		Vecteur vec2 = new Vecteur(tableau);

		// Création d'un vecteur avec un fichier
		String fileName = "vecteur.txt";
		// Recuperation du fichier depuis resources
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL resource = classLoader.getResource(fileName);
		// Sauvgarde du chemin dans une variable fichier
		File fichier = new File(resource.getFile());
		// Creation du vecteur aec le fichier
		Vecteur vec3 = new Vecteur(fichier.getPath());

		// Tester si bel et bien l'exception se declanche quand le fichier n'existe pas
		try {
			Vecteur vec4 = new Vecteur("fichier_introuvable.txt");
			System.out.println("vec4 cree avec succee vec4 : " + vec4);
		} catch (Exception e) {
			System.err.println("Nom du fichier introuvable" + e.getMessage());
		}

		// Affichage des vecteurs
		System.out.println("Vecteur 1 : " + vec1);
		System.out.println("Vecteur 2 : " + vec2);
		System.out.println("Vecteur 3 : " + vec3);

		// Modification des valeurs du vecteur 1
		for (int i = 0; i < vec1.taille(); i++) {
			vec1.setValeur(i, i + 1); // Affecte 1, 2, 3, 4
		}
		System.out.println("Vecteur 1 après modification : " + vec1);

		// Verification de l'accées au coefficient
		System.out.println("vec1[2] = " + vec1.getValeur(2));

		// verfier la methode taille
		System.out.println("vec2.length = " + vec2.taille());

		// Produit scalaire
		double produit = produitScalaire(vec1, vec3);
		System.out.println("Produit scalaire : " + produit);

		// Produit scalaire san verification
		double produitSansVerif = produitScalaireSansVerif(vec1, vec3);
		System.out.println("Produit scalaire sans Verification : " + produit);

		// Tester si l'exception est declancher si la taille est differentes
		try {
			double produitVerifex = Vecteur.produitScalaire(vec1, vec2);
			System.out.println("Produit scalaire sans vérification : " + produitSansVerif);
		} catch (Exception e) {
			System.err.println("Erreur attendue pour produit scalaire sans vérification : " + e.getMessage());
		}

		// Normes des vecteurs
		System.out.println("Norme L1 de vec1 : " + vec1.normeL1());
		System.out.println("Norme L2 de vec1 : " + vec1.normeL2());
		System.out.println("Norme Linfini de vec1 : " + vec1.normeLinfini());
	}
}
