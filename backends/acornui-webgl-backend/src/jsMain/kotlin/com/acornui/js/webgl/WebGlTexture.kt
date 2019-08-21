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

package com.acornui.js.webgl

import com.acornui.graphic.BlendMode
import com.acornui.graphic.RgbData
import com.acornui.io.BufferFactory
import com.acornui.gl.core.*
import com.acornui.graphic.Texture
import com.acornui.io.ProgressReporter
import com.acornui.math.Matrix4
import com.acornui.io.UrlRequestData
import kotlinx.coroutines.CompletableDeferred
import org.w3c.dom.HTMLImageElement
import org.w3c.dom.url.URL
import kotlin.browser.document
import kotlin.browser.window

/**
 * @author nbilyk
 */
class WebGlTexture(
		gl: Gl20,
		glState: GlState
) : GlTextureBase(gl, glState) {

	val image = document.createElement("img") as HTMLImageElement

	override val widthPixels: Int
		get() {
			return image.naturalWidth
		}

	override val heightPixels: Int
		get() {
			return image.naturalHeight
		}

	private val _rgbData by lazy {
		// Creates a temporary frame buffer, draws the image to that, uses gl readPixels to get the data, then disposes.
		val batch = glState.batch
		val previousShader = glState.shader
		refInc()
		val framebuffer = Framebuffer(gl, glState, widthPixels, heightPixels, false, false)
		framebuffer.begin()
		glState.viewProjection = Matrix4.IDENTITY
		glState.setTexture(this)
		glState.blendMode(BlendMode.NORMAL, false)
		batch.putVertex(-1f, -1f, 0f, u = 0f, v = 0f)
		batch.putVertex(1f, -1f, 0f, u = 1f, v = 0f)
		batch.putVertex(1f, 1f, 0f, u = 1f, v = 1f)
		batch.putVertex(-1f, 1f, 0f, u = 0f, v = 1f)
		batch.putQuadIndices()
		batch.flush()
		val pixelData = BufferFactory.byteBuffer(widthPixels * heightPixels * 4)
		gl.readPixels(0, 0, widthPixels, heightPixels, Gl20.RGBA, Gl20.UNSIGNED_BYTE, pixelData)
		framebuffer.end()
		glState.shader = previousShader
		framebuffer.dispose()
		val rgbData = RgbData(widthPixels, heightPixels, true)
		val bytes = rgbData.bytes
		var i = 0
		while (pixelData.hasRemaining) {
			bytes[i++] = pixelData.get()
		}
		refDec()
		rgbData
	}

	override val rgbData: RgbData
		get() = _rgbData
}


/**
 * Creates an http request, processing the results as a [Texture].
 */
suspend fun loadTexture(
		gl: Gl20,
		glState: GlState,
		requestData: UrlRequestData,
		progressReporter: ProgressReporter,
		initialTimeEstimate: Float
): Texture {
	val completion = CompletableDeferred<Texture>()
	val path = requestData.toUrlStr()
	val jsTexture = WebGlTexture(gl, glState)
	if (js("URL.prototype != undefined") == true) {
		// Not supported in IE
		if (path.startsWith("http", ignoreCase = true) && URL(path).origin !== window.location.origin) {
			jsTexture.image.crossOrigin = ""
		}
	}
	jsTexture.image.src = path

	jsTexture.image.onload = {
		completion.complete(jsTexture)
	}
	jsTexture.image.onerror = {
		msg, url, lineNo, columnNo, error ->
		completion.completeExceptionally(Exception(msg?.toString() ?: "Unknown Error"))
	}
	return completion.await()
}