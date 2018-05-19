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


(defn make-svg-outline-arc
  "make an outline arc"
  [{:keys [x y r state t tM]}]
  (let [a (+ tau (radians (/ t tM)))
        [x2 y2] (trans-ra x y r (- (+ a half-pi)))
        flag (if (> a pi) 1 0)]
    [:path
      {:d (str
            "M " x ", " y
            "m" 0 (- r)
            "A " r ", " r ", " 0 ", " flag ", " 0 ", " x2 ", " y2)
        :fill "transparent"
        :stroke (if (= state "on") "#FF0000" "#FFAA00")
        :stroke-width 3}]))

(defn make-svg-outline-circle
  "make an outline circle"
  [{:keys [x y r state t tM]}]
  [:circle
    {:cx x :cy y :r r
     :fill "transparent"
     :stroke "black" :stroke-width 1}])

(defn make-svg-outline
  "make the outline circle or arc"
  [{:keys [t tM] :as shape}]
  (let [ratio (/ t tM)]
    (cond
      (> ratio 0) (make-svg-outline-circle shape)
      (> ratio -1) (make-svg-outline-arc shape)
      :else nil)))

(defn get-fill-color
  "choose the color to fill the circle or sector"
  [state]
  (if (= state "on") "rgba(0, 100, 200, 0.5)" "rgba(100, 0, 200, 0.5)"))

(defn make-svg-fill-circle
  "make a circle from a shape map"
  [{:keys [x y r state t tM]}]
  [:circle
    {:cx x :cy y :r r
      :fill (get-fill-color state)}])

(defn make-svg-fill-arc
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
        :fill (get-fill-color state)}]))

(defn make-svg-fill
  "draw a shape with svg"
  [{:keys [t tM] :as shape}]
  (let [ratio (/ t tM)]
    (cond
      (>= ratio 1) (make-svg-fill-circle shape)
      (> ratio 0) (make-svg-fill-arc shape)
      :else nil)))

(defn make-svg-shape
  [shape]
  (if (not shape) nil
    [:g (make-svg-fill shape)
        (make-svg-outline shape)]))

(defn make-svg-shapes
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
  [mode num red]
  (cond
    (= mode :pause) "#888855"
    (= num 0) "555555"
    (= red num) "#885555" ;; if all shapes are red, show the red bg
    :else "#DFDFD0"))

(defn svg-board
  "draw the svgs"
  []
  (let [mode (<sub [:mode])
        num (<sub [:shape-count])
        red (<sub [:red-count])]
    [:svg
      {:style {:background-color (get-bg-color mode num red)
                :position "fixed"
                :top 0
                :left 0
                :width "100%"
                :height "100%"}}
      (make-svg-shapes)
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
