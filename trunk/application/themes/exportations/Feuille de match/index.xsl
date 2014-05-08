<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:java="http://xml.apache.org/xslt/java" exclude-result-prefixes="java">
	<!-- Importation des templates communs -->
	<xsl:import href="../../common.xsl"/>
	<xsl:import href="../common.xsl"/>
	
	<!-- Cible XML -->
	<xsl:output method="xml" indent="yes" encoding="utf-8" />
	
	<!-- Paramètres-->
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
							Feuille de match
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
										<fo:table-cell border="0.5pt solid black" padding="2mm"><fo:block></fo:block></fo:table-cell>
									</fo:table-row>
								</xsl:if>
								
								<!-- Poule -->
								<xsl:if test="count(/concours/listeCategories/categorie[count(listePoules/poule) != 1]) != 0">
									<fo:table-row>
										<fo:table-cell border="0.5pt solid black" padding="2mm" background-color="#CCCCCC">
											<fo:block font-weight="bold">Poule :</fo:block>
										</fo:table-cell>
										<fo:table-cell border="0.5pt solid black" padding="2mm"><fo:block></fo:block></fo:table-cell>
									</fo:table-row> 
								</xsl:if>
								
								<!-- Phase -->
								<fo:table-row>
									<fo:table-cell border="0.5pt solid black" padding="2mm" background-color="#CCCCCC">
										<fo:block font-weight="bold">Phase :</fo:block>
									</fo:table-cell>
									<fo:table-cell border="0.5pt solid black" padding="2mm"><fo:block></fo:block></fo:table-cell>
								</fo:table-row>
								
								<!-- Lieu/Emplacement -->
								<xsl:if test="count(/concours/listeLieux/lieu) &gt; 1">
									<fo:table-row>
										<fo:table-cell border="0.5pt solid black" padding="2mm" background-color="#CCCCCC">
											<fo:block font-weight="bold">Lieu :</fo:block>
										</fo:table-cell>
										<fo:table-cell border="0.5pt solid black" padding="2mm"><fo:block></fo:block></fo:table-cell>
									</fo:table-row>	
								</xsl:if>
								<xsl:if test="count(/concours/listeLieux/lieu[count(listeEmplacements/emplacement) &gt; 1]) != 0">
									<fo:table-row>
										<fo:table-cell border="0.5pt solid black" padding="2mm" background-color="#CCCCCC">
											<fo:block font-weight="bold">Emplacement :</fo:block>
										</fo:table-cell>
										<fo:table-cell border="0.5pt solid black" padding="2mm"><fo:block></fo:block></fo:table-cell>
									</fo:table-row>						
								</xsl:if>
								
								<!-- Date -->
								<fo:table-row>
									<fo:table-cell border="0.5pt solid black" padding="2mm" background-color="#CCCCCC">
										<fo:block font-weight="bold">Date :</fo:block>
									</fo:table-cell>
									<fo:table-cell border="0.5pt solid black" padding="2mm"><fo:block></fo:block></fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
						
						<!-- Participants -->
						<fo:table width="100%" border="0.5pt solid black">
							<fo:table-column column-width="50%" />
							<fo:table-column column-width="50%" />
							<fo:table-body>
								<fo:table-row height="1.5cm">
									<fo:table-cell border="0.5pt solid black" background-color="{$couleurJoueurA}" padding="2mm"><fo:block></fo:block></fo:table-cell>
									<fo:table-cell border="0.5pt solid black" background-color="{$couleurJoueurB}" padding="2mm"><fo:block></fo:block></fo:table-cell>
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
													<xsl:when test="/concours/points/@victoire = 0 and (/concours/points/@egaliteActivee != 'oui' or /concours/points/@egalite = 0) and /concours/points/@defaite = 0 and /concours/points/@forfait = 0">
														Victoire <xsl:if test="/concours/points/@egaliteActivee = 'oui'">/ Egalité</xsl:if> / Défaite / Forfait
													</xsl:when>
													<xsl:otherwise>
													   Victoire (<xsl:value-of select="/concours/points/@victoire" />pts)
														<xsl:if test="/concours/points/@egaliteActivee = 'oui'">
															/ Egalité (<xsl:value-of select="/concours/points/@egalite" />pts)
														</xsl:if>
														/ Défaite  (<xsl:value-of select="/concours/points/@defaite" />pts)
														/ Forfait  (<xsl:value-of select="/concours/points/@forfait" />pts)
													</xsl:otherwise>
												</xsl:choose> *
											</fo:block>
											<fo:block font-size="8pt" font-style="italic" margin-top="4pt">* entourer le résultat</fo:block>
										</fo:block>
									</fo:table-cell>
									<fo:table-cell border="0.5pt solid black" padding="2mm" number-columns-spanned="2">
										<fo:block text-align="center">
											<fo:block font-size="9pt">
												<xsl:choose>
													<xsl:when test="/concours/points/@victoire = 0 and (/concours/points/@egaliteActivee != 'oui' or /concours/points/@egalite = 0) and /concours/points/@defaite = 0 and /concours/points/@forfait = 0">
														Victoire <xsl:if test="/concours/points/@egaliteActivee = 'oui'">/ Egalité</xsl:if> / Défaite / Forfait
													</xsl:when>
													<xsl:otherwise>
													   Victoire (<xsl:value-of select="/concours/points/@victoire" />pts)
														<xsl:if test="/concours/points/@egaliteActivee = 'oui'">
															/ Egalité (<xsl:value-of select="/concours/points/@egalite" />pts)
														</xsl:if>
														/ Défaite  (<xsl:value-of select="/concours/points/@defaite" />pts)
														/ Forfait  (<xsl:value-of select="/concours/points/@forfait" />pts)
													</xsl:otherwise>
												</xsl:choose> *
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
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
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