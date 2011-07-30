package events;

import common.ContestOrgException;

@SuppressWarnings("serial")
public class ContestOrgEventException extends ContestOrgException
{
	// Constructeur
	public ContestOrgEventException(String message) {
		// Appeller le constructeur du parent
		super(message);
	}
	
}
