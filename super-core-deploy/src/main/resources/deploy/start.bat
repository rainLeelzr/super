title "@project.artifactId@"
chcp 65001
java -Xms512M -Xmx2048M -XX:MetaspaceSize=512M -XX:MaxMetaspaceSize=2048M -Dfile.encoding=UTF-8 -jar @project.artifactId@.jar
pause