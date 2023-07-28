if (buttonDevice) {
  paragraph "Supported Commands: ${buttonDevice.getSupportedCommands().toListString()}"
  // [lutronKeypad, push, release, updated]

  paragraph "Supported Attributes: ${buttonDevice.getSupportedAttributes().toListString()}"
  // [buttonLed-1, buttonLed-2, buttonLed-3, buttonLed-4, buttonLed-5, buttonLed-6, buttonLed-7,
  // numberOfButtons, pushed, released]
} else {
  paragraph "NO GOOD"
}