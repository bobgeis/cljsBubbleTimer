(ns teatime.view
  "ns for views"
  (:require
    [clojure.string :as str]
    [reagent.core :as r]
    [teatime.model :as mod]
    [helper.log :refer [jlog clog]]
    [helper.rf :refer [>evt <sub]]))



(defn make-svg-circle
  "make a circle from a shape map"
  [{:keys [x y r state t tM] :as shape}]
  ; (clog shape)
  [:circle {:cx x :cy y :r r :fill (if (= state :on) "blue" "purple")}])

(defn make-svg-circles
  "make the circle timers"
  []
  (let [shapes (<sub [:shapes])
        circles (map make-svg-circle shapes)]
     [circles]))

(defn maybe-active-circle
  "return a group that might contain an active circle"
  []
  (let [mouse-circle (<sub [:mouse-circle])]
    [:g]))

(defn svg-board
  "draw the svgs"
  []
  (-> [:svg]
    (into (make-svg-circles))))

(defn main-view
  []
  (svg-board))

(defn render-root
  "render the root view"
  []
  (r/render
    [main-view]
    (js/document.getElementById "app")))
