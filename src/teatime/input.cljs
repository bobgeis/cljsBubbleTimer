(ns teatime.input
  "ns for user input handlers"
  (:require
    [re-frame.core :as rf]
    [helper.log :refer [jlog clog]]
    [helper.browser :refer [get-app-element]]))


(defn disp-mouse-handler
  "dispatch a mouse event"
  [e kw]
  (rf/dispatch [kw {:x e.x :y e.y :shift e.shiftKey :alt e.altKey}]))

(defn disp-key-handler
  "dispatch a key event"
  [e kw]
  (rf/dispatch [kw {:key e.key :shift e.shiftKey :alt e.altKey}]))

(defn add-listener
  "add an event listener to an element"
  [ele event handler]
  (.addEventListener ele event handler))

(defn add-top-listeners
  "add the event listeners"
  []
  (let [div (get-app-element)]
    (add-listener div "pointerdown" #(disp-mouse-handler % :pointer-down))
    (add-listener div "pointermove" #(disp-mouse-handler % :pointer-move))
    (add-listener div "pointerleave" #(disp-mouse-handler % :pointer-leave))
    (add-listener div "pointerup" #(disp-mouse-handler % :pointer-up))
    (add-listener js/document "keyup" #(disp-key-handler % :key-up))))
