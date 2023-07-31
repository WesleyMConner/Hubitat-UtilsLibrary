import com.hubitat.app.DeviceWrapper as DeviceWrapper
import com.hubitat.app.DeviceWrapperList as DeviceWrapperList
import com.hubitat.hub.domain.Event as Event
import com.hubitat.hub.domain.Hub as Hub

library (
 name: "UtilsLibrary",
 namespace: "wesmc",
 author: 'WesleyMConner',
 description: "General-Purpose Methods that are Reusable across Hubitat Projects",
 category: "general purpose",
 documentationLink: 'https://github.com/WesleyMConner/Hubitat-UtilsLibrary/README.adoc',
 importUrl: 'https://github.com/WesleyMConner/Hubitat-UtilsLibrary.git'
)

// -------------
// G L O B A L S
// -------------
BLACK = 'rgba(0, 0, 0, 1.0)'
BLUE = 'rgba(51, 92, 255, 1.0)'
LIGHT_GREY = 'rgba(100, 100, 100, 1.0)'
RED = 'rgba(255, 0, 0, 1.0)'

// ---------------------------------------
// P A R A G R A P H   F O R M A T T I N G
// ---------------------------------------
String heading(String s) {
  HEADING_CSS = "font-size: 2em; font-weight: bold;"
  return """<span style="${HEADING_CSS}">${s}</span>"""
}

String important(String s) {
  IMPORTANT_CSS = "font-size: 1em; color: ${RED};"
  return """<span style="${IMPORTANT_CSS}">${s}</span>"""
}

String emphasis(String s) {
  EMPHASIS_CSS = "font-size: 1.3em; color: ${BLUE}; margin-left: 0px;"
  return """<span style="${EMPHASIS_CSS}">${s}</span>"""
}

String emphasis2(String s) {
  EMPHASIS2_CSS = "font-size: 1.1em; color: ${BLUE}; margin-left: 0px;"
  return """<span style="${EMPHASIS2_CSS}">${s}</span>"""
}

String normal(String s) {
  NORMAL_CSS = "font-size: 1.1em;"
  return """<span style="${NORMAL_CSS}">${s}</span>"""
}

String bullet(String s) {
  BULLET_CSS = "font-size: 1.0em; margin-left: 10px;"
  return """<span style="${BULLET_CSS}">&#x2022;&nbsp;&nbsp;${s}</span>"""
}

String comment(String s) {
  COMMENT_CSS = "font-size: 0.8em; color: ${LIGHT_GREY}; font-style: italic"
  return """<span style="${COMMENT_CSS}">${s}</span>"""
}

String red(String s) {
  RED_BOLD = "color: ${RED}; font-style: bold"
  return """<span style="${RED_BOLD}">${s}</span>"""
}

// -------------------------------------------------------------------
// C O L L A P S I B L E   I N P U T
//
//   Wrap Hubitat input() with a slider (bool) that enables hiding the
//   input() to declutter the screen.
//
//   Default values for all arguments are provided.
// -------------------------------------------------------------------
void collapsibleInput (Map args = [:]) {
  Map _args = [
        blockLabel: "E R R O R: Missing 'blockLabel'",
              name: "E R R O R: Missing 'name'",
             title: "E R R O R: Missing 'title'",
              type: "E R R O R: Missing 'type'",
    submitOnChange: true,
          required: true,
          multiple: true
  ] << args
  String boolSwitchName = "hide${_args.name}"
  //String toggleTitle =
  input (
    name: boolSwitchName,
    type: 'bool',
    title: settings[boolSwitchName]
      ? "Hiding ${_args.blockLabel} (devices=${settings[_args.name].size()})"
      : "Showing ${_args.blockLabel}",
    submitOnChange: true,
    defaultValue: false
  )
  if (!settings[boolSwitchName]) {
    input (
      name: _args.name,
      type: _args.type,
      title: _args.title,
      submitOnChange: _args.submitOnChange,
      required: _args.required,
      multiple: _args.multiple
    )
  }
}

// ---------------------------------------------
// H T M L   I N S P E C T I O N   M E T H O D S
// ---------------------------------------------
String hubPropertiesAsHtml() {
  Hub hub = location.hub
  String hubProperties = hub.getProperties().collect{k, v ->
    "<tr><th>${k}</th><td>$v</td></tr>"
  }.join('')
  return "<b>Hub Properties</b><br/><table>${hubProperties}</table>"
}

String roomsAsHtml(ArrayList<LinkedHashMap> rooms) {
  String dataRows = rooms.collect{ r ->
    """<tr>
      <td width="5%">${r.id}</td>
      <td width="10%">${r.name}</td>
      <td width="90%">${r.deviceIds}</td>
    </tr>"""
  }.join('')
  return """
    <b>Room Info</b>
    <table>
      <tr><th>id</th><th>name</th><th>deviceIds</th><tr/>
      ${dataRows}
    </table>
  """
}

String devicesAsHtml(DeviceWrapperList devices) {
  // Not helpful
  //   - d.getMetaPropertyValues()
  //   = d.type() DOES NOT EXIST
  //   = d.type always null
  String headerRow = """<tr>
    <th style='border: 1px solid black' align='center'>Id</th>
    <th style='border: 1px solid black' align='center'>Display Name</th>
    <th style='border: 1px solid black' align='center'>Room Id</th>
    <th style='border: 1px solid black' align='center'>Room Name</th>
    <th style='border: 1px solid black' align='center'>Supported Attributes</th>
    <th style='border: 1px solid black' align='center'>Data</th>
    <th style='border: 1px solid black' align='center'>Current States</th>
    <th style='border: 1px solid black' align='center'>Supported Commands</th>
    <th style='border: 1px solid black' align='center'>Parent Device ID</th>
    <th style='border: 1px solid black' align='center'>Disabled?</th>
    <th style='border: 1px solid black' align='center'>Type</th>
  </tr>"""
  String dataRows = settings.devices.collect{d ->
    """<tr>
      <td style='border: 1px solid black' align='center'>${d.id}</td>
      <td style='border: 1px solid black' align='center'>${d.displayName}</td>
      <td style='border: 1px solid black' align='center'>${d.getRoomId()}</td>
      <td style='border: 1px solid black' align='center'>${d.getRoomName()}</td>
      <td style='border: 1px solid black' align='center'>${d.getSupportedAttributes()}</td>
      <td style='border: 1px solid black' align='center'>${d.getData()}</td>
      <td style='border: 1px solid black' align='center'>${d.getCurrentStates}</td>
      <td style='border: 1px solid black' align='center'>${d.getSupportedCommands}</td>
      <td style='border: 1px solid black' align='center'>${d.getParentDeviceId()}</td>
      <td style='border: 1px solid black' align='center'>${d.isDisabled()}</td>
      <td style='border: 1px solid black' align='center'>${d.type}</td>
    </tr>"""
  }.join()
  return "<table>${headerRow}${dataRows}</table>"
}

// -----------------------------------------------------
// A D D   S O L I C I T E D   D A T A   T O   S T A T E
// -----------------------------------------------------
