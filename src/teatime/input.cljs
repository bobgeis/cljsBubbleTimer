(ns teatime.input
  "ns for user input handlers"
  (:require
    [re-frame.core :as rf]
    [helper.log :refer [jlog clog]]
    [helper.browser :refer [get-app-element]]))


(defn disp-mouse-handler
  [e kw]
  (rf/dispatch [kw {:x e.x :y e.y :shift e.shiftKey :alt e.altKey}]))

(defn disp-key-handler
  [e kw]
  (rf/dispatch [kw {:key e.key :shift e.shiftKey :alt e.altKey}]))

(defn add-listener
  [ele event handler]
  (.addEventListener ele event handler))

(defn add-top-listeners
  []
  (let [div (get-app-element)]
    (add-listener div "mousedown" #(disp-mouse-handler % :mouse-down))
    (add-listener div "mousemove" #(disp-mouse-handler % :mouse-move))
    (add-listener div "mouseleave" #(disp-mouse-handler % :mouse-leave))
    (add-listener div "mouseup" #(disp-mouse-handler % :mouse-up))
    (add-listener js/document "keyup" #(disp-key-handler % :key-up))))