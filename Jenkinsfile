pipeline {
    agent any

    // 변수 선언
    environment {
        TIME_ZONE = 'Asia/Seoul'
        PROFILE = 'local'
        AWS_CREDENTIAL_NAME = 'aws-key'
        DEPLOY_CREDENTIAL_NAME = 'deploy-ssh-key'
        REGION = 'ap-northeast-2'
        ECR_PATH = '471112630428.dkr.ecr.ap-northeast-2.amazonaws.com'
        ECR_REPOSITORY_NAME = 'board'
        DEPLOY_HOST = ''
    }

    // 작업정의
    stages {

        stage('Step 1. Pull codes from Github') {
            steps {
                checkout scm
            }
            postBuild("Step 1")
        }

        stage('Step 2. Build codes by Gradle') {
            steps {
                sh "./gradlew clean build"
            }
            postBuild("Step 2")
        }

    }

}

// 공통 post 빌드 함수
def postBuild(stepName) {
    post {
        success {
            echo "Success ${stepName}"
        }
        failure {
            echo "Fail ${stepName}"
        }
    }
}