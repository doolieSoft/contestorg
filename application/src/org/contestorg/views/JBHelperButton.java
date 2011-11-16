package org.contestorg.views;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

@SuppressWarnings("serial")
public abstract class JBHelperButton extends JButton implements ActionListener
{
	// Parent
	private Window w_parent;

	// Constructeur
	public JBHelperButton(Window w_parent) {
		// Appeller le constructeur du parent
		super("Aide", new ImageIcon("img/farm/16x16/help.png"));

		// S'abonner à ses propres évenements
		this.addActionListener(this);

		// Retenir le parent
		this.w_parent = w_parent;
	}

	// Action sur valider
	protected abstract String message ();

	// Implémentation de ActionListener
	public void actionPerformed (ActionEvent event) {
		ViewHelper.dinformation(this.w_parent, this.message());
	}
}
