package algLin;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

public class Spline {
	  private double[] x; // abscisses des points de support
	  private double[] y; // ordonnées des points de support
	  private double[] g; // dérivées secondes calculées aux points de support
	  private int n; // nombre de points de support

	  /**
	   * Construit une spline cubique naturelle à partir des points de support. Les
	   * conditions aux bords sont naturelles : g[0] = g[n-1] = 0.
	   *
	   * @param x tableau des abscisses (doivent être triées par ordre croissant)
	   * @param y tableau des ordonnées
	   * @throws Exception en cas d'incohérence dans les tableaux
	   */
	  public Spline(double[] x, double[] y) throws Exception {
	    if (x.length != y.length) {
	      throw new Exception("Les tableaux x et y doivent avoir la même taille.");
	    }
	    if (x.length < 2) {
	      throw new Exception("Au moins 2 points sont nécessaires pour construire une spline.");
	    }
	    this.n = x.length;
	    this.x = new double[n];
	    this.y = new double[n];
	    for (int i = 0; i < n; i++) {
	      this.x[i] = x[i];
	      this.y[i] = y[i];
	    }
	    // Calcul des dérivées secondes dès l'instanciation
	    computeSecondDerivatives();
	  }

	  /**
	   * Calcule les dérivées secondes (g[i]) en construisant le système tridiagonal
	   * issu des conditions d'interpolation par splines cubiques naturelles. Le
	   * système est de taille n avec : - Pour i = 0 et i = n-1 : g[i] = 0 (conditions
	   * naturelles) - Pour 1 ≤ i ≤ n-2 : a_i * g[i-1] + b_i * g[i] + c_i * g[i+1] =
	   * d_i où : a_i = h_{i-1} b_i = 2(h_{i-1} + h_i) c_i = h_i d_i = 6 \left(
	   * \frac{y_{i+1} - y_i}{h_i} - \frac{y_i - y_{i-1}}{h_{i-1}} \right) Ce système
	   * est résolu avec la méthode de Thomas.
	   *
	   * @throws Exception en cas d'erreur lors de la résolution
	   */
	  private void computeSecondDerivatives() throws Exception {
	    // Allocation des tableaux pour le système
	    double[] a = new double[n]; // sous-diagonale (a[0] non utilisé)
	    double[] b = new double[n]; // diagonale principale
	    double[] c = new double[n]; // sur-diagonale (c[n-1] non utilisé)
	    double[] d = new double[n]; // second membre

	    // Conditions aux bords : g[0] = g[n-1] = 0
	    b[0] = 1.0;
	    d[0] = 0.0;
	    b[n - 1] = 1.0;
	    d[n - 1] = 0.0;

	    // Pour i = 1 à n-2, construction des coefficients
	    for (int i = 1; i < n - 1; i++) {
	      double h_i = x[i] - x[i - 1];
	      double h_ip1 = x[i + 1] - x[i];
	      a[i] = h_i;
	      b[i] = 2 * (h_i + h_ip1);
	      c[i] = h_ip1;
	      d[i] = 6 * ((y[i + 1] - y[i]) / h_ip1 - (y[i] - y[i - 1]) / h_i);
	    }

	    // Construction de la matrice tridiagonale à l'aide de Mat3Diag
	    Mat3Diag matrice = new Mat3Diag(n);
	    for (int i = 0; i < n; i++) {
	      if (i == 0 || i == n - 1) {
	        matrice.remplacecoef(1, i, b[i]); // seules les conditions aux bords sont actives
	      } else {
	        matrice.remplacecoef(0, i, a[i]); // sous-diagonale
	        matrice.remplacecoef(1, i, b[i]); // diagonale principale
	        matrice.remplacecoef(2, i, c[i]); // sur-diagonale
	      }
	    }

	    // Construction du second membre sous forme d'un Vecteur
	    Vecteur secondMembre = new Vecteur(d);

	    // Utilisation de la méthode de Thomas pour résoudre le système tridiagonal
	    Thomas systeme = new Thomas(matrice, secondMembre);
	    Vecteur gVector = systeme.resolution();

	    // Récupération des dérivées secondes dans le tableau g
	    g = new double[n];
	    for (int i = 0; i < n; i++) {
	      g[i] = gVector.getCoef(i, 0);
	    }
	  }

	  /**
	   * Évalue la spline cubique en une valeur d'abscisse X.
	   *
	   * @param X la valeur à évaluer
	   * @return la valeur interpolée S(X)
	   * @throws DataOutOfRangeEception si X n'est pas dans [x[0], x[n-1]]
	   */
	  public double evaluate(double X) throws DataOutOfRangeException {
	    if (X < x[0] || X > x[n - 1]) {
	      throw new DataOutOfRangeException(
	          "La valeur " + X + " est hors de l'intervalle [" + x[0] + ", " + x[n - 1] + "].");
	    }
	    // Recherche de l'intervalle [x[j], x[j+1]] tel que x[j] <= X <= x[j+1]
	    int j = 0;
	    for (int i = 0; i < n - 1; i++) {
	      if (X >= x[i] && X <= x[i + 1]) {
	        j = i;
	        break;
	      }
	    }
	    // Calcul de la longueur de l'intervalle
	    double gama = x[j + 1] - x[j];

	    // Calcul des coefficients alpha et beta
	    double alpha = (x[j + 1] - X) / gama;
	    double beta = (X - x[j]) / gama;

	    // Calcul selon l'algorithme des splines cubiques (décomposition en trois
	    // termes)
	    double t1 = (g[j]/ 6.0) * (Math.pow(alpha, 3) - alpha) * (gama * gama) ;
	    double t2 = (g[j + 1] / 6) * (Math.pow(beta, 3) - beta) * (gama * gama);
	    double t3 = y[j] * alpha + y[j + 1] * beta;

	    return t1 + t2 + t3;
	  }

	  // Accesseurs éventuels
	  public double[] getX() {
	    return x.clone();
	  }

	  public double[] getY() {
	    return y.clone();
	  }

	  public double[] getSecondDerivatives() {
	    return g.clone();
	  }
	

 
        /**
         * <p>main.</p>
         *
         * @param args an array of {@link java.lang.String} objects
         */
      public static void main(String[] args) {
     
	    try {
	      // Demande du nom du fichier de points de support
	      Scanner sc = new Scanner(System.in);
	      System.out.print("Entrez le nom du fichier de points (ex: points_spline.txt) : ");
	      String fileName = sc.nextLine();

	      // Lecture des coordonnées depuis le fichier placé dans src/main/resources
	      List<Double> lx = new ArrayList<>();
	      List<Double> ly = new ArrayList<>();

	      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	      // Utilisation de getResourceAsStream pour accéder au fichier dans les
	      // ressources Maven
	      InputStream inputStream = classLoader.getResourceAsStream(fileName);
	      if (inputStream == null) {
	        throw new FileNotFoundException("Fichier introuvable dans src/main/resources : " + fileName);
	      }
	      Scanner fileScanner = new Scanner(inputStream);

	      // Lecture du header si présent (ex : "15 2") et vérification du format
	      if (fileScanner.hasNextInt()) {
	        int nbLignes = fileScanner.nextInt();
	        int nbColonnes = fileScanner.nextInt();
	        if (nbColonnes != 2) {
	          throw new Exception("Le fichier doit contenir 2 colonnes (x et y).");
	        }
	      }

	      // Lecture des paires de valeurs
	      while (fileScanner.hasNextDouble()) {
	        lx.add(fileScanner.nextDouble());
	        if (!fileScanner.hasNextDouble()) {
	          throw new Exception("Fichier mal formaté : nombre impair de valeurs.");
	        }
	        ly.add(fileScanner.nextDouble());
	      }
	      fileScanner.close();

	      double[] xPoints = lx.stream().mapToDouble(Double::doubleValue).toArray();
	      double[] yPoints = ly.stream().mapToDouble(Double::doubleValue).toArray();

	      // Construction de la spline cubique naturelle
	      Spline spline = new Spline(xPoints, yPoints);

	      // Détermination de l'intervalle d'interpolation
	      double xmin = xPoints[0];
	      double xmax = xPoints[xPoints.length - 1];
	      double step = (xmax - xmin) / 99.0;
	      double[] xEval = new double[100];
	      double[] yEval = new double[100];

	      for (int i = 0; i < 100; i++) {
	        xEval[i] = xmin + i * step;
	        yEval[i] = spline.evaluate(xEval[i]);
	      }

	      // Création du graphique avec XChart
	      XYChart chart = new XYChartBuilder().width(800).height(600).title("Interpolation par spline cubique")
	          .xAxisTitle("X").yAxisTitle("S(x)").build();

	      // Ajout de la courbe de la spline interpolée (les 100 points calculés)
	      chart.addSeries("Spline Cubique", xEval, yEval);

	      // Ajout des points de support avec un style de rendu en scatter (points isolés)
	      var supportSeries = chart.addSeries("Points de Support", xPoints, yPoints);
	      supportSeries.setXYSeriesRenderStyle(org.knowm.xchart.XYSeries.XYSeriesRenderStyle.Scatter);
	      // Vous pouvez également définir un symbole spécifique (par exemple, DIAMOND)
	      // supportSeries.setMarker(org.knowm.xchart.style.markers.SeriesMarkers.Diamond);

	      // Affichage graphique
	      new SwingWrapper<>(chart).displayChart();

	    } catch (DataOutOfRangeException e) {
	      System.err.println("Erreur d'interpolation : " + e.getMessage());
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }

}
