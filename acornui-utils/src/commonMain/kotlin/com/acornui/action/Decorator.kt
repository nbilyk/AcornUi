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

package com.acornui.action

interface Decorator<in T, out R> {
	fun decorate(target: T): R
}

fun <T> noopDecorator(): Decorator<T, T> {
	@Suppress("UNCHECKED_CAST")
	return NoopDecorator as Decorator<T, T>
}

/**
 * A decorator that does... NOTHING!
 */
private object NoopDecorator : Decorator<Any, Any> {
	override fun decorate(target: Any): Any {
		return target
	}
}
