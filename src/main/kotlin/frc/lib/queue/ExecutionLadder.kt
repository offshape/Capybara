package frc.lib.queue

import edu.wpi.first.wpilibj2.command.SubsystemBase

open class ExecutionLadder<T>(private val queue: Array<() -> T?> = arrayOf()) : SubsystemBase() {
    private var onChange: ((T) -> Unit)? = null
    private var lastProduct: T? = null

    fun setOnChange(action: (T) -> Unit) {
        onChange = action
    }

    override fun periodic() {
        for (i in queue) {
            val product = i.invoke()
            if (product != null) {
                if (product != lastProduct) {
                    onChange?.invoke(product)
                    lastProduct = product
                }
                break
            }
        }
    }
}