package org.contestorg.views;


import java.awt.Window;
import java.util.ArrayList;

import org.contestorg.common.Pair;
import org.contestorg.common.Quintuple;
import org.contestorg.common.TrackableList;
import org.contestorg.controlers.ContestOrg;
import org.contestorg.infos.InfosModelConcours;
import org.contestorg.infos.InfosModelParticipant;
import org.contestorg.infos.InfosModelPrix;
import org.contestorg.infos.InfosModelPropriete;
import org.contestorg.infos.InfosModelProprieteParticipant;
import org.contestorg.interfaces.ICollector;



@SuppressWarnings("serial")
public class JDParticipantEditer extends JDParticipantAbstract
{
	
	// Ancien nom du participant
	private String ancienNomParticipant;

	// Constructeur
	public JDParticipantEditer(Window w_parent, ICollector<Quintuple<String,String,InfosModelParticipant, TrackableList<Pair<String, InfosModelProprieteParticipant>>, TrackableList<String>>> collector, Quintuple<String,String,InfosModelParticipant,ArrayList<Pair<String,InfosModelProprieteParticipant>>,ArrayList<String>> infos) {
		// Appeller le constructeur du parent
		super(w_parent, ContestOrg.get().getTypeParticipants() == InfosModelConcours.PARTICIPANTS_EQUIPES ? "Editer une équipe" : "Editer un joueur", collector);
		
		// Retenir l'ancien nom du participant
		this.ancienNomParticipant = infos.getThird().getNom();
		
		// Remplir les entrées avec les informations du participant
		this.jp_categoriePoule.setCategorie(infos.getFirst());
		this.jp_categoriePoule.setPoule(infos.getSecond());
		this.jtf_nom.setText(infos.getThird().getNom());
		this.jtf_nom.setText(infos.getThird().getNom());
		this.jtf_stand.setText(infos.getThird().getStand());
		this.jtf_ville.setText(infos.getThird().getVille());
		this.jcb_statut.setSelectedIndex(infos.getThird().getStatut().ordinal());
		this.jtf_membres.setText(infos.getThird().getMembres());
		this.jta_details.setText(infos.getThird().getDetails());
		
		// Séléctionner les prix
		if(this.jl_prix != null) {
			int[] indexes = new int[infos.getFifth().size()];
			ArrayList<InfosModelPrix> prixDisponibles = ContestOrg.get().getCtrlParticipants().getListePrix();
			for(int i=0;i<prixDisponibles.size();i++) {
				for(int j=0;j<infos.getFifth().size();j++) {
					if(infos.getFifth().get(j).equals(prixDisponibles.get(i).getNom())) {
						indexes[j] = i; 
					}
				}
			}
			this.jl_prix.setSelectedIndices(indexes);
		}
		this.prix.fill(infos.getFifth());
		
		// Remplir les propriétés
		ArrayList<InfosModelPropriete> proprietesDisponibles = ContestOrg.get().getCtrlParticipants().getListeProprietes();
		for(int i=0;i<proprietesDisponibles.size();i++) {
			for(int j=0;j<infos.getFourth().size();j++) {
				if(proprietesDisponibles.get(i).getNom().equals(infos.getFourth().get(j).getFirst())) {
					this.jtfs_proprietes[i].setText(infos.getFourth().get(j).getSecond().getValue());
				}
			}
		}
		this.proprietesParticipant.fill(infos.getFourth());
	}

	// Implémentation de checkNomParticipant
	@Override
	protected boolean checkNomParticipant () {
		// Récupérer le nouveau nom du participant
		String nouveauNomParticipant = this.jtf_nom.getText().trim();
		
		// Retourner true si le participant n'a pas changer de nom ou s'il n'y a pas de participant qui a le même nom
		return this.ancienNomParticipant.equals(nouveauNomParticipant) || !ContestOrg.get().getCtrlParticipants().isParticipantExiste(this.jtf_nom.getText().trim());
	}
	
}
