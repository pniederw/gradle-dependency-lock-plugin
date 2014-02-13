/*
 * Copyright 2014 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nebula.plugin.dependencylock

import groovy.json.JsonSlurper
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging

class DependencyLockPlugin implements Plugin<Project> {
    Logger logger = Logging.getLogger(DependencyLockPlugin)

    @Override
    void apply(Project project) {
        File dependenciesLock = new File(project.projectDir, 'dependencies.lock')

        if (dependenciesLock.exists()) {
            def locks = new JsonSlurper().parseText(dependenciesLock.text)
            def forcedModules = locks.direct.collect { "${it.group}:${it.artifact}:${it.locked}" }
            logger.debug(forcedModules.toString())

            project.configurations.all {
                resolutionStrategy.forcedModules = forcedModules
            }
        }
    }
}
