// ---------------------------------------------------------------------------------
// Demo-Utils - Exercise functions from the lUtils library.
//
// Copyright (C) 2023-Present Wesley M. Conner
//
// LICENSE
//   Licensed under the Apache License, Version 2.0 (aka Apache-2.0, the
//   "License"), see http://www.apache.org/licenses/LICENSE-2.0. You may
//   not use this file except in compliance with the License. Unless
//   required by applicable law or agreed to in writing, software
//   distributed under the License is distributed on an "AS IS" BASIS,
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
//   implied.
// ---------------------------------------------------------------------------------
import com.hubitat.hub.domain.Event as Event
import com.hubitat.app.DeviceWrapper as DevW

// The Groovy Linter generates NglParseError on Hubitat #include !!!
#include WesMC.lUtils

definition (
  name: 'Demo-Utils',
  namespace: 'WesMC',
  author: 'Wesley M. Conner',
  description: 'Exercise lUtils methods',
  singleInstance: true,
  iconUrl: '',
  iconX2Url: ''
)

preferences {
  page(
    name: 'Demo-Utils',
    title: h1("Demo-Utils (${app.id})"),
    install: true,
    uninstall: true
  ) {
    //---------------------------------------------------------------------------------
    // Per https://community.hubitat.com/t/issues-with-deselection-of-settings/36054/42
    //-> app.removeSetting('..')
    //-> atomicState.remove('..')
    //---------------------------------------------------------------------------------
    app.updateLabel("Demo-Utils (${app.id})")
    section {
      ArrayList ex1 = [null, 'a', 'a', 'b', 'c', null, 'a']
      ArrayList ex2 = [null, 'a', 'd', 'b', 'c', null, 'a']
      ArrayList ex3 = [null, 'a', 'c', null, 'd', 'b ', 'a']
      paragraph """
        <b>safeParseInt() Examples</b>
          safeParseInt(): ${safeParseInt()}
          safeParseInt(''): ${safeParseInt('')}
          safeParseInt('0'): ${safeParseInt('0')}
          safeParseInt('-51'): ${safeParseInt('-51')}
          safeParseInt('22'): ${safeParseInt('22')}

        <b>cleanStrings() Examples</b>
          cleanStrings(${ex1}) -> ${cleanStrings(ex1)}
          cleanStrings(${ex2}) -> ${cleanStrings(ex2)}
          cleanStrings(${ex3}) -> ${cleanStrings(ex3)}

        <b>modeNames() Example</b>
          ${modeNames()}

        <b>blackBar(), greenBar() and redBar() Examples</b>
          ${blackBar()}
          ${greenBar()}
          ${redBar()}

        ${h1('h1(...) Header Level 1 Example')}

        ${h2('h2(...) Header Level 2 Example')}

        ${h3('h3(...) Header Level 3 Example')}

        ${bullet1('bullet1(...) Example')}

        ${bullet2('bullet2(...) Example')}

        ${b('bold text example')}
        ${i('italic text example')}
        ${bi('bold italic text example')}

      ${h2('To complete the Demo ...')}
        Click <b>"Done"</b> and <b>Review</b> the Hubitat Logs for:
          (1) Log thresholding examples
              setLogLevel(), logTrace(), logDebug(), logInfo(), logWarn(), logError()
          (2) Examples of switchState(DevW d)
          (3) Examples of eventDetails(Event e) <i>(in a VSW subscription callback)</i>
      """
    }
  }
}

void installed() {
  unsubscribe()
  initialize()
}

void uninstalled() {
}

void updated() {
  unsubscribe()
  initialize()
}

void initialize() {
  // Exercise Log Levels
  ['TRACE', 'DEBUG', 'INFO', 'WARN', 'ERROR'].each { level ->
    setLogLevel('INFO')
    logInfo(
      'initialize',
      "With log level ${b(level)}, calling logTrace()..logError()."
    )
    setLogLevel(level)
    logTrace('initialize', "Testing 1, 2, 3 .. logTrace() example at level=${b(level)}")
    logDebug('initialize', "Testing 1, 2, 3 .. logDebug() example at level=${b(level)}")
    logInfo('initialize', "Testing 1, 2, 3 .. logInfo() example at level=${b(level)}")
    logWarn('initialize', "Testing 1, 2, 3 .. logWarn() example at level=${b(level)}")
    logError('initialize', "Testing 1, 2, 3 .. logError() example at level=${b(level)}")
  }
  // Exercise switchState(d)
  setLogLevel('INFO')
  String demoDNI = 'demo-utils-vsw'
  DevW d = getChildDevice(demoDNI) ?: addChildDevice('hubitat', 'Virtual Switch', demoDNI)
  subscribe(d, vswEventHandler, ['filterEvents': true])
  logInfo('#245', "Child Device: ${demoDNI} has initial state: ${switchState(d)}")
  d.on()
  pauseExecution(50)  // 50ms for device to respond to on()
  logInfo('#248', "After ${demoDNI}.on(), has state: ${switchState(d)}")
  d.off()
  pauseExecution(50)  // 50ms for device to respond to off()
  logInfo('#251', "After ${demoDNI}.off(), has state: ${switchState(d)}")
  deleteChildDevice(demoDNI)
}

void vswEventHandler(Event e) {
  logInfo(
    'vswEventHandler',
    "eventDetails(Event e): ${eventDetails(e)}"
  )
}
