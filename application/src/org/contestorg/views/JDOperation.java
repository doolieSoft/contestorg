package org.contestorg.views;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import org.contestorg.interfaces.IEndListener;
import org.contestorg.interfaces.IOperation;
import org.contestorg.interfaces.IOperationListener;

/**
 * Boîte de dialogue d'une opération
 */
@SuppressWarnings("serial")
public class JDOperation extends JDPattern implements IOperationListener
{

	/** Statut de l'opération */
	private JLabel jl_statutOperation = new JLabel(new ImageIcon("img/farm/32x32/hourglass.png"));
	
	/** Avancement de l'opération */
	private JProgressBar jpb_avancementOperation = new JProgressBar(0, 100);
	
	/** Avancement de l'opération */
	private JLabel jl_avancementOperation = new JLabel("Veuillez patientez pendant le lancement de l'opération ...",SwingConstants.CENTER);

	/** Annulation demandée ? */
	private boolean demandeAnnulation = false;

	/** Opération terminée ? */
	private boolean operationTerminee = false;
	
	/** Fermer la fenêtre si opération réussie ? */
	private boolean autoexit;
	
	/** Operation associée */
	private IOperation operation;
	
	/** Listeners de fin */
	private ArrayList<IEndListener> listeners = new ArrayList<IEndListener>();

	// Constructeurs
	
	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param titre titre de la boîte de dialogue
	 * @param operation opération
	 */
	public JDOperation(Window w_parent, String titre, IOperation operation) {
		this(w_parent, titre, operation, false, true);
	}
	
	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param titre titre de la boîte de dialogue
	 * @param operation opération
	 * @param autoexit fermer la fenêtre si opération réussie ?
	 * @param allowstop autoriser l'utilisateur à arrêter l'opération ?
	 */
	public JDOperation(Window w_parent, String titre, IOperation operation, boolean autoexit, boolean allowstop) {
		// Appeller le constructeur du parent
		super(w_parent, titre);
		
		// Retenir l'opération et l'autoexit
		this.operation = operation;
		this.autoexit = autoexit;
		
		// Ne pas autoriser le bouton annuler si nécéssaire
		this.jb_annuler.setEnabled(allowstop);
		
		// Ecouter l'opération
		this.operation.addListener(this);

		// Barre de progression
		JPanel jp_progression = new JPanel(new GridBagLayout());

		GridBagConstraints contrainte_image = new GridBagConstraints();
		contrainte_image.gridx = 0;
		contrainte_image.weightx = 0;
		contrainte_image.insets = new Insets(4, 0, 2, 4);
		contrainte_image.anchor = GridBagConstraints.BASELINE_LEADING;

		GridBagConstraints contrainte_barre = new GridBagConstraints();
		contrainte_barre.gridx = 1;
		contrainte_barre.weightx = 1;
		contrainte_barre.insets = new Insets(4, 0, 2, 0);
		contrainte_barre.gridwidth = GridBagConstraints.REMAINDER;
		contrainte_barre.anchor = GridBagConstraints.BASELINE_LEADING;
		contrainte_barre.fill = GridBagConstraints.HORIZONTAL;

		jp_progression.add(this.jl_statutOperation, contrainte_image);
		jp_progression.add(this.jpb_avancementOperation, contrainte_barre);

		this.jp_contenu.add(jp_progression);

		// Message
		this.jp_contenu.add(ViewHelper.center(this.jl_avancementOperation));

		// Espace vertical
		this.jp_contenu.add(Box.createVerticalStrut(5));

		// Désactiver le bouton valider
		this.jb_valider.setEnabled(false);

		// Augmenter la largeur préférée pour prévoir les messages longs
		this.jl_avancementOperation.setPreferredSize(new Dimension(500, this.jl_avancementOperation.getPreferredSize().height));

		// Pack
		this.pack();
	}
	
	/**
	 * @see JDPattern#ok()
	 */
	@Override
	protected void ok () {
		if (this.operationTerminee) {
			// Masquer la fenêtre
			this.setVisible(false);
		} else {
			// Demander à l'utilisateur d'attendre la fin de l'opération
			ViewHelper.derror(this, "Veuillez attendre la fin de l'opération ...");
		}
	}

	/**
	 * @see JDPattern#quit()
	 */
	@Override
	protected void quit () {		
		// Vérifier si l'opération est bien terminée
		if(this.operationTerminee) {
			// Masquer la fenêtre
			this.setVisible(false);
			
			// Signaler aux listeners que l'opération est terminée
			for(IEndListener listener : this.listeners) {
				listener.end();
			}
		} else if(!this.demandeAnnulation) {			
			// Retenir la demande d'annulation
			this.demandeAnnulation = true;
			
			// Désactiver les boutons valider et annuler
			this.jb_valider.setEnabled(false);
			this.jb_annuler.setEnabled(false);
			
			// Message d'attente
			this.jl_avancementOperation.setText("Arret de l'opération ...");
			
			// Demande d'annulation
			this.operation.operationStop();
		} else {
			// Demande de patienter
			ViewHelper.derror(this, "Veuillez attendre la fin de l'arret de l'opération ...");
		}
	}
	
	/**
	 * Fin de l'opération
	 * @param icone icone de fin
	 */
	private void fin(String icone) {		
		// Retenir la fin de l'opération 
		this.operationTerminee = true;
		
		// Quitter la fenêtre si il y a eu une demande d'annulation
		if(this.demandeAnnulation) {
			this.quit();
		}
		
		// Activer/Desactiver les boutons valider/annuler
		this.jb_valider.setEnabled(true);
		this.jb_annuler.setEnabled(false);
		
		// Modifier l'image de statut de l'opération
		this.jl_statutOperation.setIcon(new ImageIcon(icone));
	}

	// Implémentation de ITestListener
	@Override
	public void progressionAvancement (double progression) {
		// Ne pas considérer la progression si demande d'annulation
		if(!this.demandeAnnulation) {
			this.jpb_avancementOperation.setValue((int)(progression * 100));
		}
	}
	@Override
	public void progressionMessage (String message) {
		// Ne pas considérer les messages si demande d'annulation
		if(!this.demandeAnnulation) {
			this.jl_avancementOperation.setText(message);
		}
	}
	@Override
	public void progressionFin () {
		// La fin est considérée comme un arret si demande d'annulation
		if(!this.demandeAnnulation) {
			this.jpb_avancementOperation.setValue(100);
			this.operationTerminee = true;
		} else {
			this.operationArret();
		}
	}
	@Override
	public void operationReussite () {
		// La réussite est considérée comme un arret si demande d'annulation
		if(!this.demandeAnnulation) {
			this.fin("img/farm/32x32/tick.png");
			if(this.autoexit) {
				try { Thread.sleep(500); } catch (InterruptedException e) { }
				this.quit();
			}
		} else {
			this.operationArret();
		}
	}
	@Override
	public void operationEchec () {
		// L'échec est considéré comme un arret si demande d'annulation
		if(!this.demandeAnnulation) {
			this.fin("img/farm/32x32/cross.png");
		} else {
			this.operationArret();
		}
	}
	@Override
	public void operationArret () {
		this.jl_avancementOperation.setText("Opération arrêtée.");
		this.fin("img/farm/32x32/stop.png");
	}
	
	// Ajouter/Supprimer des listeners
	
	/**
	 * Ajouter un listener
	 * @param listener listener à ajouter
	 */
	public void addListener(IEndListener listener) {
		this.listeners.add(listener);
	}
	
	/**
	 * Supprimer un listener
	 * @param listener listener à supprimer
	 */
	public void removeListener(IEndListener listener) {
		this.listeners.remove(listener);
	}

}
