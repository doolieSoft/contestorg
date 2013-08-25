<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<!-- Importation des templates communs -->
	<xsl:import href="../../common.xsl"/>
	<xsl:import href="../common.xsl"/>
	
	<!-- Cible XML (XSL-FO) -->
	<xsl:output method="xml" indent="yes" />
	
	<!-- Paramètres-->
	<xsl:param name="idCategorie" />

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
							<xsl:choose>
								<xsl:when test="/concours/@participants = 'equipes'">
									Classement combiné des équipes aux phases qualificatives et éliminatoires
								</xsl:when>
								<xsl:otherwise>
									Classement combiné des joueurs aux phases qualificatives et éliminatoires
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
			
			<!-- Classement des participants aux phases éliminatoires -->
			<xsl:if test="count(./classementCategoriePhasesEliminatoires/listeClassementParticipants/classementParticipant) != count(./classementCategoriePhasesQualificatives/listeClassementParticipants/classementParticipant)">
				<fo:block space-before="5mm" space-after="2mm">
					<xsl:choose>
						<xsl:when test="/concours/@participants = 'equipes'">
							Equipes qualifiées aux phases éliminatoires :
						</xsl:when>
						<xsl:otherwise>
							Joueurs qualifiés aux phases éliminatoires :
						</xsl:otherwise>
					</xsl:choose>
				</fo:block>
			</xsl:if>
			<xsl:apply-templates select="./classementCategoriePhasesEliminatoires/listeClassementParticipants" mode="phasesEliminatoires">
			</xsl:apply-templates>
			
			<!-- Liste des poules -->
			<xsl:if test="count(./classementCategoriePhasesEliminatoires/listeClassementParticipants/classementParticipant) != count(./classementCategoriePhasesQualificatives/listeClassementParticipants/classementParticipant)">
				<fo:block space-before="5mm" space-after="2mm">
					<xsl:choose>
						<xsl:when test="/concours/@participants = 'equipes'">
							Equipes non qualifiées aux phases éliminatoires :
						</xsl:when>
						<xsl:otherwise>
							Joueurs non qualifiés aux phases éliminatoires :
						</xsl:otherwise>
					</xsl:choose>
				</fo:block>
				<xsl:apply-templates select="./classementCategoriePhasesQualificatives/listeClassementParticipants" mode="phasesQualificatives">
				</xsl:apply-templates>
			</xsl:if>
		</xsl:if>
	</xsl:template>
	
	<!-- Template d'un classement de participants aux phases éliminatoires -->
	<xsl:template match="listeClassementParticipants" mode="phasesEliminatoires">
		<fo:block>
			<fo:table width="100%" border="0.5pt solid black">
				<fo:table-column column-width="15%" />
				<fo:table-column column-width="85%" />

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
					</fo:table-row>
				</fo:table-header>
				<fo:table-body>
					<xsl:for-each select="./classementParticipant">
						<xsl:sort select="@rang" data-type="number" />
						<xsl:variable name="id" select="./@refParticipant" />
						<fo:table-row>
							<fo:table-cell border="0.5pt solid black" padding="2mm">
								<fo:block><xsl:value-of select="./@rang" /></fo:block>
							</fo:table-cell>
							<fo:table-cell border="0.5pt solid black" padding="2mm">
								<fo:block>
									<xsl:if test="//participant[@id=$id]/@stand != ''"><xsl:value-of select="//participant[@id=$id]/@stand" /> - </xsl:if>
									<xsl:value-of select="//participant[@id=$id]/@nom" />
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:for-each>
				</fo:table-body>
			</fo:table>
		</fo:block>
	</xsl:template>
	
	<!-- Template d'un classement de participants aux phases éliminatoires -->
	<xsl:template match="listeClassementParticipants" mode="phasesQualificatives">
		<fo:block>
			<fo:table width="100%" border="0.5pt solid black">
				<fo:table-column column-width="15%" />
				<fo:table-column column-width="85%" />

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
					</fo:table-row>
				</fo:table-header>
				<fo:table-body>
					<xsl:variable name="nbTotalParticipantsPhasesEliminatoires" select="count(../../classementCategoriePhasesEliminatoires/listeClassementParticipants/classementParticipant)" />
					<xsl:for-each select="./classementParticipant">
						<xsl:sort select="@rang" data-type="number" />
						<xsl:variable name="id" select="./@refParticipant" />
						<xsl:if test="count(../../../classementCategoriePhasesEliminatoires/listeClassementParticipants/classementParticipant[@refParticipant = $id]) = 0">
							<xsl:variable name="idCategorie" select="../../../@id" />
							<xsl:variable name="rang" select="./@rang" />
							<xsl:variable name="nbParticipantsPhasesEliminatoires" select="count(//categorie[@id = $idCategorie]/classementCategoriePhasesQualificatives/listeClassementParticipants/classementParticipant[@rang &lt; $rang][@refParticipant = //categorie[@id = $idCategorie]/classementCategoriePhasesEliminatoires/listeClassementParticipants/classementParticipant/@refParticipant])" />
							<fo:table-row>
								<fo:table-cell border="0.5pt solid black" padding="2mm">
									<fo:block><xsl:value-of select="$nbTotalParticipantsPhasesEliminatoires + ./@rang - $nbParticipantsPhasesEliminatoires"/></fo:block>
								</fo:table-cell>
								<fo:table-cell border="0.5pt solid black" padding="2mm">
									<fo:block>
										<xsl:if test="//participant[@id=$id]/@stand != ''"><xsl:value-of select="//participant[@id=$id]/@stand" /> - </xsl:if>
										<xsl:value-of select="//participant[@id=$id]/@nom" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</xsl:if>
					</xsl:for-each>
				</fo:table-body>
			</fo:table>
		</fo:block>
	</xsl:template>
</xsl:stylesheet>