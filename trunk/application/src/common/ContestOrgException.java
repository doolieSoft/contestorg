package common;

/**
 * Classe des exceptions générées par ContestOrg
 */
@SuppressWarnings("serial")
public abstract class ContestOrgException extends Exception
{

	// Constructeur
	public ContestOrgException(String message) {
		super(message);
	}

}
