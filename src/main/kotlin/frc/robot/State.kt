package frc.robot

import frc.lib.signals.Gateway
import frc.lib.signals.Supplier

/*
    * Super cool state manager
 */
object State {
    val stateSupplier = Gateway(States.entries, States.DEFAULT)

    enum class States {
        DEFAULT // Normal state, basically when the robot is doing nothing special
    }

    object MechanismData {
        var armPosition : Double? = null // This is just example, normally you would set this to your default configuration, if theres no soul default make things null and have fun
    }

    object SensorData { // Feel free to use getters and setters with Interfaces, or just let subsystems talk to this (I would do that if your sending loads of calls)
        val armZeroSwitch : Boolean
            get() = false
    }
}