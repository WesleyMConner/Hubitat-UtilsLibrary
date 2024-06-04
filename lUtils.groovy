
// ---------------------------------------------------------------------------------
// H ( U B I T A T )   U ( S E R )   I ( N T E R F A C E )
//
//  Copyright (C) 2023-Present Wesley M. Conner
//
// Licensed under the Apache License, Version 2.0 (aka Apache-2.0, the
// "License"); you may not use this file except in compliance with the
// License. You may obtain a copy of the License at
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ---------------------------------------------------------------------------------
import com.hubitat.app.DeviceWrapper as DevW
import com.hubitat.app.InstalledAppWrapper as InstAppW
import com.hubitat.hub.domain.Event as Event

library(
 name: 'lUtils',
 namespace: 'wesmc',
 author: 'WesleyMConner',
 description: 'Methods leveraged generally across other libraries and Apps',
 category: 'general purpose',
 documentationLink: 'TBD',
 importUrl: 'TBD'
)

////
//// Convenience Methods
////

Integer safeParseInt(String s) {
  Integer result = null
  switch (s) {
    case null:
    case '':
      break
    default:
      result = s.toInteger()
  }
  return result
}

ArrayList cleanStrings(ArrayList list) {
  // Prunes nulls, empty strings and dups
  return list.findAll { s -> s ?: null }.unique()
}

ArrayList modeNames() {
  return getLocation().getModes()*.name
}

String eventDetails(Event e) {
  String rows = """
    <tr>
      <th align='right'>descriptionText</th>
      <td>${e.descriptionText}</td>
    </tr>
    <tr>
      <th align='right'>displayName</th>
      <td>${e.displayName}</td>
    </tr>
    <tr>
      <th align='right'>deviceId</th>
      <td>${e.deviceId} (hubitat)</td>
    </tr>
    <tr>
      <th align='right'>name</th>
      <td>${e.name}</td>
    </tr>
    <tr>
      <th align='right'>value</th>
      <td>${e.value}</td>
    </tr>
    <tr>
      <th align='right'>isStateChange</th>
      <td>${e.isStateChange}</td>
    </tr>
    """
  return "<table>${rows}</table>"
}

////
//// Convenience HTML-like Methods
////

String blackBar() {
  return '<hr style="border: 5px solid black;"/>'
}

String greenBar() {
  return '<hr style="border: 5px solid green;"/>'
}

String redBar() {
  return '<hr style="border: 5px solid red;"/>'
}

String h1(String s) {
  // font-weight: bold;
  return """<span style='font-size: 2em; font-family: Roboto;'>${s}</span>"""
}

String h2(String s) {
  return """<span style='font-size: 1.3em; font-family: Roboto;'>${s}</span>"""
}

String h3(String s) {
  return """<span style='font-size: 1.1em; font-family: Roboto;'>${s}</span>"""
}

String bullet1(String s) {
  return "&#x2022;&nbsp;&nbsp;${s}"
}

String bullet2(String s) {
  return "&nbsp;&nbsp;&nbsp;&#x2022;&nbsp;&nbsp;${s}"
}

String b(def val) {
  String retVal = '<b>null</b>'
  if (val == '0') {
    retVal = '<b>0</b>'
  } else if (val == 0) {
    retVal = '<b>0</b>'
  } else if (val) {
    retVal = "<b>${val}</b>"
  }
  return retVal
}

String i(def val) {
  return val ? "<i>${val}</i>" : '<i>null</i>'
}

String bi(def val) {
  return i(b(val))
}

////
//// Threshold-Based Logging
////

void setLogLevel(String logThreshold) {
  Map logThresholdToLogLevel = [ TRACE: 5, DEBUG: 4, INFO: 3, WARN: 2, ERROR: 1 ]
  atomicState['logLevel'] = logThresholdToLogLevel."${logThreshold}" ?: 'INFO'
}

String appInfo(InstAppW app) {
  return "${app?.label ?: 'MISSING_LABEL'} (${app?.id ?: 'MISSING_ID'})"
}

void logTrace(String fnName, String s) {
  // Fails closed if logLevel is missing.
  if ((state.logLevel ?: 5) > 4) {
    log.trace("${appInfo(app)} <b>${fnName}</b> → ${s}")
  }
}

void logDebug(String fnName, String s) {
  // Fails closed if logLevel is missing.
  if ((state.logLevel ?: 5) > 3) {
    log.debug("${appInfo(app)} <b>${fnName}</b> → ${s}")
  }
}

void logInfo(String fnName, String s) {
  // Fails closed if logLevel is missing.
  if ((state.logLevel ?: 5) > 2) {
    log.info("${appInfo(app)} <b>${fnName}</b> → ${s}")
  }
}

void logWarn(String fnName, String s) {
  // Fails closed if logLevel is missing.
  if ((state.logLevel ?: 5) > 1) {
    log.warn("${appInfo(app)} <b>${fnName}</b> → ${s}")
  }
}

void logError(String fnName, String s) {
  // No conditional test to ensure all errors appear.
  log.error(
    "${appInfo(app)} <b>${fnName}</b> → ${s}"
  )
}

////
//// ArrayList (vs String) Threshold-Based Logging
////

void logError(String fnName, ArrayList ls, String delim = '<br/>&nbsp&nbsp') {
  logError(fnName, ls.join(delim))
}

void logWarn(String fnName, ArrayList ls, String delim = '<br/>&nbsp&nbsp') {
  logWarn(fnName, ls.join(delim))
}

void logInfo(String fnName, ArrayList ls, String delim = '<br/>&nbsp&nbsp') {
  logInfo(fnName, ls.join(delim))
}

void logDebug(String fnName, ArrayList ls, String delim = '<br/>&nbsp&nbsp') {
  logDebug(fnName, ls.join(delim))
}

void logTrace(String fnName, ArrayList ls, String delim = '<br/>&nbsp&nbsp') {
  logTrace(fnName, ls.join(delim))
}
