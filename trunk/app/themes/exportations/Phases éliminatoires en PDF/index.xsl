<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<!-- Importation des templates communs -->
	<xsl:import href="../../common.xsl"/>
	<xsl:import href="../common.xsl"/>
	
	<!-- Cible XML (XSL-FO) -->
	<xsl:output method="xml" indent="yes" encoding="utf-8" />
	
	<!-- Paramètres-->
	<xsl:param name="idCategorie" />

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
							<xsl:when test="count(/concours/listeCategories/categorie) > 1">
								<h1><xsl:value-of select="/concours/listeCategories/categorie[@id=$idCategorie]/@nom" /></h1>
							</xsl:when>
							<xsl:otherwise>
								<h1>Phases éliminatoires</h1>
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
						<xsl:choose>
							<!-- Catégorie existante -->
							<xsl:when test="count(/concours/listeCategories/categorie[@id=$idCategorie]) = 1">
								<xsl:apply-templates select="/concours/listeCategories/categorie[@id=$idCategorie]">
								</xsl:apply-templates>
							</xsl:when>
							<!-- Catégorie inexistante -->
							<xsl:otherwise>
								<xsl:call-template name="pdf-erreur">
								  <xsl:with-param name="erreur">La catégorie indiquée n'existe pas.</xsl:with-param>
								</xsl:call-template>
							</xsl:otherwise>
						</xsl:choose>
					</fo:block>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
	
	<!-- Template d'une catégorie -->
	<xsl:template match="categorie">
		<fo:block>
			<fo:table width="100%">
				<fo:table-column column-width="{100 div (count(./listePhasesEliminatoires/phaseEliminatoire)+1)}%" />
				<xsl:for-each select="./listePhasesEliminatoires/phaseEliminatoire">
					<fo:table-column column-width="{100 div (count(../phaseEliminatoire)+1)}%" />
				</xsl:for-each>
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell>
							<fo:block>
								<fo:table>
									<fo:table-column column-width="100%" />
									<fo:table-body>
										<xsl:apply-templates select="./listePhasesEliminatoires/phaseEliminatoire[position() = 1]" mode="diagramme-equipes">
											<xsl:sort select="@numero" data-type="number" />
											<xsl:with-param name="nbCellulesA">1</xsl:with-param>
											<xsl:with-param name="nbCellulesB">3</xsl:with-param>
										</xsl:apply-templates>
									</fo:table-body>
								</fo:table>
							</fo:block>
						</fo:table-cell>
						<xsl:apply-templates select="./listePhasesEliminatoires/phaseEliminatoire[position() = 1]" mode="diagramme-matchs">
							<xsl:sort select="@numero" data-type="number" />
							<xsl:with-param name="nbCellulesA">1</xsl:with-param>
							<xsl:with-param name="nbCellulesB">3</xsl:with-param>
						</xsl:apply-templates>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
		</fo:block>
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
		<fo:table-cell>
			<fo:block>
				<fo:table>
					<fo:table-column column-width="100%" />
					<fo:table-body>
						<!-- Cellules d'espacement du haut -->
						<xsl:call-template name="loop">
							<xsl:with-param name="n"><xsl:value-of select="$nbCellulesA" /></xsl:with-param>
							<xsl:with-param name="contenu"><fo:table-row><fo:table-cell padding-top="0.5pt" padding-bottom="0.5pt" height="0.6cm"><fo:block></fo:block></fo:table-cell></fo:table-row></xsl:with-param>
						</xsl:call-template>
		
						<!--  Cellules des matchs -->
						<xsl:apply-templates select="./matchPhaseEliminatoire" mode="diagramme-matchs">
							<xsl:with-param name="nbCellules"><xsl:value-of select="$nbCellulesB" /></xsl:with-param>
						</xsl:apply-templates>
						
						<!-- Cellules d'espacement du bas -->
						<xsl:call-template name="loop">
							<xsl:with-param name="n"><xsl:value-of select="$nbCellulesA" /></xsl:with-param>
							<xsl:with-param name="contenu"><fo:table-row><fo:table-cell padding-top="0.5pt" padding-bottom="0.5pt" height="0.6cm"><fo:block></fo:block></fo:table-cell></fo:table-row></xsl:with-param>
						</xsl:call-template>
					</fo:table-body>
				</fo:table>
			</fo:block>
		</fo:table-cell>
		
		<!-- Appeller la prochaine phase éliminatoire -->
		<xsl:variable name="numero"><xsl:value-of select="./@numero" /></xsl:variable>
		<xsl:apply-templates select="../phaseEliminatoire[@numero = $numero+1]" mode="diagramme-matchs">
			<xsl:sort select="@numero" data-type="number" />
			<xsl:with-param name="nbCellulesA"><xsl:number value="$nbCellulesB" /></xsl:with-param>
			<xsl:with-param name="nbCellulesB"><xsl:number value="number($nbCellulesB)*2+1" /></xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	
	<!-- Template d'un match de phase éliminatoire -->
	<xsl:template match="matchPhaseEliminatoire" mode="diagramme-equipes">
		<fo:table-row>
			<fo:table-cell border="0.5pt solid black" background-color="#CCCCCC" height="0.6cm" display-align="center" padding-left="0.2cm">
				<fo:block>
					<!-- Equipe A -->
					<xsl:call-template name="equipe">
					  <xsl:with-param name="id" select="./participation[1]/@refEquipe" />
					</xsl:call-template>
				</fo:block>
			</fo:table-cell>	
		</fo:table-row>
		<fo:table-row><fo:table-cell padding-top="0.5pt" padding-bottom="0.5pt" height="0.6cm"><fo:block></fo:block></fo:table-cell></fo:table-row>
		<fo:table-row>
			<fo:table-cell border="0.5pt solid black" background-color="#CCCCCC" height="0.6cm" display-align="center" padding-left="0.2cm">
				<fo:block>
					<!-- Equipe B -->
					<xsl:call-template name="equipe">
					  <xsl:with-param name="id" select="./participation[2]/@refEquipe" />
					</xsl:call-template>
				</fo:block>
			</fo:table-cell>
		</fo:table-row>
		<xsl:if test="position() != last()">
			<fo:table-row><fo:table-cell padding-top="0.5pt" padding-bottom="0.5pt" height="0.6cm"><fo:block></fo:block></fo:table-cell></fo:table-row>
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
					<xsl:with-param name="contenu"><fo:table-row><fo:table-cell padding-top="0.5pt" padding-bottom="0.5pt" height="0.6cm"><fo:block></fo:block></fo:table-cell></fo:table-row></xsl:with-param>
				</xsl:call-template>
			</xsl:if>
			
			<!-- Cellule du match -->
			<fo:table-row>
				<fo:table-cell border="0.5pt solid black" background-color="#CCCCCC" height="0.6cm" display-align="center" padding-left="0.2cm">
					<fo:block>
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
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
		</xsl:if>
	</xsl:template>
	
	<!-- Template d'une équipe -->
	<xsl:template name="equipe">
		<xsl:param name="id" />
		<xsl:value-of select="//equipe[@id=$id]/@nom" />
	</xsl:template>
</xsl:stylesheet>