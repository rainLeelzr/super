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
echo '5秒后将自动打印日志，ctrl+c 可退出日志，且不会关闭正在运行的程序...'

sleep 5

tail -f -n 1000 ./logs/@service-name@/*.log

