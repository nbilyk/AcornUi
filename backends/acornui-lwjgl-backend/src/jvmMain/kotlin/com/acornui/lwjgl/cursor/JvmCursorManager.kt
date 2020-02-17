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

package com.acornui.lwjgl.cursor

import com.acornui.LifecycleBase
import com.acornui.asset.load
import com.acornui.cursor.Cursor
import com.acornui.cursor.CursorManagerBase
import com.acornui.cursor.StandardCursors
import com.acornui.graphic.RgbData
import com.acornui.io.JvmBufferUtil
import com.acornui.io.Loader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWImage

class JvmCursorManager(window: Long, rgbDataLoader: Loader<RgbData>, scope: CoroutineScope) : CursorManagerBase() {

	val cursorsPath = "assets/uiskin/cursors/"

	init {
		// Cursors
		with (StandardCursors) {
			DEFAULT = JvmStandardCursor(window, GLFW.GLFW_ARROW_CURSOR)
			IBEAM = JvmStandardCursor(window, GLFW.GLFW_IBEAM_CURSOR)
			CROSSHAIR = JvmStandardCursor(window, GLFW.GLFW_CROSSHAIR_CURSOR)
			HAND = JvmStandardCursor(window, GLFW.GLFW_HAND_CURSOR)
			RESIZE_EW = JvmStandardCursor(window, GLFW.GLFW_HRESIZE_CURSOR)
			RESIZE_NS = JvmStandardCursor(window, GLFW.GLFW_VRESIZE_CURSOR)
			ALIAS = JvmTextureCursor(window, rgbDataLoader, scope, cursorsPath + "Alias.png", 2, 2)
			ALL_SCROLL = JvmTextureCursor(window, rgbDataLoader, scope, cursorsPath + "AllScroll.png", 12, 12)
			CELL = JvmTextureCursor(window, rgbDataLoader, scope, cursorsPath + "Cell.png", 12, 12)
			COPY = JvmTextureCursor(window, rgbDataLoader, scope, cursorsPath + "Copy.png", 2, 2)
			HELP = JvmTextureCursor(window, rgbDataLoader, scope, cursorsPath + "Help.png", 2, 2)
			MOVE = JvmTextureCursor(window, rgbDataLoader, scope, cursorsPath + "Move.png", 12, 12)
			NONE = HiddenCursor(window)
			NOT_ALLOWED = JvmTextureCursor(window, rgbDataLoader, scope, cursorsPath + "NotAllowed.png", 12, 12)
			POINTER_WAIT = JvmTextureCursor(window, rgbDataLoader, scope, cursorsPath + "PointerWait.png", 1, 3)
			RESIZE_NE = JvmTextureCursor(window, rgbDataLoader, scope, cursorsPath + "ResizeNE.png", 13, 13)
			RESIZE_SE = JvmTextureCursor(window, rgbDataLoader, scope, cursorsPath + "ResizeSE.png", 13, 13)
			WAIT = JvmTextureCursor(window, rgbDataLoader, scope, cursorsPath + "Wait.png", 6, 2)
		}
	}
}

/**
 * Loads a texture atlas and pulls out the cursor region, sending the pixels to the OS.
 */
class JvmTextureCursor(
		private val window: Long,
		val rgbDataLoader: Loader<RgbData>,
		scope: CoroutineScope,
		texturePath: String,
		val hotX: Int,
		val hotY: Int
) : LifecycleBase(), Cursor {

	private var cursor: Long = -1L

	init {
		scope.launch {
			setTexture(rgbDataLoader.load(texturePath))
		}
	}

	private fun setTexture(rgbData: RgbData) {
		val i = GLFWImage.create()
		i.width(rgbData.width)
		i.height(rgbData.height)
		i.pixels(JvmBufferUtil.wrap(rgbData.bytes))
		if (cursor != -1L) {
			GLFW.glfwDestroyCursor(cursor)
		}
		cursor = GLFW.glfwCreateCursor(i, hotX, hotY)
		if (isActive) onActivated()
	}

	override fun onActivated() {
		if (cursor == -1L) return // Not ready
		GLFW.glfwSetCursor(window, cursor)
	}

	override fun onDeactivated() {
		if (cursor == -1L) return
		GLFW.glfwSetCursor(window, 0L) // TODO: this probably doesn't work... no NULL?
	}

	override fun dispose() {
		super.dispose()

		if (cursor != -1L) {
			GLFW.glfwDestroyCursor(cursor)
		}
	}
}

class JvmStandardCursor(
		private val window: Long,
		shape: Int
) : LifecycleBase(), Cursor {

	private val cursor: Long = GLFW.glfwCreateStandardCursor(shape)

	override fun onActivated() {
		GLFW.glfwSetCursor(window, cursor)
	}

	override fun onDeactivated() {
		GLFW.glfwSetCursor(window, 0L)
	}

	override fun dispose() {
		super.dispose()
		GLFW.glfwDestroyCursor(cursor)
	}
}

class HiddenCursor(val window: Long) : LifecycleBase(), Cursor {

	override fun onActivated() {
		GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN)
	}

	override fun onDeactivated() {
		GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL)
	}
}
