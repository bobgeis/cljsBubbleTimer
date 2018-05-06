(ns helper.browser
  "some browser functions")

(defn get-app-element
  "get the app element"
  []
  (js/document.getElementById "app"))

(def raf
  "request animation frame
  https://developer.mozilla.org/en-US/docs/Web/API/window/requestAnimationFrame
  - takes a callback to call on the next animation frame
  the callback receives a timestamp as the arg
  (ideally: 60 fps or every ~16ms)
  note that the callback will also need to call raf if you want it called again
  - raf returns a frame id that can be used to abort using
  window.cancelAnimationFrame()"
  js/requestAnimationFrame)