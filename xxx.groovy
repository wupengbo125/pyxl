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




pipeline {
    agent any
    // ... 省略其他阶段的定义 ...

    post {
        always {
            // 无论构建成功还是失败，都执行发送消息
            sendXmattersMessage(currentBuild.currentResult)
        }
    }
}

// 定义发送Xmatters消息的方法
def sendXmattersMessage(String buildResult) {
    // 假设Xmatters的API端点
    def xmattersApiEndpoint = 'https://your-xmatters-instance/api/integration/1/functions/{function-id}/triggers'
    def xmattersApiKey = 'your-xmatters-api-key' // 这应该是你的Xmatters API密钥
    def message = (buildResult == 'SUCCESS') ? 'success' : 'fail'

    // 构建发送到Xmatters的JSON负载
    def jsonPayload = "{\"properties\":{\"message\":\"${message}\",\"buildResult\":\"${buildResult}\"}}"

    // 调用curl发送消息
    sh """
    curl -X POST "$xmattersApiEndpoint" \
         -H 'Content-Type: application/json' \
         -H 'Authorization: Bearer $xmattersApiKey' \
         -d '$jsonPayload'
    """
}
pipeline {
    // ... your pipeline stages
    post {
        success {
            sh """
            curl -X POST -u username:password --data-urlencode "text=Build Successful: ${env.JOB_NAME} ${env.BUILD_NUMBER}" http://your.xmatter.http.endpoint
            """
        }
        failure {
            sh """
            curl -X POST -u username:password --data-urlencode "text=Build Failed: ${env.JOB_NAME} ${env.BUILD_NUMBER}" http://your.xmatter.http.endpoint
            """
        }
    }
}

