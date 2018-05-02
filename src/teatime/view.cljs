(ns teatime.view
  "ns for views"
  (:require
    [clojure.string :as str]
    [reagent.core :as r]
    [helper.log :refer [jlog clog]]
    [helper.rf :refer [>evt <sub]]))



(defn make-svg-circle
  "make a circle from a shape map"
  [{:keys [x y r on t tM]}]
  [:circle {:cx x :cy y :r r :fill (if on "blue" "purple")}])

(defn svg-board
  "draw the svgs"
  []
  (let [shapes (<sub [:shapes])]
    ; (into [:svg] make-svg-circle shapes)))
    [:div "Hello!"]))

(defn main-view
  []
  (clog (svg-board)))

(defn render-root
  "render the root view"
  []
  (r/render
    [main-view]
    (js/document.getElementById "app")))
