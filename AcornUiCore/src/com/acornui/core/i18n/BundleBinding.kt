/*
 * Copyright 2017 Nicholas Bilyk
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

package com.acornui.core.i18n

import com.acornui.core.Disposable
import com.acornui.core.di.*
import com.acornui.signal.bind

/**
 * This class is responsible for tracking a set of callbacks and cached files for an [I18n] bundle.
 * When this binding is disposed, the handlers are all removed and the cached file references are decremented.
 */
class BundleBinding(override val injector: Injector, bundleName: String) : Scoped, Disposable {

	/**
	 * The bundle this binding is watching.
	 */
	val bundle: I18nBundleRo = inject(I18n).getBundle(bundleName)

	private var bundleLoader = loadBundle(bundleName)
	private val callbacks = ArrayList<(I18nBundleRo) -> Unit>()

	private val bundleBinding: Disposable

	init {
		bundleBinding = bundle.bind {
			for (i in 0..callbacks.lastIndex) {
				callbacks[i].invoke(bundle)
			}
		}
	}

	@Deprecated("use bind", ReplaceWith("bind(callback)"))
	operator fun invoke(callback: (I18nBundleRo) -> Unit) {
		bind(callback)
	}

	fun bind(callback: (I18nBundleRo) -> Unit) {
		callbacks.add(callback)
		callback(bundle)
	}

	fun unbind(callback: (I18nBundleRo) -> Unit) {
		callbacks.remove(callback)
	}

	override fun dispose() {
		callbacks.clear()
		bundleBinding.dispose()
		bundleLoader.dispose()
	}
}

/**
 * Instantiates a bundle binding object.
 */
fun Scoped.bundleBinding(bundleName: String): BundleBinding {
	return BundleBinding(injector, bundleName)
}

/**
 * Invokes the callback when this bundle has changed.
 */
fun Owned.i18n(bundleName: String) : BundleBinding {
	return own(BundleBinding(injector, bundleName))
}