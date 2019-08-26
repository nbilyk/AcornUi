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

package com.acornui.mock

import com.acornui.input.MouseInput
import com.acornui.input.WhichButton
import com.acornui.input.interaction.MouseInteractionRo
import com.acornui.input.interaction.TouchInteractionRo
import com.acornui.input.interaction.TouchRo
import com.acornui.input.interaction.WheelInteractionRo
import com.acornui.signal.Signal
import com.acornui.signal.emptySignal

object MockMouseInput : MouseInput {
	override val touchModeChanged: Signal<() -> Unit> = emptySignal()
	override val touchMode: Boolean = false
	override val overCanvasChanged: Signal<(Boolean) -> Unit> = emptySignal()
	override val overCanvas: Boolean = false
	override val canvasX: Float = 0f
	override val canvasY: Float = 0f
	override val touches: List<TouchRo> = emptyList()

	override val touchStart: Signal<(TouchInteractionRo) -> Unit> = emptySignal()
	override val touchEnd: Signal<(TouchInteractionRo) -> Unit> = emptySignal()
	override val touchMove: Signal<(TouchInteractionRo) -> Unit> = emptySignal()
	override val touchCancel: Signal<(TouchInteractionRo) -> Unit> = emptySignal()
	override val mouseDown: Signal<(MouseInteractionRo) -> Unit> = emptySignal()
	override val mouseUp: Signal<(MouseInteractionRo) -> Unit> = emptySignal()
	override val mouseMove: Signal<(MouseInteractionRo) -> Unit> = emptySignal()
	override val mouseWheel: Signal<(WheelInteractionRo) -> Unit> = emptySignal()

	override fun mouseIsDown(button: WhichButton): Boolean {
		return false
	}

	override fun dispose() {
	}
}