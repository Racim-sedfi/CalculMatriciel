# CalculMatriciel — Algèbre linéaire et analyse numérique en Java

Bibliothèque Java d'algèbre linéaire numérique : manipulation de matrices et de vecteurs,
résolution de systèmes linéaires (méthodes directes), calcul de normes et de
conditionnement, ainsi que deux applications : l'interpolation par **splines cubiques** et
l'approximation polynomiale par la **méthode des moindres carrés**, avec affichage des courbes.

## Technologies

- Java 17
- Maven
- XChart (tracé des courbes)

## Fonctionnalités principales

- **Matrices et vecteurs** (`Matrice`, `Vecteur`) : produit, transposée, inverse, déterminant,
  sous-matrice, identité, lecture depuis un fichier.
- **Normes et conditionnement** : norme 1 et norme infinie (`Norme_1`, `Norme_inf`),
  conditionnement `cond_1` / `cond_inf`, étude de la matrice de Hilbert (`HilbertMatrice`).
- **Systèmes linéaires** (classe abstraite `SysLin`) :
  - systèmes diagonaux et triangulaires inférieurs / supérieurs (y compris à diagonale unité) ;
  - factorisation **LDR** (méthode de Helder) avec résolution pour plusieurs seconds membres ;
  - **algorithme de Thomas** pour les matrices tridiagonales (`Mat3Diag`).
- **Spline cubique naturelle** (`Spline`) : calcul des dérivées secondes aux points de support
  et tracé de la courbe interpolée.
- **Moindres carrés** (`ModPoly`) : ajustement d'un polynôme de degré choisi à un nuage de
  points et tracé du résultat.
- Exceptions dédiées : `IrregularSysLinException` (système irrégulier, pivot nul),
  `DataOutOfRangeException`.

## Structure du projet

```
├── pom.xml
└── src/main
    ├── java/algLin/          # Classes de la bibliothèque
    └── resources/            # Fichiers d'entrée : matrices, vecteurs, points de support
```

## Compilation et exécution

Prérequis : JDK 17 et Maven. Les commandes sont à lancer depuis la racine du projet.

```bash
mvn compile

# Algorithme de Thomas sur un système tridiagonal
mvn exec:java -Dexec.mainClass=algLin.Thomas

# Spline cubique : saisir par exemple spline.txt
mvn exec:java -Dexec.mainClass=algLin.Spline

# Moindres carrés : saisir par exemple points.txt
mvn exec:java -Dexec.mainClass=algLin.ModPoly
```

Les fichiers de points (`src/main/resources`) utilisent la virgule comme séparateur décimal :
ils sont lus avec `Scanner`, ce qui suppose une locale française
(au besoin : `MAVEN_OPTS="-Duser.language=fr -Duser.country=FR"`).

## Documentation

La Javadoc peut être générée avec :

```bash
mvn javadoc:javadoc    # résultat dans target/reports/apidocs/
```

## Auteur

Racim Sedfi — les classes `Thomas` et `Mat3Diag` ont été écrites par un autre contributeur
(signées « azedi » dans le code).
