#!/bin/sh

#-------------------------------------------------------------------
# 定义变量，可修改以下变量
#-------------------------------------------------------------------

# 模块名
project_name="@project.artifactId@"

# 运行包名
project_jar="@project.artifactId@-exec.jar"

# 默认JVM内存参数，如果设置了环境变量 JVM_MEMORY_VARS ，则会被环境变量覆盖
jvm_memory_vars=${JVM_MEMORY_VARS="-Xms6G -Xmx6G -Xmn3G -XX:MetaspaceSize=256M -XX:MaxMetaspaceSize=256M"}

# 默认JVM非内存参数，如果设置了环境变量 JVM_VARS ，则会被环境变量覆盖
jvm_vars=${JVM_VARS="-server -XX:SurvivorRatio=8 -XX:InitialSurvivorRatio=8 -XX:+PrintCommandLineFlags"}

# 是否打印gc信息
jvm_print_gc=${JVM_PRINT_GC="false"}

# java 远程调试端口。当设置此值时，本java应用会开启此端口，提供给开发人员进行连接，从而实现代码级别调试
debug_port=${DEBUG_PORT=""}

# 日志输出目录
log_path="./logs/"

# 启动后是否自动打印日志，如果设置了环境变量 AUTO_TAIL_LOG，则会被环境变量覆盖
auto_tail_log=${AUTO_TAIL_LOG="true"}

# 启动 java 的命令是否结合 nohup 进行不挂断运行，如果设置了环境变量 RUN_AS_NOHUP，则会被环境变量覆盖
run_as_nohup=${RUN_AS_NOHUP="true"}

# 当在 docker 环境中，启动 java 报错后，会导致容器退出，可配置此参数，阻止容器退出，便于进入容器调试问题
keep_docker_running=${KEEP_DOCKER_RUNNING="false"}

#-------------------------------------------------------------------
# 以下内容请不要修改
#-------------------------------------------------------------------

pid=''

print_usage() {
        echo "./run.sh                           ---Start the ${project_name} server."
        echo "./run.sh start                     ---Start the ${project_name} server."
        echo "./run.sh stop                      ---Stop the ${project_name} server."
        echo "./run.sh status                    ---Status the ${project_name} server."
        echo "./run.sh log                       ---print log."
        echo "./run.sh h|H|help|HELP             ---Print help information."
}

get_pid() {
        if [ ! -f "application.pid" ]; then
                echo "file 'application.pid' not found."
                pid=''
        else
                pid=`head -n 1 application.pid`
                echo "found pid[${pid}] in file './application.pid'"
                pid="${pid}"
        fi
}

start() {
        check_jdk

        get_pid

        if [ "$pid" != "" ]; then
                if [ -d "/proc/${pid}" ]; then
                        echo "${project_name} is running, pid is ${pid}, can not start repeatedly!"
                        exit 1
                fi
        fi

        echo "try to start ${project_name} ..."
        echo ""

        jvm_params="${jvm_vars} ${jvm_memory_vars}"
        if [ $jvm_print_gc = "true" ]; then
            jvm_params="${jvm_params} -XX:+PrintGC -XX:+PrintGCDetails"
        fi
        if [ -n "$debug_port" ]; then
            jvm_params="${jvm_params} -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=$debug_port"
        fi

        cmd="java ${jvm_params} -jar ${project_jar}"
        if [ $run_as_nohup = "true" ]; then
            cmd="nohup $cmd 1>/dev/null 2>&1 &"
        fi

        echo "executing cmd:"
        echo "$cmd"
        eval $cmd
        echo ""

        if [ $auto_tail_log = "true" ]; then
            echo 'log will printing after 5 second using command "tail -f -n 500" automatic.'
            echo 'you can use "ctrl+c" to exit log printing, and will not close the application.'
            echo ''

            sleep 5
            print_log
        else
            echo "app started, use './run.sh status' to check status"
        fi


        if [ $keep_docker_running = "true" ]; then
            tail -f /dev/null
        fi
}

stop() {
        echo "try to stop ${project_name} ..."

        get_pid

        if [ "$pid" = "" ]; then
                echo "${project_name} is not running!"
                return 0
        fi

        if [ -d "/proc/${pid}" ]; then
                echo "${project_name} is running, pid is ${pid}"
                kill ${pid}
                if [ $? -ne 0 ]; then
                        echo "failed to stop ${project_name}!"
                        return 1
                else
                        echo "${project_name} stopped."
                        return 0
                fi
        else
                echo "${project_name} is not running!"
        fi
}

status() {
        get_pid
        if [ "$pid" = "" ]; then
                echo "${project_name} is not running."
        else
                if [ -d "/proc/${pid}" ]; then
                        echo "${project_name} is running. pid ${pid}."
                else
                        echo "can not found running pid ${pid}, ${project_name} is not running!"
                fi
        fi
}

print_log() {
        if [ ! -d ${log_path} ]; then
                echo "print log error, can not found log folder [${log_path}], please try print log later."
        else
                cd ${log_path}
                filename=$(ls -t | head -n1 | awk '{print $0}')
                tail -f -n 500 $filename
        fi
}

check_jdk() {
        if command -v java >/dev/null; then
                java_version=`java -version 2>&1 | sed '1!d' | sed -e 's/"//g' -e 's/version//'`
                echo "java_version: ${java_version}"
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
                h|H|help|HELP) print_usage ;;
                *)
                        echo "illegal parameter : $1"
                        echo ""
                        print_usage
                        ;;
                esac
        fi
}

run $1
