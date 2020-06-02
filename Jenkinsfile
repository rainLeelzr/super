#!groovy
pipeline {
    agent any

    environment {
        GIT_REPO = "git@gitee.com:isass/super.git"
        AUTHORS = sh(script: "git log --oneline -1 --format=%an", returnStdout: true).trim()
        COMMIT_LOGS = sh(script: "git log -1 --pretty='%s'", returnStdout: true).trim()
        RESULT = '成功'
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
                sh "mvn -U -am clean deploy -DskipTests"
            }
        }
    }

    post {
        always {
            #sh """
            #    curl -H "Content-Type:application/json" -X POST -d '{
            #            "groupId": "sysmonitorgroup",
            #            "type": 2,
            #            "textCard": {
            #                "title": "[${JOB_NAME}]部署${RESULT}",
            #                "description": "作者：${AUTHORS}<br/>内容：${COMMIT_LOGS}",
            #                "url": "${BUILD_URL}console",
            #                "buttonText": "详情"
            #            }
            #        }' https://ms.isass.shop/message/enterprise-wechat/group-message
            #    """
            deleteDir()
        }
    }
}