(ns teatime.input
  "ns for user input handlers"
  (:require
    [re-frame.core :as rf]
    [helper.log :refer [jlog clog]]))


(defn disp-mouse-handler
  [e kw]
  ; (jlog e)
  (rf/dispatch [kw {:x e.x :y e.y :shift e.shift}]))

(defn disp-key-handler
  [e kw]
  ; (jlog e)
  (rf/dispatch [kw {:key (.-key e)}]))

(defn add-listener
  [ele event handler]
  (.addEventListener ele event handler))

(defn add-doc-listener
  [event handler]
  (add-listener js/document event handler))

(defn add-top-listeners
  []
  (add-doc-listener "mousedown" #(disp-mouse-handler % :mouse-down))
  (add-doc-listener "mousemove" #(disp-mouse-handler % :mouse-move))
  (add-doc-listener "mouseout" #(disp-mouse-handler % :mouse-out))
  (add-doc-listener "mouseup" #(disp-mouse-handler % :mouse-up))
  (add-doc-listener "keyup" #(disp-key-handler % :key-up)))
