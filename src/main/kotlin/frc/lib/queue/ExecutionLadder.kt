package frc.lib.queue

import edu.wpi.first.wpilibj2.command.Subsystem

open class ExecutionLadder<T>(private val queue: List<() -> T?> = mutableListOf()) : Subsystem {
    private var onChange: ((T) -> Unit)? = null

    fun setOnChange(action: (T) -> Unit) {
        onChange = action
    }

    override fun periodic() {
        queue.iterator().let { iterator ->
            while (iterator.hasNext()) {
                val product = iterator.next()()
                if (product != null) {
                    onChange?.invoke(product)
                    break
                }
            }
        }
    }
}