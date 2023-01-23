pipeline {
    agent any
    environment {
    	GROUP_ID = "gt.gob.sat"
    	ARTIFACT_ID = "sat_ifi_sipf"
    	VERSION = "1.0"
    }
    triggers {
        pollSCM('H/10 * * * *')
    }    
    stages {
        stage('Build Java') {
            steps {
			    withMaven(
			        // Maven installation declared in the Jenkins "Global Tool Configuration"
			        maven: 'M3') {
			
			      // Run the maven build
			      sh "mvn clean -DskipTests verify"
			
			    }
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    app = docker.build("${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${GROUP_ID}/${ARTIFACT_ID}:${VERSION}", "--build-arg JAR_FILE=${ARTIFACT_ID}.jar .")
                }
            }
        }
        stage('Push Docker Image') {
            steps {
            	sh "aws ecr get-login --no-include-email --region ${AWS_REGION} | awk '{printf \$6}' | docker login -u AWS --password-stdin https://${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"
                script {
                	app.push()
                }
            }
        }
    }
    post {
		always {
			emailext (
			    from: "jenkins-aws@sat.gob.gt",
      			subject: "JENKINS-AWS: ${env.JOB_NAME} [${env.BUILD_NUMBER}]",
      			body: """<p><span style="font-weight: bold">Integración en Jenkins-AWS del proyecto:</span> ${env.JOB_NAME} [${env.BUILD_NUMBER}]</p>
      				<p><span style="font-weight: bold">Revisión de SVN:</span> ${env.SVN_REVISION}</p>
      				<p><span style="font-weight: bold">Resultado:</span> ${currentBuild.currentResult}</p>""",
      			recipientProviders: [[$class: 'DevelopersRecipientProvider']],
      			attachLog: true
    		)
		}
    } 
	
}