/*
 * Copyright 2016 Nicholas Bilyk
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

package com.acornui.core

// Commented out due to https://github.com/polyforest/acornui/issues/121
//import com.acornui.component.UiComponentImpl
//import com.acornui.test.MockInjector.owner
//import com.acornui.test.benchmark
//import kotlin.test.Test
//
//class UiComponentPerformance {
//
//	@Test fun construction() {
//		val owner = owner
//		UiComponentImpl(owner) // Exclude first-time
//
//		val speed = benchmark {
//			UiComponentImpl(owner)
//		}
//
//		// Construct avg: 0.03143258ms  Theoretical best: 0.001ms
//		println("Construct 1000 avg: ${speed}ms")
//	}
//}