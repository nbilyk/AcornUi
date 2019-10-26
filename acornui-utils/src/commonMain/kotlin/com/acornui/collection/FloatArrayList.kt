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

@file:Suppress("NOTHING_TO_INLINE")

package com.acornui.collection

import com.acornui.recycle.Clearable

/**
 * A read-only view of a FloatArray.

 * At the time of writing, inline classes cannot implement equals, so unlike most lists, equals is not a deep check.
 * Use [contentEquals] instead.
 */
interface FloatArrayListRo : List<Float> {

	/**
	 * Returns the unboxed FloatArray.
	 * @suppress
	 */
	val native: FloatArray
}

inline infix fun FloatArrayListRo.contentEquals(other: FloatArrayListRo): Boolean = native.contentEquals(other.native)
inline infix fun FloatArray.contentEquals(other: FloatArrayListRo): Boolean = contentEquals(other.native)
inline infix fun FloatArrayListRo.contentEquals(other: FloatArray): Boolean = native.contentEquals(other)

/**
 * @see kotlin.collections.copyInto
 */
fun FloatArrayListRo.copyInto(other: FloatArrayList, destinationOffset: Int = 0, startIndex: Int = 0, endIndex: Int = size) = native.copyInto(other.native, destinationOffset, startIndex, endIndex)

/**
 * @see kotlin.collections.copyInto
 */
fun FloatArray.copyInto(other: FloatArrayList, destinationOffset: Int = 0, startIndex: Int = 0, endIndex: Int = size) = copyInto(other.native, destinationOffset, startIndex, endIndex)

/**
 * @see kotlin.collections.copyInto
 */
fun FloatArrayListRo.copyInto(other: FloatArray, destinationOffset: Int = 0, startIndex: Int = 0, endIndex: Int = size) = native.copyInto(other, destinationOffset, startIndex, endIndex)


/**
 * A wrapper to [FloatArray] that implements [List].
 */
inline class FloatArrayList(val inner: FloatArray) : FloatArrayListRo {

	override val size: Int
		get() = inner.size

	override fun contains(element: Float): Boolean {
		return inner.contains(element)
	}

	override fun containsAll(elements: Collection<Float>): Boolean {
		for (element in elements) {
			if (!contains(element)) return false
		}
		return true
	}

	override fun get(index: Int): Float {
		return inner[index]
	}

	operator fun set(index: Int, element: Float) {
		inner[index] = element
	}

	override fun indexOf(element: Float): Int {
		return inner.indexOf(element)
	}

	override fun isEmpty(): Boolean {
		return inner.isEmpty()
	}

	override fun iterator(): Iterator<Float> {
		return inner.iterator()
	}

	override fun lastIndexOf(element: Float): Int {
		return inner.lastIndexOf(element)
	}

	override fun listIterator(): FloatArrayIterator {
		return FloatArrayIterator(inner)
	}

	override fun listIterator(index: Int): FloatArrayIterator {
		val iterator = FloatArrayIterator(inner)
		iterator.cursor = index
		return iterator
	}

	override fun subList(fromIndex: Int, toIndex: Int): List<Float> {
		return SubList(this, fromIndex, toIndex)
	}

	override val native: FloatArray
		get() = inner
}

/**
 * An iterator object for a FloatArray.
 * Use this wrapper when using an Array<T> where an Iterable<T> is needed.
 */
open class FloatArrayIterator(
		val array: FloatArray
) : Clearable, ListIterator<Float>, Iterable<Float> {

	var cursor: Int = 0     // index of next element to return
	var lastRet: Int = -1   // index of last element returned; -1 if no such

	override fun hasNext(): Boolean {
		return cursor != array.size
	}

	override fun next(): Float {
		val i = cursor
		if (i >= array.size)
			throw Exception("Iterator does not have next.")
		cursor = i + 1
		lastRet = i
		return array[i]
	}

	override fun nextIndex(): Int {
		return cursor
	}

	override fun hasPrevious(): Boolean {
		return cursor != 0
	}

	override fun previous(): Float {
		val i = cursor - 1
		if (i < 0)
			throw Exception("Iterator does not have previous.")
		cursor = i
		lastRet = i
		return array[i]
	}

	override fun previousIndex(): Int {
		return cursor - 1
	}

	/**
	 * A FloatArrayIterator can have elements be set, but it cannot implement [MutableListIterator] because the array's
	 * size cannot change.
	 */
	fun set(element: Float) {
		if (lastRet < 0)
			throw Exception("Cannot set before iteration.")
		array[lastRet] = element
	}

	override fun clear() {
		cursor = 0
		lastRet = -1
	}

	override fun iterator(): Iterator<Float> {
		return this
	}
}