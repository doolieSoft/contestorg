<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<!-- Importation des templates communs -->
	<xsl:import href="../../common.xsl"/>
	<xsl:import href="../common.xsl"/>
	
	<!-- Cible XML (XSL-FO) -->
	<xsl:output method="xml" indent="yes" />
	
	<!-- Paramètres-->
	<xsl:param name="idCategorie" />
	<xsl:param name="idPoule" />

	<!-- Template principal -->
	<xsl:template match="/">
		<fo:root>
			<!-- Déclaration de la mise en page -->
			<fo:layout-master-set>
				<fo:simple-page-master master-name="page" margin="1cm" page-height="21cm" page-width="29.7cm">
					<fo:region-body margin="1cm" />
					<fo:region-before extent="1cm" />
					<fo:region-after extent="1cm" />
				</fo:simple-page-master>
			</fo:layout-master-set>

			<!-- Corps du document -->
			<fo:page-sequence master-reference="page">
				<!-- Entete -->
				<fo:static-content flow-name="xsl-region-before">
					<xsl:call-template name="pdf-entete">
					  <xsl:with-param name="titre">
							<xsl:choose>
								<xsl:when test="/concours/@participants = 'equipes'">
									Classement détaillé des équipes aux phases qualificatives
								</xsl:when>
								<xsl:otherwise>
									Classement détaillé des joueurs aux phases qualificatives
								</xsl:otherwise>
							</xsl:choose>
						</xsl:with-param>
					</xsl:call-template>
				</fo:static-content>

				<!-- Pied de page -->
				<fo:static-content flow-name="xsl-region-after">
					<xsl:call-template name="pdf-pied">
					</xsl:call-template>
				</fo:static-content>

				<!-- Contenu -->
				<fo:flow flow-name="xsl-region-body">
					<fo:block font-size="10pt">
						<!-- Liste des catégories -->
						<xsl:apply-templates select="/concours/listeCategories">
						</xsl:apply-templates>
					</fo:block>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
	
	<!-- Template d'une liste de catégories -->
	<xsl:template match="listeCategories">
		<xsl:choose>
			<!-- Catégorie non indiquée -->
			<xsl:when test="$idCategorie = ''">
				<xsl:apply-templates select="./categorie">
				</xsl:apply-templates>
			</xsl:when>
			<!-- Catégorie existante -->
			<xsl:when test="count(./categorie[@id=$idCategorie]) = 1">
				<xsl:apply-templates select="./categorie[@id=$idCategorie]">
				</xsl:apply-templates>
			</xsl:when>
			<!-- Catégorie inexistante -->
			<xsl:otherwise>
				<xsl:call-template name="pdf-erreur">
				  <xsl:with-param name="erreur">La catégorie indiquée n'existe pas.</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- Template d'une catégorie -->
	<xsl:template match="categorie">
		<xsl:if test="count(./listePoules/poule/listeParticipants/participant) != 0">
			<!-- Titre -->
			<xsl:if test="count(../categorie) != 1">
				<xsl:call-template name="pdf-titre-h1">
				  <xsl:with-param name="titre"><xsl:value-of select="./@nom" /></xsl:with-param>
				</xsl:call-template>
			</xsl:if>
			
			<!-- Liste des poules -->
			<xsl:apply-templates select="./listePoules">
			</xsl:apply-templates>
		</xsl:if>
	</xsl:template>
	
	<!-- Template d'une liste de poules -->
	<xsl:template match="listePoules">
		<xsl:choose>
			<!-- Poule non indiquée -->
			<xsl:when test="$idPoule = ''">
				<xsl:apply-templates select="./poule">
				</xsl:apply-templates>
			</xsl:when>
			<!-- Poule existante -->
			<xsl:when test="count(./poule[@id=$idPoule]) = 1">
				<xsl:apply-templates select="./poule[@id=$idPoule]">
				</xsl:apply-templates>
			</xsl:when>
			<!-- Poule inexistante -->
			<xsl:otherwise>
				<xsl:call-template name="pdf-erreur">
				  <xsl:with-param name="erreur">La poule indiquée n'existe pas.</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- Template d'une poule -->
	<xsl:template match="poule">
		<xsl:if test="count(./listeParticipants/participant) != 0">
			<!-- Titre -->
			<xsl:if test="count(../poule) != 1">
				<xsl:call-template name="pdf-titre-h2">
				  <xsl:with-param name="titre"><xsl:value-of select="./@nom" /></xsl:with-param>
				</xsl:call-template>
			</xsl:if>
			
			<!-- Liste des participants -->
			<xsl:apply-templates select="./classementPoulePhasesQualificatives/listeClassementParticipants">
			</xsl:apply-templates>
		</xsl:if>
	</xsl:template>
	
	<!-- Template d'un classement de participants -->
	<xsl:template match="listeClassementParticipants">
		<fo:block>
			<fo:table width="100%" border="0.5pt solid black">
				<fo:table-column column-width="50pt" />
				<fo:table-column />
				<xsl:for-each select="/concours/listeCriteres/critere">
					<xsl:if test="count(./critereNbPoints) = 1"><fo:table-column /></xsl:if>
					<xsl:if test="count(./critereNbVictoires) = 1"><fo:table-column /></xsl:if>
					<xsl:if test="count(./critereNbEgalites) = 1"><fo:table-column /></xsl:if>
					<xsl:if test="count(./critereNbDefaites) = 1"><fo:table-column /></xsl:if>
					<xsl:if test="count(./critereNbForfaits) = 1"><fo:table-column /></xsl:if>
					<xsl:if test="count(./critereQuantiteObjectif) = 1"><fo:table-column /></xsl:if>
					<xsl:if test="count(./critereGoalAverage[@type = 'general']) = 1"><fo:table-column /></xsl:if>
				</xsl:for-each>
				<fo:table-header>
					<fo:table-row>
						<fo:table-cell border="0.5pt solid black" background-color="#CCCCCC" padding="2mm">
							<fo:block font-weight="bold">Rang</fo:block>
						</fo:table-cell>
						<fo:table-cell border="0.5pt solid black" background-color="#CCCCCC" padding="2mm">
							<fo:block font-weight="bold">
								<xsl:choose>
									<xsl:when test="/concours/@participants = 'equipes'">
										Equipe
									</xsl:when>
									<xsl:otherwise>
										Joueur
									</xsl:otherwise>
								</xsl:choose>
							</fo:block>
						</fo:table-cell>
						<xsl:for-each select="/concours/listeCriteres/critere">
							<xsl:if test="count(./critereNbPoints) = 1">
								<fo:table-cell border="0.5pt solid black" background-color="#CCCCCC" padding="2mm"><fo:block font-weight="bold">Points</fo:block></fo:table-cell>
							</xsl:if>
							<xsl:if test="count(./critereNbVictoires) = 1">
								<fo:table-cell border="0.5pt solid black" background-color="#CCCCCC" padding="2mm"><fo:block font-weight="bold">Victoires</fo:block></fo:table-cell>
							</xsl:if>
							<xsl:if test="count(./critereNbEgalites) = 1">
								<fo:table-cell border="0.5pt solid black" background-color="#CCCCCC" padding="2mm"><fo:block font-weight="bold">Egalités</fo:block></fo:table-cell>
							</xsl:if>
							<xsl:if test="count(./critereNbDefaites) = 1">
								<fo:table-cell border="0.5pt solid black" background-color="#CCCCCC" padding="2mm"><fo:block font-weight="bold">Défaites</fo:block></fo:table-cell>
							</xsl:if>
							<xsl:if test="count(./critereNbForfaits) = 1">
								<fo:table-cell border="0.5pt solid black" background-color="#CCCCCC" padding="2mm"><fo:block font-weight="bold">Forfaits</fo:block></fo:table-cell>
							</xsl:if>
							<xsl:if test="count(./critereQuantiteObjectif) = 1">
								<xsl:variable name="refObjectif" select="./critereQuantiteObjectif/@refObjectif" />
								<fo:table-cell border="0.5pt solid black" background-color="#CCCCCC" padding="2mm"><fo:block font-weight="bold"><xsl:apply-templates select="/concours/listeObjectifs/objectif[@id = $refObjectif]" /></fo:block></fo:table-cell>
							</xsl:if>
							<xsl:if test="count(./critereGoalAverage[@type = 'general']) = 1">
								<fo:table-cell border="0.5pt solid black" background-color="#CCCCCC" padding="2mm">
									<fo:block font-weight="bold">
										Goal-av. gén.
										<xsl:choose>
											<xsl:when test="./critereGoalAverage/@methode = 'difference'">par diff.</xsl:when>
											<xsl:when test="./critereGoalAverage/@methode = 'division'">par div.</xsl:when>
										</xsl:choose>
										<xsl:choose>
											<xsl:when test="./critereGoalAverage/@donnee = 'points'"> (points)</xsl:when>
											<xsl:when test="./critereGoalAverage/@donnee = 'resultat'"> (victoires)</xsl:when>
											<xsl:when test="./critereGoalAverage/@donnee = 'quantiteObjectif'">
												<xsl:variable name="refObjectif" select="./critereGoalAverage/@refObjectif" />
												<xsl:apply-templates select="/concours/listeObjectifs/objectif[@id = $refObjectif]" mode="avecParentheses" />
											</xsl:when>
										</xsl:choose>
									</fo:block>
								</fo:table-cell>
							</xsl:if>
						</xsl:for-each>
					</fo:table-row>
				</fo:table-header>
				<fo:table-body>
					<xsl:apply-templates select="./classementParticipant">
						<xsl:sort select="@rang" data-type="number" />
					</xsl:apply-templates>
				</fo:table-body>
			</fo:table>
			<xsl:if test="count(/concours/listeCriteres/critere/critereGoalAverage[@type = 'particulier']) != 0 or count(/concours/listeCriteres/critere/critereRencontresDirectes) != 0">
				<fo:block font-style="italic" padding-top="10pt">
					Les critères de classement tenant compte des rencontres directes entre les participants ne sont pas affichés. 
				</fo:block>
			</xsl:if>
		</fo:block>
	</xsl:template>
	
	<!-- Template d'un objectif -->
	<xsl:template match="objectifPoints"><xsl:value-of select="./@nom" /></xsl:template>
	<xsl:template match="objectifPourcentage"><xsl:value-of select="./@nom" /></xsl:template>
	<xsl:template match="objectifNul"><xsl:value-of select="./@nom" /></xsl:template>
	<xsl:template match="objectifPoints" mode="avecParentheses">(<xsl:value-of select="./@nom" />)</xsl:template>
	<xsl:template match="objectifPourcentage" mode="avecParentheses">(<xsl:value-of select="./@nom" />)</xsl:template>
	<xsl:template match="objectifNul" mode="avecParentheses">(<xsl:value-of select="./@nom" />)</xsl:template>
	
	<!-- Template d'un participant dans un classement -->
	<xsl:template match="classementParticipant">
		<xsl:variable name="refParticipant" select="./@refParticipant" />
		<fo:table-row>
			<fo:table-cell border="0.5pt solid black" padding="2mm">
				<fo:block><xsl:value-of select="./@rang" /></fo:block>
			</fo:table-cell>
			<fo:table-cell border="0.5pt solid black" padding="2mm">
				<fo:block>
					<xsl:value-of select="//participant[@id=$refParticipant]/@nom" />
				</fo:block>
			</fo:table-cell>
			<xsl:for-each select="/concours/listeCriteres/critere">
				<xsl:if test="count(./critereNbPoints) = 1">
					<fo:table-cell border="0.5pt solid black" padding="2mm"><fo:block text-align="right">
						<xsl:call-template name="critereNbPoints">
						  <xsl:with-param name="refParticipant"><xsl:value-of select="$refParticipant" /></xsl:with-param>
						</xsl:call-template>
					</fo:block></fo:table-cell>
				</xsl:if>
				<xsl:if test="count(./critereNbVictoires) = 1">
					<fo:table-cell border="0.5pt solid black" padding="2mm"><fo:block text-align="right">
						<xsl:call-template name="critereNbVictoires">
						  <xsl:with-param name="refParticipant"><xsl:value-of select="$refParticipant" /></xsl:with-param>
						</xsl:call-template>
					</fo:block></fo:table-cell>
				</xsl:if>
				<xsl:if test="count(./critereNbEgalites) = 1">
					<fo:table-cell border="0.5pt solid black" padding="2mm"><fo:block text-align="right">
						<xsl:call-template name="critereNbEgalites">
						  <xsl:with-param name="refParticipant"><xsl:value-of select="$refParticipant" /></xsl:with-param>
						</xsl:call-template>
					</fo:block></fo:table-cell>
				</xsl:if>
				<xsl:if test="count(./critereNbDefaites) = 1">
					<fo:table-cell border="0.5pt solid black" padding="2mm"><fo:block text-align="right">
						<xsl:call-template name="critereNbDefaites">
						  <xsl:with-param name="refParticipant"><xsl:value-of select="$refParticipant" /></xsl:with-param>
						</xsl:call-template>
					</fo:block></fo:table-cell>
				</xsl:if>
				<xsl:if test="count(./critereNbForfaits) = 1">
					<fo:table-cell border="0.5pt solid black" padding="2mm"><fo:block text-align="right">
						<xsl:call-template name="critereNbForfaits">
						  <xsl:with-param name="refParticipant"><xsl:value-of select="$refParticipant" /></xsl:with-param>
						</xsl:call-template>
					</fo:block></fo:table-cell>
				</xsl:if>
				<xsl:if test="count(./critereQuantiteObjectif) = 1">
					<fo:table-cell border="0.5pt solid black" padding="2mm"><fo:block text-align="right">
						<xsl:call-template name="critereQuantiteObjectif">
						  <xsl:with-param name="refParticipant"><xsl:value-of select="$refParticipant" /></xsl:with-param>
						  <xsl:with-param name="refObjectif"><xsl:value-of select="./critereQuantiteObjectif/@refObjectif" /></xsl:with-param>
						</xsl:call-template>
					</fo:block></fo:table-cell>
				</xsl:if>
				<xsl:if test="count(./critereGoalAverage[@type = 'general'][@donnee = 'points']) = 1">
					<fo:table-cell border="0.5pt solid black" padding="2mm"><fo:block text-align="right">
						<xsl:call-template name="critereGoalAverageGeneralPoints">
						  <xsl:with-param name="refParticipant"><xsl:value-of select="$refParticipant" /></xsl:with-param>
						  <xsl:with-param name="methode"><xsl:value-of select="./critereGoalAverage/@methode" /></xsl:with-param>
						</xsl:call-template>
					</fo:block></fo:table-cell>
				</xsl:if>
				<xsl:if test="count(./critereGoalAverage[@type = 'general'][@donnee = 'resultat']) = 1">
					<fo:table-cell border="0.5pt solid black" padding="2mm"><fo:block text-align="right">
						<xsl:call-template name="critereGoalAverageGeneralResultat">
						  <xsl:with-param name="refParticipant"><xsl:value-of select="$refParticipant" /></xsl:with-param>
						  <xsl:with-param name="methode"><xsl:value-of select="./critereGoalAverage/@methode" /></xsl:with-param>
						</xsl:call-template>
					</fo:block></fo:table-cell>
				</xsl:if>
				<xsl:if test="count(./critereGoalAverage[@type = 'general'][@donnee = 'quantiteObjectif']) = 1">
					<fo:table-cell border="0.5pt solid black" padding="2mm"><fo:block text-align="right">
						<xsl:call-template name="critereGoalAverageGeneralQuantiteObjectif">
						  <xsl:with-param name="refParticipant"><xsl:value-of select="$refParticipant" /></xsl:with-param>
						  <xsl:with-param name="refObjectif"><xsl:value-of select="./critereGoalAverage/@refObjectif" /></xsl:with-param>
						  <xsl:with-param name="methode"><xsl:value-of select="./critereGoalAverage/@methode" /></xsl:with-param>
						</xsl:call-template>
					</fo:block></fo:table-cell>
				</xsl:if>
			</xsl:for-each>
		</fo:table-row>
	</xsl:template>
	
	<!-- Template d'un critère de classement pour un participant donné -->
	<xsl:template name="critereNbPoints">
		<xsl:param name="refParticipant" />
		<xsl:value-of select="format-number(//participant[@id = $refParticipant]/@pointsPhasesQualifs, '0.00')" />
	</xsl:template>
	<xsl:template name="critereNbVictoires">
		<xsl:param name="refParticipant" />
		<xsl:value-of select="count(//matchPhaseQualificative/participation[@refParticipant = $refParticipant][@resultat = 'victoire'])" />
	</xsl:template>
	<xsl:template name="critereNbEgalites">
		<xsl:param name="refParticipant" />
		<xsl:value-of select="count(//matchPhaseQualificative/participation[@refParticipant = $refParticipant][@resultat = 'egalite'])" />
	</xsl:template>
	<xsl:template name="critereNbDefaites">
		<xsl:param name="refParticipant" />
		<xsl:value-of select="count(//matchPhaseQualificative/participation[@refParticipant = $refParticipant][@resultat = 'defaite'])" />
	</xsl:template>
	<xsl:template name="critereNbForfaits">
		<xsl:param name="refParticipant" />
		<xsl:value-of select="count(//matchPhaseQualificative/participation[@refParticipant = $refParticipant][@resultat = 'forfait'])" />
	</xsl:template>
	<xsl:template name="critereQuantiteObjectif">
		<xsl:param name="refParticipant" />
		<xsl:param name="refObjectif" />
		<xsl:value-of select="sum(//matchPhaseQualificative/participation[@refParticipant = $refParticipant]/objectifRempli[@refObjectif = $refObjectif]/@quantite)" />
	</xsl:template>
	<xsl:template name="critereGoalAverageGeneralPoints">
		<xsl:param name="refParticipant" />
		<xsl:param name="methode" />
		<xsl:call-template name="critereGoalAverageGeneral">
		  <xsl:with-param name="valeurParticipant"><xsl:value-of select="sum(//matchPhaseQualificative/participation[@refParticipant = $refParticipant]/@points)" /></xsl:with-param>
		  <xsl:with-param name="valeurAdversaires"><xsl:value-of select="sum(//matchPhaseQualificative/participation[@refParticipant = $refParticipant]/../participation[@refParticipant != $refParticipant]/@points)" /></xsl:with-param>
		  <xsl:with-param name="methode"><xsl:value-of select="$methode" /></xsl:with-param>
		  <xsl:with-param name="formatDiff">0.00</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="critereGoalAverageGeneralResultat">
		<xsl:param name="refParticipant" />
		<xsl:param name="methode" />
		<xsl:call-template name="critereGoalAverageGeneral">
		  <xsl:with-param name="valeurParticipant"><xsl:value-of select="count(//matchPhaseQualificative/participation[@refParticipant = $refParticipant][@resultat = 'victoire'])" /></xsl:with-param>
		  <xsl:with-param name="valeurAdversaires"><xsl:value-of select="count(//matchPhaseQualificative/participation[@refParticipant = $refParticipant][@resultat = 'defaite']) + count(//matchPhaseQualificative/participation[@refParticipant = $refParticipant][@resultat = 'forfait'])" /></xsl:with-param>
		  <xsl:with-param name="methode"><xsl:value-of select="$methode" /></xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="critereGoalAverageGeneralQuantiteObjectif">
		<xsl:param name="refParticipant" />
		<xsl:param name="refObjectif" />
		<xsl:param name="methode" />
		<xsl:call-template name="critereGoalAverageGeneral">
		  <xsl:with-param name="valeurParticipant"><xsl:value-of select="sum(//matchPhaseQualificative/participation[@refParticipant = $refParticipant]/objectifRempli[@refObjectif = $refObjectif]/@quantite)" /></xsl:with-param>
		  <xsl:with-param name="valeurAdversaires"><xsl:value-of select="sum(//matchPhaseQualificative/participation[@refParticipant = $refParticipant]/../participation[@refParticipant != $refParticipant]/objectifRempli[@refObjectif = $refObjectif]/@quantite)" /></xsl:with-param>
		  <xsl:with-param name="methode"><xsl:value-of select="$methode" /></xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="critereGoalAverageGeneral">
		<xsl:param name="valeurParticipant"  />
		<xsl:param name="valeurAdversaires" />
		<xsl:param name="formatDiff">0</xsl:param>
		<xsl:param name="formatDiv">0.00</xsl:param>
		<xsl:param name="methode" />
		<xsl:choose>
			<xsl:when test="$methode = 'difference'">
				<xsl:value-of select="format-number($valeurParticipant - $valeurAdversaires, $formatDiff)" />
			</xsl:when>
			<xsl:when test="$methode = 'division'">
				<xsl:choose>
					<xsl:when test="$valeurAdversaires = 0">
						<xsl:value-of select="$valeurParticipant" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="format-number($valeurParticipant div $valeurAdversaires, $formatDiv)" />
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>