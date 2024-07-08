package frc.lib.signals

import edu.wpi.first.util.sendable.Sendable
import edu.wpi.first.util.sendable.SendableBuilder
import edu.wpi.first.wpilibj2.command.Subsystem
import edu.wpi.first.wpilibj2.command.SubsystemBase
import kotlin.enums.EnumEntries

/**
 * This maintains a current signal of type T (an enum), and allows for listeners, followers, and conditionals to be attached to it.
 * When the signal is set, all attached listeners, followers of that signal, and conditionals that return true are triggered.
 *
 * @param T The type of the enum that this signals.Signal class will use.
 * @param entries The entries of the enum type T.
 */
class Gateway<T: Enum<T>>(entries: EnumEntries<T>, startup : T) : Sendable {
    private var currentSignal = startup
    private val listeners = mutableListOf<(T) -> Unit>()
    private val followers = mutableMapOf<T, MutableList<(T) -> Unit>>()
    private val conditionals = mutableListOf<Pair<(T) -> Boolean, (T) -> Unit>>()

    private inner class SignalSubsystem : SubsystemBase() {init {
        name = "Global Signal"
    }
    }

    // The current signal. When set, all listeners, followers of that signal, and conditionals are checked and possibly triggered.
    var signal: T
        get() = currentSignal!!

        @Synchronized // Make sure that the signal is set atomically to allow advanced use cases (coroutines)
        set(value) {
            currentSignal = value
            listeners.forEach { it(value) }
            followers[value]?.forEach { it(value) }
            conditionals.forEach { (condition, action) ->
                if (condition(value)) action(value)
            }
        }


    /**
     * Adds a listener to the gateway.
     * The listener is a function that takes a signal of type T and returns Unit.
     * It is triggered whenever the gateway is set.
     *
     * @param listener The listener to add.
     */
    fun addListener(listener: (T) -> Unit) {
        listeners.add(listener)
    }

    /**
     * Adds a follower to a specific gateway.
     * The follower is a function that takes a signal of type T and returns Unit.
     * It is triggered whenever the gateway it is following is set.
     *
     * @param signal The signal to follow.
     * @param listener The follower to add.
     */
    fun addFollower(signal: T, listener: (T) -> Unit) {
        followers.getOrPut(signal) { mutableListOf() }.add(listener)
    }

    /**
     * Adds a conditional to the gateway.
     * The conditional is a pair of a condition and an action.
     * The condition is a function that takes a signal of type T and returns a Boolean.
     * The action is a function that takes a signal of type T and returns Unit.
     * If the condition returns true when the signal is set, the action is triggered.
     *
     * @param condition The condition of the conditional.
     * @param action The action of the conditional.
     */
    fun addConditional(condition: (T) -> Boolean, action: (T) -> Unit) {
        conditionals.add(Pair(condition, action))
    }

    override fun initSendable(builder: SendableBuilder?) {
        builder?.setSmartDashboardType("Gateway")
        builder?.addStringArrayProperty("Listeners", { listeners.map { it.toString() }.toTypedArray() }, null)
        builder?.addStringArrayProperty("Followers", { followers.map { it.toString() }.toTypedArray() }, null)
        builder?.addStringArrayProperty("Conditionals", { conditionals.map { it.toString() }.toTypedArray() }, null)
    }
}