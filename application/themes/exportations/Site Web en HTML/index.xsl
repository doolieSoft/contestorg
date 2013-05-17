<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:java="http://xml.apache.org/xslt/java" exclude-result-prefixes="java">
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
					<xsl:call-template name="html-entete" />
					
					<!-- Menu -->
					<xsl:call-template name="menu" />
					
					<!-- Liste des catégories -->
					<xsl:apply-templates select="/concours/listeCategories">
					</xsl:apply-templates>
					
					<!-- Footer -->
					<xsl:call-template name="html-pied" />
				</div>
			</body>
		</html>
	</xsl:template>
	
	<!-- Template d'une liste de catégories -->
	<xsl:template match="listeCategories">
		<!-- Choix de la catégorie -->
		<xsl:if test="count(./categorie[count(./listePoules/poule/listeParticipants/participant) > 0]) > 1">
			<div class="buttons-categories">
				<xsl:for-each select="./categorie[count(./listePoules/poule/listeParticipants/participant) > 0]">
					<a href="#" onclick="$(this).parent().children().removeAttr('class');$(this).attr('class','selected');show('categorie-{./@id}');return false;"><xsl:value-of select="./@nom" /></a>
				</xsl:for-each>
			</div>
		</xsl:if>
		
		<!-- Liste des catégories -->
		<div>
			<xsl:apply-templates select="./categorie[count(./listePoules/poule/listeParticipants/participant) > 0]">
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
						<!-- Participants -->
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
								<xsl:apply-templates select="./phaseEliminatoire[position() = 1]" mode="diagramme-participants">
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
		
		<!-- Liste des participants -->
		<div class="bloc">
			<ul>
				<xsl:apply-templates select="./listeClassementParticipants/classementParticipant">
			  		<xsl:sort select="@rang" data-type="number" />
				</xsl:apply-templates>
			</ul>
		</div>
	</xsl:template>
		
	<!-- Template d'une phase éliminatoire -->
	<xsl:template match="phaseEliminatoire" mode="diagramme-participants">
		<xsl:apply-templates select="./matchPhaseEliminatoire" mode="diagramme-participants">
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
	<xsl:template match="matchPhaseEliminatoire" mode="diagramme-participants">
		<tr>
			<td class="participantPhasesEliminatoires">
				<!-- Participant A -->
				<xsl:call-template name="participant">
				  <xsl:with-param name="id" select="./participation[1]/@refParticipant" />
				</xsl:call-template>
			</td>
		</tr>
		<tr>
			<td class="videPhasesEliminatoires"></td>
		</tr>
		<tr>
			<td class="participantPhasesEliminatoires">
				<!-- Participant B -->
				<xsl:call-template name="participant">
				  <xsl:with-param name="id" select="./participation[2]/@refParticipant" />
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
						<xsl:when test="count(./participation[@resultat = 'victoire']/@refParticipant) = 1">
							<xsl:call-template name="participant">
							  <xsl:with-param name="id" select="./participation[@resultat = 'victoire']/@refParticipant" />
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
		<xsl:variable name="timestamp" select="@timestamp"></xsl:variable>
		<xsl:variable name="refEmplacement" select="@refEmplacement"></xsl:variable>
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
					<!-- Participant -->
					<xsl:call-template name="participant">
					  <xsl:with-param name="id" select="./participation[1]/@refParticipant" />
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
					
					<!-- Participant -->
					<xsl:call-template name="participant">
					  <xsl:with-param name="id" select="./participation[2]/@refParticipant" />
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					...
				</xsl:otherwise>
			</xsl:choose>
			
			<!-- Date/Emplacement -->
			<xsl:if test="$timestamp != '' or $refEmplacement != ''">
				<p class="comment" style="margin-top:0px;">
					Match joué
				
					<!-- Date -->
					<xsl:if test="$timestamp != ''">
						le
						<xsl:value-of select="java:format(java:java.text.SimpleDateFormat.new('dd/MM/yyyy'),java:java.util.Date.new($timestamp*1000))" />
						à
						<xsl:value-of select="java:format(java:java.text.SimpleDateFormat.new('HH:mm'),java:java.util.Date.new($timestamp*1000))" /> 
					</xsl:if>
				
					<!-- Emplacement -->
					<xsl:if test="$refEmplacement != ''">
						sur l'emplacement "<xsl:value-of select="//emplacement[@id=$refEmplacement]/@nom"></xsl:value-of>" du lieu "<xsl:value-of select="//emplacement[@id=$refEmplacement]/../../@nom"></xsl:value-of>"
					</xsl:if>
				</p>
			</xsl:if>
		</li>
	</xsl:template>
	
	<!-- Template d'une liste de poules -->
	<xsl:template match="listePoules">
		<!-- Choix de la poule -->
		<xsl:if test="count(./poule[count(./listeParticipants/participant) > 0]) > 1">
			<div class="buttons-poules">
				<xsl:for-each select="./poule[count(./listeParticipants/participant) > 0]">
					<a href="#" onclick="$(this).parent().children().removeAttr('class');$(this).attr('class','selected');show('poule-{./@id}');return false;"><xsl:value-of select="./@nom" /></a>
				</xsl:for-each>
			</div>
		</xsl:if>
		
		<!-- Liste des poules -->
		<div>
			<xsl:apply-templates select="./poule[count(./listeParticipants/participant) > 0]">
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
			
			<!-- Participants -->
			<xsl:if test="count(./listeParticipants/participant) != 0">	
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
				
				<!-- Participants -->
				<xsl:apply-templates select="./listeParticipants/participant">
				  <xsl:sort select="@nom" />
				</xsl:apply-templates>
			</xsl:if>
		</div>
	</xsl:template>
	
	<!-- Template d'un classement de phase qualificative -->
	<xsl:template match="classementPoulePhasesQualificatives">
		<!-- Titre -->
		<h3>Classement</h3>
		
		<!-- Liste des participants -->
		<div class="bloc">
			<ul>
				<xsl:apply-templates select="./listeClassementParticipants/classementParticipant">
			  		<xsl:sort select="@rang" data-type="number" />
				</xsl:apply-templates>
			</ul>
		</div>
	</xsl:template>
	
	<!-- Template d'un participant dans un classement -->
	<xsl:template match="classementParticipant">
		<li>
			<!-- Rang du participant -->
			<xsl:value-of select="./@rang" /> -
		
			<!-- Nom du participant -->
			<xsl:call-template name="participant">
			  <xsl:with-param name="id" select="./@refParticipant" />
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
		<xsl:variable name="timestamp" select="@timestamp"></xsl:variable>
		<xsl:variable name="refEmplacement" select="@refEmplacement"></xsl:variable>
		<li>		
			<!-- Participant A -->
			<xsl:call-template name="participant">
			  <xsl:with-param name="id" select="./participation[1]/@refParticipant" />
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
				
			<!-- Participant B -->
			<xsl:call-template name="participant">
			  <xsl:with-param name="id" select="./participation[2]/@refParticipant" />
			</xsl:call-template>
			
			<!-- Date/Emplacement -->
			<xsl:if test="$timestamp != '' or $refEmplacement != ''">
				<p class="comment" style="margin-top:0px;">
					Match joué
				
					<!-- Date -->
					<xsl:if test="$timestamp != ''">
						le
						<xsl:value-of select="java:format(java:java.text.SimpleDateFormat.new('dd/MM/yyyy'),java:java.util.Date.new($timestamp*1000))" />
						à
						<xsl:value-of select="java:format(java:java.text.SimpleDateFormat.new('HH:mm'),java:java.util.Date.new($timestamp*1000))" /> 
					</xsl:if>
				
					<!-- Emplacement -->
					<xsl:if test="$refEmplacement != ''">
						sur l'emplacement "<xsl:value-of select="//emplacement[@id=$refEmplacement]/@nom"></xsl:value-of>" du lieu "<xsl:value-of select="//emplacement[@id=$refEmplacement]/../../@nom"></xsl:value-of>"
					</xsl:if>
				</p>
			</xsl:if>
		</li>
	</xsl:template>
	
	<!-- Template d'un participant -->
	<xsl:template name="participant">
		<xsl:param name="id" />
		<xsl:choose>
			<xsl:when test="$id != ''">
				<a href="#participant-{$id}" title="Se rendre sur la fiche du participant">
					<xsl:value-of select="//participant[@id=$id]/@nom" />
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
	<xsl:template match="participant">
		<!-- Ancre -->
		<a name="participant-{./@id}" />
	
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
			<xsl:call-template name="html-participant-statut">
			  <xsl:with-param name="id" select="./@id" />
			</xsl:call-template>
			<br/>
			
			<!-- Stand -->
			<xsl:if test="./@stand != ''">
				<b>Stand :</b>&#160;<xsl:value-of select="./@stand" />
				<br/>
			</xsl:if>
			
			<!-- Details -->
			<xsl:if test="./@details != ''">
				<b>Stand :</b>&#160;<xsl:value-of select="./@details" />
				<br/>
			</xsl:if>
			
			<!-- Participations -->
			<xsl:call-template name="participationsPhasesQualificatives">
			  <xsl:with-param name="id" select="./@id" />
			</xsl:call-template>
			<xsl:call-template name="participationsPhasesEliminatoires">
			  <xsl:with-param name="id" select="./@id" />
			</xsl:call-template>
		</div>
	</xsl:template>
	
	<!-- Templates des participations d'un participant -->
	<xsl:template name="participationsPhasesQualificatives">
		<xsl:param name="id" />
		<xsl:choose>
			<xsl:when test="count(//matchPhaseQualificative[participation/@refParticipant = $id]) != 0">
				<b>Participations aux phases qualificatives:</b>
				<ul>
					<xsl:apply-templates select="//matchPhaseQualificative[participation/@refParticipant = $id]">
					</xsl:apply-templates>
				</ul>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="participationsPhasesEliminatoires">
		<xsl:param name="id" />
		<xsl:choose>
			<xsl:when test="count(//matchPhaseEliminatoire[participation/@refParticipant = $id]) != 0">
				<b>Participations aux phases éliminatoires :</b>
				<ul>
					<xsl:apply-templates select="//matchPhaseEliminatoire[participation/@refParticipant = $id]" mode="liste">
					</xsl:apply-templates>
				</ul>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	
</xsl:stylesheet>
