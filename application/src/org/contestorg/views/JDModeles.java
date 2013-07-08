package org.contestorg.views;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.contestorg.common.Tools;
import org.contestorg.controllers.ContestOrg;
import org.contestorg.infos.InfosModele;
import org.contestorg.interfaces.ICollector;

/**
 * Boîte de dialogue de séléction de modèles
 */
@SuppressWarnings("serial")
public class JDModeles extends JDPattern implements ICollector<Integer>
{
	/** Panel des modèles */
	private JPanel jp_modeles;
	
	/** Boutons des modèles */
	private ArrayList<JButton> jb_modeles;

	/** Bouton de concours personnalisé */
	private JButton jb_personnalise;

	/** Liste des modèles */
	private ArrayList<InfosModele> modeles;
	
	/** Modèle transmis à la boîte de dialogue de sélection d'une variante */
	private InfosModele modele;
	
	/** Boîte de dialogue de sélection d'une variante */
	private JDModeleVariante jd_modele_variante;

	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 */
	public JDModeles(Window w_parent) {
		// Appeler le constructeur du parent
		super(w_parent, "Nouveau concours");
		
		// Récupérer la liste des modèles
		this.modeles = ContestOrg.get().getCtrlOut().getModeles();
		
		// Masquer le bouton de validation
		this.jb_valider.setVisible(false);
		
		// Séléction d'un modèle
		this.jp_contenu.add(ViewHelper.title("Séléctionner un modèle", ViewHelper.H1));
		
		// Vérifier s'il y a des modèles disponibles
		if(this.modeles != null && this.modeles.size() != 0) {
			// Nombre de colonnes et lignes
			int cols = 3;
			int rows = (int)Math.ceil((double)this.modeles.size()/(double)cols);
			
			// Créer le panel des modèles
			this.jp_modeles = new JPanel(new GridLayout(rows, cols));
			this.jp_contenu.add(this.jp_modeles);
			
			// Ajouter un bouton par modèle
			this.jb_modeles = new ArrayList<JButton>();
			for(final InfosModele modele : this.modeles) {
				JPanel jp_modele = new JPanel(new BorderLayout());
				JButton jb_modele;
				if(modele.getImage() != null) {
					jb_modele = new JButton(modele.getNom(), new ImageIcon(modele.getChemin()+File.separator+modele.getImage()));
				} else {
					jb_modele = new JButton(modele.getNom());
				}
				if(modele.getVariantes().size() == 1) {
					jb_modele.setToolTipText("Ouvrir le modèle");
				} else {
					jb_modele.setToolTipText("Afficher les variantes");
				}
				jp_modele.add(jb_modele, BorderLayout.CENTER);
				if(modele.getDescription() != null && !modele.getDescription().equals("")) {
					JButton jb_description = new JButton(new ImageIcon("img/farm/16x16/information.png"));
					jb_description.setToolTipText("Afficher la description");
					jp_modele.add(jb_description, BorderLayout.EAST);
					
					final JDModeles parent = this;
					jb_description.addActionListener(new ActionListener() {
						/**
						 * @see ActionListener#actionPerformed(ActionEvent)
						 */
						@Override
						public void actionPerformed (ActionEvent e) {
							// Afficher la description
							new JDModeleDescription(parent, modele.getNom(), modele.getDescription()).setVisible(true);
						}
					});
				}
				this.jp_modeles.add(jp_modele);
				this.jb_modeles.add(jb_modele);
				jb_modele.addActionListener(this);
			}
			
			// Saviez-vous ?
			this.jp_contenu.add(Box.createVerticalStrut(5));
			this.jp_contenu.add(ViewHelper.pinformation("Saviez-vous que vous pouvez créer vos propres modèles ?",Tools.isWindows() ? "doc/documentation.chm" : "doc/documentation.html","Ouvrir l'aide"));

			// Créer votre propre concours
			this.jp_contenu.add(ViewHelper.title("Créer votre propre concours", ViewHelper.H1));
			this.jb_personnalise = new JButton("Concours personnalisé", new ImageIcon("img/farm/32x32/cog.png"));
			this.jb_personnalise.addActionListener(this);
			this.jp_contenu.add(ViewHelper.center(this.jb_personnalise));
		} else {
			// Erreur
			this.jp_contenu.add(ViewHelper.perror("Erreur lors de la récupération des modèles (cf. log.txt)."));
		}
		
		// Pack
		this.pack();
	}
	
	/**
	 * @see JDPattern#ok()
	 */
	@Override
	protected void ok () {
	}

	/**
	 * @see JDPattern#quit()
	 */
	@Override
	protected void quit () {
		// Fermer la boîte de dialogue
		this.setVisible(false);
	}
	
	/**
	 * @see ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed (ActionEvent event) {
		if(event.getSource() == this.jb_personnalise) {
			// Fermer la boîte de dialogue
			this.setVisible(false);
			
			// Afficher la fenêtre de création personnalisé de concours
			JDialog dialog = new JDConcoursCreer(this.w_parent);
			dialog.setVisible(true);
		} else {
			// Vérifier si l'action de vient pas d'un bouton de modèle
			if(this.jb_modeles != null) {
				for(int i=0;i<this.jb_modeles.size();i++) {
					if(event.getSource() == this.jb_modeles.get(i)) {
						// Récupérer le modèle courant
						InfosModele modele = this.modeles.get(i);
						
						// Vérifier si le modèle n'a pas plusieurs variantes
						if(modele.getVariantes().size() > 1) {
							// Retenir le modèle transmis à la boîte de dialogue de sélection de variante
							this.modele = modele;
							
							// Ouvrir la boîte de dialogue de sélection de variante et l'afficher
							this.jd_modele_variante = new JDModeleVariante(this, modele, this);
							this.jd_modele_variante.setVisible(true);
						} else {
							// Construire le chemin associé à la seul variante du modèle
							String chemin = modele.getChemin()+File.separator+modele.getVariantes().get(0).getFichier();
							
							// Demander l'ouverture du modèle
							if(ContestOrg.get().modeleOuvrir(chemin)) {
								// Fermer la boîte de dialogue
								this.setVisible(false);
							}
						}
						
						// Arrêter l'éxecution de la méthode
						return;
					}
				}
			}
			
			// Appeler l'implémentation du parent
			super.actionPerformed(event);
		}
	}

	/**
	 * @see ICollector#collect(Object)
	 */
	@Override
	public void collect (Integer index) {
		// Construire le chemin associé à la variante sélectionnée du modèle
		String chemin = this.modele.getChemin()+File.separator+this.modele.getVariantes().get(index).getFichier();
		
		// Demander l'ouverture de la variante
		if(ContestOrg.get().modeleOuvrir(chemin)) {
			// Fermer la boîte de dialogue de sélection de la variante
			this.jd_modele_variante.setVisible(false);
			
			// Fermer la boîte de dialogue
			this.setVisible(false);
		}
	}

	/**
	 * @see ICollector#cancel()
	 */
	@Override
	public void cancel () {
		// Fermer la boîte de dialogue de sélection de la variante
		this.jd_modele_variante.setVisible(false);
	}
	
	/**
	 * Boîte de dialogue pour afficher la description d'un modèle
	 */
	private class JDModeleDescription extends JDPattern {

		/**
		 * Constructeur
		 * @param w_parent fenêtre parent
		 * @param titre titre de la fenêtre
		 * @param description description du modèle
		 */
		public JDModeleDescription(Window w_parent, String titre, String description) {
			// Appeler le constructeur parent
			super(w_parent, titre);
			
			// Masquer le bouton "Valider"
			this.jb_valider.setVisible(false);
			
			// Changer le bouton "Annuler" en "Fermer"
			this.jb_annuler.setText("Fermer");
			
			// Ajouter la description
			this.jp_contenu.add(new JLabel("<html><div style=\"width:500px;\">"+description+"</div></html>"));
			
			// Pack
			this.pack();
		}

		/**
		 * @see JDPattern#ok()
		 */
		@Override
		protected void ok () {
		}

		/**
		 * @see JDPattern#quit()
		 */
		@Override
		protected void quit () {
			// Fermer la fenêtre
			this.setVisible(false);
		}
		
	}
	
}
