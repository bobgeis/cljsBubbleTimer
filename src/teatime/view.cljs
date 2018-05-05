(ns teatime.view
  "ns for views"
  (:require
    [clojure.string :as str]
    [reagent.core :as r]
    [teatime.model :as mod]
    [helper.log :refer [jlog clog]]
    [helper.rf :refer [>evt <sub]]
    [helper.browser :refer [get-app-element]]))



(defn make-svg-circle
  "make a circle from a shape map"
  [{:keys [x y r state t tM] :as shape}]
  ; (clog shape)
  [:circle
    {:cx x :cy y :r r
     :fill (if (= state :on) "rgba(0, 0, 255, 0.5)" "purple")}])

(defn make-svg-circles
  "make the circle timers"
  []
  (let [shapes (<sub [:shapes])]
     (into [:g] (map make-svg-circle) shapes)))

(defn svg-board
  "draw the svgs"
  []
  [:svg
    (make-svg-circles)])
  ; (-> [:svg]
  ;   (into (make-svg-circles))))

(defn main-view
  []
  (svg-board))

(defn render-root
  "render the root view"
  []
  (r/render
    [main-view]
    (get-app-element)))
