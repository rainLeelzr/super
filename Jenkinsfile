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
            echo "[${JOB_NAME}]部署结果[${currentBuild.result}]"
            echo "作者：${AUTHORS}\n内容：${COMMIT_LOGS}"
            deleteDir()
        }
    }
}