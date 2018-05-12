(ns teatime.view
  "ns for views"
  (:require
    [reagent.core :as r]
    [teatime.model :as mod]
    [helper.log :refer [jlog clog]]
    [helper.rf :refer [>evt <sub]]
    [helper.fun :refer [trans-ra radians half-pi pi tau]]
    [helper.browser :refer [get-app-element]]))

(defn radius-to-min-sec
  "turn a radius (px) into mm:ss"
  [r]
  (str (Math/floor (/ r 60)) ":" (.slice (str "0" (Math/floor (mod r 60))) -2)))

(defn make-svg-circle
  "make a circle from a shape map"
  [{:keys [x y r state t tM]}]
  [:circle
    {:cx x :cy y :r r
     :fill (if (= state "on") "rgba(0, 0, 255, 0.5)"
              "rgba(150, 0, 200, 0.5)")}])

(defn make-svg-arc
  "draw an arc with svg"
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
       :fill (if (= state "on") "rgba(0, 0, 255, 0.5)"
                "rgba(150, 0, 200, 0.5)")}]))

(defn make-svg-shape
  "draw a shape with svg"
  [{:keys [t tM] :as shape}]
  (let [ratio (/ t tM)]
    (cond
      (>= ratio 1) (make-svg-circle shape)
      (> ratio 0) (make-svg-arc shape)
      :else nil)))

(defn make-svg-circles
  "make the circle timers"
  []
  (let [shapes (<sub [:shapes])]
     (into [:g] (map make-svg-shape) shapes)))

(defn mouse-circle
  "draw the mouse circle if there is one"
  []
  (let [{:keys [x y r]} (<sub [:mouse-circle])]
    (if (> r mod/min-radius)
      [:g
        [:circle {:cx x :cy y :r r
                  :fill "rgba(0, 150, 200, 0.8)"}]
        [:text {:x x :y y
                :font-size 30 :font-family "Arial"}
          (radius-to-min-sec r)]]
      [:g])))

(defn get-bg-color
  "get the background color of the main svg"
  [mode num]
  (cond
    (= mode :pause) "#888855"
    (= num 0) "555555"
    ;; (= red num) "#885555" ;; if all shapes are red, show the red bg
    :else "#DFDFD0"))

(defn svg-board
  "draw the svgs"
  []
  (let [mode (<sub [:mode])
        num (<sub [:shape-count])]
    [:svg
      {:style {:background-color (get-bg-color mode num)}}
      (make-svg-circles)
      (mouse-circle)]))

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
