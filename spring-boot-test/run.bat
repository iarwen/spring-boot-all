@echo off
title 我的app
set WORKDIR="%cd%"
echo 当前工作目录%WORKDIR%
cd /D F:\workspace_icar_server\spring-boot-test
call mvn clean
call mvn package -Dmaven.test.skip=true
pause
copy /y .\target\spring-boot-test-0.0.1.jar %WORKDIR%\
cd /D %WORKDIR%
java -jar spring-boot-test-0.0.1.jar
