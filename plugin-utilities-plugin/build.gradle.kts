group = rootProject.group
version = rootProject.version

dependencies {
    implementation(project(":plugin-utilities-core"))
    implementation(project(":plugin-utilities-test"))

    implementation("com.xenomachina:kotlin-argparser:2.0.7")
    implementation("org.junit.jupiter:junit-jupiter:5.7.0")
}

// TODO: move into the project
open class CliTask : org.jetbrains.intellij.tasks.RunIdeTask() {
    // Name of the runner
    @get:Input
    val runner: String? by project

    // Input directory with files
    @get:Input
    val input: String? by project

    // Output directory to store indexes and methods data
    @get:Input
    val output: String? by project

    init {
        jvmArgs = listOf(
            "-Djava.awt.headless=true",
            "--add-exports",
            "java.base/jdk.internal.vm=ALL-UNNAMED",
            "-Djdk.module.illegalAccess.silent=true"
        )
        maxHeapSize = "20g"
        standardInput = System.`in`
        standardOutput = System.`out`
    }
}

tasks {
    register<CliTask>("cli") {
        dependsOn("buildPlugin")
        args = listOfNotNull(
            runner,
            input?.let { "--input=$it" },
            output?.let { "--output=$it" }
        )
    }
}
