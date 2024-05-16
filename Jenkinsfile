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

    tools {
        jdk 'JDK 17' // Jenkins에서 미리 설정된 JDK 17 이름
    }

    // 작업정의
    stages {
        stage('Step 1. Pull codes from Github') {
            steps {
                checkout scm
            }
            post {
                success {
                    postBuild("Step 1")
                }
                failure {
                    postBuild("Step 1")
                }
            }
        }

        stage('Step 2. Build codes by Gradle') {
            steps {
                sh "./gradlew clean build"
            }
            post {
                success {
                    postBuild("Step 2")
                }
                failure {
                    postBuild("Step 2")
                }
            }
        }
    }
}

// 공통 post 빌드 함수
def postBuild(stepName) {
    echo "Success ${stepName}"
    // 여기에 추가적인 작업을 할 수 있습니다.
}
