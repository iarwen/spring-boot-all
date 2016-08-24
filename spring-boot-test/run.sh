#/bin/bash
cd /home/arwencompany/桌面/springboot-app1
WORKDIR=$PWD
SRCHOME=/home/arwencompany/IdeaProjects/spring-boot-all

echo 当前工作目录 $WORKDIR
sleep 1
rm -rf ./lib
cd $SRCHOME
mvn clean install -Dmaven.test.skip=true

cd $SRCHOME/spring-boot-test
mvn dependency:copy-dependencies -DincludeScope=compile -DoutputDirectory=./target/lib
cp -rf $SRCHOME/spring-boot-test/target/lib $WORKDIR
cd ..
mvn clean package -Dmaven.test.skip=true
read -s -n1 -p "按任意键继续 ... "
cp $SRCHOME/spring-boot-test/target/spring-boot-test-0.0.1.jar $WORKDIR

cd $WORKDIR
java -jar spring-boot-test-0.0.1.jar
