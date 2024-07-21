// ---------------------------------------------------------------------------------
// Demo-Utils - Demo functions from the lUtils library.
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
// The Groovy Linter generates NglParseError on Hubitat #include !!!
#include Wmc.WmcUtilsLib_1_0_0
import com.hubitat.app.ChildDeviceWrapper as ChildDevW
import com.hubitat.app.DeviceWrapper as DevW
import com.hubitat.app.InstalledAppWrapper as InstAppW
import com.hubitat.hub.domain.Event as Event
import groovy.json.JsonOutput as JsonOutput
import groovy.json.JsonSlurper as JsonSlurper
import groovy.transform.Field
import java.lang.Math as Math
import java.lang.Object as Object
import java.util.concurrent.ConcurrentHashMap

definition (
  name: 'WmcUtilsDemo_1_0_0',
  namespace: 'Wmc',
  author: 'Wesley M. Conner',
  description: 'Demo lUtils methods',
  singleInstance: true,
  iconUrl: '',
  iconX2Url: ''
)

preferences {
  page(
    name: 'WmcUtilsDemo_1.0.0',
    title: h1("WmcUtilsDemo_1.0.0 (${app.id})"),
    install: true,
    uninstall: true
  ) {
    //---------------------------------------------------------------------------------
    // Per https://community.hubitat.com/t/issues-with-deselection-of-settings/36054/42
    //-> app.removeSetting('..')
    //-> atomicState.remove('..')
    //---------------------------------------------------------------------------------
    section {

String s = sArg ?: app?.getLabel() ?: app?.getName()?.toString() ?: device?.getDeviceNetworkId()
Long i = iArg ?: app?.id ?: (device?.id as Long)
String key = "${s}&#x25FC;${i}"
String ss = (s && s.length() > 15 ) ? "…${s.substring(s.length() - 15, s.length())}" : s
paragraph([
  "sArg: ${sArg}",
  "app?.getLabel(): ${app?.getLabel()}",
  "app?.getName()?.toString(): ${app?.getName()?.toString()}",
  "device?.getDeviceNetworkId(): ${device?.getDeviceNetworkId()}",
  "iArg: ${iArg}",
  "app?.id: ${app?.id}",
  "device?.id as Long: ${device?.id as Long}",
  "s: ${s}",
  "i: ${i}",
  "key: ${key}",
  "ss: ${ss}"
].join('<br/>'))
      paragraph([
        h1('Header Level 1'),
        h2('Header Level 2'),
        h3('Header Level 3')
      ].join('<br/>'))
      demoBullets()
      demoTextEmphasis()
      demoMapToTable()
      demoParseInt()
      demoCleanStrings()
      demoModeNames()
      demoColorBars()
      demoAlert()
      demoTdBordered()
      demoColorTable()
      exerciseHuedCache()
      testsRunWhenDoneIsClicked()
    }
  }
}

void demoBullets() {
  paragraph([
    h1('Demo Bullets'),
    bullet1('bullet1()'),
    bullet2('bullet2()'),
    bullet3('bullet3()'),
    square1('square1()'),
    square2('square2()'),
    square3('square3()')
  ].join('<br/>'))
}

void demoTextEmphasis() {
  ArrayList x = ['one', 'two', 'three', 'four']
  Map y = [a: 'apple', b: 'banana', c: 'cantelope']
  paragraph([
    h1('Demo Text Emphasis'),
    "b(..): ${b('This is bold')}",
    "i(..): ${i('This is italic')}",
    "bi(..): ${bi('This is bold & italic')}",
    "List x: ${x}",
    "bList(x): ${bList(x)}",
    "Map y: ${y}",
    "bMap(y): ${bMap(y)}"
  ].join('<br/>'))
}

void demoMapToTable() {
  Map y = [a: 'apple', b: 'banana', c: 'cantelope']
  paragraph([
    h1('Demo mapToTable()'),
    b('With Borders - mapToTable(y, true)'),
    mapToTable(y, true),
    b('Without Borders - mapToTable(y, false)'),
    mapToTable(y, false)
  ].join('<br/>'))
}

void demoParseInt() {
  paragraph([
    h1('Demo safeParseInt()'),
    "safeParseInt(): ${safeParseInt()}",
    "safeParseInt(''): ${safeParseInt('')}",
    "safeParseInt('0'): ${safeParseInt('0')}",
    "safeParseInt('-51'): ${safeParseInt('-51')}",
    "safeParseInt('22'): ${safeParseInt('22')}"
  ].join('<br/>'))
}

void demoCleanStrings() {
  ArrayList ex1 = [null, 'a', 'a', 'b', 'c', null, 'a']
  ArrayList ex2 = [null, 'a', 'd', 'b', 'c', null, 'a']
  ArrayList ex3 = [null, 'a', 'c', null, 'd', 'b ', 'a']
  paragraph([
    h1('Demo cleanStrings()'),
    "cleanStrings(${ex1}) -> ${cleanStrings(ex1)}",
    "cleanStrings(${ex2}) -> ${cleanStrings(ex2)}",
    "cleanStrings(${ex3}) -> ${cleanStrings(ex3)}"
  ].join('<br/>'))
}

void demoModeNames() {
  paragraph([
    h1('Demo modeNames()'),
    "modeNames() -> ${modeNames()}"
  ].join('<br/>'))
}

void demoColorBars() {
  paragraph([
    h1('Demo Color Bars'),
    h2('blackBar()'),
    blackBar(),
    h2('greenBar()'),
    greenBar(),
    h2('redBar()'),
    redBar(),
  ].join('<br/>'))
}

void demoAlert() {
  paragraph([
    h1('Demo Alert'),
    alert('This is a sample alert!')
  ].join('<br/>'))
}

void demoTdBordered() {
  paragraph([
    h1('Demo Individually-Bordered Table Cell'),
    '<table><tr><td>one</td><td>two</td><td>three</td></tr>',
    "<tr><td>four</td>${tdBordered('five')}<td>six</td></tr>",
    "<tr><td>seven</td><td>eight</td>${tdBordered('nine')}</tr>",
    '</table>'
  ].join())
}

void demoColorTable() {
  paragraph([
    h1('Color Hued() Colors Table'),
    getFgBgTable()
  ].join('<br/>'))
}

void exerciseHuedCache() {
  paragraph([
    h1('Exercise Hued Cache'),
    *addTestDataToCache(),
    *removeTestDataFromCache()
  ].join('<br/>'))
}

ArrayList addTestDataToCache() {
  return [
    h2('CREATING SAMPLE ENTRIES IN HUED_CACHE:'),
    "hued('Apple', 15) -> ${hued('Apple', 15)}",
    "hued('Banana', 1015) -> ${hued('Banana', 1015)}",
    "hued('Carrot', 2015) -> ${hued('Carrot', 2015)}",
    "hued('Donut', 3015) -> ${hued('Donut', 3015)}",
    "hued('Egg', 4015) -> ${hued('Egg', 4015)}",
    "hued('FrenchFries', 5015) -> ${hued('FrenchFries', 5015)}",
    "hued('Guava', 6015) -> ${hued('Guava', 6015)}",
    "hued('HotDog', 7015) -> ${hued('HotDog', 7015)}",
    *getHuedCacheContents()
  ]
}

ArrayList removeTestDataFromCache() {
  // Iterate an in-memory snapshot of the ConcurrentHashMap to
  // direct ConcurrentHashMap key removals.
  ArrayList results = [h2('REMOVING SAMPLE ENTRIES FROM HUED_CACHE')]
  HUED_CACHE.findAll{ k1, v1 -> (k1) } each { k, v ->
    if (['Apple_15', 'Banana_1015', 'Carrot_2015', 'Donut_3015',
      'Egg_4015', 'FrenchFries_5015', 'Guava_6015',
      'HotDog_7015'].contains(k)) {
      results << "Removing key: ${k}"
      HUED_CACHE.remove(k)
    }
  }
  return [*results, *getHuedCacheContents()]
}

void testsRunWhenDoneIsClicked() {
  paragraph([
    h1('Tests that Run When "Done" is clicked ...'),
    bullet2('The log functions in this library:'),
    bullet3('Can reduce log data sent to Hubitat.'),
    bullet3('The method "setLogLevel()" adjusts what is sent.'),
    bullet3("${b('setLogLevel("TRACE")')} sends everything."),
    bullet3("${b('setLogLevel("DEBUG")')} suppresses TRACE."),
    bullet3("${b('setLogLevel("INFO")')} suppresses TRACE & DEBUG."),
    bullet3("${b('setLogLevel("WARN")')} suppresses TRACE, DEBUG & INFO."),
    bullet3("${b('setLogLevel("ERROR")')} suppresses TRACE, DEBUG, INFO & WARN."),
    bullet3('ERROR is never suppressed.'),
    h2("${b('demoLogLevels()')} is called MULTIPLE TIMES to issue:"),
    bullet2("A sample ${b('logTrace()')} call"),
    bullet2("A sample ${b('logDebug()')} call"),
    bullet2("A sample ${b('logInfo()')} call"),
    bullet2("A sample ${b('logWarn()')} call"),
    bullet2("A Sample ${b('logError()')} call"),
    h2("Each time ${b('demoLogLevels()')} is called, the log level is reduced."),
    bullet2("In loop #1 all five samples are sent."),
    bullet2("In loop #2 ${b('logTrace()')} is suppressed."),
    bullet2("In loop #3 ${b('logDebug()')} is also suppressed"),
    bullet2("In loop #4 ${b('logInfo()')} is also suppressed"),
    bullet2("In loop #5 ${b('logWarn()')} is also suppressed")
  ].join('<br/>'))
}

void installed() {
  // Called when a bare device is first constructed.
  demoLogFiltering()
}

void updated() {
  // Called when a human uses the Hubitat GUI's Device drilldown page to edit
  // preferences (aka settings) AND presses 'Save Preferences'.
  demoLogFiltering()
}

void demoLogFiltering() {
  ['TRACE', 'DEBUG', 'INFO', 'WARN', 'ERROR'].each { level ->
    log.info("Calling demoLogLevels() with setLogLevel(${b(level)}):")
    setLogLevel(level)
    demoLogLevels()
  }
}

void demoLogLevels() {
  logTrace('demoLogLevels', 'logTrace(..) test only')
  logDebug('demoLogLevels', 'logDebug(..) test only')
  logInfo('demoLogLevels', 'logInfo(..) test only')
  logWarn('demoLogLevels', 'logWarn(..) test only')
  logError('demoLogLevels', 'logError(..) test only')
}

// UNUSED / UNSUPPORTED

//-> void uninstalled() {
//->   // Called on device tear down.
//-> }

//-> void initialize() {
//->   // Called on hub startup (per capability "Initialize").
//-> }
