package frc.lib.led

import edu.wpi.first.wpilibj.AddressableLEDBuffer

interface Animation {
    fun onStart()
    fun onUpdate(buffer: AddressableLEDBuffer, startingIndex : Int, endingIndex : Int) {}
}