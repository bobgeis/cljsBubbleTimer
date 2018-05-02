(ns teatime.core
    (:require
      [reagent.core :as reagent :refer [atom]]
      [re-frame.core :as rf]
      [helper.log :refer [jlog plog]]
      [helper.rf :refer [spy]]))

(enable-console-print!)

(defn hello-world "doc string" []
  [:div
   [:h3 "Edit this and watch it change!"]])

(reagent/render-component [hello-world]
                          (. js/document (getElementById "app")))

(defn on-js-reload [])
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)


(rf/reg-event-db :init
  (fn [db _]
    (merge db {:mode :run :shapes []})))

(defonce begin!
  (do
    (rf/dispatch-sync [:init])
    (spy)))

