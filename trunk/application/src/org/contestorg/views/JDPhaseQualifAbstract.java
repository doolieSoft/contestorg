package org.contestorg.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.contestorg.common.Quintuple;
import org.contestorg.controllers.ContestOrg;
import org.contestorg.infos.Configuration;
import org.contestorg.infos.Couple;
import org.contestorg.infos.InfosModelConcours;
import org.contestorg.infos.InfosModelMatchPhasesQualifs;
import org.contestorg.infos.InfosModelPhaseQualificative;
import org.contestorg.interfaces.ICollector;
import org.contestorg.interfaces.IGeneration;
import org.contestorg.interfaces.IGenerationListener;

/**
 * Boîte de dialogue de création/édition d'une phase qualificative
 */
@SuppressWarnings("serial")
public class JDPhaseQualifAbstract extends JDPattern implements ItemListener, IGenerationListener<Configuration<String>>
{
	
	/** Collecteur des informations de la phase qualificative */
	private ICollector<Quintuple<String,String,Configuration<String>, InfosModelPhaseQualificative, InfosModelMatchPhasesQualifs>> collector;
	
	// Participants
	
	/** Liste des participants */
	protected ArrayList<String> participants;
	
	/** Rangs des participants */
	private HashMap<String,Integer> rangsParticipants = new HashMap<String, Integer>();
	
	// Choix de la catégorie et de la poule

	/** Panel des catégories et poules */
	protected JPCategoriePoule jp_categoriePoule = new JPCategoriePoule();
	
	// Paramètres de la génération
	
	/** Mode avancé */
	private JRadioButton jrb_mode_avance = new JRadioButton("Avancé", false);
	
	/** Mode basique */
	private JRadioButton jrb_mode_basique = new JRadioButton("Basique", true);
	
	/** Panel des participants */
	private JPanel jp_participants;
	
	/** Séléctions des participants */
	protected JCheckBox[] jcbs_participants;
	
	// Avancement de la génération
	
	/** Statut de la génération */
	private JLabel jl_statutGeneration = new JLabel(new ImageIcon("img/farm/32x32/hourglass.png"));
	/** Avancement de la génération */
	private JProgressBar jpb_avancementGeneration = new JProgressBar(0,100);
	/** Avancement de la génération */
	private JLabel jl_avancementGeneration = new JLabel("En attente de demande de génération ...");
	/** Bouton "Générer" */
	private JButton jb_generer = new JButton("Générer", new ImageIcon("img/farm/16x16/control_play_blue.png"));
	/** Bouton "Arrêter" */
	private JButton jb_arreter = new JButton("Arrêter", new ImageIcon("img/farm/16x16/control_stop_blue.png"));

	/** Génération */
	private IGeneration<Configuration<String>> generation;

	/** Arrêt demandé ? */
	private boolean demandeArret = false;
	/** Annulation demandé ? */
	private boolean demandeAnnulation = false;
	
	// Conservation/Restauration de configuration
	
	/** Bouton "Conserver" */
	private JButton jb_conserver = new JButton("Conserver", new ImageIcon("img/farm/16x16/box.png"));
	
	/** Bouton "Restaurer" */
	private JButton jb_restaurer = new JButton("Restaurer", new ImageIcon("img/farm/16x16/box_down.png"));
	
	/** Configuration conservée */
	private Configuration<String> configurationConservee = null;
	
	// Meilleure configuration trouvée
	
	/** Nombre de matchs déjàs joués */
	private JTextField jtf_matchsDejaJoues = new JTextField();
	
	/** Nombre de villes communes */
	private JTextField jtf_villesCommunes = new JTextField();
	
	/** Différence moyenne de rang */
	private JTextField jtf_differenceMoyenneRang = new JTextField();
	
	/** Différence maximale de rang */
	private JTextField jtf_differenceMaximaleRang = new JTextField();
	
	/** Panel du résultat */
	private JPanel jp_resultat;
	
	/** Listes des participants A */
	@SuppressWarnings("rawtypes")
	private JComboBox[] jcbs_participantsA;
	
	/** Listes des participants B */
	@SuppressWarnings("rawtypes")
	private JComboBox[] jcbs_participantsB;
	
	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param titre titre de la boîte de dialogue
	 * @param collector collecteur des informations de la phase qualificative
	 * @param nomCategorie nom de la catégorie de destination
	 * @param nomPoule nom de la poule de destination
	 */
	public JDPhaseQualifAbstract(Window w_parent, String titre, ICollector<Quintuple<String,String,Configuration<String>, InfosModelPhaseQualificative, InfosModelMatchPhasesQualifs>> collector, String nomCategorie, String nomPoule) {
		// Appeller le constructeur du parent
		super(w_parent, titre);
		
		// Retenir le collector
		this.collector = collector;
		
		// Catégorie et poule
		this.jp_contenu.add(this.jp_categoriePoule);
		this.jp_categoriePoule.setNomCategorie(nomCategorie);
		this.jp_categoriePoule.setNomPoule(nomPoule);
		
		// Ecoute les changements de catégorie et de poule
		this.jp_categoriePoule.addItemListener(new ItemListener() {
			/**
			 * @see ItemListener#itemStateChanged(ItemEvent)
			 */
			@Override
			public void itemStateChanged (ItemEvent event) {
				// Réinitialiser la configuration conservée
				configurationConservee = null;
				
				// Désactiver le bouton "restaurer"
				jb_restaurer.setEnabled(false);
				
				// Tout rafraichir
				refreshAll();
			}
		});
		
		// Paramètres de la génération
		this.jp_contenu.add(ViewHelper.title("Paramètres de la génération", ViewHelper.H1));
		
		// Mode de génération
		ButtonGroup bg_mode = new ButtonGroup();
		bg_mode.add(this.jrb_mode_avance);
		bg_mode.add(this.jrb_mode_basique);
		JPanel jp_mode = new JPanel(new GridLayout(1, 2));
		jp_mode.add(this.jrb_mode_avance);
		jp_mode.add(this.jrb_mode_basique);
		
		JLabel[] jls_mode = { new JLabel("Mode : ") };
		JComponent[] jcs_mode = { jp_mode };
		this.jp_contenu.add(ViewHelper.inputs(jls_mode, jcs_mode));
		
		// Participants
		this.jp_contenu.add(ViewHelper.left(new JLabel(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Equipes participantes :" : "Joueurs participants : ")));
		
		this.jp_participants = new JPanel();
		this.jp_participants.setLayout(new BoxLayout(this.jp_participants, BoxLayout.Y_AXIS));
		
		JScrollPane jsp_participants = new JScrollPane(this.jp_participants);
		jsp_participants.setPreferredSize(new Dimension(jsp_participants.getPreferredSize().width, 140));
		this.jp_contenu.add(jsp_participants);
		
		// Avancement de la génération
		this.jp_contenu.add(ViewHelper.title("Avancement de la génération", ViewHelper.H1));

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

		jp_progression.add(this.jl_statutGeneration, contrainte_image);
		jp_progression.add(this.jpb_avancementGeneration, contrainte_barre);

		this.jp_contenu.add(jp_progression);
		
		this.jp_contenu.add(ViewHelper.center(this.jl_avancementGeneration));
		this.jp_contenu.add(Box.createVerticalStrut(5));
		this.jp_contenu.add(ViewHelper.left(this.jb_generer, this.jb_arreter));
		this.jp_contenu.add(Box.createVerticalStrut(5));
		this.jb_arreter.setEnabled(false);
		
		// Meilleure configuration trouvée
		this.jp_contenu.add(ViewHelper.title("Meilleure configuration trouvée", ViewHelper.H1));
		JLabel[] jls_resultat = { new JLabel("Nombre de matchs déjà joués : "), new JLabel("Différence moyenne de rang : "), new JLabel("Différence maximale de rang : "), new JLabel("Nombre de villes communes :") };
		JComponent[] jcs_resultat = { this.jtf_matchsDejaJoues, this.jtf_differenceMoyenneRang, this.jtf_differenceMaximaleRang, this.jtf_villesCommunes };
		this.jp_contenu.add(ViewHelper.inputs(jls_resultat, jcs_resultat));
		
		this.jtf_matchsDejaJoues.setEditable(false);
		this.jtf_differenceMoyenneRang.setEditable(false);
		this.jtf_differenceMaximaleRang.setEditable(false);
		this.jtf_villesCommunes.setEditable(false);

		this.jp_contenu.add(Box.createVerticalStrut(5));
		
		this.jp_contenu.add(ViewHelper.left(this.jb_conserver,this.jb_restaurer));
		this.jb_restaurer.setEnabled(false);
		
		this.jp_contenu.add(Box.createVerticalStrut(5));		
		
		this.jp_resultat = new JPanel();
		this.jp_resultat.setLayout(new BoxLayout(this.jp_resultat, BoxLayout.Y_AXIS));

		JPanel jp_resultat_largeur = new JPanel(new BorderLayout());
		jp_resultat_largeur.add(this.jp_resultat, BorderLayout.NORTH);
		JScrollPane jsp_resultat = new JScrollPane(jp_resultat_largeur);
		jsp_resultat.setPreferredSize(new Dimension(jsp_resultat.getPreferredSize().width, 140));
		
		this.jp_contenu.add(jsp_resultat);
		
		// Rafraichir les données d'après la catégorie et la poule séléctionné
		this.refreshAll();
		
		// Ecouter les boutons
		this.jb_generer.addActionListener(this);
		this.jb_arreter.addActionListener(this);
		this.jb_conserver.addActionListener(this);
		this.jb_restaurer.addActionListener(this);
		
		// Ajouter le bouton d'aide
		this.addButton(new JBHelperButton(this) {
			@Override
			protected String getMessage () {
				return "<h1>Mode avancé</h1>" +
					   "Le mode avancé teste l'ensemble des configurations possibles. Dès que l'algorithme<br/>" +
					   "remonte une meilleure configuration que celle précédement trouvée, celle-ci apparait<br/>" +
					   "dans le cadre \"Meilleure configuration trouvée\". Au délà de 8 participants, le temps de<br/>" +
					   "génération devient exponentiellement long. Vous pouvez arrêter la génération à tout<br/>" +
					   "moment et considérer la meilleure configuration trouvée jusque là.<br/>" +
					   "<br/>" +
					   "<h1>Mode basique</h1>" +
					   "Le mode basique est bien moins gourmant en ressource. Il génère tous les couples de<br/>" +
					   "participants possibles et les trie en fonction de l'affinité des participants que les composent.<br/>" +
					   "L'algorithme choisit ensuite des couples compatibles en retrouvés en haut de classement.<br/>" +
					   "Celui ci peut parfois donner de bon résultats mais il ne s'agit pas forcement de la meilleure<br/>" +
					   "configuration.<br/>" +
					   "<br/>" +
					   "<h1>Conserver/Restaurer</h1>" +
					   "Ces boutons sont à utiliser avec le mode basique. Il vous permettent de garder en mémoire une<br/>" +
					   "configuration et de la restaurer si aucune meilleure configuration n'est remontée." +
					   "<br/>" +
					   "<h1>Critères de génération</h1>" +
					   "<ol>" +
					   "<li>Est-ce que les participants ont déjà joué ensemble ?</li>" +
					   "<li>Est-ce que les participants sont de même niveau ?</li>" +
					   "<li>Est-ce que les participants viennent de la même ville ?</li>" +
					   "</ol>";
			}
		});
		
		// Elargir la fenêtre
		this.setPreferredSize(new Dimension(this.getPreferredSize().width + 250, this.getPreferredSize().height));
		
		// Pack
		this.pack();
	}
	
	/**
	 * @see JDPattern#ok()
	 */
	@Override
	protected void ok () {
		// Vérifier si une génération n'est pas en cours
		if(this.generation == null) {
			// Récupérer la liste des participants séléctionnés
			ArrayList<String> participantsSelectionnes = this.getParticipantsSelectionnes();
			
			// Vérifier la conformité de la configuration choisie
			boolean erreur = false;
			ArrayList<String> participantsConfiguration = new ArrayList<String>();
			for(int i=0;i<this.jcbs_participantsA.length;i++) {
				// Vérifier si un participant joue avec lui-même
				if(this.jcbs_participantsA[i].getSelectedIndex() == this.jcbs_participantsB[i].getSelectedIndex()) {
					// Récupérer le nom du participant
					String nomParticipant = this.isFantome() && this.jcbs_participantsA[i].getSelectedIndex() == this.jcbs_participantsA[i].getItemCount()-1 ? (ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Equipe fantome" : "Joueur fantome") : participantsSelectionnes.get(this.jcbs_participantsA[i].getSelectedIndex());
					
					// Erreur
					ViewHelper.derror(this, (ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "L'équipe" : "Le joueur")+" \""+nomParticipant+"\" a un match contre "+(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "elle-même" : "lui-même")+".");
					erreur = true;
				}
				if(!erreur) {
					// Vérifier si un participant participe plusieurs fois dans la phase qualificative 
					if(!this.isFantome() || this.jcbs_participantsA[i].getSelectedIndex() != this.jcbs_participantsA[i].getItemCount()-1) {
						if(participantsConfiguration.contains(participantsSelectionnes.get(this.jcbs_participantsA[i].getSelectedIndex()))) {
							erreur = !ViewHelper.confirmation(this,(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "L'équipe" : "Le joueur")+" \""+participantsSelectionnes.get(this.jcbs_participantsA[i].getSelectedIndex())+"\" participe plusieurs fois dans la phase qualificative. Désirez-vous continuer ?"); 
						} else {
							participantsConfiguration.add(participantsSelectionnes.get(this.jcbs_participantsA[i].getSelectedIndex()));
						}
					}
					if(!this.isFantome() || this.jcbs_participantsB[i].getSelectedIndex() != this.jcbs_participantsB[i].getItemCount()-1) {
						if(participantsConfiguration.contains(participantsSelectionnes.get(this.jcbs_participantsB[i].getSelectedIndex()))) {
							erreur = !ViewHelper.confirmation(this,(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "L'équipe" : "Le joueur")+" \""+participantsSelectionnes.get(this.jcbs_participantsB[i].getSelectedIndex())+"\" participe plusieurs fois dans la phase qualificative. Désirez-vous continuer ?"); 
						} else {
							participantsConfiguration.add(participantsSelectionnes.get(this.jcbs_participantsB[i].getSelectedIndex()));
						}
					}
				}
			}
			if(this.jcbs_participantsA.length == 0) {
				// Erreur
				ViewHelper.derror(this, "Il n'y a aucun match défini dans la phase qualificative.");
				erreur = true;
			}
			
			// Transformer la informations au collector
			if(!erreur) {
				this.collector.collect(new Quintuple<String, String, Configuration<String>, InfosModelPhaseQualificative, InfosModelMatchPhasesQualifs>(this.jp_categoriePoule.getNomCategorie(),this.jp_categoriePoule.getNomPoule(),this.getConfiguration(), new InfosModelPhaseQualificative(), new InfosModelMatchPhasesQualifs(null,null)));
			}
		}
	}
		
	/**
	 * @see JDPattern#quit()
	 */
	@Override
	protected void quit () {
		// Vérifier si une génération n'est pas en cours
		if(this.generation != null) {
			// Vérifier si la demande d'annuler n'a pas déjà été faite
			if(this.demandeAnnulation) {
				// Erreur
				ViewHelper.derror(this, "Veuillez attendre la fin de l'annulation de la génération ...");
			} else {
				// Retenir la demande d'annulation
				this.demandeAnnulation = true;
				this.demandeArret = false;
				
				// Désactiver les boutons annuler et arreter
				this.jb_arreter.setEnabled(false);
				this.jb_annuler.setEnabled(false);
				
				// Message d'attente
				this.jl_avancementGeneration.setText("Annulation de la génération ...");
				
				// Demande d'annulation
				this.generation.generationCancel();
			}
		} else {
			// Annuler
			this.collector.cancel();
		}
	}
	
	/**
	 * Récupérer la configuration actuelle
	 * @return configuration actuelle
	 */
	private Configuration<String> getConfiguration () {
		if (this.jcbs_participantsA != null && this.jcbs_participantsB != null) {
			ArrayList<String> participantsSelectionnes = this.getParticipantsSelectionnes();
			Configuration<String> configuration = new Configuration<String>(this.jcbs_participantsA.length);
			boolean fantome = this.isFantome();
			for (int i = 0; i < this.jcbs_participantsA.length; i++) {
				String participantA = fantome && this.jcbs_participantsA[i].getSelectedIndex() == this.jcbs_participantsA[i].getItemCount() - 1 ? null : participantsSelectionnes.get(this.jcbs_participantsA[i].getSelectedIndex());
				String participantB = fantome && this.jcbs_participantsB[i].getSelectedIndex() == this.jcbs_participantsB[i].getItemCount() - 1 ? null : participantsSelectionnes.get(this.jcbs_participantsB[i].getSelectedIndex());
				configuration.addCouple(new Couple<String>(participantA, participantB));
			}
			return configuration;
		}
		return null;
	}
	
	/**
	 * Définir la configuration actuelle
	 * @param configuration configuration
	 */
	protected void setConfiguration(Configuration<String> configuration) {
		// Placer la configuration
		ArrayList<String> participantsSelectionnes = this.getParticipantsSelectionnes();
		for(int i=0;i<configuration.getCouples().length;i++) {
			// Séléctionner le participant A
			if(configuration.getCouples()[i].getParticipantA() != null) {
				this.jcbs_participantsA[i].setSelectedIndex(participantsSelectionnes.indexOf(configuration.getCouples()[i].getParticipantA()));
			} else {
				this.jcbs_participantsA[i].setSelectedIndex(this.jcbs_participantsA[i].getItemCount()-1);
			}
			
			// Séléctionner le participant B
			if(configuration.getCouples()[i].getParticipantB() != null) {
				this.jcbs_participantsB[i].setSelectedIndex(participantsSelectionnes.indexOf(configuration.getCouples()[i].getParticipantB()));
			} else {
				this.jcbs_participantsB[i].setSelectedIndex(this.jcbs_participantsB[i].getItemCount()-1);
			}
		}
		
		// Rafraichir les couleurs
		this.refreshColors();
	}
	
	/**
	 * Tout rafraichir
	 */
	private void refreshAll() {
		// Rafraichir la liste des participants
		this.refreshParticipants();
		
		// Rafraichir la liste des matchs
		this.refreshMatchs();
		
		// Rafraichir les statistiques
		this.refreshStatistiques();
		
		// Rafraichir les couleurs
		this.refreshColors();
	}
	
	/**
	 * Rafriachir la liste des participants
	 */
	private void refreshParticipants() {
		// Récupérer les participants qui peuvent participer
		this.participants = ContestOrg.get().getCtrlPhasesQualificatives().getListeParticipantsParticipants(this.jp_categoriePoule.getNomCategorie(),this.jp_categoriePoule.getNomPoule());
		
		// Récupérer les rangs des participants qui peuvent participer
		this.rangsParticipants.clear();
		for(String participant : this.participants) {
			this.rangsParticipants.put(participant, ContestOrg.get().getCtrlPhasesQualificatives().getRang(participant));
		}
		
		// Ajouter les participants qui peuvent participer
		this.jp_participants.removeAll();
		if(this.participants.size() != 0) {
			this.jcbs_participants = new JCheckBox[this.participants.size()];
			for (int i = 0; i < this.participants.size(); i++) {
				this.jcbs_participants[i] = new JCheckBox(this.participants.get(i));
				this.jcbs_participants[i].setSelected(true);
				this.jcbs_participants[i].addItemListener(this);
				this.jp_participants.add(this.jcbs_participants[i]);
			}
		} else {
			this.jp_participants.add(new JPanel());
			this.jp_participants.add(ViewHelper.center(ViewHelper.pwarning(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Aucune équipe n'est homologuée." : "Aucune joueur n'est homologué")));
			this.jp_participants.add(new JPanel());
		}
		this.jp_participants.revalidate();
	}
	
	/**
	 * Rafraichir les statistiques
	 */
	private void refreshStatistiques () {
		// Récupérer la configuration
		Configuration<String> configuration = this.getConfiguration();
		
		// Récupérer certaines données
		int matchsDejaJoues = ContestOrg.get().getCtrlPhasesQualificatives().getNbMatchDejaJoues(configuration);
		int nbVillesCommunes = ContestOrg.get().getCtrlPhasesQualificatives().getNbVillesCommunes(configuration);
		
		// Calculer la différence moyenne et maximale de rang
		int differenceMoyenne = 0; int differenceMaximale = 0;
		for(Couple<String> couple : configuration.getCouples()) {
			if(couple.getParticipantA() != null && couple.getParticipantB() != null) {
				int difference = Math.abs(this.rangsParticipants.get(couple.getParticipantA())-this.rangsParticipants.get(couple.getParticipantB()));
				differenceMoyenne += difference;
				differenceMaximale = Math.max(differenceMaximale, difference);
			}
		}
		differenceMoyenne = configuration.getCouples().length == 0 ? 0 : differenceMoyenne/configuration.getCouples().length;
		
		// Remplir les champs
		this.jtf_matchsDejaJoues.setText(String.valueOf(matchsDejaJoues));
		this.jtf_villesCommunes.setText(String.valueOf(nbVillesCommunes));
		this.jtf_differenceMaximaleRang.setText(String.valueOf(differenceMaximale));
		this.jtf_differenceMoyenneRang.setText(String.valueOf(differenceMoyenne));
		
		// Changer la couleur des champs
		this.jtf_matchsDejaJoues.setBackground(matchsDejaJoues > 0 ? new Color(250, 90, 90) : new Color(168, 239, 101));
		this.jtf_villesCommunes.setBackground(nbVillesCommunes > 0 ? new Color(250, 180, 76) : new Color(168, 239, 101));
		this.jtf_differenceMaximaleRang.setBackground(differenceMaximale > 1 ? new Color(250, 180, 76) : new Color(168, 239, 101));
		this.jtf_differenceMoyenneRang.setBackground(differenceMoyenne > 1 ? new Color(250, 180, 76) : new Color(168, 239, 101));
	}
	
	/**
	 * Rafraichir les listes des matchs
	 */
	private void refreshMatchs() {
		// Récupérer le nombre de matchs
		int nbMatchs = this.getParticipantsSelectionnes().size() / 2;
		
		// Mettre à jour la liste des matchs
		this.jp_resultat.removeAll();
		this.jcbs_participantsA = new JComboBox[nbMatchs];
		this.jcbs_participantsB = new JComboBox[nbMatchs];
		for (int i = 0; i < nbMatchs; i++) {
			JPanel jp_match = new JPanel(new GridLayout(1, 2));
			
			this.jcbs_participantsA[i] = new JComboBox<String>(this.getParticipantsSelectionnes(true).toArray(new String[this.getParticipantsSelectionnes().size()]));
			this.jcbs_participantsA[i].setSelectedIndex(i * 2);
			this.jcbs_participantsB[i] = new JComboBox<String>(this.getParticipantsSelectionnes(true).toArray(new String[this.getParticipantsSelectionnes().size()]));
			this.jcbs_participantsB[i].setSelectedIndex(i * 2 + 1);
			
			jp_match.add(this.jcbs_participantsA[i]);
			jp_match.add(this.jcbs_participantsB[i]);
			
			this.jcbs_participantsA[i].addItemListener(this);
			this.jcbs_participantsB[i].addItemListener(this);
			
			this.jp_resultat.add(jp_match);
		}
		this.jp_resultat.revalidate();
	}
	
	/**
	 * Rafraichir les couleurs
	 */
	private void refreshColors() {
		// Lister les participants qui participent plusieurs fois
		ArrayList<Integer> participants = new ArrayList<Integer>();
		ArrayList<Integer> participantsRedondants = new ArrayList<Integer>();
		for(int i=0;i<this.jcbs_participantsA.length;i++) {
			// Ajouter le participant A
			if(!participants.contains(this.jcbs_participantsA[i].getSelectedIndex())) {
				participants.add(this.jcbs_participantsA[i].getSelectedIndex());
			} else if(!participantsRedondants.contains(this.jcbs_participantsA[i].getSelectedIndex())) {
				participantsRedondants.add(this.jcbs_participantsA[i].getSelectedIndex());
			}
			
			// Ajouter le participant B
			if(!participants.contains(this.jcbs_participantsB[i].getSelectedIndex())) {
				participants.add(this.jcbs_participantsB[i].getSelectedIndex());
			} else if(!participantsRedondants.contains(this.jcbs_participantsB[i].getSelectedIndex())) {
				participantsRedondants.add(this.jcbs_participantsB[i].getSelectedIndex());
			}
		}
		
		// Récupérer les participants séléctionnés
		ArrayList<String> participantsSelectionnes = this.getParticipantsSelectionnes();
		
		// Changer la couleur des listes si nécéssaire
		for(int i=0;i<this.jcbs_participantsA.length;i++) {			
			// Vérifier si le participant ne joue pas avec lui-même
			if(this.jcbs_participantsA[i].getSelectedIndex() == this.jcbs_participantsB[i].getSelectedIndex()) {
				this.jcbs_participantsA[i].setBackground(new Color(250, 90, 90));
				this.jcbs_participantsA[i].setOpaque(true);
				this.jcbs_participantsA[i].setToolTipText(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Une équipe ne peut pas avoir un match avec elle même." : "Un joueur ne peut pas avoir un match avec lui même.");
				this.jcbs_participantsB[i].setBackground(new Color(250, 90, 90));
				this.jcbs_participantsB[i].setOpaque(true);
				this.jcbs_participantsB[i].setToolTipText(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Une équipe ne peut pas avoir un match avec elle même." : "Un joueur ne peut pas avoir un match avec lui même.");
			} else {
				// Récupérer le nom des participants
				String nomParticipantA = this.isFantome() && this.jcbs_participantsA[i].getSelectedIndex() == this.jcbs_participantsA[i].getItemCount()-1 ? null : participantsSelectionnes.get(this.jcbs_participantsA[i].getSelectedIndex());
				String nomParticipantB = this.isFantome() && this.jcbs_participantsB[i].getSelectedIndex() == this.jcbs_participantsB[i].getItemCount()-1 ? null : participantsSelectionnes.get(this.jcbs_participantsB[i].getSelectedIndex());
				
				// Récupérer le nombre de rencontres
				int nbRencontres = ContestOrg.get().getCtrlPhasesQualificatives().getNbRencontres(nomParticipantA, nomParticipantB);
				
				// Changer la couleur des listes
				if(nbRencontres > 0) {
					this.jcbs_participantsA[i].setBackground(new Color(250, 90, 90));
					this.jcbs_participantsA[i].setOpaque(true);
					this.jcbs_participantsA[i].setToolTipText("Les deux "+(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "équipes" : "joueurs")+" se sont déjà rencontrés "+nbRencontres+" fois.");
					this.jcbs_participantsB[i].setBackground(new Color(250, 90, 90));
					this.jcbs_participantsB[i].setOpaque(true);
					this.jcbs_participantsB[i].setToolTipText("Les deux "+(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "équipes" : "joueurs")+" se sont déjà rencontrés "+nbRencontres+" fois.");
				} else {
					// Vérifier si le participant A ne participe pas plusieurs fois
					if(participantsRedondants.contains(this.jcbs_participantsA[i].getSelectedIndex())) {
						this.jcbs_participantsA[i].setBackground(new Color(250, 180, 76));
						this.jcbs_participantsA[i].setOpaque(true);
						this.jcbs_participantsA[i].setToolTipText((ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "L'équipe" : "Le joueur")+" participe plusieurs fois dans la phase qualificative.");
					} else {
						this.jcbs_participantsA[i].setBackground(new Color(168, 239, 101));
						this.jcbs_participantsA[i].setOpaque(true);
						this.jcbs_participantsA[i].setToolTipText("Les deux "+(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "équipes" : "joueurs")+" ne se sont jamais rencontrés.");
					}

					// Vérifier si le participant B ne participe pas plusieurs fois
					if(participantsRedondants.contains(this.jcbs_participantsB[i].getSelectedIndex())) {
						this.jcbs_participantsB[i].setBackground(new Color(250, 180, 76));
						this.jcbs_participantsB[i].setOpaque(true);
						this.jcbs_participantsB[i].setToolTipText((ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "L'équipe" : "Le joueur")+" participe plusieurs fois dans la phase qualificative.");
					} else {
						this.jcbs_participantsB[i].setBackground(new Color(168, 239, 101));
						this.jcbs_participantsB[i].setOpaque(true);
						this.jcbs_participantsB[i].setToolTipText("Les deux "+(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "équipes" : "joueurs")+" ne se sont jamais rencontrés.");
					}
				}
			}
		}
	}
	
	/**
	 * Savoir si le participant fantome est nécéssaire
	 * @return participant fantome nécéssaire ?
	 */
	private boolean isFantome () {
		int nbParticipants = 0;
		for (int i = 0; i < this.participants.size(); i++) {
			if (this.jcbs_participants[i].isSelected()) {
				nbParticipants++;
			}
		}
		return nbParticipants % 2 != 0;
	}
	
	/**
	 * Récupérer la liste des participants séléctionnés (avec le participant fantome si nécéssaire)
	 * @return liste des participants séléctionnés (avec le participant fantome si nécéssaire)
	 */
	private ArrayList<String> getParticipantsSelectionnes () {
		return this.getParticipantsSelectionnes(false);
	}
	
	/**
	 * Récupérer la liste des participants séléctionnés (avec le participant fantome si nécéssaire)
	 * @param rang ajouter le rang des participants ?
	 * @return liste des participants séléctionnés (avec le participant fantome si nécéssaire)
	 */
	private ArrayList<String> getParticipantsSelectionnes (boolean rang) {
		// Initialiser la liste
		ArrayList<String> participantsSelectionnes = new ArrayList<String>();
		
		// Remplir la liste
		for (int i = 0; i < this.participants.size(); i++) {
			if (this.jcbs_participants[i].isSelected()) {
				if(!rang) {
					participantsSelectionnes.add(this.participants.get(i));
				} else {
					participantsSelectionnes.add(this.participants.get(i)+" (rang "+this.rangsParticipants.get(this.participants.get(i))+")");
				}
			}
		}
		if (participantsSelectionnes.size() % 2 != 0) {
			participantsSelectionnes.add(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Equipe fantome" : "Joueur fantome");
		}
		
		// Retourner la liste
		return participantsSelectionnes;
	}
	
	/**
	 * Récupérer le nombre de participants séléctionnés (sans le participant fantome)
	 * @return nombre de participants séléctionnés (sans le participant fantome)
	 */
	private int getNbParticipantsSelectionnes() {
		int nbParticipantsSelectionnes = 0;
		for (int i = 0; i < this.participants.size(); i++) {
			if (this.jcbs_participants[i].isSelected()) {
				nbParticipantsSelectionnes++;
			}
		}
		return nbParticipantsSelectionnes;
	}
	
	/**
	 * @see ItemListener#itemStateChanged(ItemEvent)
	 */
	@Override
	public void itemStateChanged (ItemEvent event) {
		for(JCheckBox checkbox : this.jcbs_participants) {
			if(event.getSource() == checkbox) {
				// Rafraichir la liste des participants
				this.refreshMatchs();
			}
		}
		
		// Récupérer les statistiques sur la configuration
		this.refreshStatistiques();
		
		// Rafraichir les couleurs
		this.refreshColors();
	}
	
	/**
	 * @see ActionListener#actionPerformed(ActionEvent)
	 */
	@Override
	public void actionPerformed (ActionEvent event) {
		if (event.getSource() == this.jb_generer) {
			// Vérifier si le nombre de participants est suffisant
			if(this.getNbParticipantsSelectionnes() < 3) {
				// Erreur
				ViewHelper.derror(this, "Il faut séléctionner au moins trois "+(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "équipes" : "joueurs")+" pour lancer une génération.");
			} else if(this.getNbParticipantsSelectionnes() <= 8 || this.jrb_mode_basique.isSelected() || ViewHelper.confirmation(this, "Au dela de 8 "+(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "équipes" : "joueurs")+", le nombre de configurations possibles devient très grand. Désirez-vous continuer ?")) {
				// Modifier les boutons
				this.jb_valider.setEnabled(false);
				this.jb_generer.setEnabled(false);
				this.jb_arreter.setEnabled(true);
				
				// Changer l'image du statut de la génération
				this.jl_statutGeneration.setIcon(new ImageIcon("img/farm/32x32/hourglass.png"));
				
				// Désactiver toutes les checkbox
				for(JCheckBox checkbox : this.jcbs_participants) {
					checkbox.setEnabled(false);
				}
				
				// Remettre la demande d'arret à false
				this.demandeArret = false;
				
				// Récupérer la génération
				this.generation = null;
				if(this.jrb_mode_avance.isSelected()) {
					this.generation = ContestOrg.get().getCtrlPhasesQualificatives().getGenerationAvance(this.jp_categoriePoule.getNomCategorie(), this.jp_categoriePoule.getNomPoule(), this.getParticipantsSelectionnes());
				} else {
					this.generation = ContestOrg.get().getCtrlPhasesQualificatives().getGenerationBasique(this.jp_categoriePoule.getNomCategorie(), this.jp_categoriePoule.getNomPoule(), this.getParticipantsSelectionnes());
				}
				
				// Ecouter la génération
				this.generation.addListener(this);
				
				// Lancer la génération
				this.generation.generationStart();
			}
		} else if(event.getSource() == this.jb_arreter) {
			// Retenir la demande d'arret
			this.demandeArret = true;
			
			// Désactiver le bouton d'arret
			this.jb_arreter.setEnabled(false);
			
			// Message d'attente
			this.jl_avancementGeneration.setText("Arret de la génération ...");
			
			// Demander l'arret de l'opération
			this.generation.generationStop();
		} else if(event.getSource() == this.jb_conserver) {
			// Conserver la configuration actuelle
			this.configurationConservee = this.getConfiguration();
			
			// Activer le bouton restaurer
			this.jb_restaurer.setEnabled(true);
		} else if(event.getSource() == this.jb_restaurer) {
			// Restaurer la configuration
			this.setConfiguration(this.configurationConservee);
			
			// Desactiver le bouton restaurer
			this.jb_restaurer.setEnabled(false);
		} else {
			// Appeller le actionPerformed du parent
			super.actionPerformed(event);
		}
	}
	
	/**
	 * Fin de l'opération
	 * @param icone icone de fin
	 */
	private void fin(String icone) {		
		// Perdre la référence de la génération 
		this.generation = null;
		
		// Quitter la fenêtre si il y a eu une demande d'annulation
		if(this.demandeAnnulation) {
			this.quit();
		}
		
		// Modifier les boutons
		this.jb_valider.setEnabled(true);
		this.jb_generer.setEnabled(true);
		this.jb_arreter.setEnabled(false);

		// Réactiver toutes les checkbox
		for(JCheckBox checkbox : this.jcbs_participants) {
			checkbox.setEnabled(true);
		}
		
		// Modifier l'image de statut de la génération
		this.jl_statutGeneration.setIcon(new ImageIcon(icone));
	}

	// Implémentation de IGenerationListener
	@Override
	public void progressionAvancement (double progression) {
		// Ne pas considérer l'avancement si demande d'annulation ou d'arret 
		if(!this.demandeAnnulation && !this.demandeArret) {
			this.jpb_avancementGeneration.setValue((int)(progression * 100));
		}
	}
	@Override
	public void progressionMessage (String message) {
		// Ne pas considérer les messages si demande d'annulation ou d'arret
		if(!this.demandeAnnulation && !this.demandeArret) {
			this.jl_avancementGeneration.setText(message);
		}
	}
	@Override
	public void progressionFin () {
		// Considérer comme une annulation/arret si demande d'annulation/arret
		if(this.demandeAnnulation) {
			this.generationArret();
		} else if(this.demandeArret) {
			this.generationAnnulation();
		} else {
			this.fin("img/farm/32x32/tick.png");
		}
	}
	@Override
	public void generationMax (Configuration<String> configuration) {
		// Ne pas considérer les générations si demande d'annulation ou d'arret
		if(!this.demandeAnnulation && !this.demandeArret) {
			this.setConfiguration(configuration);
		}
	}
	@Override
	public void generationArret () {
		// L'arret est considéré comme une annulation si demande d'annulation
		if(!this.demandeAnnulation) {
			this.jl_avancementGeneration.setText("Génération arrêtée.");
			this.fin("img/farm/32x32/stop.png");
		} else {
			this.generationAnnulation();
		}
	}
	@Override
	public void generationAnnulation () {
		// L'annulation est considérée comme un arret si demande d'arret
		if(!this.demandeArret) {
			if(this.demandeAnnulation) {
				// Annuler
				this.collector.cancel();
			} else {
				this.jl_avancementGeneration.setText("Génération annulée.");
				this.fin("img/farm/32x32/stop.png");
			}
		} else {
			this.generationAnnulation();
		}
	}
}
