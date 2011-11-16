package org.contestorg.out;

import org.contestorg.common.ContestOrgException;

@SuppressWarnings("serial")
public class ContestOrgOutException extends ContestOrgException
{

	// Constructeur
	public ContestOrgOutException(String message) {
		// Appeller le constructeur du parent 
		super(message);
	}
	
}
