@echo off
set "CATALINA_HOME=C:\Users\shubh\Downloads\apache-tomcat-10.1.59-windows-x64\apache-tomcat-10.1.59"
set "CATALINA_BASE=C:\Users\shubh\Downloads\apache-tomcat-10.1.59-windows-x64\apache-tomcat-10.1.59"
set "JAVA_HOME=C:\Program Files\Java\jdk-25.0.2"

echo Deploying latest shopsphere.war to Tomcat webapps...
copy /Y "C:\Users\shubh\.gemini\ShopSphere\web\target\shopsphere.war" "%CATALINA_HOME%\webapps\shopsphere.war"

echo Starting Apache Tomcat...
call "%CATALINA_HOME%\bin\catalina.bat" %*
