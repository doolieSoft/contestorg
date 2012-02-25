package org.contestorg.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Window;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.contestorg.interfaces.IMoodyListener;

/**
 * Panel pour la fenêtre principale
 */
@SuppressWarnings("serial")
abstract public class JPPrincipalAbstract extends JPanel implements IMoodyListener
{
	
	/** Fenêtre parente */
	protected Window w_parent;

	// Panels
	
	/** Panel du haut */
	protected JPanel jp_haut;
	
	/** Panel de contenu */
	protected JPanel jp_contenu;
	
	/** Panel du bas */
	protected JPanel jp_bas;

	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 */
	public JPPrincipalAbstract(Window w_parent) {
		// Retenir la fenêtre parente
		this.w_parent = w_parent;

		// Layout
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// Panneau du haut
		this.jp_haut = new JPanel();
		this.jp_haut.setBackground(new Color(200, 200, 200));
		this.jp_haut.setBorder(new LineBorder(new Color(200, 200, 200), 8));
		this.jp_haut.setMaximumSize(new Dimension(this.jp_haut.getMaximumSize().width, 150));
		this.add(jp_haut);

		// Contenu
		this.jp_contenu = new JPanel(new BorderLayout());
		this.add(this.jp_contenu);

		// Panneau du bas
		this.jp_bas = new JPanel();
		this.jp_bas.setBackground(new Color(200, 200, 200));
		this.jp_bas.setBorder(new LineBorder(new Color(200, 200, 200), 8));
		this.jp_bas.setMaximumSize(new Dimension(this.jp_haut.getMaximumSize().width, 150));
		this.add(jp_bas);
	}

}
