package controlers;

import infos.InfosConnexionServeur;
import infos.InfosModelCategorie;
import infos.InfosModelChemin;
import infos.InfosModelCompPhasesQualifsAbstract;
import infos.InfosModelConcours;
import infos.InfosModelDiffusion;
import infos.InfosModelEmplacement;
import infos.InfosModelExportation;
import infos.InfosModelHoraire;
import infos.InfosModelLieu;
import infos.InfosModelObjectif;
import infos.InfosModelPoule;
import infos.InfosModelPrix;
import infos.InfosModelPropriete;
import infos.InfosModelTheme;
import interfaces.IHistoryListener;
import interfaces.IListValidator;
import interfaces.IOperation;

import java.util.ArrayList;

import javax.swing.JDialog;

import log.Log;
import log.Report;
import models.FrontModel;
import models.ModelConcours;

import org.apache.log4j.PropertyConfigurator;

import out.PersistanceXML;
import views.FiltreFichier;
import views.JDConcoursCreer;
import views.JDConcoursEditer;
import views.JDConnexionCreer;
import views.JDOperation;
import views.JFPrincipal;
import views.ViewHelper;

import common.TrackableList;
import common.MoodyAbstract;
import common.Pair;
import common.Triple;

import events.Action;

/**
 * Controleur principal
 */
public class ContestOrg extends MoodyAbstract implements IHistoryListener
{

	// Instance unique de contestOrg
	private static ContestOrg contestOrg;
	
	// Etats possibles
	public static final int STATE_OPEN		= 1;	// Est ce qu'un concours est ouvert ?
	public static final int STATE_SAVE 		= 2;	// Est ce que le concours est sauvegardé ?
	public static final int STATE_SERVER 	= 4;	// Est ce que le mode serveur est utilisé ? 
	public static final int STATE_EDIT 		= 8;	// Est ce que le mode édition est activé ?

	// Vues
	private JFPrincipal jf_general;	// Fenetre principale
	private JDialog jd_concours;	// Fenetre de création et d'édition de concours
	private JDialog jd_connexion;	// Fenetre de connexion au serveur de ContestOrg

	// Controleurs
	private CtrlEquipes ctrl_equipes;
	private CtrlPhasesQualificatives ctrl_qualifications;
	private CtrlPhasesEliminatoires ctrl_finales;
	private CtrlOut ctrl_out;
	
	// Chemin du concours
	private String chemin;

	// Constructeur
	private ContestOrg() {		
		// Configuration
		PropertyConfigurator.configure("configuration.ini");
		
		// Log du lancement de l'application
		Log.getLogger().info("Lancement de l'application");
		
		// Retenir l'instance ContestOrg
		ContestOrg.contestOrg = this;

		// Créer les différents controleurs
		this.ctrl_equipes = new CtrlEquipes();
		this.ctrl_qualifications = new CtrlPhasesQualificatives();
		this.ctrl_finales = new CtrlPhasesEliminatoires();
		this.ctrl_out = new CtrlOut();
		
		// S'abonner à l'historique
		FrontModel.get().getHistory().addListener(this);

		// Lancer la fenetre generale
		this.jf_general = new JFPrincipal("ContestOrg 2.0.0.0a");
		this.addListener(this.jf_general);
		this.jf_general.setVisible(true);
	}

	// Récupérer l'instance de ContestOrg
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
	public CtrlEquipes getCtrlEquipes() {
		return this.ctrl_equipes;
	}
	public CtrlPhasesQualificatives getCtrlPhasesQualificatives() {
		return this.ctrl_qualifications;
	}
	public CtrlPhasesEliminatoires getCtrlPhasesEliminatoires() {
		return this.ctrl_finales;
	}
	public CtrlOut getCtrlOut() {
		return this.ctrl_out;
	}
	
	// Récupérer des données sur le concours
	public int getTypeParticipants() {
		return FrontModel.get().getConcours().getTypeParticipants();
	}
	public int getTypeQualifications() {
		return FrontModel.get().getConcours().getTypeQualifications();
	}
	
	// Récupérer des valideurs de liste
	public IListValidator<InfosModelObjectif> getObjectifsValidator() {
		return FrontModel.get().getObjectifsValidator();
	}
	public IListValidator<InfosModelCompPhasesQualifsAbstract> getComparateursValidator() {
		return FrontModel.get().getComparateursValidator();
	}
	public IListValidator<Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme>> getExportationsValidator() {
		return FrontModel.get().getExportationsValidator();
	}
	public IListValidator<Pair<InfosModelDiffusion,InfosModelTheme>> getDiffusionsValidator() {
		return FrontModel.get().getDiffusionsValidator();
	}
	public IListValidator<InfosModelPrix> getPrixValidator() {
		return FrontModel.get().getPrixValidator();
	}
	public IListValidator<Triple<InfosModelLieu, TrackableList<InfosModelEmplacement>, TrackableList<InfosModelHoraire>>> getLieuxValidator() {
		return FrontModel.get().getLieuxValidator();
	}
	public IListValidator<InfosModelEmplacement> getEmplacementsValidator() {
		return FrontModel.get().getEmplacementsValidator();
	}
	public IListValidator<InfosModelHoraire> getHorairesValidator() {
		return FrontModel.get().getHorairesValidator();
	}
	public IListValidator<InfosModelPropriete> getProprietesValidator() {
		return FrontModel.get().getProprietesValidator();
	}
	public IListValidator<InfosModelCategorie> getCategoriesValidator() {
		return FrontModel.get().getCategoriesValidator();
	}
	public IListValidator<Pair<InfosModelPoule, ArrayList<String>>> getPoulesValidator() {
		return FrontModel.get().getPoulesValidator();
	}
	
	// ==== Procédures liés au concours
	
	// Procédure de création
	public void procedureConcoursNouveauDemarrer () {
		// Fermer le précédant concours
		this.procedureConcoursFermer();
		
		// Créer et afficher la fenetre de création de concours
		this.jd_concours = new JDConcoursCreer(this.jf_general);
		this.jd_concours.setVisible(true);
	}
	public void procedureConcoursNouveau (InfosModelConcours infos, TrackableList<InfosModelObjectif> objectifs, TrackableList<InfosModelCompPhasesQualifsAbstract> comparateurs, TrackableList<Triple<InfosModelExportation,InfosModelChemin,InfosModelTheme>> exportations, int publication, TrackableList<Pair<InfosModelDiffusion,InfosModelTheme>> diffusions, TrackableList<InfosModelPrix> prix, TrackableList<Triple<InfosModelLieu,TrackableList<InfosModelEmplacement>,TrackableList<InfosModelHoraire>>> lieux, TrackableList<InfosModelPropriete> proprietes) {
		try {			
			// Créer le nouveau concours
			FrontModel.get().nouveauConcours(infos);
			
			// Configurer le nouveau concours
			FrontModel.get().configurerConcours(infos, objectifs, comparateurs, exportations, publication, diffusions, prix, lieux, proprietes);
			
			// Initialiser aux états ouvert et édition
			this.initStates(STATE_OPEN,STATE_EDIT);
			
			// Masquer et détruire la fenetre de création de concours 
			this.jd_concours.setVisible(false);
			this.jd_concours = null;
		} catch (Exception e) {
			// Erreur
			this.error("Erreur lors de la création/configuration du concours.", e);
		}
	}
	public void procedureConcoursNouveauAnnuler () {
		// Masquer et détruire la fenetre de création de concours 
		this.jd_concours.setVisible(false);
		this.jd_concours = null;
	}

	// Procédure de configuration
	public void procedureConcoursConfigurerDemarrer () {
		// Récupérer le front
		FrontModel front = FrontModel.get();
		
		// Créer et afficher la fenetre de configuration de concours
		this.jd_concours = new JDConcoursEditer(this.jf_general,front.getConcours().toInformation(),front.getListeObjectifs(),front.getListeComparateurs(),front.getListeExportations(),front.getPublicationIndex(),front.getListeDiffusions(),front.getListePrix(),front.getListeLieux(),front.getListeProprietes());
		this.jd_concours.setVisible(true);
	}
	public void procedureConcoursConfigurer (InfosModelConcours infos, TrackableList<InfosModelObjectif> objectifs, TrackableList<InfosModelCompPhasesQualifsAbstract> comparateurs, TrackableList<Triple<InfosModelExportation,InfosModelChemin,InfosModelTheme>> exportations, int publication, TrackableList<Pair<InfosModelDiffusion,InfosModelTheme>> diffusions, TrackableList<InfosModelPrix> prix, TrackableList<Triple<InfosModelLieu,TrackableList<InfosModelEmplacement>,TrackableList<InfosModelHoraire>>> lieux, TrackableList<InfosModelPropriete> proprietes) {
		try {
			// Configurer le concours
			FrontModel.get().configurerConcours(infos, objectifs, comparateurs, exportations, publication, diffusions, prix, lieux, proprietes);
			
			// Retirer l'état sauvegardé
			this.removeState(STATE_SAVE);
			
			// Masquer et détruire la fenetre de création de concours 
			this.jd_concours.setVisible(false);
			this.jd_concours = null;
		} catch (Exception e) {
			// Erreur
			this.error("Erreur lors de la configuration du concours.", e);
		}
	}
	public void procedureConcoursConfigurerAnnuler () {
		// Masquer et détruire la fenetre de configuration de concours 
		this.jd_concours.setVisible(false);
		this.jd_concours = null;
	}

	// Fermeture du concours
	public void procedureConcoursFermer() {
		// Revenir à l'état initial
		this.initStates();
		
		// Fermer le front
		try {
			FrontModel.get().close();
		} catch (Exception e) {
			// Erreur
			this.error("Erreur lors de la fermeture du concours.", e);
		}
		
		// Perdre la référence du chemin
		this.chemin = null;
	}
	
	// Procédures de persistance
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
					// Créer la fenetre associée à l'opération
					JDOperation jd_operation = new JDOperation(this.jf_general,"Exportations automatiques",operation,true,true);
					
					operation.operationStart();		// Démarrer l'opération
					jd_operation.setVisible(true);	// Afficher la fenetre
				}
			} else {
				// Message d'erreur
				ViewHelper.derror(this.jf_general, "Une erreur est survenue lors de la sauvegarde du concours.");
			}
		}
	}
	
	// ==== Procédures liées à la connexion au serveur
	
	// Procédure de connexion
	public void procedureServeurConnexionDemarrer () {
		// Créer et afficher la fenetre de connexion
		this.jd_connexion = new JDConnexionCreer(this, this.jf_general);
		this.jd_connexion.setVisible(true);
	}
	public void procedureServeurConnexion (InfosConnexionServeur infos) {
		// TODO Connexion au serveur
	}
	public void procedureServeurConnexionAnnuler () {
		// Masquer et détruire la fenetre de connexion 
		this.jd_connexion.setVisible(false);
		this.jd_connexion = null;
	}
	
	// Procédure d'édition
	public void procedureServeurEditionDemarrer() {
		// TODO
	}
	public void procedureServeurEditionAnnuler() {
		// TODO
	}
	
	// ==== Autres

	// Erreur fatale
	public void error(String description, Exception... exceptions) {
		// Log de l'erreur
		for(Exception exception : exceptions) {
			Log.getLogger().fatal(description,exception);
		}
		
		// Message d'erreur
		ViewHelper.derror(null,"Erreur fatale","Une erreur fatale est survenue. L'application va être fermée.");
		if(ViewHelper.confirmation(null, "Un rapport d'erreur peut être envoyé aux développeurs de l'application. Autorisez-vous l'envoi de ce rapport ?")) {
			// Préparer le rapport
			Report report = new Report(description);
			for(Exception exception : exceptions) {
				report.pushException(exception);
			}
			IOperation operation = report.send();
			
			// Démarrer l'opération et afficher la progression
			JDOperation jd_operation = new JDOperation(null, "Envoi du rapport d'erreur", operation, true, false);
			operation.operationStart();
			jd_operation.setVisible(true);
		}
		
		// Log de la fermeture
		Log.getLogger().info("Fermeture de l'application avec erreur");
		
		// Quitter l'application
		System.exit(0);
	}
	
	// Quitter l'application
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
