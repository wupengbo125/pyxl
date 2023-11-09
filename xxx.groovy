pipeline {
    agent any

    stages {
        stage('Example Stage 1') {
            steps {
                // 您的步骤逻辑...
            }
        }
        stage('Check Health Status') {
            steps {
                script {
                    // 假设`healthStatus`是从某个步骤或者文件中获取的
                    def healthStatus = 'healthy' // 这里应该是获取实际值的代码

                    if (healthStatus != 'healthy') {
                        // 如果不健康，则使构建失败
                        error("Health status is not healthy, job failed.")
                    }
                }
            }
        }
        stage('Example Stage 2') {
            steps {
                // 您的步骤逻辑...
            }
        }
        // ... 其他阶段 ...
    }

    post {
        always {
            // 这里的代码无论构建结果如何都会执行
            script {
                def currentResult = currentBuild.currentResult
                def message = (currentResult == 'SUCCESS') ? 'success' : 'fail'

                // 发送Xmatters消息
                sendXmattersMessage(message)
            }
        }
    }
}

// 定义发送Xmatters消息的方法
def sendXmattersMessage(String message) {
    // 实际发送消息的逻辑...
    println("Sending Xmatters message: ${message}")
}
