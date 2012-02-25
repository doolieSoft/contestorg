package org.contestorg.views;

import java.awt.Window;
import java.util.ArrayList;

import org.contestorg.common.Pair;
import org.contestorg.common.Triple;
import org.contestorg.controllers.ContestOrg;
import org.contestorg.infos.InfosModelChemin;
import org.contestorg.infos.InfosModelCompPhasesQualifsAbstract;
import org.contestorg.infos.InfosModelConcours;
import org.contestorg.infos.InfosModelDiffusion;
import org.contestorg.infos.InfosModelEmplacement;
import org.contestorg.infos.InfosModelExportation;
import org.contestorg.infos.InfosModelHoraire;
import org.contestorg.infos.InfosModelLieu;
import org.contestorg.infos.InfosModelObjectif;
import org.contestorg.infos.InfosModelPrix;
import org.contestorg.infos.InfosModelPropriete;
import org.contestorg.infos.InfosModelTheme;

/**
 * Boîte de dialogue d'édition de concours
 */
@SuppressWarnings("serial")
public class JDConcoursEditer extends JDConcoursAbstract
{

	/**
	 * Constructeur
	 * @param w_parent fenêtre parent
	 * @param infos informations du concours
	 * @param objectifs liste des objectifs
	 * @param comparateurs liste des comparateurs
	 * @param exportations liste des exportations
	 * @param publication indice de l'exportation qui fait office de publication
	 * @param diffusions liste des diffusion
	 * @param prix liste des prix
	 * @param lieux liste des lieux
	 * @param proprietes liste des propriétés
	 */
	public JDConcoursEditer(Window w_parent, InfosModelConcours infos, ArrayList<InfosModelObjectif> objectifs, ArrayList<InfosModelCompPhasesQualifsAbstract> comparateurs, ArrayList<Triple<InfosModelExportation,InfosModelChemin,InfosModelTheme>> exportations, int publication, ArrayList<Pair<InfosModelDiffusion,InfosModelTheme>> diffusions, ArrayList<InfosModelPrix> prix, ArrayList<Triple<InfosModelLieu,ArrayList<InfosModelEmplacement>,ArrayList<InfosModelHoraire>>> lieux, ArrayList<InfosModelPropriete> proprietes) {
		// Appeller le constructeur du parent
		super(w_parent, "Configurer le concours");

		// Remplir les champs avec les données du concours
		this.jp_general.setConcoursNom(infos.getConcoursNom());
		this.jp_general.setConcoursLieu(infos.getConcoursLieu());
		this.jp_general.setConcoursTelephone(infos.getConcoursTelephone());
		this.jp_general.setConcoursSite(infos.getConcoursSite());
		this.jp_general.setConcoursEmail(infos.getConcoursEmail());
		this.jp_general.setConcoursDescription(infos.getConcoursDescription());

		// Remplir les champs avec les données de l'organisme
		this.jp_general.setOrganismeNom(infos.getOrganisateurNom());
		this.jp_general.setOrganismeLieu(infos.getOrganisateurLieu());
		this.jp_general.setOrganismeTelephone(infos.getOrganisateurTelephone());
		this.jp_general.setOrganismeSite(infos.getOrganisateurSite());
		this.jp_general.setOrganismeEmail(infos.getOrganisateurEmail());
		this.jp_general.setOrganismeDescription(infos.getOrganisateurDescription());
		
		// Remplir les types de participants et de qualifications
		this.jp_general.setTypeParticipants(infos.getTypeParticipants());
		this.jp_general.setTypeQualifications(infos.getTypeQualifications());
		
		// Remplir les informations de points
		this.jp_points.setPointsVictoire(infos.getPointsVictoire());
		this.jp_points.setPointsEgalite(infos.getPointsEgalite());
		this.jp_points.setPointsDefaite(infos.getPointsDefaite());
		this.jp_points.setObjectifs(objectifs);
		this.jp_points.setComparateurs(comparateurs);
		
		// Remplir les informations d'exportations
		this.jp_exportations.setExportations(exportations);
		this.jp_exportations.setPublication(publication);
		this.jp_exportations.setDiffusions(diffusions);
		
		// Remplir les informations de prix
		this.jp_prix.setPrix(prix);

		// Remplir les informations de programmation
		this.jp_programmation.setProgrammationDuree(infos.getProgrammationDuree());
		this.jp_programmation.setProgrammationInterval(infos.getProgrammationInterval());
		this.jp_programmation.setProgrammationPause(infos.getProgrammationPause());

		// Remplir les informations de lieux
		this.jp_lieux.setLieux(lieux);

		// Remplir les informations de propriétés
		this.jp_proprietes.setProprietes(proprietes);
	}

	/**
	 * @see JDPattern#ok()
	 */
	@Override
	protected void ok () {
		// Vérifier les validité des données
		if (this.check()) {
			// Demander la configuration du concours
			ContestOrg.get().procedureConcoursConfigurer(
				this.getInfosModelConcours(), this.jp_points.getObjectifs(), this.jp_points.getComparacteurs(),
				this.jp_exportations.getExportations(), this.jp_exportations.getPublication(), this.jp_exportations.getDiffusions(),
				this.jp_prix.getPrix(), this.jp_lieux.getLieux(), this.jp_proprietes.getProprietes()
			);
		}
	}

	/**
	 * @see JDPattern#quit()
	 */
	@Override
	protected void quit () {
		// Demander l'annulation de la procédure de configuration de concours
		ContestOrg.get().procedureConcoursConfigurerAnnuler();
	}

}
