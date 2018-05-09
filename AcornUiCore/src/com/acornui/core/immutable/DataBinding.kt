package com.acornui.core.immutable

import com.acornui.core.Disposable
import com.acornui.core.di.Owned
import com.acornui.core.di.own
import com.acornui.signal.Signal
import com.acornui.signal.Signal2

class DataBinding<T>(initialValue: T) : Disposable {

	private val _changed = Signal2<T, T>()

	val changed: Signal<(T, T) -> Unit>
		get() = _changed

	private val _wrapped = HashMap<(T) -> Unit, DataChangeHandler<T>>()

	private var _value: T = initialValue

	var value: T
		get() = _value
		set(value) {
			if (_changed.isDispatching) return
			val old = _value
			if (old == value) return
			_value = value
			_changed.dispatch(old, value)
		}

	/**
	 * When the data has changed, the callback will be invoked.
	 * If the data has been set, the callback will be invoked immediately.
	 *
	 * If you need to know the old value as well, use the [changed] signal.
	 */
	fun bind(callback: (T) -> Unit) {
		val handler: DataChangeHandler<T> = { _, new: T ->
			callback(new)
		}
		_wrapped[callback] = handler
		_changed.add(handler)
		callback(_value)
	}

	/**
	 * Removes the callback handler that was added via [bind].
	 */
	fun remove(callback: (T) -> Unit) {
		val handler = _wrapped[callback]
		if (handler != null) {
			_wrapped.remove(callback)
			_changed.remove(handler)
		}
	}

	override fun dispose() {
		_changed.dispose()
		_wrapped.clear()
	}
}

/**
 * Mirrors changes from two data binding objects. If one changes, the other will be set.
 */
fun <T> DataBinding<T>.mirror(other: DataBinding<T>) {
	bind {
		other.value = it
	}
	other.bind {
		value = it
	}
}

/**
 * A handler for when a data binding has changed.
 * oldValue, newValue
 */
typealias DataChangeHandler<T> = (T, T) -> Unit

fun <T> Owned.dataBinding(initialValue: T): DataBinding<T> {
	return own(DataBinding(initialValue))
}