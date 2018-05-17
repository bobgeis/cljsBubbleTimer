(ns teatime.model
  "ns for the db and manipulation fns"
  (:require
    [re-frame.core :as rf]
    [com.rpl.specter :as sp]
    [helper.browser :refer [set-local-storage get-local-storage del-local-storage play-audio]]
    [helper.fun :refer [distance within? filtermap]]
    [helper.log :refer [jlog clog]]
    [helper.rf :refer [spy]]))

(def audio-file
  "audio file path"
  "audio/bubbles.mp3")

(def min-radius
  "circles smaller than this (px) will not be created"
  5)

(defn radius-to-time
  "convert radius (px) to time (ms)"
  [r]
  (* 1000 r))

(defn make-circle
  "make a circle shape from a map"
  [{:keys [x y r]}]
  (let [t (radius-to-time r)]
    {:x x :y y :r r :state "on" :t t :tM t}))

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
   :shapes []
   :mouse nil})

(defn add-shape
  "add a shape to the db"
  [db shape]
  (update db :shapes #(conj % shape)))

(defn count-red
  "count the number of red shapes in the list"
  [shapes]
  (count (filter #(> 0 (:t %)) shapes)))

(defn filter-tick-shape
  "tick one shape, return nil if it should be removed"
  [shape dt]
  (if (= "off" (:state shape)) shape
    (let [t (- (:t shape) dt)]
     (if (> t (- (:tM shape)))
       (assoc shape :t t)
       nil))))

(defn filter-tick-shapes
  "tick & filter a vector of shapes"
  [shapes dt]
  (filtermap #(filter-tick-shape % dt) shapes))

(defn tick-shapes
  "tick if needed"
  [db dt]
  (if (not (= :run (:mode db))) {}
    (let [red (count-red (:shapes db))
          db' (update db :shapes filter-tick-shapes dt)
          red' (count-red (:shapes db'))]
      (if (= red red') {:db db'}
        {:db db' :play-sound audio-file}))))

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

(defn create-new-shape
  "add a new shape to the db"
  [db stop start]
  (-> db
    clear-mouse
    (add-shape (make-circle-from-points start stop))))

(defn toggle-shape-state
  "toggle the :state of a shape :on/:off"
  [shape]
  (assoc shape :state (if (= "on" (:state shape)) "off" "on")))

(defn within-shape?
  "is this point within the shape?"
  [{r :r :as shape} point]
  (within? shape point r))

(defn maybe-click-shape
  "pause or kill a circle under the upclick"
  [db {:keys [x y shift ctrl] :as stop}]
  (sp/transform [:shapes (sp/filterer #(within-shape? % stop)) sp/FIRST]
    (if shift sp/NONE toggle-shape-state)
    (clear-mouse db)))

(defn stop-mouse
  "handle mouse up"
  [db stop]
  (let [start (get-in db [:mouse :start])
        r (distance stop start)]
    (cond
      (> r min-radius) (create-new-shape db stop start)
      :else (maybe-click-shape db stop))))

(defn no-axn
  "no-op action"
  [cofx data]
  {:db (:db cofx)})

(defn toggle-mode
  "toggle the mode :run <-> :pause"
  [cofx data]
  {:db (assoc (:db cofx) :mode (if (= :run (:mode (:db cofx))) :pause :run))})

(defn store-state
  "store the current db in local storage"
  [cofx data]
  {:set-local-store [(get-in cofx [:db :shapes]) "teatime"]})

(defn clear-store
  "remove anything stored in local storage for this app"
  [cofx data]
  {:clear-local-store "teatime"})

(def keyup->axn
  "map of keyups to action functions"
  {" " toggle-mode
   "Enter" store-state
   "Escape" clear-store})


;; reg cofx

(rf/reg-cofx :get-local-store
  (fn [cofx ls-key]
    (assoc cofx :get-local-store
      (get-local-storage ls-key []))))


;; reg fx

(rf/reg-fx :set-local-store
  (fn [[data ls-key]]
    (set-local-storage ls-key data)))

(rf/reg-fx :clear-local-store
  (fn [ls-key]
    (del-local-storage ls-key)))

(rf/reg-fx :play-sound
  (fn [id]
    (play-audio id)))


;; reg event

(rf/reg-event-fx :init
  [(rf/inject-cofx :get-local-store "teatime")]
  (fn [cofx _]
    {:db (assoc (init-model) :shapes (:get-local-store cofx))}))

(rf/reg-event-fx :tick
  (fn [cofx [_ dt]]
    (tick-shapes (:db cofx) dt)))

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

(rf/reg-event-fx :key-up
  (fn [cofx [_ data]]
    ((get keyup->axn (:key data) no-axn) cofx data)))


;; reg sub

(rf/reg-sub :shapes
  (fn [db _] (reverse (or (:shapes db) []))))

(rf/reg-sub :mouse-circle
  (fn [db _]
    (if-let [stop (get-in db [:mouse :stop])]
      (make-circle-from-points (get-in db [:mouse :start]) stop)
      nil)))

(rf/reg-sub :mode
  (fn [db _] (:mode db)))

(rf/reg-sub :shape-count
  (fn [db _] (count (:shapes db))))

(rf/reg-sub :red-count
  (fn [db _] (count-red (:shapes db))))
