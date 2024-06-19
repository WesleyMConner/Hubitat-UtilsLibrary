
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
// IMPORTS THAT CLIENTS SHOULD ADD:
//   - import com.hubitat.app.ChildDeviceWrapper as ChildDevW
//   - import com.hubitat.app.DeviceWrapper as DevW
//   - import com.hubitat.app.InstalledAppWrapper as InstAppW
//   - import com.hubitat.hub.domain.Event as Event
//   - import groovy.json.JsonOutput as JsonOutput
//   - import groovy.json.JsonSlurper as JsonSlurper
//   - import java.lang.Math as Math
//   - import java.lang.Object as Object


library(
 name: 'lUtils',
 namespace: 'wesmc',
 author: 'WesleyMConner',
 description: 'Methods leveraged generally across other libraries and Apps',
 category: 'general purpose',
 documentationLink: 'TBD',
 importUrl: 'TBD'
)

// -------------------
// Convenience Methods
// -------------------

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

String toJson(def thing) {
  def output = new JsonOutput()
  return output.toJson(thing)
}

/* groovylint-disable-next-line MethodReturnTypeRequired */
def fromJson(String json) {
  def result
  if (json) {
    def slurper = new JsonSlurper()
    result = slurper.parseText(json)
  }
  return result
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

// -----------------------------
// Convenience HTML-like Methods
// -----------------------------

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

String bList(ArrayList list) {
  return '[' + list.inject('') { s, e -> s << "${b(e)}, " } + ']'
}

String bMap(Map map) {
  ArrayList terms = []
  map.each { k, v -> terms.add("${i(k)}: ${b(v)}") }
  return '[' + terms.join(', ') + ']'
}

String tdBordered(String content) {
  return """<td style="border: solid 1px black;">${content}</td>"""
}

String bMapTable(Map map) {
  return [ 'Map',
    "<table rules='all'><tr><th>${i('KEYS')}</th><th>${b('VALUES')}</tr>",
    map.inject('') { s, k, v ->
      s << "<tr><td align='right'>${i(k)}</td><td>${b(v)}</td></tr>"
    },
    '</table>'
  ].join()
}

// -----------------------
// Threshold-Based Logging
// -----------------------

void setLogLevel(String logThreshold) {
  Map logThresholdToLogLevel = [ TRACE: 5, DEBUG: 4, INFO: 3, WARN: 2, ERROR: 1 ]
  atomicState['logLevel'] = logThresholdToLogLevel."${logThreshold}" ?: 'INFO'
}

String appHued(InstAppW a) {
log.info("appHUed a is ${a.class}")
  // Allow any caller to get a fancy label for an App.
  return fancyLabel(a.getLabel(), a.id.toInteger())
}

String devHued(ChildDevW d) {
  // Allow any caller to get a fancy label for a Device.
  return fancyLabel(d.getDeviceNetworkId(), d.id.toInteger())
}

String devHued(DevW d) {
  // Allow any caller to get a fancy label for a Device.
  return fancyLabel(d.getDeviceNetworkId(), d.id.toInteger())
}

String eventSender(Event e) {
  // Returns a fancyLabel for the Application/Device sending the Event.
  return fancyLabel(e.displayName, e.deviceId)
}

String fancyLabel(pLabel = null, pId = null) {
  // Automatically populates data for caller if no parameters are specified.
  // Methods appHued() and devHued() provide parameters.
  label = pLabel ?: app?.getLabel() ?: device?.getDeviceNetworkId()
  id = pId ?: (app?.id ?: device.id).toInteger()
  // The following is the result of testing
  //   - Bucket differation was poor when using label.hashCode() only.
  //   - Multiplying by (a relatively consecutive) ID had limited benefit.
  //   - Dividing by ID produced better bucketing.
  String hash = "${Math.round(label.hashCode() / id) % 17}"
  // Colors were relatively easy to differentiate (on a white background)
  // Some colors were a tad dark (on a black background)
  Map hashToColor = [
    '-16': '#696969',   // dim gray
    '-15': '#708090',   // slate gray
    '-14': '#4B0082',   // indigo
    '-13': '#0000FF',   // blue
    '-12': '#4682B4',   // steel blue
    '-11': '#4169E1',   // royal blue
    '-10': '#1E90FF',   // dodger blue
     '-9': '#6495ED',   // corn flower blue
     '-8': '#00BFFF',   // deep sky blue
     '-7': '#008080',   // teal
     '-6': '#20B2AA',   // light sea green
     '-5': '#00CED1',   // dark turquoise
     '-4': '#228B22',   // forest green
     '-3': '#32CD32',   // lime green
     '-2': '#00FF00',   // lime
     '-1': '#6B8E23',   // olive drab
      '0': '#808000',   // olive
      '1': '#8B4513',   // saddle brown
      '2': '#B8860B',   // dark golden rod
      '3': '#DAA520',   // golden rod
      '4': '#800000',   // maroon
      '5': '#FF0000',   // red
      '6': '#FF1493',   // deep pink
      '7': '#CD5C5C',   // indian red
      '8': '#D2691E',   // chocolate
      '9': '#FF8000',   // orange
     '10': '#8B008B',   // dark magenta
     '11': '#9400D3',   // dark violet
     '12': '#7B68EE',   // medium slate blue
     '13': '#9370DB',   // medium purple
     '14': '#C71585',   // medium violet-red
     '15': '#FF00FF',   // magenta / fuchsia
     '16': '#DA70D6',   // orchid
     '17': '#DB7093',   // pale violet red
  ]
  //Integer hash = identityHashCode(label)
  return """<span style="color: ${hashToColor[hash]};">${label}</span>"""
}

String stripFancy(String html) {
  return html?.replaceAll(/<\/?[^>]*>/, '')
}

void logTrace(String fnName, String s) {
  // Fails closed if logLevel is missing.
  if ((state.logLevel ?: 5) > 4) {
    log.trace("${fancyLabel()}.<b>${fnName}⟮ ⟯</b> → ${s}")
  }
}

void logDebug(String fnName, String s) {
  // Fails closed if logLevel is missing.
  if ((state.logLevel ?: 5) > 3) {
    log.debug("${fancyLabel()}.<b>${fnName}⟮ ⟯</b> → ${s}")
  }
}

void logInfo(String fnName, String s) {
  // Fails closed if logLevel is missing.
  if ((state.logLevel ?: 5) > 2) {
    log.info("${fancyLabel()}.<b>${fnName}⟮ ⟯</b> → ${s}")
  }
}

void logWarn(String fnName, String s) {
  // Fails closed if logLevel is missing.
  if ((state.logLevel ?: 5) > 1) {
    log.warn("${fancyLabel()}.<b>${fnName}⟮ ⟯</b> → ${s}")
  }
}

void logError(String fnName, String s) {
  // No conditional test to ensure all errors appear.
  log.error(
    "${fancyLabel()}.<b>${fnName}⟮ ⟯</b> → ${s}"
  )
}

// --------------------------------------
// ArrayList (vs String) Logging Variants
// --------------------------------------

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
