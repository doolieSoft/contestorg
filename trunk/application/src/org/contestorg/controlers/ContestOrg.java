﻿package org.contestorg.controlers;


import java.util.ArrayList;

import javax.swing.JDialog;


import org.apache.log4j.PropertyConfigurator;
import org.contestorg.common.MoodyAbstract;
import org.contestorg.common.Pair;
import org.contestorg.common.TrackableList;
import org.contestorg.common.Triple;
import org.contestorg.events.Action;
import org.contestorg.infos.InfosConnexionServeur;
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
import org.contestorg.interfaces.IListValidator;
import org.contestorg.interfaces.IOperation;
import org.contestorg.log.Log;
import org.contestorg.log.Report;
import org.contestorg.models.FrontModel;
import org.contestorg.models.FrontModelConfiguration;
import org.contestorg.models.ModelConcours;
import org.contestorg.out.PersistanceXML;
import org.contestorg.views.FiltreFichier;
import org.contestorg.views.JDConcoursCreer;
import org.contestorg.views.JDConcoursEditer;
import org.contestorg.views.JDConnexionCreer;
import org.contestorg.views.JDOperation;
import org.contestorg.views.JFPrincipal;
import org.contestorg.views.ViewHelper;


/**
 * Controleur principal
 */
public class ContestOrg extends MoodyAbstract implements IHistoryListener
{
	
	// Version de ContestOrg
	public static final String VERSION = "2.0.0b";  

	// Instance unique de ContestOrg
	private static ContestOrg contestOrg;
	
	// Etats possibles
	public static final int STATE_OPEN		= 1;	// Est ce qu'un concours est ouvert ?
	public static final int STATE_SAVE 		= 2;	// Est ce que le concours est sauvegardé ?
	public static final int STATE_SERVER	= 4;	// Est ce que le mode serveur est utilisé ? 
	public static final int STATE_EDIT 		= 8;	// Est ce que le mode édition est activé ?

	// Vues
	private JFPrincipal jf_general;	// fenêtre principale
	private JDialog jd_concours;	// fenêtre de création et d'édition de concours
	private JDialog jd_connexion;	// fenêtre de connexion au serveur de ContestOrg

	// Controleurs
	private CtrlParticipants ctrl_participants;
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
		this.ctrl_participants = new CtrlParticipants();
		this.ctrl_qualifications = new CtrlPhasesQualificatives();
		this.ctrl_finales = new CtrlPhasesEliminatoires();
		this.ctrl_out = new CtrlOut();
		
		// S'abonner à l'historique
		FrontModel.get().getHistory().addListener(this);

		// Lancer la fenêtre generale
		this.jf_general = new JFPrincipal("ContestOrg "+ContestOrg.VERSION);
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
	public CtrlParticipants getCtrlParticipants() {
		return this.ctrl_participants;
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
		return FrontModel.get().getFrontModelConfiguration().getObjectifsValidator();
	}
	public IListValidator<InfosModelCompPhasesQualifsAbstract> getComparateursValidator() {
		return FrontModel.get().getFrontModelConfiguration().getComparateursValidator();
	}
	public IListValidator<Triple<InfosModelExportation, InfosModelChemin, InfosModelTheme>> getExportationsValidator() {
		return FrontModel.get().getFrontModelConfiguration().getExportationsValidator();
	}
	public IListValidator<Pair<InfosModelDiffusion,InfosModelTheme>> getDiffusionsValidator() {
		return FrontModel.get().getFrontModelConfiguration().getDiffusionsValidator();
	}
	public IListValidator<InfosModelPrix> getPrixValidator() {
		return FrontModel.get().getFrontModelConfiguration().getPrixValidator();
	}
	public IListValidator<Triple<InfosModelLieu, TrackableList<InfosModelEmplacement>, TrackableList<InfosModelHoraire>>> getLieuxValidator() {
		return FrontModel.get().getFrontModelConfiguration().getLieuxValidator();
	}
	public IListValidator<InfosModelEmplacement> getEmplacementsValidator() {
		return FrontModel.get().getFrontModelConfiguration().getEmplacementsValidator();
	}
	public IListValidator<InfosModelHoraire> getHorairesValidator() {
		return FrontModel.get().getFrontModelConfiguration().getHorairesValidator();
	}
	public IListValidator<InfosModelPropriete> getProprietesValidator() {
		return FrontModel.get().getFrontModelConfiguration().getProprietesValidator();
	}
	public IListValidator<InfosModelCategorie> getCategoriesValidator() {
		return FrontModel.get().getFrontModelParticipants().getCategoriesValidator();
	}
	public IListValidator<Pair<InfosModelPoule, ArrayList<String>>> getPoulesValidator() {
		return FrontModel.get().getFrontModelParticipants().getPoulesValidator();
	}
	
	// ==== Procédures liés au concours
	
	// Procédure de création
	public void procedureConcoursNouveauDemarrer () {
		// Fermer le précédant concours
		this.procedureConcoursFermer();
		
		// Créer et afficher la fenêtre de création de concours
		this.jd_concours = new JDConcoursCreer(this.jf_general);
		this.jd_concours.setVisible(true);
	}
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
	public void procedureConcoursNouveauAnnuler () {
		// Masquer et détruire la fenêtre de création de concours 
		this.jd_concours.setVisible(false);
		this.jd_concours = null;
	}

	// Procédure de configuration
	public void procedureConcoursConfigurerDemarrer () {
		// Récupérer le front
		FrontModelConfiguration front = FrontModel.get().getFrontModelConfiguration();
		
		// Créer et afficher la fenêtre de configuration de concours
		this.jd_concours = new JDConcoursEditer(this.jf_general,FrontModel.get().getConcours().toInfos(),front.getListeObjectifs(),front.getListeComparateurs(),front.getListeExportations(),front.getPublicationIndex(),front.getListeDiffusions(),front.getListePrix(),front.getListeLieux(),front.getListeProprietes());
		this.jd_concours.setVisible(true);
	}
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
	public void procedureConcoursConfigurerAnnuler () {
		// Masquer et détruire la fenêtre de configuration de concours 
		this.jd_concours.setVisible(false);
		this.jd_concours = null;
	}

	// Fermeture du concours
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
	public void procedureServeurConnexionDemarrer () {
		// Créer et afficher la fenêtre de connexion
		this.jd_connexion = new JDConnexionCreer(this, this.jf_general);
		this.jd_connexion.setVisible(true);
	}
	public void procedureServeurConnexion (InfosConnexionServeur infos) {
		// TODO Connexion au serveur
	}
	public void procedureServeurConnexionAnnuler () {
		// Masquer et détruire la fenêtre de connexion 
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
