package frc.robot.auto

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import frc.lib.queue.CommandDispatcher
import monologue.Annotations.Log
import monologue.Logged

class AutoController : Logged {
    @Log.NT(key = "Selected Auto") val autoSelection = SendableChooser<Autos>()
    @Log(key = "Retained Auto") private var selectedAuto = Autos.DO_NOTHING

    private val dispatcher = CommandDispatcher("Auto Dispatcher") {
        null
    }

    init {
        for (auto in Autos.entries) {
            autoSelection.addOption(auto.fancyName, auto)
        }

        autoSelection.setDefaultOption("Do Nothing (DEFAULT)", Autos.DO_NOTHING)

        autoSelection.onChange {
            // Update LEDs and other indicators
            selectedAuto = it
        }
    }
}