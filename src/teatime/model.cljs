(ns teatime.model
  "ns for the db and manipulation fns"
  (:require
    [re-frame.core :as rf]
    [helper.log :refer [jlog clog]]
    [helper.rf :refer [spy]]))


(defn example-shape-1
  []
  {:x 250 :y 400 :r 20 :t 15 :tM 20 :on :true})

(defn make-circle
  "make a circle shape from a map"
  [{:keys [x y r]}]
  {:x x :y y :r 30 :on true :t 15 :tM 20})

(defn init-model
  "return an initial model"
  []
  {:mode :run
   :shapes [(example-shape-1)]})

(defn add-shape
  "add a shape to the db"
  [db shape]
  (update db :shapes #(conj % shape)))




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


(rf/reg-event-db :mouse-down
  (fn [db v1]
    ; (clog v1)
    db))

(rf/reg-event-db :mouse-move
  (fn [db _]
    db))

(rf/reg-event-db :mouse-up
  (fn [db [_ data]]
    ; (clog data)
    (add-shape db (make-circle data))))

(rf/reg-event-db :key-up
  (fn [db v1]
    ; (clog v1)
    db))


;; reg sub

(rf/reg-sub :shapes
  (fn [db _] (:shapes db)))
