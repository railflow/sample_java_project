pipeline {
    agent any

    options {
        buildDiscarder(logRotator(numToKeepStr: '20'))
        timestamps()
    }

    environment {
        BUILDBUTLER_API_KEY = credentials('BUILDBUTLER_APIKEY')
        JDK_DIR = "${WORKSPACE}/.jdk/jdk-17"
    }

    stages {
        stage('Install JDK 17') {
            steps {
                sh '''
                    if [ ! -d "${JDK_DIR}" ]; then
                        mkdir -p "${WORKSPACE}/.jdk"
                        curl -fL "https://api.adoptium.net/v3/binary/latest/17/ga/linux/x64/jdk/hotspot/normal/eclipse" \
                            -o "${WORKSPACE}/.jdk/jdk17.tar.gz"
                        tar -xzf "${WORKSPACE}/.jdk/jdk17.tar.gz" -C "${WORKSPACE}/.jdk"
                        mv "${WORKSPACE}/.jdk"/jdk-17* "${JDK_DIR}"
                        rm "${WORKSPACE}/.jdk/jdk17.tar.gz"
                    fi
                    "${JDK_DIR}/bin/java" -version
                '''
                script {
                    env.JAVA_HOME = env.JDK_DIR
                    env.PATH = "${env.JDK_DIR}/bin:${env.PATH}"
                }
            }
        }

        stage('Unit Tests') {
            options {
                catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE')
            }
            steps {
                sh './gradlew mergeTestReports --continue'
            }
            post {
                always {
                    archiveArtifacts artifacts: 'build/test-results/combined/TEST-combined.xml', allowEmptyArchive: true
                    junit allowEmptyResults: true, testResults: 'build/test-results/combined/TEST-combined.xml'
                }
            }
        }

        stage('Build JAR') {
            steps {
                sh './gradlew jar'
            }
            post {
                success {
                    archiveArtifacts artifacts: 'build/libs/*.jar'
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh "docker build -t sample-java-project:${env.GIT_COMMIT} ."
            }
        }

        stage('Deploy') {
            steps {
                sh '''
                    echo "Deploying to staging..."
                    echo "Health check..."
                    echo "Deploy complete"
                '''
            }
        }

        /*
        stage('Report to Build Butler') {
            steps {
                sh """
                    npx @railflow/buildbutler-gitlab-ci \
                        --api-key "${BUILDBUTLER_API_KEY}" \
                        --test-results 'build/test-results/combined/TEST-combined.xml'
                """
            }
        }
        */
    }

    post {
        always {
            cleanWs()
        }
    }
}
