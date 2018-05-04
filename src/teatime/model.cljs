(ns teatime.model
  "ns for the db and manipulation fns"
  (:require
    [re-frame.core :as rf]
    [helper.fun :refer [distance]]
    [helper.log :refer [jlog clog]]
    [helper.rf :refer [spy]]))


(defn example-shape-1
  []
  {:x 250 :y 400 :r 20 :t 15 :tM 20 :on :true})

(defn make-circle
  "make a circle shape from a map"
  [{:keys [x y r]}]
  {:x x :y y :r r :on true :t 15 :tM 20})

(defn make-circle-from-points
  "make circle from two points,
  first arg will be the center,
  the second arg will be on the edge"
  [{x1 :x y1 :y} {x2 :x y2 :y}]
  (let [r (distance x1 y1 x2 y2)]
    (make-circle {:x x1 :y y1 :r r})))

(defn init-model
  "return an initial model"
  []
  {:mode :run
   :shapes [(example-shape-1)]
   :mouse nil})

(defn add-shape
  "add a shape to the db"
  [db shape]
  (update db :shapes #(conj % shape)))

(defn clear-mouse
  "clear the mouse map from db"
  [db]
  (assoc db :mouse nil))

(defn add-mouse
  "assoc mouse pos data"
  [db {:keys [x y]}]
  (assoc db :mouse {:start {:x x :y y}}))

(defn move-mouse
  "handle mouse moves"
  [db {:keys [x y]}]
  (if-not (:mouse db)
    db
    (assoc-in db [:mouse :stop] {:x x :y y})))

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
  (fn [db [_ data]]
    (add-mouse db data)))

(rf/reg-event-db :mouse-move
  (fn [db [_ data]]
    (move-mouse db data)))

(rf/reg-event-db :mouse-out
  (fn [db _]
    (clear-mouse db)))

(rf/reg-event-db :mouse-up
  (fn [db [_ data]]
    (-> db
    ;  clog
     clear-mouse
     (add-shape (make-circle-from-points (get-in db [:mouse :start]) data)))))

(rf/reg-event-db :key-up
  (fn [db v1]
    ; (clog v1)
    db))


;; reg sub

(rf/reg-sub :shapes
  (fn [db _] (:shapes db)))

(rf/reg-sub :mouse-circle
  (fn [db _]
    (let [start (get-in db [:mouse :start])
          stop (get-in db [:mouse :stop])]
      (make-circle-from-points start stop))))
