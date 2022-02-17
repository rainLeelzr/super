#!/bin/sh

#-------------------------------------------------------------------
# 定义变量，可修改以下变量
#-------------------------------------------------------------------

# 模块名
PROJECT_NAME="@project.artifactId@"

# 运行包名
PROJECT_JAR="@project.artifactId@-exec.jar"

# 默认JVM内存参数，如果设置了环境变量 JVM_MEMORY_VARS，则会被环境变量覆盖
JVM_DEFAULT_MEMORY_VARS=" -Xms6G -Xmx6G -Xmn3G -XX:MetaspaceSize=256M -XX:MaxMetaspaceSize=256M "

# 默认JVM非内存参数，如果设置了环境变量 JVM_VARS，则会被环境变量覆盖
JVM_DEFAULT_VARS=" -server -XX:SurvivorRatio=8 -XX:InitialSurvivorRatio=8 -XX:+PrintCommandLineFlags -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintCommandLineFlags "

# 日志输出目录
LOG_PATH="./logs/@service-name@"

# 启动后是否自动打印日志，如果设置了环境变量 AUTO_PRINT_LOG，则会被环境变量覆盖
AUTO_PRINT_LOG_DEFAULT="true"

#-------------------------------------------------------------------
# 以下内容请不要修改
#-------------------------------------------------------------------

PID=''

print_usage() {
        echo "./run.sh                           ---Start the ${PROJECT_NAME} server."
        echo "./run.sh start                     ---Start the ${PROJECT_NAME} server."
        echo "./run.sh stop                      ---Stop the ${PROJECT_NAME} server."
        echo "./run.sh status                    ---Status the ${PROJECT_NAME} server."
        echo "./run.sh log                       ---print log."
        echo "./run.sh h|H|help|HELP             ---Print help information."
}

get_pid() {
        if [ ! -f "application.pid" ]; then
                echo "file 'application.pid' not found."
                PID=''
        else
                PID=`head -n 1 application.pid`
                echo "found pid[${PID}] in file './application.pid'"
                PID="${PID}"
        fi
}

start() {
        check_jdk

        get_pid

        if [ "$PID" != "" ]; then
                if [ -d "/proc/${PID}" ]; then
                        echo "${PROJECT_NAME} is running, pid is ${PID}, can not start repeatedly!"
                        exit 1
                fi
        fi

        echo "try to start ${PROJECT_NAME} ..."
        echo ""

        JVM_VARS_TEMP=''
        if [ -n "$JVM_VARS" ]; then
                echo "found env var: JVM_VARS=${JVM_VARS}"
                JVM_VARS_TEMP=$JVM_VARS
        else
                JVM_VARS_TEMP=${JVM_DEFAULT_VARS}
        fi

        JVM_MEMORY_VARS_TEMP=''
        if [ -n "$JVM_MEMORY_VARS" ]; then
                echo "found env var: JVM_MEMORY_VARS=$JVM_MEMORY_VARS"
                JVM_MEMORY_VARS_TEMP=$JVM_MEMORY_VARS
        else
                JVM_MEMORY_VARS_TEMP=${JVM_DEFAULT_MEMORY_VARS}
        fi
        echo ""
        echo "executing cmd: java ${JVM_VARS_TEMP} ${JVM_MEMORY_VARS_TEMP} -jar ${PROJECT_JAR} 1>/dev/null 2>&1 &"

        nohup java ${JVM_VARS_TEMP} ${JVM_MEMORY_VARS_TEMP} -jar ${PROJECT_JAR} 1>/dev/null 2>&1 &

        echo ''

        sleep 5

        AUTO_PRINT_LOG_TEMP=''
        if [ -n "$AUTO_PRINT_LOG" ]; then
                AUTO_PRINT_LOG_TEMP=$AUTO_PRINT_LOG
        else
                AUTO_PRINT_LOG_TEMP=${AUTO_PRINT_LOG_DEFAULT}
        fi

        if [ $AUTO_PRINT_LOG_TEMP = "true" ]; then
            echo 'log will printing after 5 second using command "tail -f -n 500" automatic.'
            echo 'you can use "ctrl+c" to exit log printing, and will not close the application.'
            echo ''

            print_log
        else
            echo "app started, use './run.sh status' to check status"
        fi


        if [ -n "$KEEP_RUNNING" ]; then
            tail -f /dev/null
        fi
}

stop() {
        echo "try to stop ${PROJECT_NAME} ..."

        get_pid

        if [ "$PID" = "" ]; then
                echo "${PROJECT_NAME} is not running!"
                return 0
        fi

        if [ -d "/proc/${PID}" ]; then
                echo "${PROJECT_NAME} is running, pid is ${PID}"
                kill ${PID}
                if [ $? -ne 0 ]; then
                        echo "failed to stop ${PROJECT_NAME}!"
                        return 1
                else
                        echo "${PROJECT_NAME} stopped."
                        return 0
                fi
        else
                echo "${PROJECT_NAME} is not running!"
        fi
}

status() {
        get_pid
        if [ "$PID" = "" ]; then
                echo "${PROJECT_NAME} is not running."
        else
                if [ -d "/proc/${PID}" ]; then
                        echo "${PROJECT_NAME} is running. pid ${PID}."
                else
                        echo "can not found running pid ${PID}, ${PROJECT_NAME} is not running!"
                fi                
        fi
}

print_log() {
        if [ ! -d ${LOG_PATH} ]; then
                echo "print log error, can not found log folder [${LOG_PATH}], please try print log later."
        else
                cd ${LOG_PATH}
                filename=$(ls -t | head -n1 | awk '{print $0}')
                tail -f -n 500 $filename
        fi
}

check_jdk() {
        if command -v java >/dev/null; then
                JAVA_VERSION=`java -version 2>&1 | sed '1!d' | sed -e 's/"//g' -e 's/version//'`
                echo "JAVA_VERSION: ${JAVA_VERSION}"
        else
                echo "jdk is not install, please install first!"
                exit 1
        fi
}

run() {
        if [ $# -eq 0 ]; then
                start
        else
                case "$1" in
                start) start ;;
                stop) stop ;;
                status) status ;;
                log) print_log ;;
                h|H|help|HELP ) print_usage ;;
                *)
                        echo "illage parameter : $1"
                        echo ""
                        print_usage
                        ;;
                esac

        fi
}

run $1
