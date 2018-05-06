(ns teatime.view
  "ns for views"
  (:require
    [reagent.core :as r]
    [teatime.model :as mod]
    [helper.log :refer [jlog clog]]
    [helper.rf :refer [>evt <sub]]
    [helper.fun :refer [trans-ra radians half-pi pi tau]]
    [helper.browser :refer [get-app-element]]))


(defn make-svg-circle
  "make a circle from a shape map"
  [{:keys [x y r state t tM]}]
  ; (clog shape)
  [:circle
    {:cx x :cy y :r r
     :fill (if (= state :on) "rgba(0, 0, 255, 0.5)" "rgba(150, 0 200, 0.5")}])

(defn make-svg-arc
  "draw a shape with svg"
  [{:keys [x y r state t tM]}]
  (let [a (radians (/ t tM))
        [x2 y2] (trans-ra x y r (- (+ a half-pi)))
        flag (if (> a pi) 1 0)]
    [:path
      {:d (str
            "M " x ", " y
            "v" (- r)
            "A " r ", " r ", " 0 ", " flag ", " 0 ", " x2 ", " y2
            "Z")
       :fill "rgba(0, 0, 255, 0.5)"}]))

(defn make-svg-circles
  "make the circle timers"
  []
  (let [shapes (<sub [:shapes])]
     (into [:g] (map make-svg-arc) shapes)))

(defn mouse-circle
  "draw the mouse circle if there is one"
  []
  (if-let [circle (<sub [:mouse-circle])]
    [:g [:circle {:cx (:x circle) :cy (:y circle) :r (:r circle) :fill "cyan"}]]
    [:g]))

(defn svg-board
  "draw the svgs"
  []
  [:svg
    (make-svg-circles)
    (mouse-circle)])

(defn main-view
  "the main view"
  []
  (svg-board))

(defn render-root
  "render the root view"
  []
  (r/render
    [main-view]
    (get-app-element)))
