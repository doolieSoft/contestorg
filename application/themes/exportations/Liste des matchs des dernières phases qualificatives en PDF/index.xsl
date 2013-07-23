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
	<xsl:param name="decalage" select="0" />

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
							Liste des matchs des dernières phases qualificatives
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
		<xsl:if test="count(./listePoules/poule[count(./listePhasesQualificatives/phaseQualificative) > $decalage]) != 0">
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
		<xsl:if test="count(./listePhasesQualificatives/phaseQualificative) != 0">
			<!-- Titre -->
			<xsl:if test="count(../poule) != 1">
				<xsl:call-template name="pdf-titre-h2">
				  <xsl:with-param name="titre"><xsl:value-of select="./@nom" /></xsl:with-param>
				</xsl:call-template>
			</xsl:if>
			
			<!-- Dernière phase qualificative -->
			<xsl:apply-templates select="./listePhasesQualificatives/phaseQualificative[position() = last() - $decalage]">
			</xsl:apply-templates>
		</xsl:if>
	</xsl:template>
	
	<!-- Template d'une phase qualificative -->
	<xsl:template match="phaseQualificative">
		<fo:block>
			<fo:table width="100%" border="0.5pt solid black">
				<fo:table-column column-width="50%" />
				<fo:table-column column-width="50%" />

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
		<xsl:variable name="idA" select="./participation[1]/@refParticipant" />
		<xsl:variable name="idB" select="./participation[2]/@refParticipant" />
		<fo:table-row>
			<fo:table-cell border="0.5pt solid black" padding="2mm">
				<fo:block text-align="left">
					<xsl:choose>
						<xsl:when test="$idA != ''">
							<xsl:if test="//participant[@id=$idA]/@stand != ''"><xsl:value-of select="//participant[@id=$idA]/@stand" /> - </xsl:if>
							<xsl:value-of select="//participant[@id=$idA]/@nom" />
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
				<fo:block text-align="right">
					<xsl:choose>
						<xsl:when test="$idB != ''">
							<xsl:if test="//participant[@id=$idB]/@stand != ''"><xsl:value-of select="//participant[@id=$idB]/@stand" /> - </xsl:if>
							<xsl:value-of select="//participant[@id=$idB]/@nom" />
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