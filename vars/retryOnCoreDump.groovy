#!/usr/bin/groovy

@NonCPS
def call(command) {
  def commandResult = sh(script: command, returnStatus: true)

  if (commandResult == 134) {
    // Only retry if command aborts due to core dump
    commandResult = sh(script: command, returnStatus: true)
  }

  return commandResult
}
