#!/bin/bash

export JAVA_HOME=~/Work/java/jdk-15.0.2
export M2_HOME=~/Work/tools/maven/maven-main

echo This script is intended to build quiz backend
echo
echo Settings:
echo JAVA_HOME=$JAVA_HOME
$JAVA_HOME/bin/java -version
echo M2_HOME=$M2_HOME
$M2_HOME/bin/mvn -version
echo
echo Running command:
echo $M2_HOME/bin/mvn -P production clean package dockerfile:build
echo
echo Executing:
$M2_HOME/bin/mvn -P production clean package dockerfile:build
