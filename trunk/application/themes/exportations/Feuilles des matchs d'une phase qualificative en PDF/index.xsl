<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:java="http://xml.apache.org/xslt/java" exclude-result-prefixes="java">
	<!-- Importation des templates communs -->
	<xsl:import href="../../common.xsl"/>
	<xsl:import href="../common.xsl"/>
	
	<!-- Cible XML -->
	<xsl:output method="xml" indent="yes" encoding="utf-8" />
	
	<!-- Paramètres-->
	<xsl:param name="idCategorie" />
	<xsl:param name="idPoule" />
	<xsl:param name="idPhase" />
	<xsl:param name="couleurJoueurA" />
	<xsl:param name="couleurJoueurB" />
	<xsl:param name="signature" />

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
							Feuille de match des phases qualificatives
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
								<xsl:apply-templates select="//phaseQualificative[@id=$idPhase]/matchPhaseQualificative">
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
	
	<!-- Template d'un match -->
	<xsl:template match="matchPhaseQualificative">
		<xsl:variable name="idA" select="./participation[1]/@refParticipant" />
		<xsl:variable name="idB" select="./participation[2]/@refParticipant" />
		<xsl:variable name="timestamp" select="@timestamp"></xsl:variable>
		<xsl:variable name="refEmplacement" select="@refEmplacement"></xsl:variable>
		<fo:block break-after="page">
			<!-- Informations générales sur le matchs -->			
			<fo:table width="100%" border="0.5pt solid black">
				<fo:table-column column-width="20%" />
				<fo:table-column column-width="80%" />
				<fo:table-body>
					<!-- Catégorie -->
					<xsl:if test="count(/concours/listeCategories/categorie) != 1">
						<fo:table-row>
							<fo:table-cell border="0.5pt solid black" padding="2mm" background-color="#CCCCCC">
								<fo:block font-weight="bold">Catégorie :</fo:block>
							</fo:table-cell>
							<fo:table-cell border="0.5pt solid black" padding="2mm">
								<fo:block><xsl:value-of select="../../../../../@nom" /></fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:if>
					
					<!-- Poule -->
					<xsl:if test="count(../../../../poule) != 1">
						<fo:table-row>
							<fo:table-cell border="0.5pt solid black" padding="2mm" background-color="#CCCCCC">
								<fo:block font-weight="bold">Poule :</fo:block>
							</fo:table-cell>
							<fo:table-cell border="0.5pt solid black" padding="2mm">
								<fo:block><xsl:value-of select="../../../@nom" /></fo:block>
							</fo:table-cell>
						</fo:table-row> 
					</xsl:if>
					
					<!-- Phase -->
					<fo:table-row>
						<fo:table-cell border="0.5pt solid black" padding="2mm" background-color="#CCCCCC">
							<fo:block font-weight="bold">Phase :</fo:block>
						</fo:table-cell>
						<fo:table-cell border="0.5pt solid black" padding="2mm">
							<fo:block><xsl:value-of select="../@numero+1" /></fo:block>
						</fo:table-cell>
					</fo:table-row>
					
					<!-- Lieu/Emplacement -->
					<xsl:if test="$refEmplacement != ''">
						<xsl:if test="count(/concours/listeLieux/lieu) &gt; 1">
							<fo:table-row>
								<fo:table-cell border="0.5pt solid black" padding="2mm" background-color="#CCCCCC">
									<fo:block font-weight="bold">Lieu :</fo:block>
								</fo:table-cell>
								<fo:table-cell border="0.5pt solid black" padding="2mm">
									<fo:block><xsl:value-of select="//emplacement[@id=$refEmplacement]/../../@nom" /></fo:block>
								</fo:table-cell>
							</fo:table-row>	
						</xsl:if>
						<fo:table-row>
							<fo:table-cell border="0.5pt solid black" padding="2mm" background-color="#CCCCCC">
								<fo:block font-weight="bold">Emplacement :</fo:block>
							</fo:table-cell>
							<fo:table-cell border="0.5pt solid black" padding="2mm">
								<fo:block><xsl:value-of select="//emplacement[@id=$refEmplacement]/@nom" /></fo:block>
							</fo:table-cell>
						</fo:table-row>						
					</xsl:if>
					
					<!-- Date -->
					<xsl:if test="$timestamp != ''">
						<fo:table-row>
							<fo:table-cell border="0.5pt solid black" padding="2mm" background-color="#CCCCCC">
								<fo:block font-weight="bold">Date :</fo:block>
							</fo:table-cell>
							<fo:table-cell border="0.5pt solid black" padding="2mm">
								<fo:block>
									<xsl:value-of select="java:format(java:java.text.SimpleDateFormat.new('dd/MM/yyyy'),java:java.util.Date.new($timestamp*1000))" />
									à
									<xsl:value-of select="java:format(java:java.text.SimpleDateFormat.new('HH:mm'),java:java.util.Date.new($timestamp*1000))" />
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:if>
				</fo:table-body>
			</fo:table>
			
			<!-- Participants -->
			<fo:table width="100%" border="0.5pt solid black">
				<fo:table-column column-width="50%" />
				<fo:table-column column-width="50%" />
				<fo:table-body>
					<fo:table-row>
						<fo:table-cell border="0.5pt solid black" background-color="{$couleurJoueurA}" padding="2mm">
							<fo:block font-weight="bold" text-align="left" font-size="14pt">
								<xsl:choose>
									<xsl:when test="$idA != ''">
										<xsl:if test="//participant[@id=$idA]/@stand != ''">
											<xsl:value-of select="//participant[@id=$idA]/@stand" /> -
										</xsl:if>
										<xsl:value-of select="//participant[@id=$idA]/@nom" />
										<xsl:if test="//participant[@id=$idA]/@ville != ''">
											<fo:block font-weight="bold" font-size="9pt" margin-top="3pt">
												<xsl:value-of select="//participant[@id=$idA]/@ville" />
											</fo:block>
										</xsl:if>
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
						<fo:table-cell border="0.5pt solid black" background-color="{$couleurJoueurB}" padding="2mm">
							<fo:block font-weight="bold" text-align="right" font-size="14pt">
								<xsl:choose>
									<xsl:when test="$idB != ''">
										<xsl:if test="//participant[@id=$idB]/@stand != ''">
											<xsl:value-of select="//participant[@id=$idB]/@stand" /> -
										</xsl:if>
										<xsl:value-of select="//participant[@id=$idB]/@nom" />
										<xsl:if test="//participant[@id=$idB]/@ville != ''">
											<fo:block font-weight="bold" font-size="9pt" margin-top="3pt">
												<xsl:value-of select="//participant[@id=$idB]/@ville" />
											</fo:block>
										</xsl:if>
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
				</fo:table-body>
			</fo:table>
			
			<!-- Objectifs/Résultat -->
			<fo:table width="100%" border="0.5pt solid black">
				<fo:table-column column-width="40%" />
				<fo:table-column column-width="10%" />
				<fo:table-column column-width="40%" />
				<fo:table-column column-width="10%" />
				<fo:table-body>
					<xsl:apply-templates select="/concours/listeObjectifs/objectif">
					</xsl:apply-templates>
					<fo:table-row>
						<fo:table-cell border="0.5pt solid black" padding="2mm" number-columns-spanned="2">
							<fo:block text-align="center">
								<fo:block font-size="9pt">
									<xsl:choose>
										<xsl:when test="/concours/points/@victoire = 0 and /concours/points/@egalite = 0 and /concours/points/@defaite = 0">
											Victoire / Egalité / Défaite *
										</xsl:when>
										<xsl:otherwise>
											Victoire (<xsl:value-of select="/concours/points/@victoire" />pts) / Egalité (<xsl:value-of select="/concours/points/@egalite" />pts) / Défaite (<xsl:value-of select="/concours/points/@defaite" />pts) *
										</xsl:otherwise>
									</xsl:choose>
								</fo:block>
								<fo:block font-size="8pt" font-style="italic" margin-top="4pt">* entourer le résultat</fo:block>
							</fo:block>
						</fo:table-cell>
						<fo:table-cell border="0.5pt solid black" padding="2mm" number-columns-spanned="2">
							<fo:block text-align="center">
								<fo:block font-size="9pt">
									<xsl:choose>
										<xsl:when test="/concours/points/@victoire = 0 and /concours/points/@egalite = 0 and /concours/points/@defaite = 0">
											Victoire / Egalité / Défaite *
										</xsl:when>
										<xsl:otherwise>
											Victoire (<xsl:value-of select="/concours/points/@victoire" />pts) / Egalité (<xsl:value-of select="/concours/points/@egalite" />pts) / Défaite (<xsl:value-of select="/concours/points/@defaite" />pts) *
										</xsl:otherwise>
									</xsl:choose>
								</fo:block>
								<fo:block font-size="8pt" font-style="italic" margin-top="4pt">* entourer le résultat</fo:block>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
					<xsl:if test="/concours/points/@victoire != 0 or /concours/points/@egalite != 0 or /concours/points/@defaite != 0 or count(/concours/listeObjectifs/objectif/objectifPoints) != 0">
						<fo:table-row>
							<fo:table-cell border="0.5pt solid black" padding="2mm"><fo:block>Total de points remportés :</fo:block></fo:table-cell>
							<fo:table-cell border="0.5pt solid black" padding="2mm"><fo:block></fo:block></fo:table-cell>
							<fo:table-cell border="0.5pt solid black" padding="2mm"><fo:block>Total de points remportés :</fo:block></fo:table-cell>
							<fo:table-cell border="0.5pt solid black" padding="2mm"><fo:block></fo:block></fo:table-cell>
						</fo:table-row>
					</xsl:if>
				</fo:table-body>
			</fo:table>
			
			<!-- Commentaires -->
			<fo:table width="100%" border="0.5pt solid black">
				<fo:table-column column-width="100%" />
				<fo:table-header>
					<fo:table-row>
						<fo:table-cell border="0.5pt solid black" background-color="#CCCCCC" padding="2mm">
							<fo:block font-weight="bold" text-align="left">
								Commentaires
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-header>
				<fo:table-body>
					<fo:table-row height="3cm">
						<fo:table-cell border="0.5pt solid black" padding="2mm">
							<fo:block></fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
			
			<!-- Signatures -->
			<xsl:if test="$signature = 1">
				<fo:table width="100%" border="0.5pt solid black">
					<fo:table-column column-width="50%" />
					<fo:table-column column-width="50%" />
					<fo:table-header>
						<fo:table-row>
							<fo:table-cell border="0.5pt solid black" background-color="#CCCCCC" padding="2mm" number-columns-spanned="2">
								<fo:block font-weight="bold">
									Signatures
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-header>
					<fo:table-body>
						<fo:table-row height="1cm">
							<fo:table-cell border="0.5pt solid black" padding="2mm">
								<fo:block></fo:block>
							</fo:table-cell>
							<fo:table-cell border="0.5pt solid black" padding="2mm">
								<fo:block></fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
				</fo:table>
			</xsl:if>
		</fo:block>
	</xsl:template>
	
	<!-- Template d'un objectif -->
	<xsl:template match="objectifPoints">
		<fo:table-row>
			<fo:table-cell border="0.5pt solid black" padding="2mm">
				<fo:block font-size="9pt">
					<xsl:value-of select="@nom" /> (<xsl:value-of select="@points" />pts) :
				</fo:block>
			</fo:table-cell>
			<fo:table-cell border="0.5pt solid black" padding="2mm"><fo:block></fo:block></fo:table-cell>
			<fo:table-cell border="0.5pt solid black" padding="2mm">
				<fo:block font-size="9pt">
					<xsl:value-of select="@nom" /> (<xsl:value-of select="@points" />pts) :
				</fo:block>
			</fo:table-cell>
			<fo:table-cell border="0.5pt solid black" padding="2mm"><fo:block></fo:block></fo:table-cell>
		</fo:table-row>
	</xsl:template>
	<xsl:template match="objectifPourcentage">
		<fo:table-row>
			<fo:table-cell border="0.5pt solid black" padding="2mm">
				<fo:block font-size="9pt">
					<xsl:value-of select="@nom" /> (<xsl:value-of select="@pourcentage" />%) :
				</fo:block>
			</fo:table-cell>
			<fo:table-cell border="0.5pt solid black" padding="2mm"><fo:block></fo:block></fo:table-cell>
			<fo:table-cell border="0.5pt solid black" padding="2mm">
				<fo:block font-size="9pt">
					<xsl:value-of select="@nom" /> (<xsl:value-of select="@pourcentage" />%) :
				</fo:block>
			</fo:table-cell>
			<fo:table-cell border="0.5pt solid black" padding="2mm"><fo:block></fo:block></fo:table-cell>
		</fo:table-row>
	</xsl:template>
	<xsl:template match="objectifNul">
		<fo:table-row>
			<fo:table-cell border="0.5pt solid black" padding="2mm">
				<fo:block font-size="9pt">
					<xsl:value-of select="@nom" /> :
				</fo:block>
			</fo:table-cell>
			<fo:table-cell border="0.5pt solid black" padding="2mm"><fo:block></fo:block></fo:table-cell>
			<fo:table-cell border="0.5pt solid black" padding="2mm">
				<fo:block font-size="9pt">
					<xsl:value-of select="@nom" /> :
				</fo:block>
			</fo:table-cell>
			<fo:table-cell border="0.5pt solid black" padding="2mm"><fo:block></fo:block></fo:table-cell>
		</fo:table-row>
	</xsl:template>
</xsl:stylesheet>