package org.contestorg.controllers;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JDialog;

import org.contestorg.common.MoodyAbstract;
import org.contestorg.common.Pair;
import org.contestorg.common.TrackableList;
import org.contestorg.common.Triple;
import org.contestorg.events.Action;
import org.contestorg.infos.InfosConnexionServeur;
import org.contestorg.infos.InfosMiseAJour;
import org.contestorg.infos.InfosModelCategorie;
import org.contestorg.infos.InfosModelChemin;
import org.contestorg.infos.InfosModelCompPhasesQualifsAbstract;
import org.contestorg.infos.InfosModelConcours;
import org.contestorg.infos.InfosModelDiffusion;
import org.contestorg.infos.InfosModelEmplacement;
import org.contestorg.infos.InfosModelExportation;
import org.contestorg.infos.InfosModelHoraire;
import org.contestorg.infos.InfosModelLieu;
import org.contestorg.infos.InfosModelObjectif;
import org.contestorg.infos.InfosModelPoule;
import org.contestorg.infos.InfosModelPrix;
import org.contestorg.infos.InfosModelPropriete;
import org.contestorg.infos.InfosModelTheme;
import org.contestorg.interfaces.IHistoryListener;
import org.contestorg.interfaces.IOperation;
import org.contestorg.interfaces.ITrackableListValidator;
import org.contestorg.log.Log;
import org.contestorg.log.Report;
import org.contestorg.models.FrontModel;
import org.contestorg.models.FrontModelConfiguration;
import org.contestorg.models.ModelConcours;
import org.contestorg.out.MiseAJour;
import org.contestorg.out.PersistanceXML;
import org.contestorg.preferences.Preferences;
import org.contestorg.views.FiltreFichier;
import org.contestorg.views.JDConcoursCreer;
import org.contestorg.views.JDConcoursEditer;
import org.contestorg.views.JDConnexionCreer;
import org.contestorg.views.JDMiseAJour;
import org.contestorg.views.JDOperation;
import org.contestorg.views.JFPrincipal;
import org.contestorg.views.ViewHelper;

/**
 * Controleur principal
 */
public class ContestOrg extends MoodyAbstract implements IHistoryListener
{
	
	/** Version de ContestOrg ([version principale].[évolutions développées].[anomalies corrigées]) */
	public static final String VERSION = "2.1.0"; // Version pas encore effective

	/** Instance unique de ContestOrg */
	private static ContestOrg contestOrg;
	
	// Etats possibles
	
	/** Est-ce que le concours est ouvert ? */
	public static final int STATE_OPEN = 1;
	
	/** Est-ce que le concours est sauvegardé ? */
	public static final int STATE_SAVE = 2;
	
	/** Est-ce que le mode serveur est utilisé ? */
	public static final int STATE_SERVER = 4;
	
	/** Est-ce que le mode édition est activé ? */
	public static final int STATE_EDIT = 8;

	// Vues
	
	/** Fenêtre principale */
	private JFPrincipal jf_general;
	
	/** Fenêtre de création et d'édition de concours */
	private JDialog jd_concours;
	
	/** Fenêtre de connexion au serveur de ContestOrg */
	private JDialog jd_connexion;

	// Controleurs
	
	/** Controleur des participants */
	private CtrlParticipants ctrl_participants;
	
	/** Controleur des phases qualificatives */
	private CtrlPhasesQualificatives ctrl_phasesQualificatives;
	
	/** Controleur des phases éliminatoires */
	private CtrlPhasesEliminatoires ctrl_phasesEliminatoires;
	
	/** Controleur pour les traitements avec l'extérieur (exportations, importations, ...) */
	private CtrlOut ctrl_out;
	
	// Autres
	
	/** Chemin du concours */
	private String chemin;
	
	/** Préférences */
	private Preferences preferences;

	/**
	 * Constructeur
	 */
	private ContestOrg() {
		// Vérifier si l'application a bien été lancé
		if(new File("conf").exists()) {
			// Retenir l'instance ContestOrg
			ContestOrg.contestOrg = this;
			
			// Configurer le logger
			Log.configure();
			
			// Log du lancement de l'application
			Log.getLogger().info("Lancement de l'application");
	
			// Charger les préférences
			this.preferences = new Preferences();
	
			// Créer les différents controleurs
			this.ctrl_participants = new CtrlParticipants();
			this.ctrl_phasesQualificatives = new CtrlPhasesQualificatives();
			this.ctrl_phasesEliminatoires = new CtrlPhasesEliminatoires();
			this.ctrl_out = new CtrlOut();
			
			// S'abonner à l'historique des modèles
			FrontModel.get().getHistory().addListener(this);
	
			// Lancer la fenêtre générale
			this.jf_general = new JFPrincipal("ContestOrg "+ContestOrg.VERSION);
			this.addListener(this.jf_general);
			this.jf_general.setVisible(true);
			
			
			// Vérifier les mises à jour
			InfosMiseAJour miseAJour = MiseAJour.verifier();
			if(miseAJour != null) {
				// Demander à l'utilisateur s'il souhaite télécharger la mise à jour
				JDMiseAJour jd_miseAJour = new JDMiseAJour(this.jf_general, miseAJour);
				jd_miseAJour.setVisible(true);
			}
		} else {
			// Message d'erreur
			ViewHelper.derror(null,"ContestOrg doit être lancé depuis son répertoire d'installation.");
			
			// Quitter l'application avec un statut d'erreur
			System.exit(1);
		}
	}

	/**
	 * Récupérer l'instance de ContestOrg
	 * @return instance de ContestOrg
	 */
	public static ContestOrg get () {
		// Créer l'instance de ContestOrg si nécéssaire
		if (ContestOrg.contestOrg == null) {
			new ContestOrg();
		}

		// Retourner l'instance de ContestOrg
		return ContestOrg.contestOrg;
	}
	
	// ==== Getters
	
	// Récupérer les controleurs
	
	/**
	 * Récupérer le controleur des participants
	 * @return contorleur des participants
	 */
	public CtrlParticipants getCtrlParticipants() {
		return this.ctrl_participants;
	}
	
	/**
	 * Récupérer le controleur des phases qualificatives
	 * @return controleur des phases qualificatives
	 */
	public CtrlPhasesQualificatives getCtrlPhasesQualificatives() {
		return this.ctrl_phasesQualificatives;
	}
	
	/**
	 * Récupérer le controleur des phases éliminatoires
	 * @return controleur des phases éliminatoires
	 */
	public CtrlPhasesEliminatoires getCtrlPhasesEliminatoires() {
		return this.ctrl_phasesEliminatoires;
	}
	
	/**
	 * Récupérer le controleur pour les traitements avec l'extérieur (exportations, importations, ...)
	 * @return controleur pour les traitements avec l'extérieur (exportations, importations, ...)
	 */
	public CtrlOut getCtrlOut() {
		return this.ctrl_out;
	}
	
	// Récupérer des données sur le concours
	
	/**
	 * Récupérer le type des participants
	 * @return type des participants
	 */
	public int getTypeParticipants() {
		return FrontModel.get().getConcours().getTypeParticipants();
	}
	
	/**
	 * Récupérer le type des phases qualificatives
	 * @return le type des phases qualificatives
	 */
	public int getTypePhasesQualificatives() {
		return FrontModel.get().getConcours().getTypePhasesQualificatives();
	}
	
	// Récupérer des validateurs de liste
	
	/**
	 * Récupérer un validateur de liste d'objectifs
	 * @return validateur de liste d'objectifs
	 */
	public ITrackableListValidator<InfosModelObjectif> getObjectifsValidator() {
		return FrontModel.get().getFrontModelConfiguration().getObjectifsValidator();
	}
	
	/**
	 * Récupérer un validateur de liste de comparateurs
	 * @return validateur de liste de comparateurs
	 */
	public ITrackableListValidator<InfosModelCompPhasesQualifsAbstract> getComparateursValidator() {
		return FrontModel.get().getFrontModelConfiguration().getComparateursValidator();
	}
	
	/**
	 * Récupérer un validateur de liste d'exportations
	 * @return validateur de liste d'exportations
	 */
	public ITrackableListValidator<Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme>> getExportationsValidator() {
		return FrontModel.get().getFrontModelConfiguration().getExportationsValidator();
	}
	
	/**
	 * Récupérer un validateur de liste de diffusions
	 * @return validateur de liste de diffusions
	 */
	public ITrackableListValidator<Pair<InfosModelDiffusion,InfosModelTheme>> getDiffusionsValidator() {
		return FrontModel.get().getFrontModelConfiguration().getDiffusionsValidator();
	}
	
	/**
	 * Récupérer un validateur de liste de prix
	 * @return validateur de liste de prix
	 */
	public ITrackableListValidator<InfosModelPrix> getPrixValidator() {
		return FrontModel.get().getFrontModelConfiguration().getPrixValidator();
	}
	
	/**
	 * Récupérer un validateur de liste de lieux 
	 * @return validateur de liste de lieux
	 */
	public ITrackableListValidator<Triple<InfosModelLieu, TrackableList<InfosModelEmplacement>, TrackableList<InfosModelHoraire>>> getLieuxValidator() {
		return FrontModel.get().getFrontModelConfiguration().getLieuxValidator();
	}
	
	/**
	 * Récupérer un validateur de liste d'emplacements
	 * @return validateur de liste d'emplacements
	 */
	public ITrackableListValidator<InfosModelEmplacement> getEmplacementsValidator() {
		return FrontModel.get().getFrontModelConfiguration().getEmplacementsValidator();
	}
	
	/**
	 * Récupérer un validateur de liste d'horaires 
	 * @return validateur de liste d'horaires
	 */
	public ITrackableListValidator<InfosModelHoraire> getHorairesValidator() {
		return FrontModel.get().getFrontModelConfiguration().getHorairesValidator();
	}
	
	/**
	 * Récupérer un validateur de liste de propriétés
	 * @return validateur de liste de propriétés
	 */
	public ITrackableListValidator<InfosModelPropriete> getProprietesValidator() {
		return FrontModel.get().getFrontModelConfiguration().getProprietesValidator();
	}
	
	/**
	 * Récupérer un validateur de liste de catégories
	 * @return validateur de liste de catégories
	 */
	public ITrackableListValidator<InfosModelCategorie> getCategoriesValidator() {
		return FrontModel.get().getFrontModelParticipants().getCategoriesValidator();
	}
	
	/**
	 * Récupérer un validateur de liste de poules
	 * @return validateur de liste de poules
	 */
	public ITrackableListValidator<Pair<InfosModelPoule, ArrayList<String>>> getPoulesValidator() {
		return FrontModel.get().getFrontModelParticipants().getPoulesValidator();
	}
	
	// Récupérer des composants graphiques
	
	/**
	 * Récupérer la fenêtre principale
	 * @return fenêtre principale
	 */
	public JFPrincipal getFenetrePrincipale() {
		return this.jf_general;
	}
	
	// Autres
	
	/**
	 * Récupérer les préférences
	 * @return préférences
	 */
	public Preferences getPreferences() {
		return this.preferences;
	}
	
	// ==== Procédures liés au concours
	
	// Procédure de création
	
	/**
	 * Lancer la procédure de création de concours
	 */
	public void procedureConcoursNouveauDemarrer () {
		// Fermer le précédant concours
		this.procedureConcoursFermer();
		
		// Créer et afficher la fenêtre de création de concours
		this.jd_concours = new JDConcoursCreer(this.jf_general);
		this.jd_concours.setVisible(true);
	}
	
	/**
	 * Créer le concours
	 * @param infos informations du concours
	 * @param objectifs liste des objectifs
	 * @param comparateurs liste des comparateurs
	 * @param exportations liste des exportations
	 * @param publication indice de l'exportation qui fait office de publication
	 * @param diffusions liste des diffusions
	 * @param prix liste des prix
	 * @param lieux liste des lieux
	 * @param proprietes liste des propriétés
	 */
	public void procedureConcoursNouveau (InfosModelConcours infos, TrackableList<InfosModelObjectif> objectifs, TrackableList<InfosModelCompPhasesQualifsAbstract> comparateurs, TrackableList<Triple<InfosModelExportation,InfosModelChemin,InfosModelTheme>> exportations, int publication, TrackableList<Pair<InfosModelDiffusion,InfosModelTheme>> diffusions, TrackableList<InfosModelPrix> prix, TrackableList<Triple<InfosModelLieu,TrackableList<InfosModelEmplacement>,TrackableList<InfosModelHoraire>>> lieux, TrackableList<InfosModelPropriete> proprietes) {
		try {			
			// Créer le nouveau concours
			FrontModel.get().nouveauConcours(infos);
			
			// Configurer le nouveau concours
			FrontModel.get().getFrontModelConfiguration().configurerConcours(infos, objectifs, comparateurs, exportations, publication, diffusions, prix, lieux, proprietes);
			
			// Initialiser aux états ouvert et édition
			this.initStates(STATE_OPEN,STATE_EDIT);
			
			// Masquer et détruire la fenêtre de création de concours 
			this.jd_concours.setVisible(false);
			this.jd_concours = null;
		} catch (Exception e) {
			// Erreur
			this.error("Erreur lors de la création/configuration du concours.", e);
		}
	}
	
	/**
	 * Annuler la procédure de création de concours
	 */
	public void procedureConcoursNouveauAnnuler () {
		// Masquer et détruire la fenêtre de création de concours 
		this.jd_concours.setVisible(false);
		this.jd_concours = null;
	}

	// Procédure de configuration
	
	/**
	 * Lancer la procédure de configuration de concours
	 */
	public void procedureConcoursConfigurerDemarrer () {
		// Récupérer le front
		FrontModelConfiguration front = FrontModel.get().getFrontModelConfiguration();
		
		// Créer et afficher la fenêtre de configuration de concours
		this.jd_concours = new JDConcoursEditer(this.jf_general,FrontModel.get().getConcours().getInfos(),front.getListeObjectifs(),front.getListeComparateurs(),front.getListeExportations(),front.getPublicationIndex(),front.getListeDiffusions(),front.getListePrix(),front.getListeLieux(),front.getListeProprietes());
		this.jd_concours.setVisible(true);
	}
	
	/**
	 * Configurer le concours
	 * @param infos informations du concours
	 * @param objectifs liste des objectifs
	 * @param comparateurs liste des comparateurs
	 * @param exportations liste des exportations
	 * @param publication indice de l'exportation qui fait office de publication
	 * @param diffusions liste des diffusions
	 * @param prix liste des prix
	 * @param lieux liste des lieux
	 * @param proprietes liste des propriétés
	 */
	public void procedureConcoursConfigurer (InfosModelConcours infos, TrackableList<InfosModelObjectif> objectifs, TrackableList<InfosModelCompPhasesQualifsAbstract> comparateurs, TrackableList<Triple<InfosModelExportation,InfosModelChemin,InfosModelTheme>> exportations, int publication, TrackableList<Pair<InfosModelDiffusion,InfosModelTheme>> diffusions, TrackableList<InfosModelPrix> prix, TrackableList<Triple<InfosModelLieu,TrackableList<InfosModelEmplacement>,TrackableList<InfosModelHoraire>>> lieux, TrackableList<InfosModelPropriete> proprietes) {
		try {
			// Configurer le concours
			FrontModel.get().getFrontModelConfiguration().configurerConcours(infos, objectifs, comparateurs, exportations, publication, diffusions, prix, lieux, proprietes);
			
			// Retirer l'état sauvegardé
			this.removeState(STATE_SAVE);
			
			// Masquer et détruire la fenêtre de création de concours 
			this.jd_concours.setVisible(false);
			this.jd_concours = null;
		} catch (Exception e) {
			// Erreur
			this.error("Erreur lors de la configuration du concours.", e);
		}
	}
	
	/**
	 * Annuler la procédure de configuration de concours
	 */
	public void procedureConcoursConfigurerAnnuler () {
		// Masquer et détruire la fenêtre de configuration de concours 
		this.jd_concours.setVisible(false);
		this.jd_concours = null;
	}

	// Fermeture du concours
	
	/**
	 * Lancer la procédure de fermeture du concours
	 */
	public void procedureConcoursFermer() {
		// Revenir à l'état initial
		this.initStates();
		
		// Fermer le front
		try {
			FrontModel.get().closeConcours();
		} catch (Exception e) {
			// Erreur
			this.error("Erreur lors de la fermeture du concours.", e);
		}
		
		// Perdre la référence du chemin
		this.chemin = null;
	}
	
	// Procédures de persistance
	
	/**
	 * Lancer la procédure d'ouverture de concours
	 */
	public void procedureConcoursOuvrirDemarrer () {
		// Ouvrir le fichier
		String chemin = ViewHelper.ouvrir(this.jf_general, "Ouvrir un fichier de concours", FiltreFichier.ext_co, FiltreFichier.des_co);

		// Si un fichier a bien été donné
		if (chemin != null) {
			// Fermer le précédant concours
			this.procedureConcoursFermer();
			
			// Conserver le chemin
			this.chemin = chemin;
			
			// Charger le concours
			ModelConcours concours = new PersistanceXML(chemin).load();

			// Erreur ?
			if (concours == null) {
				// Message d'erreur
				ViewHelper.derror(this.jf_general, "Une erreur s'est produite lors du chargement du fichier.");
				
				// Nettoyer les models
				FrontModel.get().clean();
			} else {
				// Envoyer le concours au front
				try {
					FrontModel.get().nouveauConcours(concours);
				} catch (Exception e) {
					// Erreur
					this.error("Erreur lors de la création du concours.", e);
				}
				
				// Initialiser aux états ouvert, édition et sauvegardé
				this.initStates(STATE_OPEN,STATE_EDIT,STATE_SAVE);
			}
		}
	}
	
	/**
	 * Lancer la procédure de sauvegarde du concours
	 */
	public void procedureConcoursSauvegarderDemarrer() {
		// Demander à l'utilisateur le chemin de sauvegarde
		if(this.chemin == null) {
			this.chemin = ViewHelper.sauvegarder(this.jf_general, "Sauvegarder le concours", FiltreFichier.ext_co, FiltreFichier.des_co);
		}
		
		// Si un fichier a bien été donné
		if (this.chemin != null) {
			// Sauvegarder le concours
			if(new PersistanceXML(chemin).save()) {
				// Ajouter l'état sauvegardé
				this.addState(STATE_SAVE);
				
				// Lancer les exportations automatiques
				IOperation operation = this.ctrl_out.getLancerExportationsAutomatiquesOperation();	// Récupérer l'opération
				if(operation != null) {
					// Créer la fenêtre associée à l'opération
					JDOperation jd_operation = new JDOperation(this.jf_general,"Exportations automatiques",operation,true,true);
					
					operation.operationStart();		// Démarrer l'opération
					jd_operation.setVisible(true);	// Afficher la fenêtre
				}
			} else {
				// Message d'erreur
				ViewHelper.derror(this.jf_general, "Une erreur est survenue lors de la sauvegarde du concours.");
			}
		}
	}
	
	// ==== Procédures liées à la connexion au serveur
	
	// Procédure de connexion
	
	/**
	 * Lancer la connexion de connexion au serveur
	 */
	public void procedureServeurConnexionDemarrer () {
		// Créer et afficher la fenêtre de connexion
		this.jd_connexion = new JDConnexionCreer(this.jf_general);
		this.jd_connexion.setVisible(true);
	}
	
	/**
	 * Se connecter au serveur
	 * @param infos informations de connexion au serveur
	 */
	public void procedureServeurConnexion (InfosConnexionServeur infos) {
		// TODO Connexion au serveur
	}
	
	/**
	 * Annuler la procédure de connexion au serveur
	 */
	public void procedureServeurConnexionAnnuler () {
		// Masquer et détruire la fenêtre de connexion 
		this.jd_connexion.setVisible(false);
		this.jd_connexion = null;
	}
	
	// Procédure d'édition du concours via le serveur
	
	/**
	 * Prendre le jeton d'édition du serveur
	 */
	public void procedureServeurEditionDemarrer() {
		// TODO
	}
	
	/**
	 * Rendre le jeton d'édition du serveur
	 */
	public void procedureServeurEditionAnnuler() {
		// TODO
	}
	
	// ==== Autres
	
	/**
	 * Signaler une erreur fatale
	 * @param description description de l'erreur
	 * @param exceptions liste des exceptions associées
	 */
	public void error(String description, Exception... exceptions) {
		// Log de l'erreur
		for(Exception exception : exceptions) {
			Log.getLogger().fatal(description,exception);
		}
		
		// Message d'erreur
		ViewHelper.derror(null,"Erreur fatale","Une erreur fatale est survenue. L'application va être fermée.");
		if(ViewHelper.confirmation(null, "Un rapport d'erreur peut être envoyé aux développeurs de l'application. Autorisez-vous l'envoi de ce rapport ?")) {
			// Préparer le rapport
			IOperation operation = new Report(description).send();
			
			// Démarrer l'opération et afficher la progression
			JDOperation jd_operation = new JDOperation(null, "Envoi du rapport d'erreur", operation, true, false);
			operation.operationStart();
			jd_operation.setVisible(true);
		}
		
		// Log de la fermeture
		Log.getLogger().info("Fermeture de l'application suite à une erreur");
		
		// Quitter l'application avec un statut d'erreur
		System.exit(1);
	}
	
	/**
	 * Quitter l'application
	 */
	public void quit () {
		// Log de la fermeture
		Log.getLogger().info("Fermeture de l'application");
		
		// Fermer l'application
		System.exit(0);
	}

	// ==== Implémentations
	
	// Implémentation de IHistoryListener
	@Override
	public void historyActionPushed (Action action) {
		this.removeState(STATE_SAVE);
	}
	@Override
	public void historyActionPoped (Action action) {
		this.removeState(STATE_SAVE);
	}
	@Override
	public void historyInit () {
	}

}
