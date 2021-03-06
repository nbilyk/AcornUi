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

package com.acornui.dom

import com.acornui.Disposable
import com.acornui.ResizeObserver
import org.w3c.dom.HTMLElement
import org.w3c.dom.ParentNode
import org.w3c.dom.asList

/**
 * Watches the dom element for resize.
 * @param callback The callback to invoke on resize.
 * @return Returns a handle to stop observation.
 */
fun HTMLElement.addResizeObserver(callback: ()->Unit): Disposable {
	val observer = ResizeObserver { _, _ ->
		callback()
	}
	observer.observe(this)
	return Disposable {
		observer.unobserve(this)
	}
}

/**
 * Returns all elements in the tab order.
 * This will include elements that are tabbable by default, visible, not hidden, and does not have a tab index of -1.
 */
fun ParentNode.getTabbableElements(): List<HTMLElement> {
	return querySelectorAll("button, [href], input, select, textarea, [tabindex]:not([tabindex='-1'])").asList().unsafeCast<List<HTMLElement>>().filter {
		!it.hidden && it.style.display != "none" && it.tabIndex != -1
	}
}

/**
 * Sets the style.display to "none" if visible is false, otherwise sets the display to [visibleDisplay].
 */
fun HTMLElement.visible(value: Boolean, visibleDisplay: String? =  null) {
	if (value) {
		if (visibleDisplay == null)
			style.removeProperty("display")
		else style.display = visibleDisplay
	} else
		style.display = "none"
}

fun HTMLElement.hide() = visible(false)
fun HTMLElement.show() = visible(true)