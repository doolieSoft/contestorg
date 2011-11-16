package org.contestorg.events;

import org.contestorg.common.ContestOrgException;

@SuppressWarnings("serial")
public class ContestOrgEventException extends ContestOrgException
{
	// Constructeur
	public ContestOrgEventException(String message) {
		// Appeller le constructeur du parent
		super(message);
	}
	
}
