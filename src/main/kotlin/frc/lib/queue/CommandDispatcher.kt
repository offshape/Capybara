package frc.lib.queue

import edu.wpi.first.wpilibj2.command.Command

class CommandDispatcher(commandName : String, private val controller : () -> Command?) : Command() {
    private var currentCommand : Command? = null
    private var isFinished = false

    init {
        name = commandName
    }

    override fun execute() {
        if (!isFinished) doTickProcess()
    }

    private fun doTickProcess() {
        if (currentCommand == null || currentCommand?.isFinished == true) {
            val result = controller.invoke()

            if (result == null) isFinished = true
            else {
                currentCommand = result
                currentCommand!!.schedule()
            }
        }
    }
}