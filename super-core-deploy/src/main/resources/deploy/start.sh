nohup \
java \
-Xms512M \
-Xmx2048M \
-XX:MetaspaceSize=512M \
-XX:MaxMetaspaceSize=2048M \
-jar \
@project.artifactId@.jar \
1>/dev/null 2>&1 &

echo 'starting...'

sleep 5

tail -f -n 1000 ./logs/@service-name@/*.log

