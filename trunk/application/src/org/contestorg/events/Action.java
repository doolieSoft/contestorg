package org.contestorg.events;

import java.util.Stack;

/**
 * Ensemble d'événements
 */
public class Action
{

	/** Nom de l'action */
	private String name;

	/** Pile des évenemens */
	private Stack<Event> events = new Stack<Event>();

	/**
	 * Constructeur
	 * @param name nom de l'action
	 */
	public Action(String name) {
		this.name = name;
	}

	/**
	 * Récupérer le nom de l'action
	 * @return nom de l'aciton
	 */
	public String getName () {
		return this.name;
	}

	/**
	 * Ajouter un événement
	 * @param event événement à ajouter
	 */
	public void push (Event event) {
		this.events.push(event);
	}

	/**
	 * Enlever et récupérer le dernier événement ajouté
	 * @return dernier événement ajouté
	 */
	public Event pop () {
		return this.events.pop();
	}
	
	/**
	 * Récupérer la pile des événements
	 * @return pile des événements
	 */
	public Stack<Event> getEvents() {
		return this.events;
	}

}
