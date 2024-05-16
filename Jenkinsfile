pipeline {
    agent any

    environment {
        mainDir = "."
        ecrLoginHelper = "docker-credential-ecr-login"
        region = "ap-northeast-2"
        ecrUrl = "471112630428.dkr.ecr.ap-northeast-2.amazonaws.com"
        repository = "board"
        deployHost = "43.201.55.230"
        githubSshId = "seoulit-ssh-key"
        AWS_CLI_PATH = "/opt/homebrew/bin" // AWS CLI가 설치된 경로를 지정
        PATH = "${AWS_CLI_PATH}:${env.PATH}"
    }

    tools {
        jdk 'JDK 17' // Jenkins에서 미리 설정된 JDK 17 이름
    }

    stages {
        stage('Pull Codes from Github') {
            steps {
                sshagent(credentials: ["${githubSshId}"]) {
                    checkout([$class: 'GitSCM', branches: [[name: '*/docker-compose']],
                              userRemoteConfigs: [[url: 'git@github.com:hwanee47/docker_board.git', credentialsId: "${githubSshId}"]]])
                }
            }
        }

        stage('Build Codes by Gradle') {
            steps {
                sh 'chmod +x ./gradlew'
                sh './gradlew clean build --info --stacktrace'
            }
        }

        stage('Build Docker Image by Jib & Push to AWS ECR Repository') {
            steps {
                withAWS(region: "${region}", credentials: "aws-key") {
                    ecrLogin()
                    script {
                        def ecrPassword = sh(returnStdout: true, script: "aws ecr get-login-password --region ${region}").trim()
                        env.awsEcrPassword = ecrPassword
                    }
                    sh """
                        curl -O https://amazon-ecr-credential-helper-releases.s3.us-east-2.amazonaws.com/0.4.0/linux-amd64/${ecrLoginHelper}
                        chmod +x ${ecrLoginHelper}
                        ./gradlew jib -Djib.to.image=${ecrUrl}/${repository}:${currentBuild.number} -Djib.console='plain' -DawsEcrPassword=$awsEcrPassword
                    """
                }
            }
        }

        stage('Deploy to AWS EC2 VM') {
            steps {
                withAWS(region: "${region}", credentials: "aws-key") {
                    sshagent(credentials: ["deploy-key"]) {
                        sh """
                            ssh -o StrictHostKeyChecking=no ubuntu@${deployHost} '
                            aws ecr get-login-password --region ${region} | docker login --username AWS --password-stdin ${ecrUrl};
                            docker run -d -p 80:8888 ${ecrUrl}:${repository}:${currentBuild.number};
                            '
                        """
                    }
                }
            }
        }
    }
}
