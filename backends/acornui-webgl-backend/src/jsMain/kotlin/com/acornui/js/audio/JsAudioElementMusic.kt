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

package com.acornui.js.audio

import com.acornui.audio.Music
import com.acornui.audio.MusicReadyState
import com.acornui.audio.AudioManager
import com.acornui.math.MathUtils
import com.acornui.signal.Signal0
import org.w3c.dom.HTMLAudioElement
import org.w3c.dom.events.Event
import kotlin.browser.document
import kotlin.time.Duration
import kotlin.time.seconds

open class JsAudioElementMusic(
		private val audioManager: AudioManager,
		private val element: HTMLAudioElement) : Music {

	override val readyStateChanged = Signal0()

	override var readyState = MusicReadyState.NOTHING

	override var onCompleted: (() -> Unit)? = null

	override val duration: Duration
		get() = element.duration.seconds

	private val elementEndedHandler = {
		event: Event ->
		if (!loop)
			onCompleted?.invoke()
		Unit
	}

	private val loadedDataHandler = {
		event: Event ->
		if (readyState == MusicReadyState.NOTHING && element.readyState >= 3) {
			// HAVE_FUTURE_DATA
			readyState = MusicReadyState.READY
			readyStateChanged.dispatch()
		}
	}

	init {
		element.addEventListener("ended", elementEndedHandler)
		element.addEventListener("loadeddata", loadedDataHandler)
		audioManager.registerMusic(this)
	}

	override val isPlaying: Boolean
		get() = !element.paused

	override var loop: Boolean
		get() = element.loop
		set(value) {
			element.loop = value
		}

	private var _volume: Float = 1f
	override var volume: Float
		get() = _volume
		set(value) {
			_volume = value
			element.volume = MathUtils.clamp(value * audioManager.musicVolume, 0f, 1f).toDouble()
		}

	override fun play() {
		element.play()
	}

	override fun pause() {
		element.pause()
	}

	override fun stop() {
		element.currentTime = 0.0
		element.pause()
	}

	override var currentTime: Duration
		get() = element.currentTime.seconds
		set(value) {
			element.currentTime = value.inSeconds
		}

	override fun update() {
	}

	override fun dispose() {
		audioManager.unregisterMusic(this)
		readyStateChanged.dispose()
		element.removeEventListener("ended", elementEndedHandler)
		element.removeEventListener("loadeddata", loadedDataHandler)

		// Untested: http://stackoverflow.com/questions/3258587/how-to-properly-unload-destroy-a-video-element
		element.pause()
		element.src = ""
		element.load()
	}
}

fun Audio(source: String): HTMLAudioElement {
	val audio = document.createElement("AUDIO") as HTMLAudioElement
	audio.src = source
	return audio
}
