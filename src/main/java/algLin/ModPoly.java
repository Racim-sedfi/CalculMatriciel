package algLin;

import java.io.File;
import java.net.URL;
import java.util.Scanner;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.markers.SeriesMarkers;

public class ModPoly {
  private double[] coefficients; // Coefficients α_j
  private int degre; // Degré du polynôme

  // Constructeur
  public ModPoly(int degre) {
    this.degre = degre;
    this.coefficients = new double[degre + 1];
  }

  // Méthode identifie : ajuste le modèle aux points de support (x, y)
  public void identifie(double[] x, double[] y) throws Exception {
    int n = x.length;

    // Construction de la matrice F
    Matrice F = new Matrice(n, degre + 1);
    for (int i = 0; i < n; i++) {
      for (int j = 0; j <= degre; j++) {
        F.remplacecoef(i, j, Math.pow(x[i], j));
      }
    }

    // Construction du vecteur Y
    Vecteur Y = new Vecteur(y);

    // Calcul des matrices FtF et FtY
    Matrice Ft = Matrice.transpose(F);
    Matrice FtF = Matrice.produit(Ft, F);
    Vecteur FtY = Matrice.produitVecteur(Ft, Y);

    // Résolution du système linéaire (FtF)X = FtY
    Helder solver = new Helder(FtF, FtY);
    Vecteur solution = solver.resolution();

    // Stockage des coefficients dans l'attribut
    for (int i = 0; i <= degre; i++) {
      coefficients[i] = solution.getCoef(i, 0);
    }
  }

  // Méthode pour évaluer le polynôme en un point donné
  public double evaluer(double x) {
    double resultat = 0;
    for (int j = 0; j < coefficients.length; j++) {
      resultat += coefficients[j] * Math.pow(x, j);
    }
    return resultat;
  }

  // Getter pour les coefficients
  public double[] getCoefficients() {
    return coefficients;
  }

//Méthode pour afficher le graphique avec XChart
  public static void afficherGraphique(double[] x, double[] y, ModPoly modele) {
    int resolution = 100;

    double minX = x[0], maxX = x[0];

    for (double xi : x) {
      if (xi < minX)
        minX = xi;
      if (xi > maxX)
        maxX = xi;
    }

    double[] xPlot = new double[resolution];
    double[] yPlot = new double[resolution];

    for (int i = 0; i < resolution; i++) {
      xPlot[i] = minX + i * (maxX - minX) / (resolution - 1);
      yPlot[i] = modele.evaluer(xPlot[i]);
    }

    // Création du graphique
    XYChart chart = new XYChartBuilder().width(800).height(600).title("Ajustement par Moindres Carrés").xAxisTitle("X")
        .yAxisTitle("Y").build();

    // Ajouter le polynôme ajusté en bleu avec des points ronds et une ligne
    // continue
    chart.addSeries("Polynôme ajusté", xPlot, yPlot).setMarker(SeriesMarkers.CIRCLE).setLineColor(java.awt.Color.BLUE);

    // Ajouter les points de support en orange avec des losanges, sans les relier
    // (points isolés)
    chart.addSeries("Points de support", x, y).setXYSeriesRenderStyle(org.knowm.xchart.XYSeries.XYSeriesRenderStyle.Scatter);

    // Afficher le graphique
    new SwingWrapper<>(chart).displayChart();
  }

  public static void main(String[] args) throws Exception {
    Scanner scanner = new Scanner(System.in);

    // Lecture du fichier contenant les points de support
    System.out.print("Entrez le nom du fichier contenant les points de support (dans src/main/resources) : ");
    String nomFichier = scanner.nextLine();

    // Chargement du fichier depuis src/main/resources
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    URL resource = classLoader.getResource(nomFichier);

    if (resource == null) {
      throw new IllegalArgumentException("Le fichier spécifié est introuvable : " + nomFichier);
    }

    File fichier = new File(resource.toURI());
    Scanner lecteurFichier = new Scanner(fichier);

    // Lecture des données
    int n = lecteurFichier.nextInt(); // Nombre de points
    double[] x = new double[n];
    double[] y = new double[n];

    for (int i = 0; i < n; i++) {
      y[i] = lecteurFichier.nextDouble();
      x[i] = lecteurFichier.nextDouble();
    }

    lecteurFichier.close();

    // Lecture du degré du polynôme
    System.out.print("Entrez le degré du polynôme : ");
    int degrePolynome = scanner.nextInt();

    // Création et ajustement du modèle polynomial
    ModPoly modele = new ModPoly(degrePolynome);
    modele.identifie(x, y);

    // Affichage des coefficients ajustés
    System.out.println("Coefficients ajustés :");
    for (int i = 0; i <= degrePolynome; i++) {
      System.out.printf("α%d = %.5f\n", i, modele.getCoefficients()[i]);
    }

    // Visualisation graphique avec XChart
    afficherGraphique(x, y, modele);
  }
}
