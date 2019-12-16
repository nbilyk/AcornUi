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

package com.acornui.gl.core

import com.acornui.graphic.ColorRo
import com.acornui.graphic.Texture
import com.acornui.io.NativeReadBuffer

/**
 * A wrapper to [Gl20] that caches properties for faster access.
 * Example:
 * gl.activeTexture(Gl20.TEXTURE0)
 * gl.getParameter(Gl20.ACTIVE_TEXTURE) // No need to query GPU, returns Gl20.TEXTURE0 from ram.
 */
open class Gl20CachedProperties(protected val wrapped: Gl20) : Gl20 {

	private val parametersB = HashMap<Int, Boolean>()
	private val parametersBv = HashMap<Int, BooleanArray>()

	private val parametersI = HashMap<Int, Int>()
	private val parametersIv = HashMap<Int, IntArray>()

	private val parametersF = HashMap<Int, Float>()
	private val parametersFv = HashMap<Int, FloatArray>()

	override fun activeTexture(texture: Int) {
		parametersI[Gl20.ACTIVE_TEXTURE] = texture
		wrapped.activeTexture(texture)
	}

	override fun attachShader(program: GlProgramRef, shader: GlShaderRef) {
		wrapped.attachShader(program, shader)
	}

	override fun bindAttribLocation(program: GlProgramRef, index: Int, name: String) {
		wrapped.bindAttribLocation(program, index, name)
	}

	override fun bindBuffer(target: Int, buffer: GlBufferRef?) {
		wrapped.bindBuffer(target, buffer)
	}

	override fun bindFramebuffer(target: Int, framebuffer: GlFramebufferRef?) {
		wrapped.bindFramebuffer(target, framebuffer)
	}

	override fun bindRenderbuffer(target: Int, renderbuffer: GlRenderbufferRef?) {
		wrapped.bindRenderbuffer(target, renderbuffer)
	}

	override fun bindTexture(target: Int, texture: GlTextureRef?) {
		wrapped.bindTexture(target, texture)
	}

	override fun blendColor(red: Float, green: Float, blue: Float, alpha: Float) {
		wrapped.blendColor(red, green, blue, alpha)
	}

	override fun blendEquation(mode: Int) {
		wrapped.blendEquation(mode)
	}

	override fun blendEquationSeparate(modeRgb: Int, modeAlpha: Int) {
		wrapped.blendEquationSeparate(modeRgb, modeAlpha)
	}

	override fun blendFunc(sfactor: Int, dfactor: Int) {
		wrapped.blendFunc(sfactor, dfactor)
	}

	override fun blendFuncSeparate(srcRgb: Int, dstRgb: Int, srcAlpha: Int, dstAlpha: Int) {
		wrapped.blendFuncSeparate(srcRgb, dstRgb, srcAlpha, dstAlpha)
	}

	override fun bufferData(target: Int, size: Int, usage: Int) {
		wrapped.bufferData(target, size, usage)
	}

	override fun bufferDatabv(target: Int, data: NativeReadBuffer<Byte>, usage: Int) {
		wrapped.bufferDatabv(target, data, usage)
	}

	override fun bufferDatafv(target: Int, data: NativeReadBuffer<Float>, usage: Int) {
		wrapped.bufferDatafv(target, data, usage)
	}

	override fun bufferDatasv(target: Int, data: NativeReadBuffer<Short>, usage: Int) {
		wrapped.bufferDatasv(target, data, usage)
	}

	override fun bufferSubDatafv(target: Int, offset: Int, data: NativeReadBuffer<Float>) {
		wrapped.bufferSubDatafv(target, offset, data)
	}

	override fun bufferSubDatasv(target: Int, offset: Int, data: NativeReadBuffer<Short>) {
		wrapped.bufferSubDatasv(target, offset, data)
	}

	override fun checkFramebufferStatus(target: Int): Int {
		val ret = wrapped.checkFramebufferStatus(target)
		return ret
	}

	override fun clear(mask: Int) {
		wrapped.clear(mask)
	}

	override fun clearColor(red: Float, green: Float, blue: Float, alpha: Float) {
		val p = parametersFv.getOrPut(Gl20.COLOR_CLEAR_VALUE) { FloatArray(4) }
		p[0] = red
		p[1] = green
		p[2] = blue
		p[3] = alpha
		wrapped.clearColor(red, green, blue, alpha)
	}

	override fun clearDepth(depth: Float) {
		parametersF[Gl20.DEPTH_CLEAR_VALUE] = depth
		wrapped.clearDepth(depth)
	}

	override fun clearStencil(s: Int) {
		parametersI[Gl20.STENCIL_CLEAR_VALUE] = s
		wrapped.clearStencil(s)
	}

	override fun colorMask(red: Boolean, green: Boolean, blue: Boolean, alpha: Boolean) {
		val p = parametersBv.getOrPut(Gl20.COLOR_WRITEMASK) { BooleanArray(4) }
		p[0] = red
		p[1] = green
		p[2] = blue
		p[3] = alpha
		wrapped.colorMask(red, green, blue, alpha)
	}

	override fun compileShader(shader: GlShaderRef) {
		wrapped.compileShader(shader)
	}

	override fun copyTexImage2D(target: Int, level: Int, internalFormat: Int, x: Int, y: Int, width: Int, height: Int, border: Int) {
		wrapped.copyTexImage2D(target, level, internalFormat, x, y, width, height, border)
	}

	override fun copyTexSubImage2D(target: Int, level: Int, xOffset: Int, yOffset: Int, x: Int, y: Int, width: Int, height: Int) {
		wrapped.copyTexSubImage2D(target, level, xOffset, yOffset, x, y, width, height)
	}

	override fun createBuffer(): GlBufferRef {
		val ret = wrapped.createBuffer()
		return ret
	}

	override fun createFramebuffer(): GlFramebufferRef {
		val ret = wrapped.createFramebuffer()
		return ret
	}

	override fun createProgram(): GlProgramRef {
		val ret = wrapped.createProgram()
		return ret
	}

	override fun createRenderbuffer(): GlRenderbufferRef {
		val ret = wrapped.createRenderbuffer()
		return ret
	}

	override fun createShader(type: Int): GlShaderRef {
		val ret = wrapped.createShader(type)
		return ret
	}

	override fun createTexture(): GlTextureRef {
		val ret = wrapped.createTexture()
		return ret
	}

	override fun cullFace(mode: Int) {
		wrapped.cullFace(mode)
	}

	override fun deleteBuffer(buffer: GlBufferRef) {
		wrapped.deleteBuffer(buffer)
	}

	override fun deleteFramebuffer(framebuffer: GlFramebufferRef) {
		wrapped.deleteFramebuffer(framebuffer)
	}

	override fun deleteProgram(program: GlProgramRef) {
		wrapped.deleteProgram(program)
	}

	override fun deleteRenderbuffer(renderbuffer: GlRenderbufferRef) {
		wrapped.deleteRenderbuffer(renderbuffer)
	}

	override fun deleteShader(shader: GlShaderRef) {
		wrapped.deleteShader(shader)
	}

	override fun deleteTexture(texture: GlTextureRef) {
		wrapped.deleteTexture(texture)
	}

	override fun depthFunc(func: Int) {
		wrapped.depthFunc(func)
	}

	override fun depthMask(flag: Boolean) {
		wrapped.depthMask(flag)
	}

	override fun depthRange(zNear: Float, zFar: Float) {
		wrapped.depthRange(zNear, zFar)
	}

	override fun detachShader(program: GlProgramRef, shader: GlShaderRef) {
		wrapped.detachShader(program, shader)
	}

	override fun disable(cap: Int) {
		wrapped.disable(cap)
	}

	override fun disableVertexAttribArray(index: Int) {
		wrapped.disableVertexAttribArray(index)
	}

	override fun drawArrays(mode: Int, first: Int, count: Int) {
		wrapped.drawArrays(mode, first, count)
	}

	override fun drawElements(mode: Int, count: Int, type: Int, offset: Int) {
		wrapped.drawElements(mode, count, type, offset)
	}

	override fun enable(cap: Int) {
		wrapped.enable(cap)
	}

	override fun enableVertexAttribArray(index: Int) {
		wrapped.enableVertexAttribArray(index)
	}

	override fun finish() {
		wrapped.finish()
	}

	override fun flush() {
		wrapped.flush()
	}

	override fun framebufferRenderbuffer(target: Int, attachment: Int, renderbufferTarget: Int, renderbuffer: GlRenderbufferRef) {
		wrapped.framebufferRenderbuffer(target, attachment, renderbufferTarget, renderbuffer)
	}

	override fun framebufferTexture2D(target: Int, attachment: Int, textureTarget: Int, texture: GlTextureRef, level: Int) {
		wrapped.framebufferTexture2D(target, attachment, textureTarget, texture, level)
	}

	override fun frontFace(mode: Int) {
		wrapped.frontFace(mode)
	}

	override fun generateMipmap(target: Int) {
		wrapped.generateMipmap(target)
	}

	override fun getActiveAttrib(program: GlProgramRef, index: Int): GlActiveInfoRef {
		val ret = wrapped.getActiveAttrib(program, index)
		return ret
	}

	override fun getActiveUniform(program: GlProgramRef, index: Int): GlActiveInfoRef {
		val ret = wrapped.getActiveUniform(program, index)
		return ret
	}

	override fun getAttachedShaders(program: GlProgramRef): Array<GlShaderRef> {
		val ret = wrapped.getAttachedShaders(program)
		return ret
	}

	override fun getAttribLocation(program: GlProgramRef, name: String): Int {
		val ret = wrapped.getAttribLocation(program, name)
		return ret
	}

	override fun getError(): Int {
		val ret = wrapped.getError()
		return ret
	}

	override fun getProgramInfoLog(program: GlProgramRef): String? {
		val ret = wrapped.getProgramInfoLog(program)
		return ret
	}

	override fun getShaderInfoLog(shader: GlShaderRef): String? {
		val ret = wrapped.getShaderInfoLog(shader)
		return ret
	}

	override fun getUniformLocation(program: GlProgramRef, name: String): GlUniformLocationRef? {
		val ret = wrapped.getUniformLocation(program, name)
		return ret
	}

	override fun hint(target: Int, mode: Int) {
		wrapped.hint(target, mode)
	}

	override fun isBuffer(buffer: GlBufferRef): Boolean {
		val ret = wrapped.isBuffer(buffer)
		return ret
	}

	override fun isEnabled(cap: Int): Boolean {
		val ret = wrapped.isEnabled(cap)
		return ret
	}

	override fun isFramebuffer(framebuffer: GlFramebufferRef): Boolean {
		val ret = wrapped.isFramebuffer(framebuffer)
		return ret
	}

	override fun isProgram(program: GlProgramRef): Boolean {
		val ret = wrapped.isProgram(program)
		return ret
	}

	override fun isRenderbuffer(renderbuffer: GlRenderbufferRef): Boolean {
		val ret = wrapped.isRenderbuffer(renderbuffer)
		return ret
	}

	override fun isShader(shader: GlShaderRef): Boolean {
		val ret = wrapped.isShader(shader)
		return ret
	}

	override fun isTexture(texture: GlTextureRef): Boolean {
		val ret = wrapped.isTexture(texture)
		return ret
	}

	override fun lineWidth(width: Float) {
		wrapped.lineWidth(width)
	}

	override fun linkProgram(program: GlProgramRef) {
		wrapped.linkProgram(program)
	}

	override fun pixelStorei(pName: Int, param: Int) {
		wrapped.pixelStorei(pName, param)
	}

	override fun polygonOffset(factor: Float, units: Float) {
		wrapped.polygonOffset(factor, units)
	}

	override fun readPixels(x: Int, y: Int, width: Int, height: Int, format: Int, type: Int, pixels: NativeReadBuffer<Byte>) {
		wrapped.readPixels(x, y, width, height, format, type, pixels)
	}

	override fun renderbufferStorage(target: Int, internalFormat: Int, width: Int, height: Int) {
		wrapped.renderbufferStorage(target, internalFormat, width, height)
	}

	override fun sampleCoverage(value: Float, invert: Boolean) {
		wrapped.sampleCoverage(value, invert)
	}

	override fun scissor(x: Int, y: Int, width: Int, height: Int) {
		wrapped.scissor(x, y, width, height)
	}

	override fun shaderSource(shader: GlShaderRef, source: String) {
		wrapped.shaderSource(shader, source)
	}

	override fun stencilFunc(func: Int, ref: Int, mask: Int) {
		wrapped.stencilFunc(func, ref, mask)
	}

	override fun stencilFuncSeparate(face: Int, func: Int, ref: Int, mask: Int) {
		wrapped.stencilFuncSeparate(face, func, ref, mask)
	}

	override fun stencilMask(mask: Int) {
		wrapped.stencilMask(mask)
	}

	override fun stencilMaskSeparate(face: Int, mask: Int) {
		wrapped.stencilMaskSeparate(face, mask)
	}

	override fun stencilOp(fail: Int, zFail: Int, zPass: Int) {
		wrapped.stencilOp(fail, zFail, zPass)
	}

	override fun stencilOpSeparate(face: Int, fail: Int, zFail: Int, zPass: Int) {
		wrapped.stencilOpSeparate(face, fail, zFail, zPass)
	}

	override fun texImage2Db(target: Int, level: Int, internalFormat: Int, width: Int, height: Int, border: Int, format: Int, type: Int, pixels: NativeReadBuffer<Byte>?) {
		wrapped.texImage2Db(target, level, internalFormat, width, height, border, format, type, pixels)
	}

	override fun texImage2Df(target: Int, level: Int, internalFormat: Int, width: Int, height: Int, border: Int, format: Int, type: Int, pixels: NativeReadBuffer<Float>?) {
		wrapped.texImage2Df(target, level, internalFormat, width, height, border, format, type, pixels)
	}

	override fun texImage2D(target: Int, level: Int, internalFormat: Int, format: Int, type: Int, texture: Texture) {
		wrapped.texImage2D(target, level, internalFormat, format, type, texture)
	}

	override fun texParameterf(target: Int, pName: Int, param: Float) {
		wrapped.texParameterf(target, pName, param)
	}

	override fun texParameteri(target: Int, pName: Int, param: Int) {
		wrapped.texParameteri(target, pName, param)
	}

	override fun texSubImage2D(target: Int, level: Int, xOffset: Int, yOffset: Int, format: Int, type: Int, texture: Texture) {
		wrapped.texSubImage2D(target, level, xOffset, yOffset, format, type, texture)
	}

	override fun uniform1f(location: GlUniformLocationRef, x: Float) {
		wrapped.uniform1f(location, x)
	}

	override fun uniform1fv(location: GlUniformLocationRef, v: FloatArray) {
		wrapped.uniform1fv(location, v)
	}

	override fun uniform1i(location: GlUniformLocationRef, x: Int) {
		wrapped.uniform1i(location, x)
	}

	override fun uniform1iv(location: GlUniformLocationRef, v: IntArray) {
		wrapped.uniform1iv(location, v)
	}

	override fun uniform2f(location: GlUniformLocationRef, x: Float, y: Float) {
		wrapped.uniform2f(location, x, y)
	}

	override fun uniform2fv(location: GlUniformLocationRef, v: FloatArray) {
		wrapped.uniform2fv(location, v)
	}

	override fun uniform2i(location: GlUniformLocationRef, x: Int, y: Int) {
		wrapped.uniform2i(location, x, y)
	}

	override fun uniform2iv(location: GlUniformLocationRef, v: IntArray) {
		wrapped.uniform2iv(location, v)
	}

	override fun uniform3f(location: GlUniformLocationRef, x: Float, y: Float, z: Float) {
		wrapped.uniform3f(location, x, y, z)
	}

	override fun uniform3fv(location: GlUniformLocationRef, v: FloatArray) {
		wrapped.uniform3fv(location, v)
	}

	override fun uniform3i(location: GlUniformLocationRef, x: Int, y: Int, z: Int) {
		wrapped.uniform3i(location, x, y, z)
	}

	override fun uniform3iv(location: GlUniformLocationRef, v: IntArray) {
		wrapped.uniform3iv(location, v)
	}

	override fun uniform4f(location: GlUniformLocationRef, x: Float, y: Float, z: Float, w: Float) {
		wrapped.uniform4f(location, x, y, z, w)
	}

	override fun uniform4fv(location: GlUniformLocationRef, v: FloatArray) {
		wrapped.uniform4fv(location, v)
	}

	override fun uniform4i(location: GlUniformLocationRef, x: Int, y: Int, z: Int, w: Int) {
		wrapped.uniform4i(location, x, y, z, w)
	}

	override fun uniform4iv(location: GlUniformLocationRef, v: IntArray) {
		wrapped.uniform4iv(location, v)
	}

	override fun uniformMatrix2fv(location: GlUniformLocationRef, transpose: Boolean, value: FloatArray) {
		wrapped.uniformMatrix2fv(location, transpose, value)
	}

	override fun uniformMatrix3fv(location: GlUniformLocationRef, transpose: Boolean, value: FloatArray) {
		wrapped.uniformMatrix3fv(location, transpose, value)
	}

	override fun uniformMatrix4fv(location: GlUniformLocationRef, transpose: Boolean, value: FloatArray) {
		wrapped.uniformMatrix4fv(location, transpose, value)
	}

	override fun useProgram(program: GlProgramRef?) {
		wrapped.useProgram(program)
	}

	override fun validateProgram(program: GlProgramRef) {
		wrapped.validateProgram(program)
	}

	override fun vertexAttrib1f(index: Int, x: Float) {
		wrapped.vertexAttrib1f(index, x)
	}

	override fun vertexAttrib1fv(index: Int, values: FloatArray) {
		wrapped.vertexAttrib1fv(index, values)
	}

	override fun vertexAttrib2f(index: Int, x: Float, y: Float) {
		wrapped.vertexAttrib2f(index, x, y)
	}

	override fun vertexAttrib2fv(index: Int, values: FloatArray) {
		wrapped.vertexAttrib2fv(index, values)
	}

	override fun vertexAttrib3f(index: Int, x: Float, y: Float, z: Float) {
		wrapped.vertexAttrib3f(index, x, y, z)
	}

	override fun vertexAttrib3fv(index: Int, values: FloatArray) {
		wrapped.vertexAttrib3fv(index, values)
	}

	override fun vertexAttrib4f(index: Int, x: Float, y: Float, z: Float, w: Float) {
		wrapped.vertexAttrib4f(index, x, y, z, w)
	}

	override fun vertexAttrib4fv(index: Int, values: FloatArray) {
		wrapped.vertexAttrib4fv(index, values)
	}

	override fun vertexAttribPointer(index: Int, size: Int, type: Int, normalized: Boolean, stride: Int, offset: Int) {
		wrapped.vertexAttribPointer(index, size, type, normalized, stride, offset)
	}

	override fun viewport(x: Int, y: Int, width: Int, height: Int) {
		wrapped.viewport(x, y, width, height)
	}

	override fun getUniformb(program: GlProgramRef, location: GlUniformLocationRef): Boolean {
		val ret = wrapped.getUniformb(program, location)
		return ret
	}

	override fun getUniformi(program: GlProgramRef, location: GlUniformLocationRef): Int {
		val ret = wrapped.getUniformi(program, location)
		return ret
	}

	override fun getUniformiv(program: GlProgramRef, location: GlUniformLocationRef, out: IntArray): IntArray {
		val ret = wrapped.getUniformiv(program, location, out)
		return ret
	}

	override fun getUniformf(program: GlProgramRef, location: GlUniformLocationRef): Float {
		val ret = wrapped.getUniformf(program, location)
		return ret
	}

	override fun getUniformfv(program: GlProgramRef, location: GlUniformLocationRef, out: FloatArray): FloatArray {
		val ret = wrapped.getUniformfv(program, location, out)
		return ret
	}

	override fun getVertexAttribi(index: Int, pName: Int): Int {
		val ret = wrapped.getVertexAttribi(index, pName)
		return ret
	}

	override fun getVertexAttribb(index: Int, pName: Int): Boolean {
		val ret = wrapped.getVertexAttribb(index, pName)
		return ret
	}

	override fun getTexParameter(target: Int, pName: Int): Int {
		val ret = wrapped.getTexParameter(target, pName)
		return ret
	}

	override fun getShaderParameterb(shader: GlShaderRef, pName: Int): Boolean {
		val ret = wrapped.getShaderParameterb(shader, pName)
		return ret
	}

	override fun getShaderParameteri(shader: GlShaderRef, pName: Int): Int {
		val ret = wrapped.getShaderParameteri(shader, pName)
		return ret
	}

	override fun getRenderbufferParameter(target: Int, pName: Int): Int {
		val ret = wrapped.getRenderbufferParameter(target, pName)
		return ret
	}

	override fun getParameterb(pName: Int): Boolean {
		return parametersB[pName] ?: wrapped.getParameterb(pName)
	}

	override fun getParameterbv(pName: Int, out: BooleanArray): BooleanArray {
		if (parametersBv.containsKey(pName))
			parametersBv[pName]!!.copyInto(out) else wrapped.getParameterbv(pName, out)
		return out
	}

	override fun getParameteri(pName: Int): Int {
		return parametersI[pName] ?: wrapped.getParameteri(pName)
	}

	override fun getParameteriv(pName: Int, out: IntArray): IntArray {
		if (parametersIv.containsKey(pName))
			parametersIv[pName]!!.copyInto(out) else wrapped.getParameteriv(pName, out)
		return out
	}

	override fun getParameterf(pName: Int): Float {
		return parametersF[pName] ?: wrapped.getParameterf(pName)
	}

	override fun getParameterfv(pName: Int, out: FloatArray): FloatArray {
		if (parametersFv.containsKey(pName))
			parametersFv[pName]!!.copyInto(out) else wrapped.getParameterfv(pName, out)
		return out
	}

	override fun getProgramParameterb(program: GlProgramRef, pName: Int): Boolean {
		val ret = wrapped.getProgramParameterb(program, pName)
		return ret
	}

	override fun getProgramParameteri(program: GlProgramRef, pName: Int): Int {
		val ret = wrapped.getProgramParameteri(program, pName)
		return ret
	}

	override fun getBufferParameter(target: Int, pName: Int): Int {
		val ret = wrapped.getBufferParameter(target, pName)
		return ret
	}

	override fun getFramebufferAttachmentParameteri(target: Int, attachment: Int, pName: Int): Int {
		val ret = wrapped.getFramebufferAttachmentParameteri(target, attachment, pName)
		return ret
	}

	override fun clearColor(color: ColorRo) {
		wrapped.clearColor(color)
	}

	override fun getSupportedExtensions(): List<String> {
		val ret = wrapped.getSupportedExtensions()
		return ret
	}
}
