@echo OFF

REM Se rendre dans le r�pertoire de la documentation
cd ..\..\doc

REM G�n�rer la documentation PDF
java -jar ..\lib\saxon-6.5.5\saxon.jar documentation.xml ..\make\generer-doc\documentation-pdf.xsl > documentation.fo
java -jar ..\lib\fop-1.0\fop-1.0.jar documentation.fo -pdf documentation.pdf
del documentation.fo

REM Se rendre dans le r�pertoire de g�n�ration de la documentation
cd ..\make\generer-doc