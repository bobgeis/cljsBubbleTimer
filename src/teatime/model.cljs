(ns teatime.model
  "ns for the db and manipulation fns"
  (:require
    [re-frame.core :as rf]
    [helper.fun :refer [distance]]
    [helper.log :refer [jlog clog]]
    [helper.rf :refer [spy]]))


(defn radius-to-time
  "convert radius (px) to time (ms)"
  [r]
  (* 1000 r))

(defn example-shape-1
  []
  {:x 250 :y 400 :r 20 :t 15 :tM 20 :state :on})

(defn make-circle
  "make a circle shape from a map"
  [{:keys [x y r]}]
  (let [t (radius-to-time r)]
    {:x x :y y :r r :state :on :t t :tM t}))

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
  ;  :shapes [(example-shape-1)]
   :shapes []
   :mouse nil})

(defn add-shape
  "add a shape to the db"
  [db shape]
  (update db :shapes #(conj % shape)))

(defn filter-tick-shape
  "tick one shape, return nil if it should be removed"
  [shape dt]
  (let [t (- (:t shape) dt)]
   (if (< t 0)
     nil
     (assoc shape :t  t))))

(defn filter-tick-shapes
  "tick & filter a vector of shapes"
  [shapes dt]
  (->> shapes
    (map #(filter-tick-shape % dt))
    (filter identity)))

(defn tick-shapes
  "progress all the shapes"
  [db dt]
  (update db :shapes filter-tick-shapes dt))

(defn clear-mouse
  "clear the mouse map from db"
  [db]
  (assoc db :mouse nil))

(defn start-mouse
  "assoc mouse pos data"
  [db {:keys [x y]}]
  (assoc db :mouse {:start {:x x :y y}}))

(defn move-mouse
  "handle mouse moves"
  [db {:keys [x y]}]
  (if-not (:mouse db)
    db
    (assoc-in db [:mouse :stop] {:x x :y y})))

(defn stop-mouse
  "handle mouse up"
  [db stop]
  (if-let [start (get-in db [:mouse :start])]
    (-> db
      clear-mouse
      (add-shape (make-circle-from-points start stop)))
    db))


;; reg cofx


;; reg fx


;; reg event

(rf/reg-event-db :init
  (fn [db _]
    (merge db (init-model))))

(rf/reg-event-db :tick
  (fn [db [_ dt]]
    ; (spy)
    (tick-shapes db dt)))

;; reg mouse events
(rf/reg-event-db :mouse-down
  (fn [db [_ data]]
    (start-mouse db data)))

(rf/reg-event-db :mouse-move
  (fn [db [_ data]]
    (move-mouse db data)))

(rf/reg-event-db :mouse-leave
  (fn [db _]
    (clear-mouse db)))

(rf/reg-event-db :mouse-up
  (fn [db [_ data]]
    (stop-mouse db data)))

(rf/reg-event-db :key-up
  (fn [db v1]
    ; (clog v1)
    db))


;; reg sub

(rf/reg-sub :shapes
  (fn [db _] (:shapes db)))

(rf/reg-sub :mouse-circle
  (fn [db _]
    (if-let [stop (get-in db [:mouse :stop])]
      (make-circle-from-points (get-in db [:mouse :start]) stop)
      nil)))
