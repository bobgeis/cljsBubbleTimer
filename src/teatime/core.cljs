(ns teatime.core
    (:require
      [reagent.core :as reagent :refer [atom]]
      [re-frame.core :as rf]
      [helper.fun :refer [raf]]
      [helper.log :refer [jlog clog]]
      [helper.rf :refer [spy]]
      [teatime.model :as mod]
      [teatime.view :as view]))

(enable-console-print!)

(defn on-js-reload [])
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)

(defn main-loop
  "the main loop:
    1: dispatch :tick
    2: call raf
    3: profit"
  []
  (rf/dispatch [:tick])
  (raf main-loop))

(defonce begin!
  (do
    (rf/dispatch-sync [:init])
    (view/render-root)
    (raf main-loop)
    (spy)))

