package frc.robot.generated

import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.math.geometry.Translation3d
import edu.wpi.first.math.util.Units

// Copyright (c) 2024 FRC 6328
// http://github.com/Mechanical-Advantage
//
// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

object FieldConstants {
    val fieldLength: Double = Units.inchesToMeters(651.223)
    val fieldWidth: Double = Units.inchesToMeters(323.277)
    val wingX: Double = Units.inchesToMeters(229.201)
    val podiumX: Double = Units.inchesToMeters(126.75)
    val startingLineX: Double = Units.inchesToMeters(74.111)

    val ampCenter: Translation2d = Translation2d(Units.inchesToMeters(72.455), fieldWidth)

    /** Staging locations for each note  */
    object StagingLocations {
        val centerlineX: Double = fieldLength / 2.0

        // need to update
        val centerlineFirstY: Double = Units.inchesToMeters(29.638)
        val centerlineSeparationY: Double = Units.inchesToMeters(66.0)
        val spikeX: Double = Units.inchesToMeters(114.0)

        // need
        val spikeFirstY: Double = Units.inchesToMeters(161.638)
        val spikeSeparationY: Double = Units.inchesToMeters(57.0)

        val centerlineTranslations: Array<Translation2d?> = arrayOfNulls(5)
        val spikeTranslations: Array<Translation2d?> = arrayOfNulls(3)

        init {
            for (i in centerlineTranslations.indices) {
                centerlineTranslations[i] =
                    Translation2d(centerlineX, centerlineFirstY + (i * centerlineSeparationY))
            }
        }

        init {
            for (i in spikeTranslations.indices) {
                spikeTranslations[i] = Translation2d(spikeX, spikeFirstY + (i * spikeSeparationY))
            }
        }
    }

    /** Each corner of the speaker *  */
    object Speaker {
        // corners (blue alliance origin)
        val topRightSpeaker: Translation3d = Translation3d(
            Units.inchesToMeters(18.055),
            Units.inchesToMeters(238.815),
            Units.inchesToMeters(83.091)
        )

        val topLeftSpeaker: Translation3d = Translation3d(
            Units.inchesToMeters(18.055),
            Units.inchesToMeters(197.765),
            Units.inchesToMeters(83.091)
        )

        val bottomRightSpeaker: Translation3d =
            Translation3d(0.0, Units.inchesToMeters(238.815), Units.inchesToMeters(78.324))
        val bottomLeftSpeaker: Translation3d =
            Translation3d(0.0, Units.inchesToMeters(197.765), Units.inchesToMeters(78.324))

        /** Center of the speaker opening (blue alliance)  */
        val centerSpeakerOpening: Translation3d = bottomLeftSpeaker.interpolate(topRightSpeaker, 0.5)
    }

    object Subwoofer {
        val ampFaceCorner: Pose2d = Pose2d(
            Units.inchesToMeters(35.775),
            Units.inchesToMeters(239.366),
            Rotation2d.fromDegrees(-120.0)
        )

        val sourceFaceCorner: Pose2d = Pose2d(
            Units.inchesToMeters(35.775),
            Units.inchesToMeters(197.466),
            Rotation2d.fromDegrees(120.0)
        )

        val centerFace: Pose2d = Pose2d(
            Units.inchesToMeters(35.775),
            Units.inchesToMeters(218.416),
            Rotation2d.fromDegrees(180.0)
        )
    }

    object Stage {
        val podiumLeg: Pose2d = Pose2d(Units.inchesToMeters(126.75), Units.inchesToMeters(161.638), Rotation2d())
        val ampLeg: Pose2d = Pose2d(
            Units.inchesToMeters(220.873),
            Units.inchesToMeters(212.425),
            Rotation2d.fromDegrees(-30.0)
        )
        val sourceLeg: Pose2d = Pose2d(
            Units.inchesToMeters(220.873),
            Units.inchesToMeters(110.837),
            Rotation2d.fromDegrees(30.0)
        )

        val centerPodiumAmpChain: Pose2d = Pose2d(
            podiumLeg.translation.interpolate(ampLeg.translation, 0.5),
            Rotation2d.fromDegrees(120.0)
        )
        val centerAmpSourceChain: Pose2d = Pose2d(
            ampLeg.translation.interpolate(sourceLeg.translation, 0.5), Rotation2d()
        )
        val centerSourcePodiumChain: Pose2d = Pose2d(
            sourceLeg.translation.interpolate(podiumLeg.translation, 0.5),
            Rotation2d.fromDegrees(240.0)
        )
        val center: Pose2d = Pose2d(Units.inchesToMeters(192.55), Units.inchesToMeters(161.638), Rotation2d())
        val centerToChainDistance: Double = center.translation.getDistance(centerPodiumAmpChain.translation)
    }

    object Amp {
        val ampTapeTopCorner: Translation2d = Translation2d(Units.inchesToMeters(130.0), Units.inchesToMeters(305.256))
        val ampBottomY: Double = fieldWidth - Units.inchesToMeters(17.75)
    }
}