(ns helper.browser
  "some browser functions"
  (:require
    [helper.log :refer [clog]]))

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

(def raf-clock
  "last raf time stamp (ms)"
  (atom 0))

(def raf-token
  "last raf stop token"
  (atom nil))

(defn wrap-raf-cb
  "wrap the raf callback"
  [callback]
  (letfn
    [(raf-loop [timestamp]
        (let [dt (- timestamp @raf-clock)]
          (reset! raf-clock timestamp)
          (callback dt)
          (reset! raf-token (raf raf-loop))))]
    raf-loop))

(defn raf-dt
  "like raf but gives dt (delta time since the last call) to the callback"
  [callback]
  ((wrap-raf-cb callback) 0))

(defn stop-raf
  "calls cancelAnimationFrame on the animation id in @raf-token if there is one"
  []
  (let [id @raf-token]
    (js/cancelAnimationFrame id)))
