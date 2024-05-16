pipeline {
    agent any

    environment {
        mainDir = "."
        ecrLoginHelper = "docker-credential-ecr-login"
        region = "ap-northeast-2"
        ecrUrl = "471112630428.dkr.ecr.ap-northeast-2.amazonaws.com/board"
        repository = "board"
        deployHost = "43.201.55.230"
        githubSshId = "seoulit-ssh-key"
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
                    sh """
                        curl -O https://amazon-ecr-credential-helper-releases.s3.us-east-2.amazonaws.com/0.4.0/linux-amd64/${ecrLoginHelper}
                        chmod +x ${ecrLoginHelper}
                        ./gradlew jib -Djib.to.image=${ecrUrl}/${repository}:${currentBuild.number} -Djib.console='plain'
                    """
                }
            }
        }

        stage('Deploy to AWS EC2 VM') {
            steps {
                sshagent(credentials: ["deploy-key"]) {
                    sh """
                        ssh -o StrictHostKeyChecking=no ubuntu@${deployHost} '
                        aws ecr get-login-password --region ${region} | docker login --username AWS --password-stdin ${ecrUrl};
                        docker run -d -p 80:8888 ${ecrUrl}/${repository}:${currentBuild.number};
                        '
                    """
                }
            }
        }
    }
}
