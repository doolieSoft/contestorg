package org.contestorg.controllers;

import java.io.File;
import java.util.ArrayList;

import org.contestorg.common.MoodyAbstract;
import org.contestorg.common.Pair;
import org.contestorg.common.TrackableList;
import org.contestorg.common.Triple;
import org.contestorg.events.Action;
import org.contestorg.infos.InfosMiseAJour;
import org.contestorg.infos.InfosModelCategorie;
import org.contestorg.infos.InfosModelChemin;
import org.contestorg.infos.InfosModelCritereClassementAbstract;
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
import org.contestorg.models.ModelConcours;
import org.contestorg.out.MiseAJour;
import org.contestorg.out.PersistanceXML;
import org.contestorg.preferences.Preferences;
import org.contestorg.views.FiltreFichier;
import org.contestorg.views.JDMiseAJour;
import org.contestorg.views.JDOperation;
import org.contestorg.views.JFPrincipal;
import org.contestorg.views.ViewHelper;

/**
 * Controleur principal
 */
public class ContestOrg extends MoodyAbstract implements IHistoryListener
{
	
	/** Version de ContestOrg ([évolution majeure].[évolution mineure].[correction]) */
	public static final String VERSION = "2.4.0";

	/** Instance unique de ContestOrg */
	private static ContestOrg contestOrg;
	
	// Etats possibles
	
	/** Est-ce que le concours est ouvert ? */
	public static final int STATE_OPEN = 1;
	
	/** Est-ce que le concours est sauvegardé ? */
	public static final int STATE_SAVE = 2;
	
	/** Est-ce que le mode édition est activé ? */
	public static final int STATE_EDIT = 4;

	// Vues
	
	/** Fenêtre principale */
	private JFPrincipal jf_general;

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
	 * Récupérer les informations du concours
	 * @return informations du concours
	 */
	public InfosModelConcours getConcours() {
		return FrontModel.get().getConcours().getInfos();
	}
	
	/**
	 * Récupérer la liste des objectifs
	 * @return liste des objectifs
	 */
	public ArrayList<InfosModelObjectif> getListeObjectifs() {
		return FrontModel.get().getFrontModelConfiguration().getListeObjectifs();
	}
	
	/**
	 * Récupérer la liste des lieux et emplacements
	 * @return liste des lieux et emplacements
	 */
	public ArrayList<Triple<InfosModelLieu, ArrayList<InfosModelEmplacement>, ArrayList<InfosModelHoraire>>> getListeLieuxEmplacements() {
		return FrontModel.get().getFrontModelConfiguration().getListeLieuxEmplacements();
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
	 * Récupérer un validateur de liste de critères de classement
	 * @return validateur de liste de critères de classement
	 */
	public ITrackableListValidator<InfosModelCritereClassementAbstract> getCriteresClassementValidator() {
		return FrontModel.get().getFrontModelConfiguration().getCriteresClassementValidator();
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
	
	// Autres
	
	/**
	 * Récupérer la fenêtre principale
	 * @return fenêtre principale
	 */
	public JFPrincipal getFenetrePrincipale() {
		return this.jf_general;
	}
		
	/**
	 * Récupérer les préférences
	 * @return préférences
	 */
	public Preferences getPreferences() {
		return this.preferences;
	}
	
	// ==== Actions sur le concours
	
	/**
	 * Créer le concours
	 * @param infos informations du concours
	 * @param objectifs liste des objectifs
	 * @param criteresClassement liste des critères de classement
	 * @param exportations liste des exportations
	 * @param publication indice de l'exportation qui fait office de publication
	 * @param diffusions liste des diffusions
	 * @param prix liste des prix
	 * @param lieux liste des lieux
	 * @param proprietes liste des propriétés
	 */
	public void concoursCreer (InfosModelConcours infos, TrackableList<InfosModelObjectif> objectifs, TrackableList<InfosModelCritereClassementAbstract> criteresClassement, TrackableList<Triple<InfosModelExportation,InfosModelChemin,InfosModelTheme>> exportations, int publication, TrackableList<Pair<InfosModelDiffusion,InfosModelTheme>> diffusions, TrackableList<InfosModelPrix> prix, TrackableList<Triple<InfosModelLieu,TrackableList<InfosModelEmplacement>,TrackableList<InfosModelHoraire>>> lieux, TrackableList<InfosModelPropriete> proprietes) {
		try {
			// Fermer le précédant concours si nécessaire
			if(FrontModel.get().getConcours() != null) {
				this.concoursFermer();
			}
			
			// Créer le nouveau concours
			FrontModel.get().nouveauConcours(infos);
			
			// Configurer le nouveau concours
			FrontModel.get().getFrontModelConfiguration().configurerConcours(infos, objectifs, criteresClassement, exportations, publication, diffusions, prix, lieux, proprietes);
			
			// Initialiser aux états ouvert et édition
			this.initStates(STATE_OPEN,STATE_EDIT);
		} catch (Exception e) {
			// Erreur
			this.erreur("Erreur lors de la création du concours.", e);
		}
	}
	
	/**
	 * Ouvrir un concours
	 */
	public void concoursOuvrir () {		
		// Ouvrir le fichier
		String chemin = ViewHelper.ouvrir(this.jf_general, "Ouvrir un fichier de concours", FiltreFichier.ext_co, FiltreFichier.des_co);

		// Si un fichier a bien été donné
		if (chemin != null) {
			// Fermer le précédant concours si nécessaire
			if(FrontModel.get().getConcours() != null) {
				this.concoursFermer();
			}
			
			// Conserver le chemin
			this.chemin = chemin;
			
			// Démarrer l'action de chargement
			FrontModel.get().getHistory().start("Chargement d'un fichier de concours");
			
			// Charger le concours
			ModelConcours concours = new PersistanceXML(chemin).load();
			
			// Fermer l'action de chargement
			FrontModel.get().getHistory().close();

			// Erreur ?
			if (concours == null) {
				// Message d'erreur
				ViewHelper.derror(this.jf_general, "Une erreur s'est produite lors du chargement du fichier.");
				
				// Nettoyer les models
				FrontModel.get().clean();
			} else {
				// Envoyer le concours à la couche métier
				try {
					FrontModel.get().nouveauConcours(concours);
				} catch (Exception e) {
					// Erreur
					this.erreur("Erreur lors de la création du concours.", e);
				}
				
				// Initialiser aux états ouvert, édition et sauvegardé
				this.initStates(STATE_OPEN,STATE_EDIT,STATE_SAVE);
			}
		}
	}
	
	/**
	 * Ouvrir une variante d'un modèle
	 * @param chemin chemin de la variante du modèle
	 * @return opération réussie ?
	 */
	public boolean modeleOuvrir(String chemin) {
		// Fermer le précédant concours si nécessaire
		if(FrontModel.get().getConcours() != null) {
			this.concoursFermer();
		}
		
		// Vérifier si le chemin est valide
		if(new File(chemin).exists()) {
			// Démarrer l'action de chargement
			FrontModel.get().getHistory().start("Chargement d'un modèle de concours");
			
			// Charger le concours
			ModelConcours concours = new PersistanceXML(chemin).load();
			
			// Fermer l'action de chargement
			FrontModel.get().getHistory().close();

			// Erreur ?
			if (concours == null) {
				// Message d'erreur
				ViewHelper.derror(this.jf_general, "Une erreur s'est produite lors du chargement du fichier.");
				
				// Nettoyer les models
				FrontModel.get().clean();
				
				// Erreur
				return false;
			} else {
				// Envoyer le concours à la couche métier
				try {
					FrontModel.get().nouveauConcours(concours);
				} catch (Exception e) {
					// Erreur
					this.erreur("Erreur lors de la création du concours.", e);
					return false;
				}
				
				// Initialiser aux états ouvert et édition
				this.initStates(STATE_OPEN,STATE_EDIT);
				
				// Réussite
				return true;
			}
		} else {
			// Message d'erreur
			ViewHelper.derror(this.jf_general, "Le fichier \""+chemin+"\" n'existe pas.");
			
			// Erreur
			return false;
		}
	}
	
	/**
	 * Configurer le concours
	 * @param infos informations du concours
	 * @param objectifs liste des objectifs
	 * @param criteresClassement liste des critères de classement
	 * @param exportations liste des exportations
	 * @param publication indice de l'exportation qui fait office de publication
	 * @param diffusions liste des diffusions
	 * @param prix liste des prix
	 * @param lieux liste des lieux
	 * @param proprietes liste des propriétés
	 */
	public void concoursConfigurer (InfosModelConcours infos, TrackableList<InfosModelObjectif> objectifs, TrackableList<InfosModelCritereClassementAbstract> criteresClassement, TrackableList<Triple<InfosModelExportation,InfosModelChemin,InfosModelTheme>> exportations, int publication, TrackableList<Pair<InfosModelDiffusion,InfosModelTheme>> diffusions, TrackableList<InfosModelPrix> prix, TrackableList<Triple<InfosModelLieu,TrackableList<InfosModelEmplacement>,TrackableList<InfosModelHoraire>>> lieux, TrackableList<InfosModelPropriete> proprietes) {
		try {
			// Configurer le concours
			FrontModel.get().getFrontModelConfiguration().configurerConcours(infos, objectifs, criteresClassement, exportations, publication, diffusions, prix, lieux, proprietes);
			
			// Retirer l'état sauvegardé
			this.removeState(STATE_SAVE);
		} catch (Exception e) {
			// Erreur
			this.erreur("Erreur lors de la configuration du concours.", e);
		}
	}
	
	/**
	 * Sauvegarder le concours
	 */
	public void concoursSauvegarder() {
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
					
					// Démarrer l'opération
					operation.operationStart();
					
					// Afficher la fenêtre
					jd_operation.setVisible(true);
				}
			} else {
				// Message d'erreur
				ViewHelper.derror(this.jf_general, "Une erreur est survenue lors de la sauvegarde du concours.");
			}
		}
	}
	
	/**
	 * Sauvegarder le concours sous
	 */
	public void concoursSauvegarderSous() {
		// Demander à l'utilisateur le chemin de sauvegarde
		this.chemin = ViewHelper.sauvegarder(this.jf_general, "Sauvegarder le concours sous", FiltreFichier.ext_co, FiltreFichier.des_co);
		
		// Si un fichier a bien été donné
		if (this.chemin != null) {
			// Sauvegarder le concours
			if(new PersistanceXML(chemin).save()) {
				// Ajouter l'état sauvegardé
				this.addState(STATE_SAVE);
			} else {
				// Message d'erreur
				ViewHelper.derror(this.jf_general, "Une erreur est survenue lors de la sauvegarde du concours.");
			}
		}
	}
	
	/**
	 * Lancer la procédure de fermeture du concours
	 */
	public void concoursFermer() {
		// Revenir à l'état initial
		this.initStates();
		
		// Fermer le front
		try {
			FrontModel.get().closeConcours();
		} catch (Exception e) {
			// Erreur
			this.erreur("Erreur lors de la fermeture du concours.", e);
		}
		
		// Perdre la référence du chemin
		this.chemin = null;
	}
	
	// ==== Autres
	
	/**
	 * Signaler une erreur fatale
	 * @param description description de l'erreur
	 * @param exceptions liste des exceptions associées
	 */
	public void erreur(String description, Exception... exceptions) {
		// Log de l'erreur
		for(Exception exception : exceptions) {
			Log.getLogger().fatal(description,exception);
		}
		
		// Message d'erreur
		ViewHelper.derror(null,"Erreur fatale","Une erreur fatale est survenue. L'application va être fermée.");
		if(ViewHelper.confirmation(null, "Un rapport d'erreur peut être envoyé aux développeurs de l'application. Autorisez-vous l'envoi de ce rapport ?")) {
			// Préparer le rapport
			IOperation operation = new Report(description, exceptions).send();
			
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
	public void quitter () {
		// Log de la fermeture
		Log.getLogger().info("Fermeture de l'application");
		
		// Quitter l'application
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
