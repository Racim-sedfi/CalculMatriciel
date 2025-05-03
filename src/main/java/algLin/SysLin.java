package algLin;

/**
 * Classe abstraite qui represente un système lineaire
 *
 * @author racim
 * @version $Id: $Id
 */
public abstract class SysLin {
	/** Dimension du système **/
	protected int ordre;
	/** Matrice carré **/
	protected Matrice matriceSystem;
	/** Vecteur du second Membre **/
	protected Vecteur secondMembre;

	/**
	 * Constructeur du système verifie que la matric eest carré et que le second
	 * membre respecte aussi la dimesnion
	 *
	 * @param matriceSystem a {@link algLin.Matrice} object
	 * @param secondMembre a {@link algLin.Vecteur} object
	 * @throws algLin.IrregularSysLinException Si le système est irrégulier ou si un pivot nul est détecté.
	 */
	public SysLin(Matrice matriceSystem, Vecteur secondMembre) throws IrregularSysLinException {
		if (matriceSystem.nbColonne() != matriceSystem.nbLigne() || matriceSystem.nbLigne() != secondMembre.taille()) {
			throw new IrregularSysLinException(
					"La Matrice n'est pas carré ou  sa taille est différente du second Membre");
		}
		this.matriceSystem = matriceSystem;
		this.secondMembre = secondMembre;
		ordre = secondMembre.taille();
	}

	/**
	 * Recuperer l'ordre du système
	 *
	 * @return l'ordre du système
	 */
	public int getOrder() {
		return ordre;
	}

	/**
	 * recuperer la matrice du système
	 *
	 * @return la matrice du système
	 */
	public Matrice getMatriceSystem() {
		return matriceSystem;
	}

	/**
	 * recuperer le vecteur du système le second Membre
	 *
	 * @return le second membre
	 */
	public Vecteur getSecondMembre() {
		return secondMembre;
	}

	/**
	 * Methode abstraite pour implementer plusieurs solution
	 *
	 * @return un vecteur qui resout le système
	 * @throws algLin.IrregularSysLinException Si le système est irrégulier ou si un pivot nul est détecté.
	 */
	abstract public Vecteur resolution() throws IrregularSysLinException;

}
