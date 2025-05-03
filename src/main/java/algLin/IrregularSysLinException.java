package algLin;

/**
 * Une exception personalisée pour signaler qu'un système est irrégulier
 *
 * @author racim
 * @version $Id: $Id
 */
public class IrregularSysLinException extends Exception {

	/**
	 * <p>Constructor for IrregularSysLinException.</p>
	 */
	public IrregularSysLinException() {

		super();
	}

	/**
	 * <p>Constructor for IrregularSysLinException.</p>
	 *
	 * @param message a {@link java.lang.String} object
	 */
	public IrregularSysLinException(String message) {
		super(message);
	}

	/**
	 * <p>Constructor for IrregularSysLinException.</p>
	 *
	 * @param cause a {@link java.lang.Throwable} object
	 */
	public IrregularSysLinException(Throwable cause) {
		super(cause);
	}

	/**
	 * <p>Constructor for IrregularSysLinException.</p>
	 *
	 * @param message a {@link java.lang.String} object
	 * @param cause a {@link java.lang.Throwable} object
	 */
	public IrregularSysLinException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * <p>Constructor for IrregularSysLinException.</p>
	 *
	 * @param message a {@link java.lang.String} object
	 * @param cause a {@link java.lang.Throwable} object
	 * @param enableSuppression a boolean
	 * @param writableStackTrace a boolean
	 */
	public IrregularSysLinException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "Le système est irrégulier.";
	}
}
