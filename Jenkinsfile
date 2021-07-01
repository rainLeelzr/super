#!groovy
pipeline {
    agent any

    environment {
        COMMIT_ID = sh(script: "git log -1 --pretty='%h'", returnStdout: true).trim()
        COMMIT_LOGS = sh(script: "git log -1 --pretty='%s'", returnStdout: true).trim()
        AUTHORS = sh(script: "git log --oneline -1 --format=%an", returnStdout: true).trim()
    }

    stages {
        stage('重置环境') {
            steps {
                sh "git checkout . && git clean -xdf"
            }
        }

        stage('编译代码') {
            when {
                anyOf {
                    branch 'master'
                    branch 'dev'
                    branch 'test'
                    branch 'uat'
                    branch 'pre'
                    branch 'prod'
                }
            }
            steps {
                sh "mvn -T 1C -U -am clean deploy -DskipTests -Pisass-deploy"
            }
        }
    }

    post {
        always {
            deleteDir()
            sh """curl -X POST -H 'Content-Type: application/json' -d ' {"msgtype": "markdown","markdown": {"content": "jenkins任务：${JOB_NAME}\n> 部署结果：${currentBuild.result}\n> 作者：${AUTHORS}\n> 内容：${COMMIT_LOGS}\n> 变更：${FILE_CHANGES}\n> 链接：[jenkins任务日志链接](${BUILD_URL}console)" } }' https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=8d3aa683-0114-4cce-90d4-508ee755de0b"""
        }
    }
}