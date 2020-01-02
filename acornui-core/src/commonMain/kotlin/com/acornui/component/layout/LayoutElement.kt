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

package com.acornui.component.layout

import com.acornui.math.*
import com.acornui.math.MathUtils.clamp

interface LayoutElementRo : BasicLayoutElementRo, TransformableRo {

	/**
	 * Returns true if visible and the includeInLayout flag is true. If this is false, this layout element will not
	 * be included in layout algorithms.
	 */
	val shouldLayout: Boolean

	/**
	 * Given a canvas position, casts a ray in the direction of the camera, and returns true if that ray intersects
	 * with this component. This will always return false if this element is not active (on the stage)
	 * @param canvasX
	 * @param canvasY
	 */
	fun containsCanvasPoint(canvasX: Float, canvasY: Float): Boolean

	/**
	 * Returns true if this primitive intersects with the provided ray (in world coordinates)
	 * If there was an intersection, the intersection vector will be set to the intersection point.
	 *
	 * @param globalRay The ray (in world coordinates) to cast.
	 *
	 * @return Returns true if the ray intersects with the bounding box of this layout element.
	 */
	fun intersectsGlobalRay(globalRay: RayRo, intersection: Vector3): Boolean

	val minWidth: Float
	val minHeight: Float
	val maxWidth: Float
	val maxHeight: Float
}

private val tmpVec = Vector3()

/**
 * Returns true if this primitive intersects with the provided ray (in world coordinates)
 *
 * @return Returns true if the ray intersects with the bounding box of this layout element.
 */
fun LayoutElementRo.intersectsGlobalRay(globalRay: RayRo): Boolean = intersectsGlobalRay(globalRay, tmpVec)

fun LayoutElementRo.clampWidth(value: Float?): Float? {
	return clamp(value, minWidth, maxWidth)
}

fun LayoutElementRo.clampHeight(value: Float?): Float? {
	return clamp(value, minHeight, maxHeight)
}
/**
 * A LayoutElement is a Transformable component that can be used in layout algorithms.
 * It has features responsible for providing explicit dimensions, and returning measured dimensions.
 * @author nbilyk
 */
interface LayoutElement : LayoutElementRo, BasicLayoutElement, Transformable {

	override var minWidth: Float
	override var minHeight: Float
	override var maxWidth: Float
	override var maxHeight: Float
}

interface BasicLayoutElementRo : SizableRo, PositionableRo {

	/**
	 * The left boundary (x + bounds.left)
	 */
	val left: Float
		get() = x + bounds.left

	/**
	 * The top boundary (y + bounds.top)
	 */
	val top: Float
		get() = y + bounds.top


	/**
	 * The right boundary (x + bounds.right)
	 */
	val right: Float
		get() = x + bounds.right

	/**
	 * The bottom boundary (y + bounds.bottom)
	 */
	val bottom: Float
		get() = y + bounds.bottom

	/**
	 * The y value representing the baseline + y position.
	 */
	val baselineY: Float
		get() = y + bounds.baselineY

	/**
	 * The layout data to be used in layout algorithms.
	 * Most layout containers have a special layout method that statically types the type of
	 * layout data that a component should have.
	 */
	val layoutData: LayoutData?
}

interface BasicLayoutElement : BasicLayoutElementRo, Sizable, Positionable {

	/**
	 * The layout data to be used in layout algorithms.
	 * Most layout containers have a special layout method that statically types the type of
	 * layout data that a component should have.
	 */
	override var layoutData: LayoutData?
}

interface SizableRo {

	/**
	 * Returns the actual, untransformed width.
	 * If layout is invalid, this will invoke a layout validation.
	 * This is the same as `bounds.width`
	 */
	val width: Float
		get() = bounds.width

	/**
	 * Returns the actual, untransformed height.
	 * If layout is invalid, this will invoke a layout validation.
	 * This is the same as `bounds.height`
	 */
	val height: Float
		get() = bounds.height

	/**
	 * The y position representing the baseline of the first line of text.
	 */
	val baseline: Float
		get() = bounds.baseline

	/**
	 * The height below the baseline.
	 */
	val descender: Float
		get() = height - baseline

	/**
	 * The actual bounds of this component.
	 */
	val bounds: BoundsRo

	/**
	 * The explicit width, as set by `width(value)`
	 * Typically one would use [width] in order to retrieve the actual width.
	 */
	val explicitWidth: Float?

	/**
	 * The explicit height, as set by `height(value)`
	 * Typically one would use [height] in order to retrieve actual height.
	 */
	val explicitHeight: Float?

}

interface Sizable : SizableRo {

	/**
	 * Does the same thing as setting [width] and [height] individually, but may be more efficient depending on
	 * implementation.
	 * @param width The explicit width for the component. Use null to use the natural measured width.
	 * @param height The explicit height for the component. Use null to use the natural measured height.
	 */
	fun setSize(width: Float?, height: Float?)

	/**
	 * Sets the explicit width for this layout element. (A null value represents using the measured width)
	 */
	fun width(value: Float?) = setSize(width = value, height = explicitHeight)


	/**
	 * Sets the explicit height for this layout element. (A null value represents using the measured height)
	 */
	fun height(value: Float?) = setSize(width = explicitWidth, height = value)

}

fun LayoutElement.setSize(bounds: BoundsRo) = setSize(bounds.width, bounds.height)
