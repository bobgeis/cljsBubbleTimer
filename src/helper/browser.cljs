(ns helper.browser
  "some browser functions")

(defn get-app-element
  "get the app element"
  []
  (js/document.getElementById "app"))

(def raf
  "request animation frame"
  js/requestAnimationFrame)