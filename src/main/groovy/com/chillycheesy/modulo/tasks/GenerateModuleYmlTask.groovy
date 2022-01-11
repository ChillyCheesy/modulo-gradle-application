package com.chillycheesy.modulo.tasks

import com.chillycheesy.modulo.extensions.ModuleExtension
import org.gradle.api.Project
import org.yaml.snakeyaml.Yaml

/**
 * The GenerateModuleYmlTask generate the module.yml file before the Plugin build.
 * The task use the {@link ModuleExtension} information to generate it.
 */
class GenerateModuleYmlTask implements ModuloTask {

    /**
     * The ModuleExtension store plugin.yml data.
     */
    private ModuleExtension moduleExtension

    /**
     * Create the Task
     * @param moduleExtension The module extention who store plugin.yml data
     */
    GenerateModuleYmlTask(ModuleExtension moduleExtension) {
        this.moduleExtension = moduleExtension
    }

    /**
     * Create the task.
     * @param project Target project.
     * @return The new task
     */
    @Override
    def generate(Project project) {
        return project.task('generateModuleYml') {
            group = 'modulo'
            description = 'Generate the module.yml file for a Modulo module.'
            doLast {
                final moduleConfig = getModuleConfigFile()
                final Yaml yaml = new Yaml()
                moduleConfig.text = yaml.dump([
                        name: moduleExtension.moduleName,
                        version: moduleExtension.version,
                        authors: moduleExtension.authors,
                        main: moduleExtension.main,
                        dependencies: moduleExtension.dependencies,
                        softDependencies: moduleExtension.softDependencies
                ])
            }
        }
    }

    /**
     * Get the module.yml file and create it if doesn't exist.
     * @return The file.
     */
    private def getModuleConfigFile() {
        final resources = new File("${moduleExtension.target}")
        resources.mkdirs()
        final file = new File("${resources.getPath()}/module.yml")
        if (!file.exists()) file.createNewFile()
        return file
    }

}
