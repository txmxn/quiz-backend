@echo off

set JAVA_HOME=C:\Users\TKayser\Documents\jdk-15
set M2_HOME=C:\Users\TKayser\Documents\apache-maven-3.8.1

echo This script is intended to build quiz backend
echo
echo Settings:
echo JAVA_HOME=%JAVA_HOME%
call %JAVA_HOME%\bin\java -version
echo M2_HOME=%M2_HOME%
call %M2_HOME%\bin\mvn -version
echo
echo Running command:
echo %M2_HOME%\bin\mvn -P production clean package dockerfile:build
echo
echo Executing:
%M2_HOME%\bin\mvn -P production clean package dockerfile:build
