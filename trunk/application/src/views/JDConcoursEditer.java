package views;

import infos.InfosModelChemin;
import infos.InfosModelCompPhasesQualifsAbstract;
import infos.InfosModelConcours;
import infos.InfosModelDiffusion;
import infos.InfosModelEmplacement;
import infos.InfosModelExportation;
import infos.InfosModelHoraire;
import infos.InfosModelLieu;
import infos.InfosModelObjectif;
import infos.InfosModelPrix;
import infos.InfosModelPropriete;
import infos.InfosModelTheme;

import java.awt.Window;
import java.util.ArrayList;

import common.Pair;
import common.Triple;

import controlers.ContestOrg;

@SuppressWarnings("serial")
public class JDConcoursEditer extends JDConcoursAbstract
{

	// Constructeur
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

	// Implémentation de ok
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

	// Implémentation de quit
	@Override
	protected void quit () {
		// Demander l'annulation de la procédure de configuration de concours
		ContestOrg.get().procedureConcoursConfigurerAnnuler();
	}

}
