<?mso-application progid="Word.Document"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:n2="urn:hl7-org:v3"
	xmlns:java="http://xml.apache.org/xslt/java"
	exclude-result-prefixes="n2 xs xsi xsl java"
	xmlns:aml="http://schemas.microsoft.com/aml/2001/core"
	xmlns:wpc="http://schemas.microsoft.com/office/word/2010/wordprocessingCanvas"
	xmlns:dt="uuid:C2F41010-65B3-11d1-A29F-00AA00C14882"
	xmlns:mc="http://schemas.openxmlformats.org/markup-compatibility/2006"
	xmlns:o="urn:schemas-microsoft-com:office:office"
	xmlns:v="urn:schemas-microsoft-com:vml"
	xmlns:w10="urn:schemas-microsoft-com:office:word"
	xmlns:w="http://schemas.microsoft.com/office/word/2003/wordml"
	xmlns:wx="http://schemas.microsoft.com/office/word/2003/auxHint"
	xmlns:wne="http://schemas.microsoft.com/office/word/2006/wordml"
	xmlns:wsp="http://schemas.microsoft.com/office/word/2003/wordml/sp2"
	xmlns:sl="http://schemas.microsoft.com/schemaLibrary/2003/core"
	w:macrosPresent="no" w:embeddedObjPresent="no" w:ocxPresent="no"
	xml:space="preserve">

	<!-- Cible XML -->
	<xsl:output method="xml" encoding="UTF-8" indent="yes" />
	
	<xsl:template match="/">
		<w:wordDocument>
			<w:ignoreSubtree w:val="http://schemas.microsoft.com/office/word/2003/wordml/sp2" />
			<o:DocumentProperties>
				<o:Author>Cyril Perrin</o:Author>
				<o:LastAuthor>Cyril Perrin</o:LastAuthor>
				<o:Revision>2</o:Revision>
				<o:TotalTime>0</o:TotalTime>
				<o:Created>2012-03-18T09:19:00Z</o:Created>
				<o:LastSaved>2012-03-18T09:19:00Z</o:LastSaved>
				<o:Pages>2</o:Pages>
				<o:Words>216</o:Words>
				<o:Characters>1189</o:Characters>
				<o:Lines>9</o:Lines>
				<o:Paragraphs>2</o:Paragraphs>
				<o:CharactersWithSpaces>1403</o:CharactersWithSpaces>
				<o:Version>14</o:Version>
			</o:DocumentProperties>
			
			<w:fonts>
				<w:defaultFonts w:ascii="Calibri" w:fareast="Calibri" w:h-ansi="Calibri" w:cs="Times New Roman" />
				<w:font w:name="Times New Roman">
					<w:panose-1 w:val="02020603050405020304" />
					<w:charset w:val="00" />
					<w:family w:val="Roman" />
					<w:pitch w:val="variable" />
					<w:sig w:usb-0="E0002AFF" w:usb-1="C0007841" w:usb-2="00000009" w:usb-3="00000000" w:csb-0="000001FF" w:csb-1="00000000" />
				</w:font>
				<w:font w:name="Cambria Math">
					<w:panose-1 w:val="02040503050406030204" />
					<w:charset w:val="00" />
					<w:family w:val="Roman" />
					<w:pitch w:val="variable" />
					<w:sig w:usb-0="E00002FF" w:usb-1="420024FF" w:usb-2="00000000" w:usb-3="00000000" w:csb-0="0000019F" w:csb-1="00000000" />
				</w:font>
				<w:font w:name="Calibri">
					<w:panose-1 w:val="020F0502020204030204" />
					<w:charset w:val="00" />
					<w:family w:val="Swiss" />
					<w:pitch w:val="variable" />
					<w:sig w:usb-0="E10002FF" w:usb-1="4000ACFF" w:usb-2="00000009" w:usb-3="00000000" w:csb-0="0000019F" w:csb-1="00000000" />
				</w:font>
			</w:fonts>
			
			<w:styles>
				<w:versionOfBuiltInStylenames w:val="7" />
				<w:latentStyles w:defLockedState="off" w:latentStyleCount="267">
					<w:lsdException w:name="Normal" />
					<w:lsdException w:name="heading 1" />
					<w:lsdException w:name="heading 2" />
					<w:lsdException w:name="heading 3" />
					<w:lsdException w:name="heading 4" />
					<w:lsdException w:name="heading 5" />
					<w:lsdException w:name="heading 6" />
					<w:lsdException w:name="heading 7" />
					<w:lsdException w:name="heading 8" />
					<w:lsdException w:name="heading 9" />
					<w:lsdException w:name="toc 1" />
					<w:lsdException w:name="toc 2" />
					<w:lsdException w:name="toc 3" />
					<w:lsdException w:name="toc 4" />
					<w:lsdException w:name="toc 5" />
					<w:lsdException w:name="toc 6" />
					<w:lsdException w:name="toc 7" />
					<w:lsdException w:name="toc 8" />
					<w:lsdException w:name="toc 9" />
					<w:lsdException w:name="caption" />
					<w:lsdException w:name="Title" />
					<w:lsdException w:name="Default Paragraph Font" />
					<w:lsdException w:name="Subtitle" />
					<w:lsdException w:name="Strong" />
					<w:lsdException w:name="Emphasis" />
					<w:lsdException w:name="Table Grid" />
					<w:lsdException w:name="Placeholder Text" />
					<w:lsdException w:name="No Spacing" />
					<w:lsdException w:name="Light Shading" />
					<w:lsdException w:name="Light List" />
					<w:lsdException w:name="Light Grid" />
					<w:lsdException w:name="Medium Shading 1" />
					<w:lsdException w:name="Medium Shading 2" />
					<w:lsdException w:name="Medium List 1" />
					<w:lsdException w:name="Medium List 2" />
					<w:lsdException w:name="Medium Grid 1" />
					<w:lsdException w:name="Medium Grid 2" />
					<w:lsdException w:name="Medium Grid 3" />
					<w:lsdException w:name="Dark List" />
					<w:lsdException w:name="Colorful Shading" />
					<w:lsdException w:name="Colorful List" />
					<w:lsdException w:name="Colorful Grid" />
					<w:lsdException w:name="Light Shading Accent 1" />
					<w:lsdException w:name="Light List Accent 1" />
					<w:lsdException w:name="Light Grid Accent 1" />
					<w:lsdException w:name="Medium Shading 1 Accent 1" />
					<w:lsdException w:name="Medium Shading 2 Accent 1" />
					<w:lsdException w:name="Medium List 1 Accent 1" />
					<w:lsdException w:name="Revision" />
					<w:lsdException w:name="List Paragraph" />
					<w:lsdException w:name="Quote" />
					<w:lsdException w:name="Intense Quote" />
					<w:lsdException w:name="Medium List 2 Accent 1" />
					<w:lsdException w:name="Medium Grid 1 Accent 1" />
					<w:lsdException w:name="Medium Grid 2 Accent 1" />
					<w:lsdException w:name="Medium Grid 3 Accent 1" />
					<w:lsdException w:name="Dark List Accent 1" />
					<w:lsdException w:name="Colorful Shading Accent 1" />
					<w:lsdException w:name="Colorful List Accent 1" />
					<w:lsdException w:name="Colorful Grid Accent 1" />
					<w:lsdException w:name="Light Shading Accent 2" />
					<w:lsdException w:name="Light List Accent 2" />
					<w:lsdException w:name="Light Grid Accent 2" />
					<w:lsdException w:name="Medium Shading 1 Accent 2" />
					<w:lsdException w:name="Medium Shading 2 Accent 2" />
					<w:lsdException w:name="Medium List 1 Accent 2" />
					<w:lsdException w:name="Medium List 2 Accent 2" />
					<w:lsdException w:name="Medium Grid 1 Accent 2" />
					<w:lsdException w:name="Medium Grid 2 Accent 2" />
					<w:lsdException w:name="Medium Grid 3 Accent 2" />
					<w:lsdException w:name="Dark List Accent 2" />
					<w:lsdException w:name="Colorful Shading Accent 2" />
					<w:lsdException w:name="Colorful List Accent 2" />
					<w:lsdException w:name="Colorful Grid Accent 2" />
					<w:lsdException w:name="Light Shading Accent 3" />
					<w:lsdException w:name="Light List Accent 3" />
					<w:lsdException w:name="Light Grid Accent 3" />
					<w:lsdException w:name="Medium Shading 1 Accent 3" />
					<w:lsdException w:name="Medium Shading 2 Accent 3" />
					<w:lsdException w:name="Medium List 1 Accent 3" />
					<w:lsdException w:name="Medium List 2 Accent 3" />
					<w:lsdException w:name="Medium Grid 1 Accent 3" />
					<w:lsdException w:name="Medium Grid 2 Accent 3" />
					<w:lsdException w:name="Medium Grid 3 Accent 3" />
					<w:lsdException w:name="Dark List Accent 3" />
					<w:lsdException w:name="Colorful Shading Accent 3" />
					<w:lsdException w:name="Colorful List Accent 3" />
					<w:lsdException w:name="Colorful Grid Accent 3" />
					<w:lsdException w:name="Light Shading Accent 4" />
					<w:lsdException w:name="Light List Accent 4" />
					<w:lsdException w:name="Light Grid Accent 4" />
					<w:lsdException w:name="Medium Shading 1 Accent 4" />
					<w:lsdException w:name="Medium Shading 2 Accent 4" />
					<w:lsdException w:name="Medium List 1 Accent 4" />
					<w:lsdException w:name="Medium List 2 Accent 4" />
					<w:lsdException w:name="Medium Grid 1 Accent 4" />
					<w:lsdException w:name="Medium Grid 2 Accent 4" />
					<w:lsdException w:name="Medium Grid 3 Accent 4" />
					<w:lsdException w:name="Dark List Accent 4" />
					<w:lsdException w:name="Colorful Shading Accent 4" />
					<w:lsdException w:name="Colorful List Accent 4" />
					<w:lsdException w:name="Colorful Grid Accent 4" />
					<w:lsdException w:name="Light Shading Accent 5" />
					<w:lsdException w:name="Light List Accent 5" />
					<w:lsdException w:name="Light Grid Accent 5" />
					<w:lsdException w:name="Medium Shading 1 Accent 5" />
					<w:lsdException w:name="Medium Shading 2 Accent 5" />
					<w:lsdException w:name="Medium List 1 Accent 5" />
					<w:lsdException w:name="Medium List 2 Accent 5" />
					<w:lsdException w:name="Medium Grid 1 Accent 5" />
					<w:lsdException w:name="Medium Grid 2 Accent 5" />
					<w:lsdException w:name="Medium Grid 3 Accent 5" />
					<w:lsdException w:name="Dark List Accent 5" />
					<w:lsdException w:name="Colorful Shading Accent 5" />
					<w:lsdException w:name="Colorful List Accent 5" />
					<w:lsdException w:name="Colorful Grid Accent 5" />
					<w:lsdException w:name="Light Shading Accent 6" />
					<w:lsdException w:name="Light List Accent 6" />
					<w:lsdException w:name="Light Grid Accent 6" />
					<w:lsdException w:name="Medium Shading 1 Accent 6" />
					<w:lsdException w:name="Medium Shading 2 Accent 6" />
					<w:lsdException w:name="Medium List 1 Accent 6" />
					<w:lsdException w:name="Medium List 2 Accent 6" />
					<w:lsdException w:name="Medium Grid 1 Accent 6" />
					<w:lsdException w:name="Medium Grid 2 Accent 6" />
					<w:lsdException w:name="Medium Grid 3 Accent 6" />
					<w:lsdException w:name="Dark List Accent 6" />
					<w:lsdException w:name="Colorful Shading Accent 6" />
					<w:lsdException w:name="Colorful List Accent 6" />
					<w:lsdException w:name="Colorful Grid Accent 6" />
					<w:lsdException w:name="Subtle Emphasis" />
					<w:lsdException w:name="Intense Emphasis" />
					<w:lsdException w:name="Subtle Reference" />
					<w:lsdException w:name="Intense Reference" />
					<w:lsdException w:name="Book Title" />
					<w:lsdException w:name="Bibliography" />
					<w:lsdException w:name="TOC Heading" />
				</w:latentStyles>
				<w:style w:type="paragraph" w:default="on" w:styleId="Normal">
					<w:name w:val="Normal" />
					<w:pPr>
						<w:spacing w:after="200" w:line="276" w:line-rule="auto" />
					</w:pPr>
					<w:rPr>
						<wx:font wx:val="Calibri" />
						<w:sz w:val="22" />
						<w:sz-cs w:val="22" />
						<w:lang w:val="FR" w:fareast="EN-US" w:bidi="AR-SA" />
					</w:rPr>
				</w:style>
				<w:style w:type="character" w:default="on" w:styleId="Policepardfaut">
					<w:name w:val="Default Paragraph Font" />
					<wx:uiName wx:val="Police par défaut" />
				</w:style>
				<w:style w:type="table" w:default="on" w:styleId="TableauNormal">
					<w:name w:val="Normal Table" />
					<wx:uiName wx:val="Tableau Normal" />
					<w:rPr>
						<wx:font wx:val="Calibri" />
						<w:lang w:val="FR" w:fareast="FR" w:bidi="AR-SA" />
					</w:rPr>
					<w:tblPr>
						<w:tblInd w:w="0" w:type="dxa" />
						<w:tblCellMar>
							<w:top w:w="0" w:type="dxa" />
							<w:left w:w="108" w:type="dxa" />
							<w:bottom w:w="0" w:type="dxa" />
							<w:right w:w="108" w:type="dxa" />
						</w:tblCellMar>
					</w:tblPr>
				</w:style>
				<w:style w:type="list" w:default="on" w:styleId="Aucuneliste">
					<w:name w:val="No List" />
					<wx:uiName wx:val="Aucune liste" />
				</w:style>
				<w:style w:type="character" w:styleId="Lienhypertexte">
					<w:name w:val="Hyperlink" />
					<wx:uiName wx:val="Lien hypertexte" />
					<w:rsid w:val="00C93EE0" />
					<w:rPr>
						<w:color w:val="0000FF" />
						<w:u w:val="single" />
					</w:rPr>
				</w:style>
				<w:style w:type="table" w:styleId="Grilledutableau">
					<w:name w:val="Table Grid" />
					<wx:uiName wx:val="Grille du tableau" />
					<w:basedOn w:val="TableauNormal" />
					<w:rsid w:val="007A6024" />
					<w:rPr>
						<wx:font wx:val="Calibri" />
					</w:rPr>
					<w:tblPr>
						<w:tblInd w:w="0" w:type="dxa" />
						<w:tblBorders>
							<w:top w:val="single" w:sz="4" wx:bdrwidth="10" w:space="0" w:color="auto" />
							<w:left w:val="single" w:sz="4" wx:bdrwidth="10" w:space="0" w:color="auto" />
							<w:bottom w:val="single" w:sz="4" wx:bdrwidth="10" w:space="0" w:color="auto" />
							<w:right w:val="single" w:sz="4" wx:bdrwidth="10" w:space="0" w:color="auto" />
							<w:insideH w:val="single" w:sz="4" wx:bdrwidth="10" w:space="0" w:color="auto" />
							<w:insideV w:val="single" w:sz="4" wx:bdrwidth="10" w:space="0" w:color="auto" />
						</w:tblBorders>
						<w:tblCellMar>
							<w:top w:w="0" w:type="dxa" />
							<w:left w:w="108" w:type="dxa" />
							<w:bottom w:w="0" w:type="dxa" />
							<w:right w:w="108" w:type="dxa" />
						</w:tblCellMar>
					</w:tblPr>
				</w:style>
			</w:styles>
			
			<w:shapeDefaults>
				<o:shapedefaults v:ext="edit" spidmax="1026" />
				<o:shapelayout v:ext="edit"><o:idmap v:ext="edit" data="1" /></o:shapelayout>
			</w:shapeDefaults>
			
			<w:docPr>
				<w:view w:val="print" />
				<w:zoom w:percent="110" />
				<w:doNotEmbedSystemFonts />
				<w:proofState w:spelling="clean" w:grammar="clean" />
				<w:defaultTabStop w:val="708" />
				<w:hyphenationZone w:val="425" />
				<w:punctuationKerning />
				<w:characterSpacingControl w:val="DontCompress" />
				<w:optimizeForBrowser />
				<w:allowPNG />
				<w:validateAgainstSchema />
				<w:saveInvalidXML w:val="off" />
				<w:ignoreMixedContent w:val="off" />
				<w:alwaysShowPlaceholderText w:val="off" />
				<w:compat>
					<w:breakWrappedTables />
					<w:snapToGridInCell />
					<w:wrapTextWithPunct />
					<w:useAsianBreakRules />
					<w:dontGrowAutofit />
				</w:compat>
				<wsp:rsids>
					<wsp:rsidRoot wsp:val="00C93EE0" />
					<wsp:rsid wsp:val="00243991" />
					<wsp:rsid wsp:val="00315910" />
					<wsp:rsid wsp:val="003C3601" />
					<wsp:rsid wsp:val="007A6024" />
					<wsp:rsid wsp:val="009D64F5" />
					<wsp:rsid wsp:val="00C93EE0" />
					<wsp:rsid wsp:val="00DD6267" />
				</wsp:rsids>
			</w:docPr>
			
			<w:body>
				<wx:sect>
				
					<!-- Titre -->
					<w:p wsp:rsidR="00C93EE0" wsp:rsidRDefault="00C93EE0" wsp:rsidP="00C93EE0">
						<w:pPr><w:jc w:val="center" /><w:rPr><w:b /><w:sz w:val="28" /></w:rPr></w:pPr>
						<w:r wsp:rsidRPr="007A6024"><w:rPr><w:b /><w:sz w:val="28" /></w:rPr><w:t>Communiqué de <xsl:value-of select="/concours/organisateur/@nom" /> <xsl:if test="/concours/organisateur/@lieu != ''">(<xsl:value-of select="/concours/organisateur/@lieu" />)</xsl:if></w:t></w:r>
						<w:proofErr w:type="gramEnd" />
						<w:r wsp:rsidRPr="007A6024"><w:rPr><w:b /><w:sz w:val="28" /></w:rPr><w:br /><w:t><xsl:value-of select="java:format(java:java.text.SimpleDateFormat.new('dd/MM/yyyy'),java:java.util.Date.new())" /> – <xsl:value-of select="/concours/@nom" /></w:t></w:r>
						<w:r wsp:rsidRPr="007A6024"><w:rPr><w:b /><w:sz w:val="28" /></w:rPr><w:br /><w:t>Organisé par <xsl:value-of select="/concours/organisateur/@nom" /> <xsl:if test="/concours/@lieu != ''">à <xsl:value-of select="/concours/@lieu" /></xsl:if></w:t></w:r>
					</w:p>
					
					<!-- Accroche du communiqué -->
					<w:p wsp:rsidR="009D64F5" wsp:rsidRPr="00315910" wsp:rsidRDefault="009D64F5" wsp:rsidP="00315910">
						<w:pPr><w:jc w:val="both" /><w:rPr><w:color w:val="FF0000" /></w:rPr></w:pPr>
						<w:r wsp:rsidRPr="00315910"><w:rPr><w:color w:val="FF0000" /></w:rPr><w:t>Emplacement réservé à l’accroche du communiqué : le succès au rendez-vous,  un public venu en nombre, les participants ravis, une très bonne ambiance, des partenaires généreux, des personnalités présentes, …</w:t></w:r>
					</w:p>
					
					<!-- A propos de l'évenement -->
					<xsl:if test="/concours/@description != ''">
						<!-- Titre -->
						<w:p wsp:rsidR="00C93EE0" wsp:rsidRPr="007A6024" wsp:rsidRDefault="00C93EE0" wsp:rsidP="00C93EE0">
							<w:pPr><w:rPr><w:b /><w:sz w:val="24" /></w:rPr></w:pPr>
							<w:r wsp:rsidRPr="007A6024"><w:rPr><w:b /><w:sz w:val="24" /></w:rPr><w:t>A propos de l’événement :</w:t></w:r>
						</w:p>
						
						<!-- Description -->
						<w:p wsp:rsidR="00C93EE0" wsp:rsidRDefault="00C93EE0" wsp:rsidP="00C93EE0"><w:r><w:t><xsl:value-of select="/concours/@description" /></w:t></w:r></w:p>
						
						<!-- Site web -->
						<xsl:if test="/concours/@site != ''">
							<w:p wsp:rsidR="00C93EE0" wsp:rsidRPr="007A6024" wsp:rsidRDefault="00C93EE0" wsp:rsidP="00C93EE0">
								<w:pPr><w:rPr><w:i /></w:rPr></w:pPr>
								<w:r wsp:rsidRPr="007A6024"><w:rPr><w:i /></w:rPr><w:t>Informations supplémentaires sur  </w:t></w:r>
								<w:hlink w:dest="{/concours/@site}"><w:r wsp:rsidRPr="007A6024"><w:rPr><w:rStyle w:val="Lienhypertexte" /><w:i /></w:rPr><w:t> <xsl:value-of select="/concours/@site" /></w:t></w:r></w:hlink>
							</w:p>
						</xsl:if>
						
						<!-- Contact -->
						<xsl:if test="/concours/@telephone != '' or /concours/@email != ''">
							<w:p wsp:rsidR="00C93EE0" wsp:rsidRDefault="00C93EE0" wsp:rsidP="00C93EE0">
								<w:r wsp:rsidRPr="007A6024"><w:rPr><w:b /></w:rPr><w:t>Contact :</w:t></w:r>
								<w:r><w:t> </w:t></w:r>
								<xsl:if test="/concours/@telephone != ''">
									<w:r><w:t><xsl:value-of select="/concours/@telephone" /></w:t></w:r>
								</xsl:if>
								<xsl:if test="/concours/@telephone != '' and /concours/@email != ''">
									<w:r><w:t>, </w:t></w:r>
								</xsl:if>
								<xsl:if test="/concours/@email != ''">
									<w:r><w:t><xsl:value-of select="/concours/@email" /></w:t></w:r>
								</xsl:if>
							</w:p>
						</xsl:if>
					</xsl:if>
					
					<!-- A propos de l'organisateur -->
					<xsl:if test="/concours/organisateur/@description != ''">
						<!-- Titre -->
						<w:p wsp:rsidR="00C93EE0" wsp:rsidRPr="007A6024" wsp:rsidRDefault="00C93EE0" wsp:rsidP="00C93EE0">
							<w:pPr><w:rPr><w:b /><w:sz w:val="24" /></w:rPr></w:pPr>
							<w:r wsp:rsidRPr="007A6024"><w:rPr><w:b /><w:sz w:val="24" /></w:rPr><w:t>A propos de <xsl:value-of select="/concours/organisateur/@nom" /> :</w:t></w:r>
						</w:p>
						
						<!-- Description -->
						<w:p wsp:rsidR="00C93EE0" wsp:rsidRDefault="00C93EE0" wsp:rsidP="00C93EE0"><w:r><w:t><xsl:value-of select="/concours/organisateur/@description" /></w:t></w:r></w:p>
						
						<!-- Site web -->
						<xsl:if test="/concours/organisateur/@site != ''">
							<w:p wsp:rsidR="00C93EE0" wsp:rsidRPr="007A6024" wsp:rsidRDefault="00C93EE0" wsp:rsidP="00C93EE0">
								<w:pPr><w:rPr><w:i /></w:rPr></w:pPr>
								<w:r wsp:rsidRPr="007A6024"><w:rPr><w:i /></w:rPr><w:t>Informations supplémentaires sur  </w:t></w:r>
								<w:hlink w:dest="{/concours/organisateur/@site}"><w:r wsp:rsidRPr="007A6024"><w:rPr><w:rStyle w:val="Lienhypertexte" /><w:i /></w:rPr><w:t> <xsl:value-of select="/concours/organisateur/@site" /></w:t></w:r></w:hlink>
							</w:p>
						</xsl:if>
						
						<!-- Contact -->
						<xsl:if test="/concours/organisateur/@telephone != '' or /concours/organisateur/@email != ''">
							<w:p wsp:rsidR="00C93EE0" wsp:rsidRDefault="00C93EE0" wsp:rsidP="00C93EE0">
								<w:r wsp:rsidRPr="007A6024"><w:rPr><w:b /></w:rPr><w:t>Contact :</w:t></w:r>
								<w:r><w:t> </w:t></w:r>
								<xsl:if test="/concours/organisateur/@telephone != ''">
									<w:r><w:t> <xsl:value-of select="/concours/organisateur/@telephone" /></w:t></w:r>
								</xsl:if>
								<xsl:if test="/concours/organisateur/@telephone != '' and /concours/organisateur/@email != ''">
									<w:r><w:t>, </w:t></w:r>
								</xsl:if>
								<xsl:if test="/concours/organisateur/@email != ''">
									<w:r><w:t><xsl:value-of select="/concours/organisateur/@email" /></w:t></w:r>
								</xsl:if>
							</w:p>
						</xsl:if>
					</xsl:if>
					
					<!-- Saut de page -->
					<w:p wsp:rsidR="00C93EE0" wsp:rsidRDefault="00C93EE0"><w:r><w:br w:type="page" /></w:r></w:p>
					
					<xsl:if test="count(/concours/listeCategories/categorie/listePoules/poule/listePhasesQualificatives/phaseQualificative/matchPhaseQualificative) != 0">
						<!-- Titre des phases qualificatives -->
						<w:p wsp:rsidR="00C93EE0" wsp:rsidRPr="007A6024" wsp:rsidRDefault="00C93EE0" wsp:rsidP="007A6024">
							<w:pPr><w:jc w:val="center" /><w:rPr><w:b /><w:sz w:val="26" /><w:sz-cs w:val="26" /></w:rPr></w:pPr>
							<w:r wsp:rsidRPr="007A6024"><w:rPr><w:b /><w:sz w:val="26" /><w:sz-cs w:val="26" /></w:rPr><w:t>Résultats des phases qualificatives</w:t></w:r>
						</w:p>
					
						<!-- Résultats des phases qualificatives pour chaque catégorie -->
						<xsl:apply-templates select="/concours/listeCategories/categorie" mode="phasesQualifs" />
					</xsl:if>
					
					<xsl:if test="count(/concours/listeCategories/categorie/listePhasesEliminatoires/phaseEliminatoire/matchPhaseEliminatoire) != 0">
						<!-- Titre des phases éliminatoires -->
						<w:p wsp:rsidR="00C93EE0" wsp:rsidRPr="007A6024" wsp:rsidRDefault="00C93EE0" wsp:rsidP="007A6024">
							<w:pPr><w:jc w:val="center" /><w:rPr><w:b /><w:sz w:val="26" /><w:sz-cs w:val="26" /></w:rPr></w:pPr>
							<w:r wsp:rsidRPr="007A6024"><w:rPr><w:b /><w:sz w:val="26" /><w:sz-cs w:val="26" /></w:rPr><w:t>Résultats des phases éliminatoires</w:t></w:r>
						</w:p>
						
						<!-- Résultats des phases éliminatoires pour chaque catégorie -->
						<xsl:apply-templates select="/concours/listeCategories/categorie" mode="phasesElims" />
					</xsl:if>
				</wx:sect>
			</w:body>
		</w:wordDocument>
	</xsl:template>
	
	<!-- Template des résultats des phases qualificatives pour une catégorie -->
	<xsl:template match="categorie" mode="phasesQualifs">
		<xsl:if test="count(./listePoules/poule/listeParticipants/participant) != 0">
			<!-- Nom de la catégorie -->
			<xsl:if test="count(../categorie) != 1">
				<w:p wsp:rsidR="007A6024" wsp:rsidRPr="007A6024" wsp:rsidRDefault="007A6024" wsp:rsidP="007A6024">
					<w:pPr><w:rPr><w:b /><w:sz w:val="24" /></w:rPr></w:pPr>
					<w:r wsp:rsidRPr="007A6024"><w:rPr><w:b /><w:sz w:val="24" /></w:rPr><w:t><xsl:value-of select="./@nom" /> : </w:t></w:r>
				</w:p>
			</xsl:if>
						
			<!-- Résultats des phases qualificatives pour chaque poule -->
			<xsl:apply-templates select="./listePoules/poule" mode="phasesQualifs" />
		</xsl:if>
	</xsl:template>
	
	<!-- Template des résultats des phases qualificatives pour une poule -->
	<xsl:template match="poule" mode="phasesQualifs">
		<xsl:if test="count(./listeParticipants/participant) != 0">
			<!-- Nom de la poule -->
			<xsl:if test="count(../poule) != 1">
				<xsl:choose>
					<xsl:when test="count(../../../categorie) = 1">
						<w:p wsp:rsidR="007A6024" wsp:rsidRPr="007A6024" wsp:rsidRDefault="007A6024" wsp:rsidP="007A6024">
							<w:pPr><w:rPr><w:b /><w:sz w:val="24" /></w:rPr></w:pPr>
							<w:r wsp:rsidRPr="007A6024"><w:rPr><w:b /><w:sz w:val="24" /></w:rPr><w:t><xsl:value-of select="./@nom" /> : </w:t></w:r>
						</w:p>
					</xsl:when>
					<xsl:otherwise>
						<w:p wsp:rsidR="009D64F5" wsp:rsidRDefault="007A6024" wsp:rsidP="009D64F5">
							<w:r><w:t><xsl:value-of select="./@nom" /> :</w:t></w:r>
						</w:p>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:if>
			
			<!-- Résultats -->
			<w:tbl>
				<w:tblPr>
					<w:tblW w:w="5000" w:type="pct" />
					<w:tblBorders><w:top w:val="single" w:sz="4" wx:bdrwidth="10" w:space="0" w:color="auto" />
						<w:left w:val="single" w:sz="4" wx:bdrwidth="10" w:space="0" w:color="auto" />
						<w:bottom w:val="single" w:sz="4" wx:bdrwidth="10" w:space="0" w:color="auto" />
						<w:right w:val="single" w:sz="4" wx:bdrwidth="10" w:space="0" w:color="auto" />
						<w:insideH w:val="single" w:sz="4" wx:bdrwidth="10" w:space="0" w:color="auto" />
						<w:insideV w:val="single" w:sz="4" wx:bdrwidth="10" w:space="0" w:color="auto" />
					</w:tblBorders>
					<w:tblLook w:val="04A0" />
				</w:tblPr>
				<w:tblGrid>
					<w:gridCol w:w="1133" />
					<w:gridCol w:w="3511" />
					<w:gridCol w:w="3511" />
					<w:gridCol w:w="1133" />
				</w:tblGrid>
				<w:tr wsp:rsidR="009D64F5" wsp:rsidRPr="00243991" wsp:rsidTr="00243991">
					<w:tc>
						<w:tcPr><w:tcW w:w="610" w:type="pct" /><w:shd w:val="clear" w:color="auto" w:fill="8DB3E2" /></w:tcPr>
						<w:p wsp:rsidR="009D64F5" wsp:rsidRPr="00243991" wsp:rsidRDefault="009D64F5" wsp:rsidP="00243991">
							<w:pPr><w:spacing w:after="0" w:line="240" w:line-rule="auto" /><w:rPr><w:b /></w:rPr></w:pPr>
							<w:r wsp:rsidRPr="00243991"><w:rPr><w:b /></w:rPr><w:t>Rang</w:t></w:r>
						</w:p>
					</w:tc>
					<w:tc>
						<w:tcPr><w:tcW w:w="1890" w:type="pct" /><w:shd w:val="clear" w:color="auto" w:fill="8DB3E2" /></w:tcPr>
						<w:p wsp:rsidR="009D64F5" wsp:rsidRPr="00243991" wsp:rsidRDefault="009D64F5" wsp:rsidP="00243991">
							<w:pPr><w:spacing w:after="0" w:line="240" w:line-rule="auto" /><w:rPr><w:b /></w:rPr></w:pPr>
							<w:r wsp:rsidRPr="00243991"><w:rPr><w:b /></w:rPr><w:t>Nom</w:t></w:r>
						</w:p>
					</w:tc>
					<w:tc>
						<w:tcPr><w:tcW w:w="1890" w:type="pct" /><w:shd w:val="clear" w:color="auto" w:fill="8DB3E2" /></w:tcPr>
						<w:p wsp:rsidR="009D64F5" wsp:rsidRPr="00243991" wsp:rsidRDefault="009D64F5" wsp:rsidP="00243991">
							<w:pPr><w:spacing w:after="0" w:line="240" w:line-rule="auto" /><w:rPr><w:b /></w:rPr></w:pPr>
							<w:r wsp:rsidRPr="00243991"><w:rPr><w:b /></w:rPr><w:t>Ville</w:t></w:r>
						</w:p>
					</w:tc>
					<w:tc>
						<w:tcPr><w:tcW w:w="610" w:type="pct" /><w:shd w:val="clear" w:color="auto" w:fill="8DB3E2" /></w:tcPr>
						<w:p wsp:rsidR="009D64F5" wsp:rsidRPr="00243991" wsp:rsidRDefault="009D64F5" wsp:rsidP="00243991">
							<w:pPr><w:spacing w:after="0" w:line="240" w:line-rule="auto" /><w:rPr><w:b /></w:rPr></w:pPr>
							<w:r wsp:rsidRPr="00243991"><w:rPr><w:b /></w:rPr><w:t>Points</w:t></w:r>
						</w:p>
					</w:tc>
				</w:tr>
				
				<!-- Classement -->
				<xsl:apply-templates select="./classementPoulePhasesQualificatives/listeClassementParticipants/classementParticipant" mode="phasesQualifs">
					<xsl:sort select="@rang" data-type="number" />
				</xsl:apply-templates>
			</w:tbl>
			
			<!-- Espacement -->
			<w:p wsp:rsidR="007A6024" wsp:rsidRDefault="007A6024" wsp:rsidP="009D64F5" />
		</xsl:if>
	</xsl:template>
	
	<!-- Template des résultats des phases qualificatives pour un participant -->
	<xsl:template match="classementParticipant" mode="phasesQualifs">
		<xsl:variable name="id" select="./@refParticipant" />
		<w:tr wsp:rsidR="009D64F5" wsp:rsidRPr="00243991" wsp:rsidTr="00243991">
			<w:tc>
				<w:tcPr><w:tcW w:w="610" w:type="pct" /><w:shd w:val="clear" w:color="auto" w:fill="auto" /></w:tcPr>
				<w:p wsp:rsidR="009D64F5" wsp:rsidRPr="00243991" wsp:rsidRDefault="009D64F5" wsp:rsidP="00243991">
					<w:pPr><w:spacing w:after="0" w:line="240" w:line-rule="auto" /></w:pPr>
					<w:r wsp:rsidRPr="00243991"><w:t><xsl:value-of select="./@rang" /></w:t></w:r>
				</w:p>
			</w:tc>
			<w:tc>
				<w:tcPr><w:tcW w:w="1890" w:type="pct" /><w:shd w:val="clear" w:color="auto" w:fill="auto" /></w:tcPr>
				<w:p wsp:rsidR="009D64F5" wsp:rsidRPr="00243991" wsp:rsidRDefault="009D64F5" wsp:rsidP="00243991">
					<w:pPr><w:spacing w:after="0" w:line="240" w:line-rule="auto" /></w:pPr>
					<w:r wsp:rsidRPr="00243991"><w:t><xsl:value-of select="//participant[@id=$id]/@nom" /></w:t></w:r>
				</w:p>
			</w:tc>
			<w:tc>
				<w:tcPr><w:tcW w:w="1890" w:type="pct" /><w:shd w:val="clear" w:color="auto" w:fill="auto" /></w:tcPr>
				<w:p wsp:rsidR="009D64F5" wsp:rsidRPr="00243991" wsp:rsidRDefault="009D64F5" wsp:rsidP="00243991">
					<w:pPr><w:spacing w:after="0" w:line="240" w:line-rule="auto" /></w:pPr>
					<w:r wsp:rsidRPr="00243991"><w:t><xsl:value-of select="//participant[@id=$id]/@ville" /></w:t></w:r>
				</w:p>
				</w:tc>
			<w:tc>
				<w:tcPr><w:tcW w:w="610" w:type="pct" /><w:shd w:val="clear" w:color="auto" w:fill="auto" /></w:tcPr>
				<w:p wsp:rsidR="009D64F5" wsp:rsidRPr="00243991" wsp:rsidRDefault="009D64F5" wsp:rsidP="00243991">
				<w:pPr><w:spacing w:after="0" w:line="240" w:line-rule="auto" /></w:pPr>
				<w:r wsp:rsidRPr="00243991"><w:t><xsl:value-of select="//participant[@id=$id]/@pointsPhasesQualifs" /></w:t></w:r>
				</w:p>
			</w:tc>
		</w:tr>
	</xsl:template>
	
	<!-- Template des résultats des phases éliminatoires pour une catégorie -->
	<xsl:template match="categorie" mode="phasesElims">
		<xsl:if test="count(./listePoules/poule/listeParticipants/participant) != 0">
			<!-- Nom de la catégorie -->
			<xsl:if test="count(../categorie) != 1">
				<w:p wsp:rsidR="007A6024" wsp:rsidRPr="007A6024" wsp:rsidRDefault="007A6024" wsp:rsidP="007A6024">
					<w:pPr><w:rPr><w:b /><w:sz w:val="24" /></w:rPr></w:pPr>
					<w:r wsp:rsidRPr="007A6024"><w:rPr><w:b /><w:sz w:val="24" /></w:rPr><w:t><xsl:value-of select="./@nom" /> : </w:t></w:r>
				</w:p>
			</xsl:if>
						
			<w:tbl>
				<w:tblPr>
					<w:tblW w:w="4942" w:type="pct" />
					<w:tblBorders><w:top w:val="single" w:sz="4" wx:bdrwidth="10" w:space="0" w:color="auto" />
						<w:left w:val="single" w:sz="4" wx:bdrwidth="10" w:space="0" w:color="auto" />
						<w:bottom w:val="single" w:sz="4" wx:bdrwidth="10" w:space="0" w:color="auto" />
						<w:right w:val="single" w:sz="4" wx:bdrwidth="10" w:space="0" w:color="auto" />
						<w:insideH w:val="single" w:sz="4" wx:bdrwidth="10" w:space="0" w:color="auto" />
						<w:insideV w:val="single" w:sz="4" wx:bdrwidth="10" w:space="0" w:color="auto" />
					</w:tblBorders>
					<w:tblLook w:val="04A0" />
				</w:tblPr>
				<w:tblGrid>
					<w:gridCol w:w="1134" /><w:gridCol w:w="4025" /><w:gridCol w:w="4021" />
				</w:tblGrid>
				<w:tr wsp:rsidR="009D64F5" wsp:rsidRPr="00243991" wsp:rsidTr="00243991">
					<w:tc>
						<w:tcPr><w:tcW w:w="618" w:type="pct" /><w:shd w:val="clear" w:color="auto" w:fill="8DB3E2" /></w:tcPr>
						<w:p wsp:rsidR="009D64F5" wsp:rsidRPr="00243991" wsp:rsidRDefault="009D64F5" wsp:rsidP="00243991">
							<w:pPr><w:spacing w:after="0" w:line="240" w:line-rule="auto" /><w:rPr><w:b /></w:rPr></w:pPr>
							<w:r wsp:rsidRPr="00243991"><w:rPr><w:b /></w:rPr><w:t>Rang</w:t></w:r>
						</w:p>
					</w:tc>
					<w:tc>
						<w:tcPr><w:tcW w:w="2192" w:type="pct" /><w:shd w:val="clear" w:color="auto" w:fill="8DB3E2" /></w:tcPr>
						<w:p wsp:rsidR="009D64F5" wsp:rsidRPr="00243991" wsp:rsidRDefault="009D64F5" wsp:rsidP="00243991">
							<w:pPr><w:spacing w:after="0" w:line="240" w:line-rule="auto" /><w:rPr><w:b /></w:rPr></w:pPr>
							<w:r wsp:rsidRPr="00243991"><w:rPr><w:b /></w:rPr><w:t>Nom</w:t></w:r>
						</w:p>
					</w:tc>
					<w:tc>
						<w:tcPr><w:tcW w:w="2191" w:type="pct" /><w:shd w:val="clear" w:color="auto" w:fill="8DB3E2" /></w:tcPr>
						<w:p wsp:rsidR="009D64F5" wsp:rsidRPr="00243991" wsp:rsidRDefault="009D64F5" wsp:rsidP="00243991">
							<w:pPr><w:spacing w:after="0" w:line="240" w:line-rule="auto" /><w:rPr><w:b /></w:rPr></w:pPr>
							<w:r wsp:rsidRPr="00243991"><w:rPr><w:b /></w:rPr><w:t>Ville</w:t></w:r>
						</w:p>
					</w:tc>
				</w:tr>
				
				<!-- Classement -->
				<xsl:apply-templates select="./classementCategoriePhasesEliminatoires/listeClassementParticipants/classementParticipant" mode="phasesElims">
					<xsl:sort select="@rang" data-type="number" />
				</xsl:apply-templates>
			</w:tbl>
			
			<!-- Espacement -->
			<w:p wsp:rsidR="007A6024" wsp:rsidRDefault="007A6024" wsp:rsidP="00C93EE0" />
		</xsl:if>
	</xsl:template>
	
	<!-- Template des résultats des phases qualificatives pour un participant -->
	<xsl:template match="classementParticipant" mode="phasesElims">
		<xsl:variable name="id" select="./@refParticipant" />
		<w:tr wsp:rsidR="009D64F5" wsp:rsidRPr="00243991" wsp:rsidTr="00243991">
			<w:tc>
				<w:tcPr><w:tcW w:w="618" w:type="pct" /><w:shd w:val="clear" w:color="auto" w:fill="auto" /></w:tcPr>
				<w:p wsp:rsidR="009D64F5" wsp:rsidRPr="00243991" wsp:rsidRDefault="009D64F5" wsp:rsidP="00243991">
					<w:pPr><w:spacing w:after="0" w:line="240" w:line-rule="auto" /></w:pPr>
					<w:r wsp:rsidRPr="00243991"><w:t><xsl:value-of select="./@rang" /></w:t></w:r>
				</w:p>
			</w:tc>
			<w:tc>
				<w:tcPr><w:tcW w:w="2192" w:type="pct" /><w:shd w:val="clear" w:color="auto" w:fill="auto" /></w:tcPr>
				<w:p wsp:rsidR="009D64F5" wsp:rsidRPr="00243991" wsp:rsidRDefault="009D64F5" wsp:rsidP="00243991">
					<w:pPr><w:spacing w:after="0" w:line="240" w:line-rule="auto" /></w:pPr>
					<w:r wsp:rsidRPr="00243991"><w:t><xsl:value-of select="//participant[@id=$id]/@nom" /></w:t></w:r></w:p>
			</w:tc>
			<w:tc>
				<w:tcPr><w:tcW w:w="2191" w:type="pct" /><w:shd w:val="clear" w:color="auto" w:fill="auto" /></w:tcPr>
				<w:p wsp:rsidR="009D64F5" wsp:rsidRPr="00243991" wsp:rsidRDefault="009D64F5" wsp:rsidP="00243991">
					<w:pPr><w:spacing w:after="0" w:line="240" w:line-rule="auto" /></w:pPr>
					<w:r wsp:rsidRPr="00243991"><w:t><xsl:value-of select="//participant[@id=$id]/@ville" /></w:t></w:r></w:p>
			</w:tc>
			</w:tr>
	</xsl:template>
</xsl:stylesheet>