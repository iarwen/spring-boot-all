@echo off
title �ҵ�app
cd /D C:\Users\Administrator\Desktop\server
set WORKDIR="%cd%"
echo ��ǰ����Ŀ¼%WORKDIR%
cd /D F:\workspace_icar_server\spring-boot-root\spring-boot-test
call mvn clean
call mvn package -Dmaven.test.skip=true
pause
copy /y .\target\spring-boot-test-0.0.1.jar %WORKDIR%\
cd /D %WORKDIR%
java -jar spring-boot-test-0.0.1.jar
