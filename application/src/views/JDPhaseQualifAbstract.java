package views;

import infos.Configuration;
import infos.Couple;
import infos.InfosModelConcours;
import infos.InfosModelMatchPhasesQualifs;
import infos.InfosModelPhaseQualificative;
import interfaces.ICollector;
import interfaces.IGeneration;
import interfaces.IGenerationListener;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
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

import common.Triple;

import controlers.ContestOrg;

@SuppressWarnings("serial")
public class JDPhaseQualifAbstract extends JDPattern implements ItemListener, IGenerationListener<Configuration<String>>
{
	
	// Collector
	private ICollector<Triple<Configuration<String>, InfosModelPhaseQualificative, InfosModelMatchPhasesQualifs>> collector;
	
	// Catégorie
	private String nomCategorie;
	
	// Poule
	private String nomPoule;
	
	// Equipes participantes
	protected ArrayList<String> equipesParticipantes;
	
	// Conservation/Restauration de configuration
	private JButton jb_conserver = new JButton("Conserver", new ImageIcon("img/farm/16x16/box.png"));
	private JButton jb_restaurer = new JButton("Restaurer", new ImageIcon("img/farm/16x16/box_down.png"));
	private Configuration<String> configurationConservee = null;
	
	// Rangs des équipes
	private HashMap<String,Integer> rangsEquipes = new HashMap<String, Integer>();
	
	// Paramètres de la génération
	private JRadioButton jrb_mode_avance = new JRadioButton("Avancé", false);
	private JRadioButton jrb_mode_basique = new JRadioButton("Basique", true);
	protected JCheckBox[] jcbs_equipesParticipantes;
	
	// Avancement de la génération
	private JLabel jl_statutGeneration = new JLabel(new ImageIcon("img/farm/32x32/hourglass.png"));
	private JProgressBar jpb_avancementGeneration = new JProgressBar(0,100);
	private JLabel jl_avancementGeneration = new JLabel("En attente de demande de génération ...");
	private JButton jb_generer = new JButton("Générer", new ImageIcon("img/farm/16x16/control_play_blue.png"));
	private JButton jb_arreter = new JButton("Arrêter", new ImageIcon("img/farm/16x16/control_stop_blue.png"));
	
	private IGeneration<Configuration<String>> generation;

	private boolean demandeArret = false;
	private boolean demandeAnnulation = false;
	
	// Meilleure configuration trouvée
	private JTextField jtf_matchDejaJoues = new JTextField();
	private JTextField jtf_villesCommunes = new JTextField();
	private JTextField jtf_differenceMoyenneRang = new JTextField();
	private JTextField jtf_differenceMaximaleRang = new JTextField();
	private JPanel jp_resultat;
	private JComboBox[] jcbs_equipesA;
	private JComboBox[] jcbs_equipesB;
	
	// Constructeur
	public JDPhaseQualifAbstract(Window w_parent, String titre, ICollector<Triple<Configuration<String>, InfosModelPhaseQualificative, InfosModelMatchPhasesQualifs>> collector, String nomCategorie, String nomPoule) {
		// Appeller le constructeur du parent
		super(w_parent, titre);
		
		// Retenir le collector, la catégorie et la poule
		this.collector = collector;
		this.nomCategorie = nomCategorie;
		this.nomPoule = nomPoule;
		
		// Récupérer les équipes participantes
		this.equipesParticipantes = ContestOrg.get().getCtrlPhasesQualificatives().getListeEquipesParticipantes(this.nomCategorie,this.nomPoule);
		
		// Récupérer les rangs des équipes participantes
		for(String equipeParticipante : this.equipesParticipantes) {
			this.rangsEquipes.put(equipeParticipante, ContestOrg.get().getCtrlPhasesQualificatives().getRang(equipeParticipante));
		}
		
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
		
		// Equipes participantes
		this.jp_contenu.add(ViewHelper.left(new JLabel(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Equipes participantes :" : "Joueurs participants : ")));
		
		JPanel jp_equipes = new JPanel();
		jp_equipes.setLayout(new BoxLayout(jp_equipes, BoxLayout.Y_AXIS));
		this.jcbs_equipesParticipantes = new JCheckBox[this.equipesParticipantes.size()];
		for (int i = 0; i < this.equipesParticipantes.size(); i++) {
			jcbs_equipesParticipantes[i] = new JCheckBox(this.equipesParticipantes.get(i));
			jcbs_equipesParticipantes[i].setSelected(true);
			jcbs_equipesParticipantes[i].addItemListener(this);
			jp_equipes.add(jcbs_equipesParticipantes[i]);
		}
		JScrollPane jsp_equipes = new JScrollPane(jp_equipes);
		jsp_equipes.setPreferredSize(new Dimension(jsp_equipes.getPreferredSize().width, 140));
		
		this.jp_contenu.add(jsp_equipes);
		
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
		JComponent[] jcs_resultat = { this.jtf_matchDejaJoues, this.jtf_differenceMoyenneRang, this.jtf_differenceMaximaleRang, this.jtf_villesCommunes };
		this.jp_contenu.add(ViewHelper.inputs(jls_resultat, jcs_resultat));
		
		this.jtf_matchDejaJoues.setEditable(false);
		this.jtf_differenceMoyenneRang.setEditable(false);
		this.jtf_differenceMaximaleRang.setEditable(false);
		this.jtf_villesCommunes.setEditable(false);

		this.jp_contenu.add(Box.createVerticalStrut(5));
		
		this.jp_contenu.add(ViewHelper.left(this.jb_conserver,this.jb_restaurer));
		this.jb_restaurer.setEnabled(false);
		
		this.jp_contenu.add(Box.createVerticalStrut(5));		
		
		this.jp_resultat = new JPanel();
		this.jp_resultat.setLayout(new BoxLayout(this.jp_resultat, BoxLayout.Y_AXIS));
		int nbMatchs = this.getEquipesSelectionnees().size() / 2;
		this.jcbs_equipesA = new JComboBox[nbMatchs];
		this.jcbs_equipesB = new JComboBox[nbMatchs];
		for (int i = 0; i < nbMatchs; i++) {
			JPanel jp_match = new JPanel(new GridLayout(1, 2));
			
			this.jcbs_equipesA[i] = new JComboBox(this.getEquipesSelectionnees(true).toArray(new String[this.getEquipesSelectionnees().size()]));
			this.jcbs_equipesA[i].setSelectedIndex(i * 2);
			this.jcbs_equipesB[i] = new JComboBox(this.getEquipesSelectionnees(true).toArray(new String[this.getEquipesSelectionnees().size()]));
			this.jcbs_equipesB[i].setSelectedIndex(i * 2 + 1);
			
			jp_match.add(this.jcbs_equipesA[i]);
			jp_match.add(this.jcbs_equipesB[i]);
			
			this.jcbs_equipesA[i].addItemListener(this);
			this.jcbs_equipesB[i].addItemListener(this);
			
			this.jp_resultat.add(jp_match);
		}
		JPanel jp_resultat_largeur = new JPanel(new BorderLayout());
		jp_resultat_largeur.add(this.jp_resultat, BorderLayout.NORTH);
		JScrollPane jsp_resultat = new JScrollPane(jp_resultat_largeur);
		jsp_resultat.setPreferredSize(new Dimension(jsp_resultat.getPreferredSize().width, 140));
		
		this.refreshColors();
		
		this.jp_contenu.add(jsp_resultat);
		
		// Récupérer les statistiques sur la configuration
		this.refreshStatistiques();
		
		// Ecouter les boutons
		this.jb_generer.addActionListener(this);
		this.jb_arreter.addActionListener(this);
		this.jb_conserver.addActionListener(this);
		this.jb_restaurer.addActionListener(this);
		
		// Ajouter le bouton d'aide
		this.addButton(new JBHelperButton(this) {
			@Override
			protected String message () {
				return "<h1>Mode avancé</h1>" +
					   "Le mode avancé teste l'ensemble des configurations possibles. Dès que l'algorithme<br/>" +
					   "remonte une meilleure configuration que celle précédement trouvée, celle-ci apparait<br/>" +
					   "dans le cadre \"Meilleure configuration trouvée\". Au délà de 8 équipes, le temps de<br/>" +
					   "génération devient exponentiellement long. Vous pouvez arrêter la génération à tout<br/>" +
					   "moment et considérer la meilleure configuration trouvée jusque là.<br/>" +
					   "<br/>" +
					   "<h1>Mode basique</h1>" +
					   "Le mode basique est bien moins gourmant en ressource. Il génère tous les couples<br/>" +
					   "d'équipes possibles et les trie en fonction de l'affinité des équipes que les composent.<br/>" +
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
					   "<li>Est-ce que les équipes ont déjà joué ensemble ?</li>" +
					   "<li>Est-ce que les équipes sont de même niveau ?</li>" +
					   "<li>Est-ce que les équipes viennent de la même ville ?</li>" +
					   "</ol>";
			}
		});
		
		// Elargir la fenetre
		this.setPreferredSize(new Dimension(this.getPreferredSize().width + 250, this.getPreferredSize().height));
		
		// Pack
		this.pack();
	}
	
	// Implémentation de ok
	@Override
	protected void ok () {
		// Vérifier si une génération n'est pas en cours
		if(this.generation == null) {
			// Récupérer la liste des équipes séléctionnées
			ArrayList<String> equipesSelectionnees = this.getEquipesSelectionnees();
			
			// Vérifier la conformité de la configuration choisie
			boolean erreur = false;
			ArrayList<String> equipesConfiguration = new ArrayList<String>();
			for(int i=0;i<this.jcbs_equipesA.length;i++) {
				// Vérifier si une équipe joue avec elle-même
				if(this.jcbs_equipesA[i].getSelectedIndex() == this.jcbs_equipesB[i].getSelectedIndex()) {
					// Récupérer le nom de l'équipe
					String nomEquipe = this.isFantome() && this.jcbs_equipesA[i].getSelectedIndex() == this.jcbs_equipesA[i].getItemCount()-1 ? (ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Equipe fantome" : "Joueur fantome") : equipesSelectionnees.get(this.jcbs_equipesA[i].getSelectedIndex());
					
					// Erreur
					ViewHelper.derror(this, (ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "L'équipe" : "Le joueur")+" \""+nomEquipe+"\" a un match contre "+(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "elle-même" : "lui-même")+".");
					erreur = true;
				}
				if(!erreur) {
					// Vérifier si une équipe participe plusieurs fois dans la phase qualificative 
					if(!this.isFantome() || this.jcbs_equipesA[i].getSelectedIndex() != this.jcbs_equipesA[i].getItemCount()-1) {
						if(equipesConfiguration.contains(equipesSelectionnees.get(this.jcbs_equipesA[i].getSelectedIndex()))) {
							erreur = !ViewHelper.confirmation(this,(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "L'équipe" : "Le joueur")+" \""+equipesSelectionnees.get(this.jcbs_equipesA[i].getSelectedIndex())+"\" participe plusieurs fois dans la phase qualificative. Désirez-vous continuer ?"); 
						} else {
							equipesConfiguration.add(equipesSelectionnees.get(this.jcbs_equipesA[i].getSelectedIndex()));
						}
					}
					if(!this.isFantome() || this.jcbs_equipesB[i].getSelectedIndex() != this.jcbs_equipesB[i].getItemCount()-1) {
						if(equipesConfiguration.contains(equipesSelectionnees.get(this.jcbs_equipesB[i].getSelectedIndex()))) {
							erreur = !ViewHelper.confirmation(this,(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "L'équipe" : "Le joueur")+" \""+equipesSelectionnees.get(this.jcbs_equipesB[i].getSelectedIndex())+"\" participe plusieurs fois dans la phase qualificative. Désirez-vous continuer ?"); 
						} else {
							equipesConfiguration.add(equipesSelectionnees.get(this.jcbs_equipesB[i].getSelectedIndex()));
						}
					}
				}
			}
			if(this.jcbs_equipesA.length == 0) {
				// Erreur
				ViewHelper.derror(this, "Il n'y a aucun match défini dans la phase qualificative.");
				erreur = true;
			}
			
			// Transformer la informations au collector
			if(!erreur) {
				this.collector.accept(new Triple<Configuration<String>, InfosModelPhaseQualificative, InfosModelMatchPhasesQualifs>(this.getConfiguration(), new InfosModelPhaseQualificative(), new InfosModelMatchPhasesQualifs(null,null)));
			}
		}
	}
		
	// Implémentation de quit
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
	
	// Récupérer la configuration actuel à partir des JComboBox
	private Configuration<String> getConfiguration () {
		if (this.jcbs_equipesA != null && this.jcbs_equipesB != null) {
			ArrayList<String> equipesSelectionnees = this.getEquipesSelectionnees();
			Configuration<String> configuration = new Configuration<String>(this.jcbs_equipesA.length);
			boolean fantome = this.isFantome();
			for (int i = 0; i < this.jcbs_equipesA.length; i++) {
				String equipeA = fantome && this.jcbs_equipesA[i].getSelectedIndex() == this.jcbs_equipesA[i].getItemCount() - 1 ? null : equipesSelectionnees.get(this.jcbs_equipesA[i].getSelectedIndex());
				String equipeB = fantome && this.jcbs_equipesB[i].getSelectedIndex() == this.jcbs_equipesB[i].getItemCount() - 1 ? null : equipesSelectionnees.get(this.jcbs_equipesB[i].getSelectedIndex());
				configuration.addCouple(new Couple<String>(equipeA, equipeB));
			}
			return configuration;
		}
		return null;
	}
	
	// Placer une configuration dans les JComboBox
	protected void setConfiguration(Configuration<String> configuration) {
		// Placer la configuration
		ArrayList<String> equipesSelectionnees = this.getEquipesSelectionnees();
		for(int i=0;i<configuration.getCouples().length;i++) {
			// Séléctionner l'équipe A
			if(configuration.getCouples()[i].getEquipeA() != null) {
				this.jcbs_equipesA[i].setSelectedIndex(equipesSelectionnees.indexOf(configuration.getCouples()[i].getEquipeA()));
			} else {
				this.jcbs_equipesA[i].setSelectedIndex(this.jcbs_equipesA[i].getItemCount()-1);
			}
			
			// Séléctionner l'équipe B
			if(configuration.getCouples()[i].getEquipeB() != null) {
				this.jcbs_equipesB[i].setSelectedIndex(equipesSelectionnees.indexOf(configuration.getCouples()[i].getEquipeB()));
			} else {
				this.jcbs_equipesB[i].setSelectedIndex(this.jcbs_equipesB[i].getItemCount()-1);
			}
		}
		
		// Rafraichir les couleurs
		this.refreshColors();
	}
	
	// Rafraichir les statistiques que la configuration actuelle
	private void refreshStatistiques () {
		// Récupérer la configuration
		Configuration<String> configuration = this.getConfiguration();
		
		// Récupérer certaines données
		int matchsDejaJoues = ContestOrg.get().getCtrlPhasesQualificatives().getNbMatchDejaJoues(configuration);
		int nbVillesCommunes = ContestOrg.get().getCtrlPhasesQualificatives().getNbVillesCommunes(configuration);
		
		// Calculer la différence moyenne et maximale de rang
		int differenceMoyenne = 0; int differenceMaximale = 0;
		for(Couple<String> couple : configuration.getCouples()) {
			if(couple.getEquipeA() != null && couple.getEquipeB() != null) {
				int difference = Math.abs(this.rangsEquipes.get(couple.getEquipeA())-this.rangsEquipes.get(couple.getEquipeB()));
				differenceMoyenne += difference;
				differenceMaximale = Math.max(differenceMaximale, difference);
			}
		}
		differenceMoyenne = configuration.getCouples().length == 0 ? 0 : differenceMoyenne/configuration.getCouples().length;
		
		// Remplir les champs
		this.jtf_matchDejaJoues.setText(String.valueOf(matchsDejaJoues));
		this.jtf_villesCommunes.setText(String.valueOf(nbVillesCommunes));
		this.jtf_differenceMaximaleRang.setText(String.valueOf(differenceMaximale));
		this.jtf_differenceMoyenneRang.setText(String.valueOf(differenceMoyenne));
		
		// Changer la couleur des champs
		this.jtf_matchDejaJoues.setBackground(matchsDejaJoues > 0 ? new Color(250, 90, 90) : new Color(168, 239, 101));
		this.jtf_villesCommunes.setBackground(nbVillesCommunes > 0 ? new Color(250, 180, 76) : new Color(168, 239, 101));
		this.jtf_differenceMaximaleRang.setBackground(differenceMaximale > 1 ? new Color(250, 180, 76) : new Color(168, 239, 101));
		this.jtf_differenceMoyenneRang.setBackground(differenceMoyenne > 1 ? new Color(250, 180, 76) : new Color(168, 239, 101));
	}
	
	// Rafraichir les listes d'équipes
	private void refreshEquipes() {
		// Récupérer le nombre de matchs
		int nbMatchs = this.getEquipesSelectionnees().size() / 2;
		
		// Mettre à jour la liste des matchs
		this.jp_resultat.removeAll();
		this.jcbs_equipesA = new JComboBox[nbMatchs];
		this.jcbs_equipesB = new JComboBox[nbMatchs];
		for (int i = 0; i < nbMatchs; i++) {
			JPanel jp_match = new JPanel(new GridLayout(1, 2));
			
			this.jcbs_equipesA[i] = new JComboBox(this.getEquipesSelectionnees(true).toArray(new String[this.getEquipesSelectionnees().size()]));
			this.jcbs_equipesA[i].setSelectedIndex(i * 2);
			this.jcbs_equipesB[i] = new JComboBox(this.getEquipesSelectionnees(true).toArray(new String[this.getEquipesSelectionnees().size()]));
			this.jcbs_equipesB[i].setSelectedIndex(i * 2 + 1);
			
			jp_match.add(this.jcbs_equipesA[i]);
			jp_match.add(this.jcbs_equipesB[i]);
			
			this.jcbs_equipesA[i].addItemListener(this);
			this.jcbs_equipesB[i].addItemListener(this);
			
			this.jp_resultat.add(jp_match);
		}
		this.jp_resultat.revalidate();
	}
	
	// Rafraichir les couleurs
	private void refreshColors() {
		// Lister les équipes qui participent plusieurs fois
		ArrayList<Integer> equipesParticipantes = new ArrayList<Integer>();
		ArrayList<Integer> equipesRedondantes = new ArrayList<Integer>();
		for(int i=0;i<this.jcbs_equipesA.length;i++) {
			// Ajouter l'équipe A
			if(!equipesParticipantes.contains(this.jcbs_equipesA[i].getSelectedIndex())) {
				equipesParticipantes.add(this.jcbs_equipesA[i].getSelectedIndex());
			} else if(!equipesRedondantes.contains(this.jcbs_equipesA[i].getSelectedIndex())) {
				equipesRedondantes.add(this.jcbs_equipesA[i].getSelectedIndex());
			}
			
			// Ajouter l'équipe B
			if(!equipesParticipantes.contains(this.jcbs_equipesB[i].getSelectedIndex())) {
				equipesParticipantes.add(this.jcbs_equipesB[i].getSelectedIndex());
			} else if(!equipesRedondantes.contains(this.jcbs_equipesB[i].getSelectedIndex())) {
				equipesRedondantes.add(this.jcbs_equipesB[i].getSelectedIndex());
			}
		}
		
		// Récupérer les equipes séléctionnées
		ArrayList<String> equipesSelectionnees = this.getEquipesSelectionnees();
		
		// Changer la couleur des listes si nécéssaire
		for(int i=0;i<this.jcbs_equipesA.length;i++) {			
			// Vérifier si l'équipe ne joue pas avec elle meme
			if(this.jcbs_equipesA[i].getSelectedIndex() == this.jcbs_equipesB[i].getSelectedIndex()) {
				this.jcbs_equipesA[i].setBackground(new Color(250, 90, 90));
				this.jcbs_equipesA[i].setOpaque(true);
				this.jcbs_equipesA[i].setToolTipText(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Une équipe ne peut pas avoir un match avec elle même." : "Un joueur ne peut pas avoir un match avec lui même.");
				this.jcbs_equipesB[i].setBackground(new Color(250, 90, 90));
				this.jcbs_equipesB[i].setOpaque(true);
				this.jcbs_equipesB[i].setToolTipText(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Une équipe ne peut pas avoir un match avec elle même." : "Un joueur ne peut pas avoir un match avec lui même.");
			} else {
				// Récupérer le nom des équipes
				String nomEquipeA = this.isFantome() && this.jcbs_equipesA[i].getSelectedIndex() == this.jcbs_equipesA[i].getItemCount()-1 ? null : equipesSelectionnees.get(this.jcbs_equipesA[i].getSelectedIndex());
				String nomEquipeB = this.isFantome() && this.jcbs_equipesB[i].getSelectedIndex() == this.jcbs_equipesB[i].getItemCount()-1 ? null : equipesSelectionnees.get(this.jcbs_equipesB[i].getSelectedIndex());
				
				// Récupérer le nombre de rencontres
				int nbRencontres = ContestOrg.get().getCtrlPhasesQualificatives().getNbRencontres(nomEquipeA, nomEquipeB);
				
				// Changer la couleur des listes
				if(nbRencontres > 0) {
					this.jcbs_equipesA[i].setBackground(new Color(250, 90, 90));
					this.jcbs_equipesA[i].setOpaque(true);
					this.jcbs_equipesA[i].setToolTipText("Les deux "+(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "équipes" : "joueurs")+" se sont déjà rencontrés "+nbRencontres+" fois.");
					this.jcbs_equipesB[i].setBackground(new Color(250, 90, 90));
					this.jcbs_equipesB[i].setOpaque(true);
					this.jcbs_equipesB[i].setToolTipText("Les deux "+(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "équipes" : "joueurs")+" se sont déjà rencontrés "+nbRencontres+" fois.");
				} else {
					// Vérifier si l'équipe A ne participe pas plusieurs fois
					if(equipesRedondantes.contains(this.jcbs_equipesA[i].getSelectedIndex())) {
						this.jcbs_equipesA[i].setBackground(new Color(250, 180, 76));
						this.jcbs_equipesA[i].setOpaque(true);
						this.jcbs_equipesA[i].setToolTipText((ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "L'équipe" : "Le joueur")+" participe plusieurs fois dans la phase qualificative.");
					} else {
						this.jcbs_equipesA[i].setBackground(new Color(168, 239, 101));
						this.jcbs_equipesA[i].setOpaque(true);
						this.jcbs_equipesA[i].setToolTipText("Les deux "+(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "équipes" : "joueurs")+" ne se sont jamais rencontrés.");
					}

					// Vérifier si l'équipe B ne participe pas plusieurs fois
					if(equipesRedondantes.contains(this.jcbs_equipesB[i].getSelectedIndex())) {
						this.jcbs_equipesB[i].setBackground(new Color(250, 180, 76));
						this.jcbs_equipesB[i].setOpaque(true);
						this.jcbs_equipesB[i].setToolTipText((ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "L'équipe" : "Le joueur")+" participe plusieurs fois dans la phase qualificative.");
					} else {
						this.jcbs_equipesB[i].setBackground(new Color(168, 239, 101));
						this.jcbs_equipesB[i].setOpaque(true);
						this.jcbs_equipesB[i].setToolTipText("Les deux "+(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "équipes" : "joueurs")+" ne se sont jamais rencontrés.");
					}
				}
			}
		}
	}
	
	// Savoir si l'équipe fantome est nécéssaire
	private boolean isFantome () {
		int nbEquipes = 0;
		for (int i = 0; i < this.equipesParticipantes.size(); i++) {
			if (this.jcbs_equipesParticipantes[i].isSelected()) {
				nbEquipes++;
			}
		}
		return nbEquipes % 2 != 0;
	}
	
	// Récupérer la liste des équipes séléctionnées (avec l'équipe fantome si nécéssaire)
	private ArrayList<String> getEquipesSelectionnees () {
		return this.getEquipesSelectionnees(false);
	}
	private ArrayList<String> getEquipesSelectionnees (boolean rang) {
		// Initialiser la liste
		ArrayList<String> equipesSelectionnees = new ArrayList<String>();
		
		// Remplir la liste
		for (int i = 0; i < this.equipesParticipantes.size(); i++) {
			if (this.jcbs_equipesParticipantes[i].isSelected()) {
				if(!rang) {
					equipesSelectionnees.add(this.equipesParticipantes.get(i));
				} else {
					equipesSelectionnees.add(this.equipesParticipantes.get(i)+" (rang "+this.rangsEquipes.get(this.equipesParticipantes.get(i))+")");
				}
			}
		}
		if (equipesSelectionnees.size() % 2 != 0) {
			equipesSelectionnees.add(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Equipe fantome" : "Joueur fantome");
		}
		
		// Retourner la liste
		return equipesSelectionnees;
	}
	
	// Récupérer le nombre d'équipes séléctionnées (sans l'équipe fantome)
	public int getNbEquipesSelectionnees() {
		int nbEquipesSelectionnees = 0;
		for (int i = 0; i < this.equipesParticipantes.size(); i++) {
			if (this.jcbs_equipesParticipantes[i].isSelected()) {
				nbEquipesSelectionnees++;
			}
		}
		return nbEquipesSelectionnees;
	}
	
	// Implémentation de ItemListener
	@Override
	public void itemStateChanged (ItemEvent event) {
		for(JCheckBox checkbox : this.jcbs_equipesParticipantes) {
			if(event.getSource() == checkbox) {
				// Rafraichir la liste des équipes
				this.refreshEquipes();
			}
		}
		
		// Récupérer les statistiques sur la configuration
		this.refreshStatistiques();
		
		// Rafraichir les couleurs
		this.refreshColors();
	}
	
	// Implémentation de ActionListener
	public void actionPerformed (ActionEvent event) {
		if (event.getSource() == this.jb_generer) {
			// Vérifier si le nombre d'équipes est suffisant
			if(this.getNbEquipesSelectionnees() < 3) {
				// Erreur
				ViewHelper.derror(this, "Il faut séléctionner au moins trois "+(ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "équipes" : "joueurs")+" pour lancer une génération.");
			} else if(this.getNbEquipesSelectionnees() <= 8 || this.jrb_mode_basique.isSelected() || ViewHelper.confirmation(this, "Au dela de 8 équipes, le nombre de configurations possibles devient très grand. Désirez-vous continuer ?")) {
				// Modifier les boutons
				this.jb_valider.setEnabled(false);
				this.jb_generer.setEnabled(false);
				this.jb_arreter.setEnabled(true);
				
				// Changer l'image du statut de la génération
				this.jl_statutGeneration.setIcon(new ImageIcon("img/farm/32x32/hourglass.png"));
				
				// Désactiver toutes les checkbox
				for(JCheckBox checkbox : this.jcbs_equipesParticipantes) {
					checkbox.setEnabled(false);
				}
				
				// Remettre la demande d'arret à false
				this.demandeArret = false;
				
				// Récupérer la génération
				this.generation = null;
				if(this.jrb_mode_avance.isSelected()) {
					this.generation = ContestOrg.get().getCtrlPhasesQualificatives().getGenerationAvance(this.nomCategorie, this.nomPoule, this.getEquipesSelectionnees());
				} else {
					this.generation = ContestOrg.get().getCtrlPhasesQualificatives().getGenerationBasique(this.nomCategorie, this.nomPoule, this.getEquipesSelectionnees());
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
	
	// Fin de l'opération
	private void fin(String icone) {		
		// Perdre la référence de la génération 
		this.generation = null;
		
		// Quitter la fenetre si il y a eu une demande d'annulation
		if(this.demandeAnnulation) {
			this.quit();
		}
		
		// Modifier les boutons
		this.jb_valider.setEnabled(true);
		this.jb_generer.setEnabled(true);
		this.jb_arreter.setEnabled(false);

		// Réactiver toutes les checkbox
		for(JCheckBox checkbox : this.jcbs_equipesParticipantes) {
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
