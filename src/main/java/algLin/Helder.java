package algLin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Classe qui hérite de SysLin qui permet de resoudre un système *
 *
 * @author racim
 * @version $Id: $Id
 */
public class Helder extends SysLin {
	/**
	 * Constructeur de la classe Helder
	 *
	 * @param matriceSystem a {@link algLin.Matrice} object
	 * @param secondMembre a {@link algLin.Vecteur} object
	 * @throws algLin.IrregularSysLinException Si le système est irrégulier ou si un pivot nul est détecté.
	 */
	public Helder(Matrice matriceSystem, Vecteur secondMembre) throws IrregularSysLinException {
		super(matriceSystem, secondMembre);
	}

	/**
	 * Effectue la factorisation LDR du système.
	 *
	 * @throws algLin.IrregularSysLinException i un pivot nul est détecté, ce qui rend le système irrégulier.
	 */
	public void factorLDR() throws IrregularSysLinException {
		int n = super.getOrder();
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				double sum = 0;
				for (int k = 0; k < Math.min(i, j); k++) {
					sum += matriceSystem.getCoef(i, k) * matriceSystem.getCoef(k, k) * matriceSystem.getCoef(k, j);
				}
				if (i == j) {
					matriceSystem.remplacecoef(i, j, matriceSystem.getCoef(i, j) - sum);
					if (matriceSystem.getCoef(i, j) == 0) {
						throw new IrregularSysLinException("Pivot nul détecté en D[" + i + "]");
					}
				} else if (i > j) {
					matriceSystem.remplacecoef(i, j, (matriceSystem.getCoef(i, j) - sum) / matriceSystem.getCoef(j, j));
				} else {
					matriceSystem.remplacecoef(i, j, (matriceSystem.getCoef(i, j) - sum) / matriceSystem.getCoef(i, i));
				}
			}
		}
	}

	/**
	 * Méthode qui resout le systeme d'une matrice non factoriser
	 *
	 * @return a {@link algLin.Vecteur} object
	 * @throws algLin.IrregularSysLinException if any.
	 */
	public Vecteur resolution() throws IrregularSysLinException {
		factorLDR();
		return resolutionPartielle();
	}

	/**
	 * Méthode pour résoudre un système en supposant que la factorisation LDR est
	 * déjà effectuée
	 *
	 * @return a {@link algLin.Vecteur} object
	 * @throws algLin.IrregularSysLinException if any.
	 */
	public Vecteur resolutionPartielle() throws IrregularSysLinException {
		int ordre = getOrder();
		Vecteur b = getSecondMembre();

		// Résolution de Ly = b (triangulaire inférieure à diagonale unité)
		SysTriangInfUnite systemeL = new SysTriangInfUnite(getMatriceSystem(), b);
		Vecteur y = systemeL.resolution();

		// Résolution de Dz = y (diagonale)
		SysDiagonal systemeD = new SysDiagonal(getMatriceSystem(), y);
		Vecteur z = systemeD.resolution();

		// Résolution de Rx = z (triangulaire supérieure à diagonale unité)
		SysTriangSupUnite systemeR = new SysTriangSupUnite(getMatriceSystem(), z);
		return systemeR.resolution();
	}
	/**
	 * Retourne le déterminant de la matrice factorisée.
	 *
	 * @return le déterminant de la matrice
	 * @throws algLin.IrregularSysLinException si la factorisation échoue
	 */
	public double getDeterminant() throws IrregularSysLinException {
	    factorLDR(); // Appliquer la factorisation LDR

	    double determinant = 1.0;
	    int n = getOrder();

	    // Le déterminant est le produit des éléments diagonaux de D (après factorisation LDR)
	    for (int i = 0; i < n; i++) {
	        determinant *= matriceSystem.getCoef(i, i);
	    }

	    return determinant;
	}

	/**
	 * Methode qui change la valeur du secondMemebre
	 *
	 * @param newSecondMembre a {@link algLin.Vecteur} object
	 */
	public void setSecondMembre(Vecteur newSecondMembre) {
		this.secondMembre = newSecondMembre;
	}

	/**
	 * Méthode pour lire une matrice depuis un fichier
	 *
	 * @param fichier a {@link java.lang.String} object
	 * @throws java.io.IOException Si le fichier n'est pas trouvé ou si une erreur survient lors de la lecture.
	 * @return a {@link algLin.Matrice} object
	 */
	public static Matrice lireMatriceDepuisRessources(String fichier) throws IOException {
		InputStream is = Helder.class.getClassLoader().getResourceAsStream(fichier);
		if (is == null) {
			throw new IOException("Fichier non trouvé : " + fichier);
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		int ordre = Integer.parseInt(reader.readLine().trim());
		double[][] coefficients = new double[ordre][ordre];

		for (int i = 0; i < ordre; i++) {
			String[] ligne = reader.readLine().trim().split("\\s+");
			for (int j = 0; j < ordre; j++) {
				coefficients[i][j] = Double.parseDouble(ligne[j]);
			}
		}
		reader.close();
		return new Matrice(coefficients);
	}

	/**
	 * Methode pour lire un vecteur depuis un fichier
	 *
	 * @param fichier a {@link java.lang.String} object
	 * @throws java.io.IOException Si le fichier n'est pas trouvé ou si une erreur survient lors de la lecture.
	 * @return a {@link algLin.Vecteur} object
	 */
	public static Vecteur lireVecteurDepuisRessources(String fichier) throws IOException {
		InputStream is = Helder.class.getClassLoader().getResourceAsStream(fichier);
		if (is == null) {
			throw new IOException("Fichier non trouvé : " + fichier);
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		int taille = Integer.parseInt(reader.readLine().trim());
		double[] valeurs = new double[taille];

		for (int i = 0; i < taille; i++) {
			valeurs[i] = Double.parseDouble(reader.readLine().trim());
		}
		reader.close();
		return new Vecteur(valeurs);
	}

	/**
	 * <p>main.</p>
	 *
	 * @param args an array of {@link java.lang.String} objects
	 */
	public static void main(String[] args) {
		try {
			// Lecture des fichiers contenant la matrice A et le second membre b
			Matrice matrice = lireMatriceDepuisRessources("matrice.txt");
			Vecteur vecteur = lireVecteurDepuisRessources("vecteur.txt");

			// Création du système avec A et b
			Helder systeme = new Helder(matrice, vecteur);

			// Résolution du système A * x = b
			Vecteur solution = systeme.resolution();
			System.out.println("Solution pour le système A * x = b : " + solution);

			// Modification du second membre et test de resolutionPartielle
			Vecteur nouveauVecteur = new Vecteur(new double[] { 1, 2, 3 }); // Exemple de second membre différent
			systeme.setSecondMembre(nouveauVecteur);
			Vecteur solutionPartielle = systeme.resolutionPartielle();
			System.out.println("Solution pour le système A * x = b' (resolutionPartielle) : " + solutionPartielle);

			// Calcul du second membre pour A² * x = b (b' = A * solution)
			Matrice produitMatrice = Matrice.produit(matrice, solution);
			File fichierSecMembreA2 = new File("src/main/resources/sec_membre_A2.txt");
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fichierSecMembreA2));
			bufferedWriter.write(produitMatrice.nbLigne() + "\n");
			for (int i = 0; i < produitMatrice.nbLigne(); i++) {
				bufferedWriter.write(produitMatrice.getCoef(i, 0) + "\n");
			}
			bufferedWriter.close();

			// Lecture du second membre depuis le fichier généré
			Vecteur nouveauSecondMembre = lireVecteurDepuisRessources("sec_membre_A2.txt");

			// Modification du second membre et résolution du système A² * x = b
			systeme.setSecondMembre(nouveauSecondMembre);
			Vecteur solutionPartielleACarre = systeme.resolutionPartielle();
			System.out
					.println("Solution pour le système A² * x = b (resolutionPartielle) : " + solutionPartielleACarre);

		} catch (IOException e) {
			System.out.println("Erreur lors de la lecture des fichiers : " + e.getMessage());
		} catch (IrregularSysLinException e) {
			System.out.println("Erreur : " + e.getMessage());
		}
	}
}
