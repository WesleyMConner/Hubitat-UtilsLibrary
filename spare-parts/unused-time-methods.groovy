void OldTestPage() {

// Date d = new Date()
// long t = now()
// long d_asLong = d.getTime()

// long tSeconds = t / 1000
// long s = tSeconds % 60
// long tMinutes = tSeconds / 60
// long m = tMinutes % 60
// long tHours = tMinutes / 60
// long h = tHours % 24

  state.sodHMS = "7:30"
  state.soeHMS = "18:00"
  state.sonHMS = "22:00"

  state.sodToday = timeToday(state.sodHMS, Loc.timeZone)
  state.soeToday = timeToday(state.soeHMS, Loc.timeZone)
  state.sonToday = timeToday(state.sonHMS, Loc.timeZone)

  Date d1 = timeToday("4:00", Loc.timeZone)
  Date d2 = timeToday("11:00", Loc.timeZone)
  Date d3 = timeToday("17:00", Loc.timeZone)
  Date d4 = timeToday("19:00", Loc.timeZone)
  Date d5 = timeToday("23:30", Loc.timeZone)

  //if (sodToday < d) {sodDate = sodDate + 1}

  dynamicPage(name: "TestPage", title: "TestApp", install: false, uninstall: false) {
    section {
// paragraph "t: ${t}\td: ${d}"
// paragraph "d_asLong: ${d_asLong}"
// paragraph "tSeconds: ${tSeconds} m: {$m} s: {$s}"
// paragraph "tSeconds: ${tSeconds} h: {$h} m: {$m} s: {$s} [GMT]"
// paragraph "t as HHMMSS: ${convertSecondsToHHMMSS(t)}"

      paragraph "sod: \t${state.sodHMS} \t${state.sodToday}"
      paragraph "soe: \t${state.soeHMS} \t${state.soeToday}"
      paragraph "son: \t${state.sonHMS} \t${state.sonToday}"

      paragraph "${d1}: ${isNight(d1)} ${isEvening(d1)}"
      paragraph "${d2}: ${isNight(d2)} ${isEvening(d2)}"
      paragraph "${d3}: ${isNight(d3)} ${isEvening(d3)}"
      paragraph "${d4}: ${isNight(d4)} ${isEvening(d4)}"
      paragraph "${d5}: ${isNight(d5)} ${isEvening(d5)}"

// paragraph "time1: ${time1}\ttimeTodayAfter ${tString}: ${timeTodayAfter(tString, time1, Loc.timeZone)}"
// paragraph "time2: ${time1}\ttimeTodayAfter ${tString}: ${timeTodayAfter(tString, time2, Loc.timeZone)}"
// paragraph "time3: ${time1}\ttimeTodayAfter ${tString}: ${timeTodayAfter(tString, time3, Loc.timeZone)}"

// paragraph "--> ${app}"
// paragraph "1: ${Loc.getMode()}"
// paragraph "2: ${Loc.currentMode}"

      input "buttonDevice", "capability.pushableButton", title: "Button Device", multiple: false, required: true, submitOnChange: true
      if (buttonDevice) {
        paragraph "Supported Commands: ${buttonDevice.getSupportedCommands().toListString()}"
        // [lutronKeypad, push, release, updated]

        paragraph "Supported Attributes: ${buttonDevice.getSupportedAttributes().toListString()}"
        // [buttonLed-1, buttonLed-2, buttonLed-3, buttonLed-4, buttonLed-5, buttonLed-6, buttonLed-7,
        // numberOfButtons, pushed, released]
      } else {
        paragraph "NO GOOD"
      }
    }
  }
}

Boolean isNight(d) {
  return (
    (state.sonToday - 1 <= d) && (d < state.sodToday)
    || (state.sonToday <= d) && (d < state.sodToday + 1)
  )
}

Boolean isEvening(d) {
  return (state.soeToday <= d) && (d < state.sonToday)
}
