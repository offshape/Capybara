![Capybara Front](/branding/Header.png "Capybara")


<div align="center">

[![Java CI with Gradle](https://github.com/offshape/Capybara/actions/workflows/gradle.yml/badge.svg)](https://github.com/offshape/Capybara/actions/workflows/gradle.yml)
![GitHub top language](https://img.shields.io/github/languages/top/offshape/Capybara)
[![License](https://img.shields.io/badge/License-MIT-blue)](#license)
[![Use this template](https://img.shields.io/badge/Generate-Use_this_template-2ea44f)](https://github.com/offshape/Capybara/generate)

</div>

<p align="center">
    A Kotlin powered FIRST Robotics Competition code template. Made for CTR Electronics swerve based robots, with Monologue logging, and a focus on simplicity and ease of use.
</p>

# Library Features
Note: Some of these features and implementations are going to be baked into WPILib's 2025 Release, Capybara's Library will have adapters for Monologue and Neopixel Animations for the 2025 Release.

## Signal/State Based Programming
Based off Coralis's Signal/State Based Programming, Capybara has a simple state machine API for controlling universal state throughout robot code, allowing for complex state based programming with ease.
Signal/State Based Programming is best fit for complex motion and system control, like a 3 axis arm, where the reference angles of an arm needs to be staged for control.

```kotlin
// An example of state based programming for a simple 2018 style elevator,
// With an elevator, wrist and intake

// Our restrictions are that the wrist can only move when high enough,
// or it will hit its bumper, so its in between the INTAKE and IDLE states that we can have issues

enum class RobotState {
    PLACING,
    INTAKE,
    IDLE,
    DISABLED // When the robot is enabled this should go to IDLE, which runs onNewProduct and updates devices
}

val currentState = Gateway(RobotState.entries, RobotState.DISABLED)

// This acts like a subsystem, and makes sure only one state is active at a time
// Internally its the command that pushes updates and updates all Suppliers, not the Reserve
val reserve = Reserve()

// In centimeters
val elevatorState = Supplier(currentState, hashMapOf(
    RobotState.PLACING to { 250 },
    RobotState.INTAKE to { 30 },
    RobotState.IDLE to { if (wristEncoder == 0) 0 else null }
), reserve)

// In degrees where 0 is parallel to the ground
val wristState = Supplier(currentState, hashMapOf(
    RobotState.PLACING to { 45 },
    RobotState.INTAKE to { if (elevatorPosition > 29) -40 else null },
    RobotState.IDLE to { 0 }
), reserve)

// In Volts 
val intakeState = Supplier(currentState, hashMapOf(
    RobotState.PLACING to { 12 },
    RobotState.INTAKE to { -8 },
    RobotState.IDLE to { 0 }
), reserve)

// Controlling Motors
elevatorState.onNewProduct {
    elevatorMotor.setReference(it)
}

// Integration with Command Based Programming (Syntax will change soon)

joystick.a().onTrue(elevatorState.changeState(RobotState.PLACING))

reserve.defaultCommand = elevatorState.changeState(RobotState.IDLE)
```

## Subsystem Templates
For systems that involve priority, like LED indications, state machines can be cloggy and hard to manage. Subsystem Templates are a way to manage these systems with ease.

```kotlin
class Neopixels : ExecutionLadder<Animation>(arrayOf(
    {
        if (DriverStation.isEStopped()) Flash(1) else null
    },
    {
        if (RobotState.isDisabled()) {
            when {
                Interfaces.power.voltage < 12.5 && RobotBase.isReal() -> Pulse(1, 1.0)
                !DriverStation.isDSAttached() -> Pulse(15, 1.0)
                DriverStation.isFMSAttached() -> Zip(Triple(0, 200, 255), 25, 3)
                else -> Pulse(120, 0.5)
            }
        } else null
    },
    {
        if (RobotState.isAutonomous()) Rainbow() else null
    },
    {
        Flash(2, Triple(0, 255, 0))
    }
)) {
    private val fullSection = LightSection(0, 58, Solid())
    private val controller = LightController(0, 59, arrayOf(fullSection), 20)

    init {
        name = "Neopixel Ladder Controller"

        setOnChange { animation ->
            fullSection.animation = animation
            log("currentAnimation", animation.toString())
        }
    }
}
```
In the backend the ExecutionLadder traverses until it gets a value other than null, which means certain values can be prioritized over others easly.

### Animations
TODO