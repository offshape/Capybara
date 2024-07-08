package frc.lib.signals

import edu.wpi.first.util.sendable.Sendable
import edu.wpi.first.util.sendable.SendableBuilder
import edu.wpi.first.wpilibj2.command.Command

class Supplier<T: Enum<T>, V>(private val gateway: Gateway<T>, private val map: HashMap<T, () -> V?>, private val reserve: Reserve<T>) : Sendable {
    private val listeners = mutableListOf<(V) -> Unit>()
    private val productListeners = mutableListOf<(V) -> Unit>()
    private var lastProduct: V? = null

    init {
        gateway.addConditional({ map.containsKey(it) }) {
            lastProduct = map[it]!!()
            productListeners.forEach { it(lastProduct!!) }
        }
    }

    fun changeState(state: T) = object : Command() {
        init { addRequirements(reserve) }

        override fun execute() {
            gateway.signal = state
        }
    }

    override fun initSendable(p0: SendableBuilder?) {
        TODO("Not yet implemented")
    }
}