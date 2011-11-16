package org.contestorg.events;

import java.util.Stack;

public class Action
{

	// Nom de l'action
	private String name;

	// Pile des évenemens
	private Stack<Event> events = new Stack<Event>();

	// Constructeur
	public Action(String name) {
		this.name = name;
	}

	// Récupérer le nom de l'action
	public String getName () {
		return this.name;
	}

	// Ajouter un événement
	public void push (Event event) {
		this.events.push(event);
	}

	// Enlever et récupérer le dernier évenement
	public Event pop () {
		return this.events.pop();
	}
	
	// Récupérer la pile des évenements
	public Stack<Event> getEvents() {
		return this.events;
	}

}
