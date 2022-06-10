#!/bin/sh

#-------------------------------------------------------------------
# 定义变量，可修改以下变量
# 变量值的优先级：运行脚本的 options > 环境变量 > 变量默认值
#-------------------------------------------------------------------

# 模块名
project_name="@project.artifactId@"

# 运行包名
project_jar="@project.artifactId@-exec.jar"

# 默认JVM内存参数，如果设置了环境变量 JVM_MEMORY_VARS ，则会被环境变量覆盖
jvm_memory_vars=${JVM_MEMORY_VARS="-Xms6G -Xmx6G -XX:NewRatio=1 -XX:SurvivorRatio=8 -XX:InitialSurvivorRatio=8 -XX:MetaspaceSize=256M -XX:MaxMetaspaceSize=512M"}

# 默认JVM非内存参数，如果设置了环境变量 JVM_VARS ，则会被环境变量覆盖
jvm_vars=${JVM_VARS="-server -XX:+PrintCommandLineFlags"}

# 是否打印gc信息
jvm_print_gc=${JVM_PRINT_GC="false"}

# java 远程调试端口。当设置此值时，本 java 应用会监听此端口，提供给开发人员进行连接，从而实现代码级别调试
debug_port=${DEBUG_PORT=""}

# jmx hostname。当设置此值时，本 java 应用会使用本参数设置 jmx 的 hostname。一般设置为本机 ip
jmx_hostname=${JMX_HOSTNAME=""}

# jmx 端口。当设置此值时，本 java 应用会监听此端口，从而对 java 程序进行性能监控
jmx_port=${JMX_PORT=""}

# java 程序生成日志文件的目录，不能随便改
log_path="./logs/"

# 启动后是否自动打印日志，如果设置了环境变量 AUTO_TAIL_LOG，则会被环境变量覆盖
auto_tail_log=${AUTO_TAIL_LOG="true"}

# 启动前是否先删除所有日志文件，如果设置了环境变量 AUTO_TAIL_LOG，则会被环境变量覆盖
rm_log=${RM_LOG="false"}

# 启动 java 的命令是否结合 nohup 进行不挂断运行，如果设置了环境变量 RUN_AS_NOHUP，则会被环境变量覆盖
run_as_nohup=${RUN_AS_NOHUP="true"}

# 当在 docker 环境中，启动 java 报错后，会导致容器退出，可配置此参数，阻止容器退出，便于进入容器调试问题
keep_docker_running=${KEEP_DOCKER_RUNNING="false"}

#-------------------------------------------------------------------
# 以下内容请不要修改
#-------------------------------------------------------------------

pid=''
command=''
CURRENT_SCRIPT_DIR=$(
        cd "$(dirname "$0")"
        pwd
)

print_usage() {
        echo "usage:"
        echo "  run.sh [command] [options]"
        echo ""
        echo "command:"
        echo "  start                     [default command] start the server"
        echo "  stop                      stop the server"
        echo "  status                    status the server"
        echo "  log                       print log"
        echo "  h, help                   print help information"

        echo ""
        echo "options:"
        echo "  -h, --help                        print help information."
        echo "  -l, --auto_tail_log true|false    whether to output logs in current process, default to true"
        echo "  -n, --run_as_nohup true|false     running server using nohup, default to true"
        echo "  -d, --debug_port [0-65535]        java remote debug listening port, must be port range[0-65535]"
        echo "  -r, --rm_log                      remove all log files befare startup"
        echo "  --print_gc                        print gc info"
        echo "  --jmx_hostname                    jmx hostname. Generally, set this parameter to the server IP address"
        echo "  --jmx_port                        listening jmx port"
}

get_pid() {
        if [ ! -f "application.pid" ]; then
                echo "file 'application.pid' not found, server maybe stopped."
                pid=''
        else
                pid=$(head -n 1 application.pid)
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
                jvm_params="${jvm_params} -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:${log_path}/gc.log"
        fi
        if [ -n "$debug_port" ]; then
                jvm_params="${jvm_params} -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=$debug_port"
        fi
        if [ -n "$jmx_port" ]; then
                jvm_params="${jvm_params} -Djava.rmi.server.hostname=${jmx_hostname} -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=$jmx_port -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false"
        fi

        cmd="java ${jvm_params} -jar ${project_jar}"
        if [ $run_as_nohup = "true" ]; then
                cmd="nohup $cmd 1>/dev/null 2>&1 &"
        fi

        if [ $rm_log = "true" ]; then
                echo "deleting all log files..."
                eval "rm -rf ${log_path}/*"
        fi

        echo "executing cmd:"
        echo "$cmd"
        eval $cmd
        echo ""

        if [[ $auto_tail_log = "true" ]] && [[ $run_as_nohup = "true" ]]; then
                echo 'log will printing after 5 second using command "tail -f -n 500" automatic.'
                echo 'you can use "ctrl+c" to exit log printing, and will not close the application.'
                echo ''

                sleep 5
                print_log
        else
                if [ $run_as_nohup != "true" ]; then
                        echo "app started, use './run.sh status' to check status"
                fi
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
                filename=$(ls -t | grep ^log | head -n1 | awk '{print $0}')
                tail -f -n 500 $filename
        fi
}

check_jdk() {
        if command -v java >/dev/null; then
                java_version=$(java -version 2>&1 | sed '1!d' | sed -e 's/"//g' -e 's/version//')
                echo "java_version: ${java_version}"
        else
                echo "jdk is not install, please install first!"
                exit 1
        fi
}

health_check() {
        get_pid
        if [ "$pid" = "" ]; then
                echo "${project_name} is not running."
                echo "${project_name} unhealth"
                exit 1
        else
                if [ -d "/proc/${pid}" ]; then
                        ip_port=$(cat config/application.properties | grep server.port)
                        port=${ip_port##*=}
                        port=${port:0:-1}
                        microService=${project_name##*-service-}
                        url="http://localhost:${port}/${microService}/actuator/health"

                        echo $url
                        # http_code=$(curl -I -m 5 -o /dev/null -s -w %{http_code} ${url})
                        resp=$(curl -s --connect-timeout 5 -m 5 $url)
                        echo $resp
                        if [[ "$resp" =~ '"UP"' ]]; then
                                echo "${project_name} health"
                                exit
                        fi
                        echo "${project_name} unhealth"
                        exit 1
                else
                        echo "can not found running pid ${pid}, ${project_name} is not running!"
                        echo "${project_name} unhealth"
                        exit 1
                fi
        fi
}

parse_options() {
        TEMP=$(getopt -n "$0" -o hl:d:n:r -l help,auto_tail_log:,debug_port:,print_gc,run_as_nohup:,jmx_hostname:,jmx_port:,rm_log -- "$@")
        if [ $? != 0 ]; then
                echo "command parse error..." >&2
                exit 1
        fi

        eval set -- "$TEMP"
        while true; do
                case "$1" in
                -h | --help)
                        print_usage
                        exit 0
                        ;;
                --print_gc)
                        echo "$1"
                        jvm_print_gc="true"
                        shift
                        ;;
                -r | --rm_log)
                        echo "$1"
                        rm_log="true"
                        shift
                        ;;
                -l | --auto_tail_log)
                        case "$2" in
                        true | false)
                                echo "$1=$2"
                                auto_tail_log=$2
                                shift 2
                                ;;
                        *)
                                echo "error value in option $1, must be true|false"
                                exit 1
                                ;;
                        esac
                        ;;
                -n | --run_as_nohup)
                        case "$2" in
                        true | false)
                                echo "$1=$2"
                                run_as_nohup=$2
                                shift 2
                                ;;
                        *)
                                echo "error value in option $1, must be true|false"
                                exit 1
                                ;;
                        esac
                        ;;
                -d | --debug_port)
                        if [[ $2 -ge 0 ]] && [[ $2 -le 65535 ]] 2>/dev/null; then
                                echo "$1=$2"
                                debug_port=$2
                                shift 2
                        else
                                echo "$1=$2"
                                echo "error value in option $1, must be port range[0-65535]"
                                exit 1
                        fi
                        ;;
                --jmx_hostname)
                        echo "$1=$2"
                        jmx_hostname=$2
                        shift 2
                        ;;
                --jmx_port)
                        if [[ $2 -ge 0 ]] && [[ $2 -le 65535 ]] 2>/dev/null; then
                                echo "$1=$2"
                                jmx_port=$2
                                shift 2
                        else
                                echo "$1=$2"
                                echo "error value in option $1, must be port range[0-65535]"
                                exit 1
                        fi
                        ;;
                --)
                        shift
                        break
                        ;;
                *)
                        echo "Internal error!"
                        exit 1
                        ;;
                esac
        done
        echo ""

        if [ $# -gt 0 ]; then
                command=$1
        fi
}

run() {
        parse_options $*

        cd $CURRENT_SCRIPT_DIR
        if [ -z "$command" ]; then
                start
        else
                case "$command" in
                start) start ;;
                stop) stop ;;
                status) status ;;
                log) print_log ;;
                health) health_check ;;
                h | help) print_usage ;;
                *)
                        echo "illegal command: $1"
                        echo ""
                        print_usage
                        ;;
                esac
        fi
}

run $*
