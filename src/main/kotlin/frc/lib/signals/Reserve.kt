package frc.lib.signals

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SubsystemBase

class Reserve<T : Enum<T>> : SubsystemBase() {
    fun setDefaultState(gateway: Gateway<T>, state : T) {
        defaultCommand = object : Command() {
            init { addRequirements(this@Reserve) }

            override fun execute() {
                gateway.signal = state
            }
        }
    }
}