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

package com.acornui.input.interaction

import com.acornui.component.UiComponent
import com.acornui.component.createOrReuse
import com.acornui.input.EventType
import com.acornui.input.InteractivityManager
import com.acornui.input.WhichButton
import com.acornui.signal.StoppableSignal
import com.acornui.time.callLater
import com.acornui.time.nowMs

interface ClickEventRo : MouseEventRo {

	val count: Int
	val fromTouch: Boolean

	companion object {
		val LEFT_CLICK = EventType<ClickEventRo>("leftClick")
		val RIGHT_CLICK = EventType<ClickEventRo>("rightClick")
		val BACK_CLICK = EventType<ClickEventRo>("backClick")
		val FORWARD_CLICK = EventType<ClickEventRo>("forwardClick")
		val MIDDLE_CLICK = EventType<ClickEventRo>("middleClick")
	}
}

/**
 * An event representing a touchStart/mouseDown touchEnd/mouseUp on the same component.
 */
open class ClickEvent : ClickEventRo, MouseEvent() {

	/**
	 * In a standard click event, this is always 1. When used in a multi click event, count is the number of
	 * consecutive clicks, each click within [ClickDispatcher.multiClickSpeed] milliseconds of the next.
	 */
	override var count: Int = 0
	override var fromTouch = false

	override fun clear() {
		super.clear()
		count = 0
		fromTouch = false
	}
}

/**
 * A click interaction is where there is a touch down event, then a touch up event on that same target.
 */
fun UiComponent.click(isCapture: Boolean = false): StoppableSignal<ClickEventRo> {
	return createOrReuse(ClickEventRo.LEFT_CLICK, isCapture)
}

fun UiComponent.rightClick(isCapture: Boolean = false): StoppableSignal<ClickEventRo> {
	return createOrReuse(ClickEventRo.RIGHT_CLICK, isCapture)
}

fun UiComponent.middleClick(isCapture: Boolean = false): StoppableSignal<ClickEventRo> {
	return createOrReuse(ClickEventRo.MIDDLE_CLICK, isCapture)
}

fun UiComponent.backClick(isCapture: Boolean = false): StoppableSignal<ClickEventRo> {
	return createOrReuse(ClickEventRo.BACK_CLICK, isCapture)
}

fun UiComponent.forwardClick(isCapture: Boolean = false): StoppableSignal<ClickEventRo> {
	return createOrReuse(ClickEventRo.FORWARD_CLICK, isCapture)
}

private val fakeClickEvent = ClickEvent()

fun UiComponent.dispatchClick(): ClickEventRo {
	fakeClickEvent.clear()
	fakeClickEvent.isFabricated = true
	fakeClickEvent.type = ClickEventRo.LEFT_CLICK
	fakeClickEvent.target = this
	fakeClickEvent.button = WhichButton.LEFT
	fakeClickEvent.timestamp = nowMs()
	fakeClickEvent.count = 1
	inject(InteractivityManager).dispatch(fakeClickEvent, this)
	return fakeClickEvent
}

private val preventDefaultHandler = { event: ClickEventRo ->
	event.handled = true
	event.preventDefault()
}

/**
 * Marks any click events as handled and default prevented for one frame.
 */
fun UiComponent.clickHandledForAFrame() {
	if (!click().contains(preventDefaultHandler)) {
		click().add(preventDefaultHandler)
		callLater { click().remove(preventDefaultHandler) }
	}
}