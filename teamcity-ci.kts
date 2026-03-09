import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.XmlReport
import jetbrains.buildServer.configs.kotlin.buildFeatures.buildCache
import jetbrains.buildServer.configs.kotlin.buildFeatures.xmlReport
import jetbrains.buildServer.configs.kotlin.buildSteps.dockerCommand
import jetbrains.buildServer.configs.kotlin.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.triggers.schedule
import jetbrains.buildServer.configs.kotlin.triggers.vcs

version = "2024.03"

project {
    params {
        password("BUILDBUTLER_API_KEY", "credentialsJSON:buildbutler-api-key",
            label = "Build Butler API Key")
    }

    buildType(UnitTests)
    buildType(BuildJar)
    buildType(DockerBuild)
    buildType(Deploy)
    buildType(ReportToBuildButler)

    buildTypesOrder = arrayListOf(UnitTests, BuildJar, DockerBuild, Deploy, ReportToBuildButler)
}

object UnitTests : BuildType({
    name = "Unit Tests"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        gradle {
            name = "Run Tests and Merge Reports"
            tasks = "mergeTestReports --continue"
            jdkHome = "%env.JDK_17%"
            useGradleWrapper = true
        }
    }

    triggers {
        vcs {
            branchFilter = "+:refs/heads/main"
        }
        schedule {
            schedulingPolicy = cron {
                minutes = "*/10"
                hours = "*"
                dayOfMonth = "*"
                month = "*"
                dayOfWeek = "*"
            }
            branchFilter = "+:refs/heads/main"
            triggerBuild = always()
        }

    }

    features {
        buildCache {
            name = "Gradle Cache"
            publish = true
            use = true
            rules = """
                %env.HOME%/.gradle/caches
                %env.HOME%/.gradle/wrapper
            """.trimIndent()
        }
        xmlReport {
            reportType = XmlReport.XmlReportType.JUNIT
            rules = "build/test-results/combined/TEST-combined.xml"
        }
    }

    artifactRules = "build/test-results/combined/TEST-combined.xml => test-results"

    // Mirror continue-on-error: downstream builds use onDependencyFailure = IGNORE
    failureConditions {
        nonZeroExitCode = false
    }
})

object BuildJar : BuildType({
    name = "Build JAR"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        gradle {
            name = "Build JAR"
            tasks = "jar"
            jdkHome = "%env.JDK_17%"
            useGradleWrapper = true
        }
    }

    features {
        buildCache {
            name = "Gradle Cache"
            publish = true
            use = true
            rules = """
                %env.HOME%/.gradle/caches
                %env.HOME%/.gradle/wrapper
            """.trimIndent()
        }
    }

    artifactRules = "build/libs/*.jar => app-jar"

    dependencies {
        snapshot(UnitTests) {
            onDependencyFailure = FailureAction.IGNORE
            onDependencyCancel = FailureAction.IGNORE
        }
    }
})

object DockerBuild : BuildType({
    name = "Docker Build"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        dockerCommand {
            name = "Build Docker Image"
            commandType = build {
                source = file {
                    path = "Dockerfile"
                }
                namesAndTags = "sample-java-project:%build.vcs.number%"
            }
        }
    }

    dependencies {
        snapshot(BuildJar) {
            onDependencyFailure = FailureAction.IGNORE
            onDependencyCancel = FailureAction.IGNORE
        }
        artifacts(BuildJar) {
            artifactRules = "app-jar/*.jar => build/libs/"
        }
    }
})

object Deploy : BuildType({
    name = "Deploy"

    steps {
        script {
            name = "Deploy to Staging"
            scriptContent = """
                echo "Deploying to staging..."
                echo "Health check..."
                echo "Deploy complete"
            """.trimIndent()
        }
    }

    dependencies {
        snapshot(DockerBuild) {
            onDependencyFailure = FailureAction.IGNORE
            onDependencyCancel = FailureAction.IGNORE
        }
    }
})

object ReportToBuildButler : BuildType({
    name = "Report to Build Butler"

    steps {
        script {
            name = "Send Results to Build Butler"
            scriptContent = """
                curl -X POST "https://app.buildbutler.dev/api/v1/results" \
                  -H "Authorization: Bearer %BUILDBUTLER_API_KEY%" \
                  -F "file=@build/test-results/combined/TEST-combined.xml"
            """.trimIndent()
        }
    }

    dependencies {
        snapshot(UnitTests) {
            onDependencyFailure = FailureAction.IGNORE
            onDependencyCancel = FailureAction.IGNORE
        }
        artifacts(UnitTests) {
            artifactRules = "test-results/TEST-combined.xml => build/test-results/combined/"
        }
    }
})
