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

package com.acornui.browser

import kotlinx.serialization.Serializable

fun String.toUrlParams(): UrlParams {
	val items = ArrayList<Pair<String, String>>()
	val split = split("&")
	for (entry in split) {
		val i = entry.indexOf("=")
		if (i != -1)
			items.add(entry.substring(0, i) to decodeURIComponent(entry.substring(i + 1)))
	}
	return UrlParams(items)
}

@Serializable
data class UrlParams(val items: List<Pair<String, String>>) {

	constructor(vararg items: Pair<String, String>) : this(items.toList())

	/**
	 * Retrieves the first parameter with the given name.
	 */
	fun get(name: String): String? {
		return items.firstOrNull { it.first == name }?.second
	}

	/**
	 * Retrieves all [items] with the given name.
	 */
	fun getAll(name: String): List<String> {
		return items.filter { it.first == name }.map { it.second }
	}

	fun contains(name: String): Boolean {
		return items.firstOrNull { it.first == name } != null
	}

	private var _queryString: String? = null

	/**
	 * Returns a uri encoded querystring in the form: foo=one&bar=two&baz=three
	 */
	val queryString: String
		get() {
			if (_queryString != null) return _queryString!!
			val result = StringBuilder()
			for ((key, value) in items) {
				result.append(encodeURIComponent(key))
				result.append("=")
				result.append(encodeURIComponent(value))
				result.append("&")
			}
			val resultString = result.toString()
			this._queryString = if (resultString.isNotEmpty())
				resultString.substring(0, resultString.length - 1)
			else
				resultString
			return _queryString!!
		}
}

external fun encodeURIComponent(str: String): String
external fun decodeURIComponent(str: String): String