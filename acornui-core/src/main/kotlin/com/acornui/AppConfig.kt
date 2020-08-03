/*
 * Copyright 2019 Poly Forest, LLC
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

package com.acornui

import com.acornui.di.Context
import kotlinx.serialization.Serializable

/**
 * Application configuration common across back-end types.
 * @author nbilyk
 */
@Serializable
data class AppConfig(

		/**
		 * All relative files will be prepended with this string.
		 */
		val rootPath: String = ""
) {

	companion object : Context.Key<AppConfig>

}

/**
 * A convenient way to get the scoped AppConfig.
 */
val Context.config: AppConfig
	get() = inject(AppConfig)
