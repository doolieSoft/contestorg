<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<!-- Importation des templates communs -->
	<xsl:import href="../../common.xsl"/>
	<xsl:import href="../common.xsl"/>
	
	<!-- Cible XML -->
	<xsl:output method="xml" indent="yes" encoding="utf-8" />
	
	<!-- Paramètres-->
	<xsl:param name="idCategorie" />
	<xsl:param name="idPoule" />
	<xsl:param name="idPhase" />

	<!-- Template principal -->
	<xsl:template match="/">
		<fo:root>
			<!-- Déclaration de la mise en page -->
			<fo:layout-master-set>
				<fo:simple-page-master master-name="page" margin="1cm">
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
							<!-- Titre -->
							<xsl:if test="count(//phaseQualificative[@id=$idPhase]/../../../../../categorie) != 1">
								<xsl:value-of select="//phaseQualificative[@id=$idPhase]/../../../../@nom" /> - 
							</xsl:if>
							<xsl:if test="count(//phaseQualificative[@id=$idPhase]/../../../poule) != 1">
								<xsl:value-of select="//phaseQualificative[@id=$idPhase]/../../@nom" /> - 
							</xsl:if>
							Résultats des matchs de la phase qualificative <xsl:value-of select="//phaseQualificative[@id=$idPhase]/@numero+1" />
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
						<!-- Liste des matchs -->
						<xsl:choose>
							<!-- Phase qualificative existante -->
							<xsl:when test="count(//phaseQualificative[@id=$idPhase]) = 1">
								<xsl:apply-templates select="//phaseQualificative[@id=$idPhase]">
								</xsl:apply-templates>
							</xsl:when>
							<!-- Phase qualificative inexistance -->
							<xsl:otherwise>
								<xsl:call-template name="pdf-erreur">
								  <xsl:with-param name="erreur">La phase qualificative indiquée n'existe pas.</xsl:with-param>
								</xsl:call-template>
							</xsl:otherwise>
						</xsl:choose>
					</fo:block>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
	
	<!-- Template d'une phase qualificative -->
	<xsl:template match="phaseQualificative">
		<fo:block>
			<fo:table width="100%" border="0.5pt solid black">
				<fo:table-column column-width="30%" />
				<fo:table-column column-width="40%" />
				<fo:table-column column-width="30%" />

				<fo:table-header>
					<fo:table-row>
						<fo:table-cell border="0.5pt solid black" background-color="#CCCCCC" padding="2mm">
							<fo:block font-weight="bold" text-align="left">
								<xsl:choose>
								<xsl:when test="/concours/@participants = 'equipes'">
									Equipe A
								</xsl:when>
								<xsl:otherwise>
									Joueur A
								</xsl:otherwise>
							</xsl:choose>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell border="0.5pt solid black" background-color="#CCCCCC" padding="2mm">
							<fo:block font-weight="bold" text-align="center">Résultat</fo:block>
						</fo:table-cell>
						<fo:table-cell border="0.5pt solid black" background-color="#CCCCCC" padding="2mm">
							<fo:block font-weight="bold" text-align="right">
								<xsl:choose>
								<xsl:when test="/concours/@participants = 'equipes'">
									Equipe B
								</xsl:when>
								<xsl:otherwise>
									Joueur B
								</xsl:otherwise>
							</xsl:choose>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-header>
				<fo:table-body>
					<xsl:apply-templates select="./matchPhaseQualificative">
					</xsl:apply-templates>
				</fo:table-body>
			</fo:table>
		</fo:block>
	</xsl:template>
	
	<!-- Template d'un match -->
	<xsl:template match="matchPhaseQualificative">
		<xsl:variable name="idA" select="./participation[1]/@refEquipe" />
		<xsl:variable name="idB" select="./participation[2]/@refEquipe" />
		<fo:table-row>
			<fo:table-cell border="0.5pt solid black" padding="2mm">
				<fo:block text-align="left">
					<xsl:choose>
						<xsl:when test="$idA != ''">
							<xsl:value-of select="//equipe[@id=$idA]/@nom" />
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
				</fo:block>
			</fo:table-cell>
			<fo:table-cell border="0.5pt solid black" padding="2mm">
				<fo:block text-align="center">
					<xsl:call-template name="pdf-participation-resultat">
					  <xsl:with-param name="id" select="./participation[1]/@id" />
					</xsl:call-template>
					/
					<xsl:call-template name="pdf-participation-resultat">
					  <xsl:with-param name="id" select="./participation[2]/@id" />
					</xsl:call-template>
				</fo:block>
			</fo:table-cell>
			<fo:table-cell border="0.5pt solid black" padding="2mm">
				<fo:block text-align="right">
					<xsl:choose>
						<xsl:when test="$idB != ''">
							<xsl:value-of select="//equipe[@id=$idB]/@nom" />
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
				</fo:block>
			</fo:table-cell>
		</fo:table-row>
	</xsl:template>
</xsl:stylesheet>