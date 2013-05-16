@echo OFF

REM Générer la documentation CHM
mkdir temp
cd temp
mkdir images
xcopy /e ..\..\..\doc\images .\images
copy ..\documentation-chm.css .\documentation.css
java -jar ..\..\..\lib\saxon-6.5.5\saxon.jar ..\..\..\doc\documentation.xml ..\documentation-chm.xsl
hhc htmlhelp
copy htmlhelp.chm ..\..\..\doc\documentation.chm
cd ..
rmdir /s /q temp