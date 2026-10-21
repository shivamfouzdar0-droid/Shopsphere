@echo off
title ShopSphere Localhost Server
echo ===================================================================
echo                     SHOPSPHERE LOCALHOST RUNNER
echo ===================================================================

set "PROJECT_DIR=%~dp0"
set "CATALINA_HOME=C:\Users\shubh\Downloads\apache-tomcat-10.1.59-windows-x64\apache-tomcat-10.1.59"
set "JAVA_HOME=C:\Program Files\Java\jdk-25.0.2"

if not exist "%PROJECT_DIR%web\target\shopsphere.war" (
    echo [1/3] Packaging shopsphere.war with Maven...
    pushd "%PROJECT_DIR%web"
    call mvn package -DskipTests
    popd
) else (
    echo [1/3] Found existing shopsphere.war package.
)

echo [2/3] Deploying shopsphere.war to Tomcat webapps...
copy /Y "%PROJECT_DIR%web\target\shopsphere.war" "%CATALINA_HOME%\webapps\shopsphere.war" >nul

echo [3/3] Opening browser at http://localhost:8080/shopsphere/ ...
start "" "http://localhost:8080/shopsphere/"

echo ===================================================================
echo  Tomcat is starting. Keep this window OPEN while testing.
echo  To stop the server, press Ctrl + C or close this window.
echo ===================================================================
call "%CATALINA_HOME%\bin\catalina.bat" run
