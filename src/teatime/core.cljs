(ns teatime.core
    (:require
      [reagent.core :as reagent :refer [atom]]
      [re-frame.core :as rf]
      [helper.log :refer [jlog clog]]
      [helper.rf :refer [spy]]
      [helper.browser :refer [raf-dt]]
      [teatime.model :as mod]
      [teatime.view :as view]
      [teatime.input :as input]))

(enable-console-print!)

(defn on-js-reload [])
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)

(defn main-loop
  "dispatch tick every animation frame"
  [dt]
  (rf/dispatch [:tick dt]))

(defonce begin!
  (do
    (rf/dispatch-sync [:init])
    (view/render-root)
    (input/add-top-listeners)
    (raf-dt main-loop)))
    ; (spy)))

