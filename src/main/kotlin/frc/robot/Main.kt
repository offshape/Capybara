// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

@file:JvmName("Main") // For the VS Code Debugger to properly attach to the JVM

package frc.robot

import edu.wpi.first.wpilibj.RobotBase

object Main {
    @JvmStatic
    fun main(args: Array<String>) = RobotBase.startRobot { Robot }
}
