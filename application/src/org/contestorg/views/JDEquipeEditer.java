package org.contestorg.views;


import java.awt.Window;
import java.util.ArrayList;

import org.contestorg.common.Pair;
import org.contestorg.common.Quintuple;
import org.contestorg.common.TrackableList;
import org.contestorg.controlers.ContestOrg;
import org.contestorg.infos.InfosModelEquipe;
import org.contestorg.infos.InfosModelPrix;
import org.contestorg.infos.InfosModelPropriete;
import org.contestorg.infos.InfosModelProprieteEquipe;
import org.contestorg.interfaces.ICollector;



@SuppressWarnings("serial")
public class JDEquipeEditer extends JDEquipeAbstract
{
	
	// Ancien nom de l'équipe
	private String ancienNomEquipe;

	// Constructeur
	public JDEquipeEditer(Window w_parent, ICollector<Quintuple<String,String,InfosModelEquipe, TrackableList<Pair<String, InfosModelProprieteEquipe>>, TrackableList<String>>> collector, Quintuple<String,String,InfosModelEquipe,ArrayList<Pair<String,InfosModelProprieteEquipe>>,ArrayList<String>> infos) {
		// Appeller le constructeur du parent
		super(w_parent, "Editer une équipe", collector);
		
		// Retenir l'ancien nom de l'équipe
		this.ancienNomEquipe = infos.getThird().getNom();
		
		// Remplir les entrées avec les informations de l'équipe
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
			ArrayList<InfosModelPrix> prixDisponibles = ContestOrg.get().getCtrlEquipes().getListePrix();
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
		ArrayList<InfosModelPropriete> proprietesDisponibles = ContestOrg.get().getCtrlEquipes().getListeProprietes();
		for(int i=0;i<proprietesDisponibles.size();i++) {
			for(int j=0;j<infos.getFourth().size();j++) {
				if(proprietesDisponibles.get(i).getNom().equals(infos.getFourth().get(j).getFirst())) {
					this.jtfs_proprietes[i].setText(infos.getFourth().get(j).getSecond().getValue());
				}
			}
		}
		this.proprietesEquipe.fill(infos.getFourth());
	}

	// Implémentation de checkNomEquipe
	@Override
	protected boolean checkNomEquipe () {
		// Récupérer le nouveau nom de l'équipe
		String nouveauNomEquipe = this.jtf_nom.getText().trim();
		
		// Retourner true si l'équipe n'a pas changer de nom ou s'il n'y a pas d'équipe qui a le meme nom
		return this.ancienNomEquipe.equals(nouveauNomEquipe) || !ContestOrg.get().getCtrlEquipes().isEquipeExiste(this.jtf_nom.getText().trim());
	}
	
}
