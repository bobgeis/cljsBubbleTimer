(ns teatime.model
  "ns for the db and manipulation fns"
  (:require
    [re-frame.core :as rf]
    [helper.log :refer [jlog clog]]
    [helper.rf :refer [spy]]))


(defn example-shape-1
  []
  {:x 250 :y 400 :r 20 :t 15 :tM 20 :on :true})

(defn init-model
  "return an initial model"
  []
  {:mode :run
   :shapes [(example-shape-1)]})





;; reg cofx




;; reg fx


;; reg event
(rf/reg-event-db :init
  (fn [db _]
    (merge db (init-model))))

(rf/reg-event-db :tick
  (fn [db _]
    ; (spy)
    db))



;; reg sub

(rf/reg-sub :shapes
  (fn [db _] (:shapes db)))
