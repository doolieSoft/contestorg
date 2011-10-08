<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- Importation des templates communs -->
	<xsl:import href="../../common.xsl"/>
	<xsl:import href="../common.xsl"/>
	<xsl:import href="common.xsl"/>
	
	<!-- Cible HTML -->
	<xsl:output method="html" encoding="utf-8" />
  
	<!-- Template principal -->
	<xsl:template match="/">
		<html>
			<!-- Head -->
			<xsl:call-template name="head" />
			
			<!-- Body -->
			<body>
				<!-- Page -->
				<div id="page">
					<!-- Header -->
					<xsl:call-template name="header" />
					
					<!-- Menu -->
					<xsl:call-template name="menu" />
					
					<!-- Liste des catégories -->
					<xsl:apply-templates select="/concours/listeCategories">
					</xsl:apply-templates>
					
					<!-- Footer -->
					<xsl:call-template name="footer" />
				</div>
			</body>
		</html>
	</xsl:template>
	
	<!-- Template d'une liste de catégories -->
	<xsl:template match="listeCategories">
		<!-- Choix de la catégorie -->
		<xsl:if test="count(./categorie[count(./listePoules/poule/listeEquipes/equipe) > 0]) > 1">
			<div class="buttons-categories">
				<xsl:for-each select="./categorie[count(./listePoules/poule/listeEquipes/equipe) > 0]">
					<a href="#" onclick="$(this).parent().children().removeAttr('class');$(this).attr('class','selected');show('categorie-{./@id}');return false;"><xsl:value-of select="./@nom" /></a>
				</xsl:for-each>
			</div>
		</xsl:if>
		
		<!-- Liste des catégories -->
		<div>
			<xsl:apply-templates select="./categorie[count(./listePoules/poule/listeEquipes/equipe) > 0]">
			</xsl:apply-templates>
		</div>
	</xsl:template>
	
	<!-- Template d'une catégorie -->
	<xsl:template match="categorie">
		<div id="categorie-{./@id}" class="categorie">
			<!-- Phases éliminatoires -->
			<xsl:apply-templates select="./listePhasesEliminatoires">
			</xsl:apply-templates>
			
			<!-- Liste des poules -->
			<xsl:apply-templates select="./listePoules">
			</xsl:apply-templates>
		</div>
	</xsl:template>
	
	<!-- Template de phases éliminatoires -->
	<xsl:template match="listePhasesEliminatoires">
		<!-- Titre -->
		<h2>Phases éliminatoires</h2>
				
		<!-- Classement -->
		<xsl:apply-templates select="../classementCategoriePhasesEliminatoires">
		</xsl:apply-templates>
		
		<!-- Phases éliminatoires sous forme de diagramme -->
		<h3>Diagramme</h3>
		<div class="bloc">
			<table width="100%">
				<thead>
					<tr>
						<th width="{100 div (count(./phaseEliminatoire)+1)}%">
							<xsl:choose>
							<xsl:when test="/concours/@participants = 'equipes'">
								Equipes
							</xsl:when>
							<xsl:otherwise>
								Joueurs
							</xsl:otherwise>
						</xsl:choose>
						</th>
						<!-- Equipes -->
						<xsl:for-each select="./phaseEliminatoire">
							<xsl:sort select="@numero" data-type="number" />
							<th width="{100 div (count(../phaseEliminatoire)+1)}%">Phase <xsl:value-of select="./@numero+1" /></th>
						</xsl:for-each>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>
							<table width="100%">
								<xsl:apply-templates select="./phaseEliminatoire[position() = 1]" mode="diagramme-equipes">
									<xsl:sort select="@numero" data-type="number" />
									<xsl:with-param name="nbCellulesA">1</xsl:with-param>
									<xsl:with-param name="nbCellulesB">3</xsl:with-param>
								</xsl:apply-templates>
							</table>
						</td>
						<xsl:apply-templates select="./phaseEliminatoire[position() = 1]" mode="diagramme-matchs">
							<xsl:sort select="@numero" data-type="number" />
							<xsl:with-param name="nbCellulesA">1</xsl:with-param>
							<xsl:with-param name="nbCellulesB">3</xsl:with-param>
						</xsl:apply-templates>
					</tr>
				</tbody>
			</table>
		</div>
		
		<!-- Phases éliminatoires sous forme de liste -->
		<xsl:apply-templates select="./phaseEliminatoire" mode="liste">
			  <xsl:sort select="@numero" data-type="number" />
		</xsl:apply-templates>
	</xsl:template>
	
	<!-- Template des classement de phases qualificatives -->
	<xsl:template match="classementCategoriePhasesEliminatoires">
		<!-- Titre -->
		<h3>Classement</h3>
		
		<!-- Liste des équipes -->
		<div class="bloc">
			<ul>
				<xsl:apply-templates select="./listeClassementEquipes/classementEquipe">
			  		<xsl:sort select="@rang" data-type="number" />
				</xsl:apply-templates>
			</ul>
		</div>
	</xsl:template>
		
	<!-- Template d'une phase éliminatoire -->
	<xsl:template match="phaseEliminatoire" mode="diagramme-equipes">
		<xsl:apply-templates select="./matchPhaseEliminatoire" mode="diagramme-equipes">
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template match="phaseEliminatoire" mode="diagramme-matchs">
		<!-- Paramètres -->
		<xsl:param name="nbCellulesA" /> <!-- Nombre de cases vides avant et après les cases pleines -->
		<xsl:param name="nbCellulesB" /> <!-- Nombre de cases vides entre chaque case pleine -->
		
		<!-- Cellule de la phase éliminatoire -->
		<td>
			<table width="100%">
				<!-- Cellules d'espacement du haut -->
				<xsl:call-template name="loop">
					<xsl:with-param name="n"><xsl:value-of select="$nbCellulesA" /></xsl:with-param>
					<xsl:with-param name="contenu"><tr><td class="videPhasesEliminatoires"></td></tr></xsl:with-param>
				</xsl:call-template>

				<!--  Cellules des matchs -->
				<xsl:apply-templates select="./matchPhaseEliminatoire" mode="diagramme-matchs">
					<xsl:with-param name="nbCellules"><xsl:value-of select="$nbCellulesB" /></xsl:with-param>
				</xsl:apply-templates>
				
				<!-- Cellules d'espacement du bas -->
				<xsl:call-template name="loop">
					<xsl:with-param name="n"><xsl:value-of select="$nbCellulesA" /></xsl:with-param>
					<xsl:with-param name="contenu"><tr><td class="videPhasesEliminatoires"></td></tr></xsl:with-param>
				</xsl:call-template>
			</table>
		</td>
		
		<!-- Appeller la prochaine phase éliminatoire -->
		<xsl:variable name="numero"><xsl:value-of select="./@numero" /></xsl:variable>
		<xsl:apply-templates select="../phaseEliminatoire[@numero = $numero+1]" mode="diagramme-matchs">
			<xsl:sort select="@numero" data-type="number" />
			<xsl:with-param name="nbCellulesA"><xsl:number value="$nbCellulesB" /></xsl:with-param>
			<xsl:with-param name="nbCellulesB"><xsl:number value="number($nbCellulesB)*2+1" /></xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template match="phaseEliminatoire" mode="liste">
		<!-- Titre -->
		<h3>Phase éliminatoire <xsl:value-of select="./@numero+1" /></h3>
		
		<!-- Liste des matchs -->
		<div class="bloc">
			<ul>
				<xsl:apply-templates select="./matchPhaseEliminatoire" mode="liste">
				</xsl:apply-templates>
			</ul>
		</div>
	</xsl:template>
	
	<!-- Template d'un match de phase éliminatoire -->
	<xsl:template match="matchPhaseEliminatoire" mode="diagramme-equipes">
		<tr>
			<td class="equipePhasesEliminatoires">
				<!-- Equipe A -->
				<xsl:call-template name="equipe">
				  <xsl:with-param name="id" select="./participation[1]/@refEquipe" />
				</xsl:call-template>
			</td>
		</tr>
		<tr>
			<td class="videPhasesEliminatoires"></td>
		</tr>
		<tr>
			<td class="equipePhasesEliminatoires">
				<!-- Equipe B -->
				<xsl:call-template name="equipe">
				  <xsl:with-param name="id" select="./participation[2]/@refEquipe" />
				</xsl:call-template>
			</td>
		</tr>
		<xsl:if test="position() != last()">
			<tr><td class="videPhasesEliminatoires"></td></tr>
		</xsl:if>
	</xsl:template>
	<xsl:template match="matchPhaseEliminatoire" mode="diagramme-matchs">
		<!-- Paramètres -->
		<xsl:param name="nbCellules" />
		
		<!-- Vérifier s'il ne s'agit pas de la petite finale -->
		<xsl:if test="count(./@petiteFinale) = 0 or ./@petiteFinale != 'oui'">
			<!-- Cellules d'espacement -->
			<xsl:if test="position() != 1">
				<xsl:call-template name="loop">
					<xsl:with-param name="n"><xsl:value-of select="$nbCellules" /></xsl:with-param>
					<xsl:with-param name="contenu"><tr><td class="videPhasesEliminatoires"></td></tr></xsl:with-param>
				</xsl:call-template>
			</xsl:if>
			
			<!-- Cellule du match -->
			<tr>
				<td class="matchPhasesEliminatoires">
					<xsl:choose>
						<xsl:when test="count(./participation[@resultat = 'victoire']/@refEquipe) = 1">
							<xsl:call-template name="equipe">
							  <xsl:with-param name="id" select="./participation[@resultat = 'victoire']/@refEquipe" />
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>
							...
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<xsl:template match="matchPhaseEliminatoire" mode="liste">
		<li>
			<!-- Grande finale ? Petite finale ? -->
			<xsl:if test="./@grandeFinale = 'oui'">
				Grande finale :
			</xsl:if>
			<xsl:if test="./@petiteFinale = 'oui'">
				Petite finale :
			</xsl:if>
			
			<!-- Participation A -->
			<xsl:choose>
				<xsl:when test="count(./participation) > 0">
					<!-- Equipe -->
					<xsl:call-template name="equipe">
					  <xsl:with-param name="id" select="./participation[1]/@refEquipe" />
					</xsl:call-template>
					
					-
					
					<!-- Résultat -->
					<xsl:call-template name="html-participation-resultat">
					  <xsl:with-param name="id" select="./participation[1]/@id" />
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					...
				</xsl:otherwise>
			</xsl:choose>
			/
			<!-- Participation B -->
			<xsl:choose>
				<xsl:when test="count(./participation) > 1">	
					<!-- Résultat -->
					<xsl:call-template name="html-participation-resultat">
					  <xsl:with-param name="id" select="./participation[2]/@id" />
					</xsl:call-template>
					
					-
					
					<!-- Equipe -->
					<xsl:call-template name="equipe">
					  <xsl:with-param name="id" select="./participation[2]/@refEquipe" />
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					...
				</xsl:otherwise>
			</xsl:choose>
		</li>
	</xsl:template>
	
	<!-- Template d'une liste de poules -->
	<xsl:template match="listePoules">
		<!-- Choix de la poule -->
		<xsl:if test="count(./poule[count(./listeEquipes/equipe) > 0]) > 1">
			<div class="buttons-poules">
				<xsl:for-each select="./poule[count(./listeEquipes/equipe) > 0]">
					<a href="#" onclick="$(this).parent().children().removeAttr('class');$(this).attr('class','selected');show('poule-{./@id}');return false;"><xsl:value-of select="./@nom" /></a>
				</xsl:for-each>
			</div>
		</xsl:if>
		
		<!-- Liste des poules -->
		<div>
			<xsl:apply-templates select="./poule[count(./listeEquipes/equipe) > 0]">
			</xsl:apply-templates>
		</div>
	</xsl:template>
	
	<!-- Template d'une poule -->
	<xsl:template match="poule">
		<div id="poule-{./@id}" class="poule">
			<!-- Phases qualificatives -->
			<xsl:if test="count(./listePhasesQualificatives/phaseQualificative) != 0">
				<!-- Titre -->
				<h2>Phases qualificatives</h2>
				
				<!-- Classement -->
				<xsl:apply-templates select="./classementPoulePhasesQualificatives">
				</xsl:apply-templates>
				
				<!-- Phases qualificatives -->
				<xsl:apply-templates select="./listePhasesQualificatives/phaseQualificative">
				  <xsl:sort select="@numero" data-type="number" />
				</xsl:apply-templates>
			</xsl:if>
			
			<!-- Equipes -->
			<xsl:if test="count(./listeEquipes/equipe) != 0">	
				<!-- Titre -->
				<h2>
					<xsl:choose>
						<xsl:when test="/concours/@participants = 'equipes'">
							Equipes
						</xsl:when>
						<xsl:otherwise>
							Joueurs
						</xsl:otherwise>
					</xsl:choose>
				</h2>
				
				<!-- Equipes -->
				<xsl:apply-templates select="./listeEquipes/equipe">
				  <xsl:sort select="@nom" />
				</xsl:apply-templates>
			</xsl:if>
		</div>
	</xsl:template>
	
	<!-- Template d'un classement de phase qualificative -->
	<xsl:template match="classementPoulePhasesQualificatives">
		<!-- Titre -->
		<h3>Classement</h3>
		
		<!-- Liste des équipes -->
		<div class="bloc">
			<ul>
				<xsl:apply-templates select="./listeClassementEquipes/classementEquipe">
			  		<xsl:sort select="@rang" data-type="number" />
				</xsl:apply-templates>
			</ul>
		</div>
	</xsl:template>
	
	<!-- Template d'une équipe dans un classement -->
	<xsl:template match="classementEquipe">
		<li>
			<!-- Rang de l'équipe -->
			<xsl:value-of select="./@rang" /> -
		
			<!-- Nom de l'équipe -->
			<xsl:call-template name="equipe">
			  <xsl:with-param name="id" select="./@refEquipe" />
			</xsl:call-template>
		</li>
	</xsl:template>
	
	<!-- Template d'une phase qualificative -->
	<xsl:template match="phaseQualificative">
		<!-- Titre -->
		<h3>Phase qualificative <xsl:value-of select="./@numero+1" /></h3>
		
		<!-- Liste des matchs -->
		<div class="bloc">
			<ul>
				<xsl:apply-templates select="./matchPhaseQualificative">
				</xsl:apply-templates>
			</ul>
		</div>
	</xsl:template>
	
	<!-- Template d'un match de phase qualificative -->
	<xsl:template match="matchPhaseQualificative">
		<li>		
			<!-- Equipe A -->
			<xsl:call-template name="equipe">
			  <xsl:with-param name="id" select="./participation[1]/@refEquipe" />
			</xsl:call-template>
			
			
			-
			
			<!-- Résultat A -->
			<xsl:call-template name="html-participation-resultat">
			  <xsl:with-param name="id" select="./participation[1]/@id" />
			</xsl:call-template>
			
			/
			
			<!-- Résultat B -->
			<xsl:call-template name="html-participation-resultat">
			  <xsl:with-param name="id" select="./participation[2]/@id" />
			</xsl:call-template>
			
			-
				
			<!-- Equipe B -->
			<xsl:call-template name="equipe">
			  <xsl:with-param name="id" select="./participation[2]/@refEquipe" />
			</xsl:call-template>
		</li>
	</xsl:template>
	
	<!-- Template d'une équipe -->
	<xsl:template name="equipe">
		<xsl:param name="id" />
		<xsl:choose>
			<xsl:when test="$id != ''">
				<a href="#equipe-{$id}" title="Se rendre sur la fiche du participant">
					<xsl:value-of select="//equipe[@id=$id]/@nom" />
				</a>
			</xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="/concours/@participants = 'equipes'">
						Equipe fantôme
					</xsl:when>
					<xsl:otherwise>
						Joueur fantôme
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="equipe">
		<!-- Ancre -->
		<a name="equipe-{./@id}" />
	
		<!-- Nom -->
		<h3>
			<xsl:value-of select="./@nom" />
			<xsl:if test="./@ville != ''">
				(<xsl:value-of select="./@ville" />)
			</xsl:if>
		</h3>
		
		<!-- Données -->
		<div class="bloc">
			<!-- Statut -->
			<b>Statut :</b>
			<xsl:call-template name="html-equipe-statut">
			  <xsl:with-param name="id" select="./@id" />
			</xsl:call-template>
			<br/>
			
			<!-- Stand -->
			<xsl:if test="./@stand != ''">
				<b>Stand :</b>&#160;<xsl:value-of select="./@stand" />
				<br/>
			</xsl:if>
			
			<!-- Membre -->
			<xsl:if test="./@membres != ''">
				<b>Membres :</b>&#160;<xsl:value-of select="./@membres" />
				<br/>
			</xsl:if>
			
			<!-- Details -->
			<xsl:if test="./@details != ''">
				<b>Stand :</b>&#160;<xsl:value-of select="./@details" />
				<br/>
			</xsl:if>
			
			<!-- Participations -->
			<b>Participations :</b>
			<xsl:call-template name="participations">
			  <xsl:with-param name="id" select="./@id" />
			</xsl:call-template>
		</div>
	</xsl:template>
	
	<!-- Template des participations d'une équipe -->
	<xsl:template name="participations">
		<xsl:param name="id" />
		<xsl:choose>
			<xsl:when test="count(//participation[@refEquipe = $id]) != 0">
				<ul>
					<xsl:apply-templates select="//matchPhaseQualificative[participation/@refEquipe = $id]">
					</xsl:apply-templates>
				</ul>
			</xsl:when>
			<xsl:otherwise>
				<blockquote>Pas encore de participations.</blockquote>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
</xsl:stylesheet>
