import org.gradle.api.Project
import java.io.File
import java.io.FileWriter

val readmeFile = File(rootProject.rootDir.parent, "README.md")
val readmeSection = "### Dependency Diagram"

val createMermaidDependencyGraph = tasks.register("createMermaidDependencyGraph") {
  group = "Reporting"
  description = "Creates a mermaid dependency graph for the project"

  doLast {
    val projects = mutableSetOf<Project>()
    val dependencies = mutableListOf<Pair<Project, Project>>()

    project.allprojects.forEach { project ->
      project.configurations.forEach { config ->
        config.dependencies
          .withType(ProjectDependency::class.java)
          .map { it.dependencyProject }
          .forEach { dependency ->
            if (dependency != project) {
              projects.add(project)
              projects.add(dependency)
              dependencies.add(project to dependency)
            }
          }
      }
    }

    val sortedProjects = projects.sortedBy { it.path }
    val mermaid = buildMermaidGraph(sortedProjects, dependencies)
    println(mermaid)
    try {
      appendMermaidGraphToReadme(mermaid)
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }
}

/**
 * Generates a Mermaid graph for module dependencies.
 * - Groups: Folders containing modules
 * - Subgraphs: Grouped modules in the graph
 * - Source: Module with dependencies
 * - Target: Dependent module
 * - Arrows: Represent dependencies between modules
 */

fun buildMermaidGraph(
  sortedProjects: List<Project>,
  dependencies: MutableList<Pair<Project, Project>>,
): String {
  // Create a list of module paths by splitting project paths by ":" and filtering out blank parts
  val modules = sortedProjects.map { it.path.split(":").filter { it.isNotBlank() } }

  // Create a list of distinct group names, including "others" for modules without a parent folder
  val groups = createGroups(modules)

  // Generate the Mermaid subgraph for each group
  val subgraphs = groups.map { group ->
    createSubgraph(group, modules)
  }.joinToString("\n")

  // Generate Mermaid arrows for each dependency between projects
  val arrows = dependencies.map { (key, value) ->
    val sourceName = key.path.split(":").filter { it.isNotBlank() }.last()
    val targetName = value.path.split(":").filter { it.isNotBlank() }.last()
    "  $sourceName --> $targetName"
  }.joinToString("\n")

  // Combine subgraphs and arrows to create the final Mermaid graph
  val mermaidConfig = """
      %%{
        init: {
          'theme': 'dark',
          'themeVariables': {
            'primaryColor': '#C4C7B300',
            'primaryTextColor': '#fff',
            'primaryBorderColor': '#7C0000',
            'lineColor': '#FF2F2F2F',
            'secondaryColor': '#006100',
            'tertiaryColor': '#fff'
          }
        }
      }%%
    """.trimIndent()
  return "${mermaidConfig}\n\ngraph LR\n$subgraphs\n$arrows"
}

// Create a list of distinct group names based on the module paths
fun createGroups(modules: List<List<String>>): List<String> {
  return modules.map { if (it.size > 1) it[0] else "others" }.distinct()
}

// Generate a Mermaid subgraph for the specified group and list of modules
fun createSubgraph(group: String, modules: List<List<String>>): String {
  // Extract module names for the current group, handling "others" modules separately
  val moduleNames = modules
    .filter { it.getOrNull(0) == group || (group == "others" && it.size == 1) }
    .map { if (it.size > 1) it[1] else it[0] }
    .joinToString("\n    ") { moduleName -> moduleName }

  // Create the subgraph string for the current group with its module names
  return "  subgraph $group\n    $moduleNames\n  end"
}


fun appendMermaidGraphToReadme(mermaid: String) {
  val readmeLines = readmeFile.readLines().toMutableList()

  val modulesIndex = readmeLines.indexOfFirst { it.startsWith(readmeSection) }

  if (modulesIndex != -1) {
    val endIndex = readmeLines.drop(modulesIndex + 1).indexOfFirst { it.startsWith("#") }
    val actualEndIndex = if (endIndex != -1) endIndex + modulesIndex + 1 else readmeLines.size

    readmeLines.subList(modulesIndex + 1, actualEndIndex).clear()
    readmeLines.add(modulesIndex + 1, "```mermaid\n$mermaid\n```")

    FileWriter(readmeFile).use { writer ->
      writer.write(readmeLines.joinToString("\n"))
    }

    println("Project module dependency graph replaced the content in README.md under the $readmeSection section ")
  } else {
    println("The $readmeSection section was not found in the README.md file")
  }
}
