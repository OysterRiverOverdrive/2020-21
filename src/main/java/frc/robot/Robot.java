// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.concurrent.TimeUnit;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.PWMTalonSRX;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.WaitCommand;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.SpeedControllerGroup;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  private final PWMVictorSPX m_left1 = new PWMVictorSPX(0);
  private final PWMVictorSPX m_left2 = new PWMVictorSPX(1);
  SpeedControllerGroup m_left = new SpeedControllerGroup(m_left1, m_left2);
  private final PWMVictorSPX m_right1 = new PWMVictorSPX(2);
  private final PWMVictorSPX m_right2 = new PWMVictorSPX(3);
  SpeedControllerGroup m_right = new SpeedControllerGroup(m_right1, m_right2);
  private final PWMTalonSRX m_noodler = new PWMTalonSRX(4);
  private final PWMTalonSRX m_shootlerLeft = new PWMTalonSRX(5);
  private final PWMTalonSRX m_shootlerRight = new PWMTalonSRX(6);
  private final PWMTalonSRX m_rotoodler = new PWMTalonSRX(7);
  private final PWMTalonSRX m_turret = new PWMTalonSRX(8);
  SpeedControllerGroup m_shootler = new SpeedControllerGroup(m_shootlerLeft, m_shootlerRight);
  private final DifferentialDrive m_robotDrive = new DifferentialDrive(m_left, m_right);
  private final Joystick m_stick = new Joystick(0);
  private final Joystick m_action = new Joystick(1);
  private int m_TurretPos = 0;
  private int m_count = 0;

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for
   * items like diagnostics that you want ran during disabled, autonomous,
   * teleoperated and test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable chooser
   * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
   * remove all of the chooser code and uncomment the getString line to get the
   * auto name from the text box below the Gyro
   *
   * <p>
   * You can add additional auto modes by adding additional comparisons to the
   * switch structure below with additional strings. If using the SendableChooser
   * make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {

    m_turret.set(0);
    m_robotDrive.arcadeDrive(-.80 * m_action.getY(), .6 * m_stick.getX());
    // m_robotDrive.tankDrive(-.80*m_stick.getY(), -.8*m_action.getY());
    m_noodler.setSpeed(0.75*m_action.getThrottle());
    if (m_action.getTrigger() == true) {
      m_shootlerRight.set(-1);
      m_shootlerLeft.set(1);
      try {
        TimeUnit.MILLISECONDS.sleep(50);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

    } else {
      m_shootlerRight.set(0);
      m_shootlerLeft.set(0);
    }

    if (m_action.getRawButton(2) == true) {
      m_rotoodler.set(0.7);
    } else {
      m_rotoodler.set(0);
    }

    if (m_action.getRawButtonPressed(12)) {
      m_TurretPos = 0;
      while (m_count > 0) {
        m_turret.set(-.2);
        m_count = m_count-1;
        try {
          TimeUnit.MILLISECONDS.sleep(50);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
        m_turret.set(0);
    }

    if (m_action.getRawButtonPressed(11)) {
      m_TurretPos = 1;

      if (m_count < 30) {
        m_turret.set(0.2);
        while (m_count < 30) {
          m_count = m_count+1;
          try {
            TimeUnit.MILLISECONDS.sleep(50);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
        m_turret.set(0);
      } else {
        m_turret.set(-0.2);
        while (m_count > 30) {
          m_count = m_count-1;
          try {
            TimeUnit.MILLISECONDS.sleep(50);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
        m_turret.set(0);
      }
    }
    if (m_action.getRawButtonPressed(10)) {
      m_TurretPos = 2;

      if (m_count < 40) {
        m_turret.set(0.2);
        while (m_count < 40) {
          m_count = m_count+1;
          try {
            TimeUnit.MILLISECONDS.sleep(50);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
        m_turret.set(0);
      } else {
        m_turret.set(-0.2);
        while (m_count > 40) {
          m_count = m_count-1;
          try {
            TimeUnit.MILLISECONDS.sleep(50);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
        m_turret.set(0);
      }
    }
    if (m_action.getRawButtonPressed(9)) {
      m_TurretPos = 3;

      if (m_count < 45) {
        m_turret.set(0.2);
        while (m_count < 45) {
          m_count = m_count+1;
          try {
            TimeUnit.MILLISECONDS.sleep(50);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
        m_turret.set(0);
      } else {
        m_turret.set(-0.2);
        while (m_count > 45) {
          m_count = m_count-1;
          try {
            TimeUnit.MILLISECONDS.sleep(50);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
        m_turret.set(0);
      }
    }
    if (m_action.getRawButtonPressed(8)) {
      m_TurretPos = 2;

      if (m_count < 60) {
        m_turret.set(0.2);
        while (m_count < 60) {
          m_count = m_count+1;
          try {
            TimeUnit.MILLISECONDS.sleep(50);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      } else {
        m_turret.set(-0.2);
        while (m_count > 60) {
          m_count = m_count-1;
          try {
            TimeUnit.MILLISECONDS.sleep(50);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
        m_turret.set(0);
      }
    }
    while (m_action.getRawButton(5)==true) {
      m_turret.set(-0.2);
    }

  }
      





    


  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}
