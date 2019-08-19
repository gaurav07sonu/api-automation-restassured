@Library('sentieo-pipeline@master') _
def utils = new io.sentieo.Utils()


node('docker') {

//    def suitesToTestInSerial = [ 'example_test' ]

   def suitesToTestInSerial = [ 'test_password', 'test_single_signon', 'test_all']
   stage('Fetch and Run Tests') {
         docker.withRegistry('https://602037364990.dkr.ecr.us-east-1.amazonaws.com/sentieouiautomation', 'ecr:us-east-1:aws-ecr') {
            
            for(int i = 0; i < suitesToTestInSerial.size(); i++){
                stage("Running ${suitesToTestInSerial[i]}") {
                    try{
                        
                        sh "docker container rm ui-test-daily-${i} || true"
                        sh "docker run --name ui-test-daily-${i} --shm-size=2gb 602037364990.dkr.ecr.us-east-1.amazonaws.com/sentieouiautomation:master gradle test -Dtestng.appurl=\"https://app.sentieo.com\" -Dtestng.userappurl=\"https://user-app.sentieo.com\" -Psuite=${suitesToTestInSerial[i]}"
                        
                    } catch(e) {

                        throw(e)

                    } finally {
                        // this makes sure that we always collect the test results
                        sh "docker cp ui-test-daily-${i}:/app/tests_output ./tests_output_${i}"
                        step([$class: 'Publisher', reportFilenamePattern: "tests_output_${i}/**/testng-results.xml"])
                    }
                    
                }
            }

        }
    }
   
   stage('Sending Test Results') { // for display purposes

        echo "TestNG tests reads complete ---------"
    
        def testsStatus = utils.getTestsStatus()
        slackColor = testsStatus.failCount > 0 ? 'danger' : 'good'
        branch_name = 'master'
        build_display_name = env.BUILD_DISPLAY_NAME
        job_name = env.JOB_NAME

        slackMessages = [ 
            "*Jenkins Job* ${job_name}",
            "*Build* ${build_display_name}, BranchName: ${branch_name}",
            "*Tests Executed* ${testsStatus.testList.join(', ')}",
            "*Test Status* - ${testsStatus.testStatusResult}"
        ]
        slackSend channel: 'ui-automation-logs', color: slackColor, message: slackMessages.join('\n')
   }
   
}
