package org.contestorg.models;

import org.contestorg.common.ContestOrgException;

@SuppressWarnings("serial")
public class ContestOrgModelException extends ContestOrgException
{
	
	// Constructeur
	public ContestOrgModelException(String message) {
		// Appeller le constructeur du parent
		super(message);
	}
	
}
