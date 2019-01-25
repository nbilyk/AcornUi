package com.acornui.build.model

import com.acornui.build.ConfigurationException
import com.acornui.build.util.ACORNUI_HOME_PATH
import java.io.File

interface ModuleVo {

	/**
	 * The base directory of the module. (Must exist)
	 */
	val baseDir: File

	/**
	 * The name of the module, will be used when matching command line parameters.
	 */
	val name: String

	/**
	 * The directory for compilation output.
	 */
	val out: File

	/**
	 * The distribution directory for jars and compiled assets.
	 */
	val dist: File
	val sourcesJar: File
	val outAssets: File

	/**
	 * A list of resource directories.
	 */
	val resources: List<File>

	/**
	 * A list of the modules this module is dependent upon.
	 * @see walkDependenciesBottomUp
	 */
	val moduleDependencies: List<ModuleVo>

	/**
	 * The list of source directories for this module.
	 */
	val sources: List<File>
}


//----------------------------
// Util
//----------------------------

/**
 * A shortcut to getting a file as a child of the [ModuleVo.baseDir].
 */
fun ModuleVo.rel(s: String): File {
	return File(baseDir, s)
}

/**
 * Returns the resources directory for the given skin.
 *
 * @param name The name of the skin folder. E.g. "basic"
 */
fun ModuleVo.skin(name: String): File {
	return File(ACORNUI_HOME_PATH, "Skins/$name/resources")
}

/**
 * Walks the module dependencies bottom-up, including this module.
 */
fun ModuleVo.walkDependenciesBottomUp(callback: (ModuleVo) -> Unit) = walkDependenciesBottomUp(HashMap(), callback)

fun ModuleVo.walkDependenciesBottomUp(exclude: HashMap<ModuleVo, Boolean>, callback: (ModuleVo) -> Unit) {
	for (i in moduleDependencies) {
		i.walkDependenciesBottomUp(exclude, callback)
	}
	if (!exclude.containsKey(this)) {
		exclude[this] = true
		callback(this)
	}
}


/**
 * The base model for all modules.
 */
abstract class ModuleVoBase(

		/**
		 * The base directory of the module. (Must exist)
		 */
		final override val baseDir: File,

		/**
		 * The name of the module, will be used when matching command line parameters.
		 */
		final override val name: String = baseDir.name,

		/**
		 * The directory for compilation output.
		 */
		final override val out: File = File("out"),

		/**
		 * The distribution directory for jars and compiled assets.
		 */
		final override val dist: File = File("dist")
) : ModuleVo {

	override var sourcesJar = File(dist, "${name}_sources.jar")

	override var outAssets = File(out, "assets/production/$name/")

	/**
	 * A list of resource directories.
	 */
	override var resources = listOf(rel("resources"))

	override var moduleDependencies = listOf<ModuleVo>()
	override var sources = listOf(rel("src"))

	init {
		if (!baseDir.exists()) throw ConfigurationException("${baseDir.absolutePath} does not exist.")
	}
}

data class JvmSettingsVo(

		val jvmJar: File,

		val outJvm: File,

		val jvmLibraryDependencies: List<File>,
		val jvmRuntimeDependencies: List<File>,

		val includeKotlinJvmRuntime: Boolean,

		/**
		 * For deployJvm, if there is no manifest file, a manifest will be created using this as the main class.
		 */
		val mainClass: String?
)


fun ModuleVo.jvmSettings(): JvmSettingsVo = JvmSettingsVo(
	jvmJar = File(dist, "${name}_jvm.jar"),
	outJvm = File(out, "jvm/production/$name/"),
	jvmLibraryDependencies = listOf(rel("lib")),
	jvmRuntimeDependencies = listOf(rel("externalLib/runtime")),
	includeKotlinJvmRuntime = false,
	mainClass = null
)


data class JsSettingsVo(
		val jsJar: File,
		val outJs: File,
		val jsLibraryDependencies: List<File>
)

/**
 * Creates a default JsSettingsVo object based on the current module settings.
 * To change defaults, use the [JsSettingsVo.copy] method.
 */
fun ModuleVo.jsSettings(): JsSettingsVo = JsSettingsVo(
		jsJar = File(dist, "${name}_js.jar"),
		outJs = File(out, "js/production/$name/"),
		jsLibraryDependencies = listOf()
)


interface JvmModuleVo : ModuleVo {

	/**
	 * The settings for building for JVM platforms.
	 */
	val jvmSettings: JvmSettingsVo
}

interface JsModuleVo : ModuleVo {

	/**
	 * The settings for building for JS platforms.
	 */
	val jsSettings: JsSettingsVo
}

interface CommonModuleVo : JsModuleVo, JvmModuleVo

open class JvmModuleVoImpl(
		baseDir: File,
		name: String = baseDir.name,
		out: File = File("out"),
		dist: File = File("dist")
) : ModuleVoBase(baseDir, name, out, dist), JvmModuleVo {

	override val jvmSettings = jvmSettings()
}

open class JsModuleVoImpl(
		baseDir: File,
		name: String = baseDir.name,
		out: File = File("out"),
		dist: File = File("dist")
) : ModuleVoBase(baseDir, name, out, dist), JsModuleVo {

	override val jsSettings = jsSettings()
}

open class CommonModuleVoImpl(
		baseDir: File,
		name: String = baseDir.name,
		out: File = File("out"),
		dist: File = File("dist")
) : ModuleVoBase(baseDir, name, out, dist), CommonModuleVo {

	override val jvmSettings = jvmSettings()
	override val jsSettings = jsSettings()
}
