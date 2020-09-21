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

package com.acornui.component.style

import com.acornui.component.UiComponent
import com.acornui.string.toRadix
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

open class CssSelector(val selector: String) {
	override fun toString(): String = selector
}

open class CssProp(val name: String) {

	override fun toString(): String = name
}

class CssClass(val className: String) : CssSelector(".$className")

class CssVar(val propName: String) : CssProp("--$propName") {

	val v: String = "var(--$propName)"
}

private var cssUid = 0

fun cssClass(): ReadOnlyProperty<Any?, CssClass> {

	return object : ReadOnlyProperty<Any?, CssClass> {

		private var className: CssClass? = null

		override fun getValue(thisRef: Any?, property: KProperty<*>): CssClass {
			if (className == null)
				className = CssClass(property.name + "_" + (++cssUid).toRadix(36))
			return className!!
		}
	}
}

@Deprecated("", ReplaceWith("cssVar()"))
fun cssProp(): ReadOnlyProperty<Any?, CssVar> = error("")

fun cssVar(): ReadOnlyProperty<Any?, CssVar> {

	return object : ReadOnlyProperty<Any?, CssVar> {

		private var cssProp: CssVar? = null

		override fun getValue(thisRef: Any?, property: KProperty<*>): CssVar {
			if (cssProp == null)
				cssProp = CssVar(property.name + "_" + (++cssUid).toRadix(36))
			return cssProp!!
		}
	}
}

class CssClassToggle(private val styleTag: CssClass) : ReadWriteProperty<UiComponent, Boolean> {

	override fun getValue(thisRef: UiComponent, property: KProperty<*>): Boolean =
		thisRef.containsClass(styleTag)

	override fun setValue(thisRef: UiComponent, property: KProperty<*>, value: Boolean) {
		if (thisRef.containsClass(styleTag) != value) {
			thisRef.toggleClass(styleTag)
		}
	}
}

fun cssClassToggle(styleTag: CssClass) = CssClassToggle(styleTag)