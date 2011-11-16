package org.contestorg.views;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

@SuppressWarnings("serial")
abstract public class JDPattern extends JDialog implements ActionListener, WindowListener
{
	// Fenetre parent
	protected Window w_parent;

	// Panels de titre et de contenu
	protected JPanel jp_titre;
	protected JPanel jp_contenu;

	// Boutons valider et annuler
	protected JButton jb_valider;
	protected JButton jb_annuler;

	// Espacements à gauche et à droite
	protected Component hs_gauche = Box.createHorizontalStrut(5);
	protected Component hs_droite = Box.createHorizontalStrut(5);

	// Panel du bas
	private JPanel jp_bas;

	// Constructeur
	public JDPattern(Window w_parent, String titre) {
		this(w_parent, titre, false, false);
	}
	public JDPattern(Window w_parent, String titre, boolean afficheTitre, boolean resizable) {
		// Parametres de la fenetre
		super(w_parent, titre); // Titre
		this.setModal(true); // Fenetre modale
		this.setLayout(new BorderLayout()); // Layout par default
		this.setResizable(resizable); // Redimensionnable ?		
		this.setIconImage(new ImageIcon("img/farm/32x32/sport.png").getImage()); // Icone de la fenêtre
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE); // Ne rien faire si echap

		// Retenir la fenetre parent
		this.w_parent = w_parent;
		
		// Titre
		if (afficheTitre) {
			this.jp_titre = new JPanel();
			this.jp_titre.add(ViewHelper.title(titre, ViewHelper.H1));
			this.add(this.jp_titre, BorderLayout.NORTH);
		}

		// Panel de contenu
		this.jp_contenu = new JPanel();
		this.jp_contenu.setLayout(new BoxLayout(this.jp_contenu, BoxLayout.Y_AXIS));
		this.add(this.jp_contenu, BorderLayout.CENTER);

		// Bord de gauche et de droite
		this.add(this.hs_gauche, BorderLayout.WEST);
		this.add(this.hs_droite, BorderLayout.EAST);

		// Boutons valider et annuler
		this.jp_bas = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		this.jb_valider = new JButton("Valider", new ImageIcon("img/farm/16x16/accept.png"));
		this.jb_annuler = new JButton("Annuler", new ImageIcon("img/farm/16x16/cancel.png"));
		this.jp_bas.add(this.jb_valider);
		this.jp_bas.add(this.jb_annuler);
		this.add(this.jp_bas, BorderLayout.SOUTH);

		// Bouton par default
		// this.getRootPane().setDefaultButton(this.jb_valider);

		// Actions liées à certaines touches
		final JDPattern jfp = this;
		Action escape = new AbstractAction() {
			public void actionPerformed (ActionEvent e) {
				jfp.quit();
			}
		};
		Action enter = new AbstractAction() {
			public void actionPerformed (ActionEvent e) {
				jfp.ok();
			}
		};

		InputMap im = this.jp_contenu.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		im.put(KeyStroke.getKeyStroke("ESCAPE"), "escape");
		im.put(KeyStroke.getKeyStroke("ENTER"), "enter");

		ActionMap am = this.jp_contenu.getActionMap();
		am.put("escape", escape);
		am.put("enter", enter);

		// Evenements
		this.addWindowListener(this);
		this.jb_valider.addActionListener(this);
		this.jb_annuler.addActionListener(this);
	}
	
	// Surcharge de pack
	public void pack() {
		// Appeller le pack du parent
		super.pack();
		
		// Récupérer les dimensions de l'écran
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		// Points/Dimensions de référence
		int x = 0, y = 0, height, width;
		if(this.w_parent != null) {
			x = this.w_parent.getX();
			y = this.w_parent.getY();
			width = this.w_parent.getWidth();
			height = this.w_parent.getHeight();
		} else {
			width = screenSize.width;
			height = screenSize.height;
		}
		
		// Positionner la fenetre au milieu
		this.setLocation((width-this.getWidth())/2+x,(height-this.getHeight())/2+y);
	}

	// Ajouter des boutons en bas
	protected void addButton (JButton button) {
		this.jp_bas.add(button);
	}

	// Action sur valider
	protected abstract void ok ();

	// Quitter la fenêtre
	protected abstract void quit ();

	// Implémentation de ActionListener
	public void actionPerformed (ActionEvent event) {
		if (event.getSource() == this.jb_annuler) {
			// Quitter la fenêtre
			this.quit();
		} else if (event.getSource() == this.jb_valider) {
			// Action sur valider
			this.ok();
		}
	}

	// Implémentation de WindowListener
	public void windowActivated (WindowEvent event) {
	}
	public void windowClosed (WindowEvent event) {
	}
	public void windowClosing (WindowEvent event) {
		this.quit();
	}
	public void windowDeactivated (WindowEvent event) {
	}
	public void windowDeiconified (WindowEvent event) {
	}
	public void windowIconified (WindowEvent event) {
	}
	public void windowOpened (WindowEvent event) {
	}

}
