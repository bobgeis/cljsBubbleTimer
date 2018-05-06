(ns helper.fun
  "helper functions")


;; misc utility

(defn square
  "square it"
  [x]
  (* x x))

(defn mmap
  "map over just the values of a map, producing a new map
  this is a copy of fmap just for maps and map-like structs
  Note how empty m ensures that the product is the same type"
  [f m]
  (into (empty m) (for [[k v] m] [k (f v)])))

(defn distance
  "get the distance between two point maps"
  ([{x1 :x y1 :y} {x2 :x y2 :y}]
   (distance x1 y1 x2 y2))
  ([x1 y1 x2 y2]
   (Math/hypot (- x2 x1) (- y2 y1))))

;; trigonometry constants

(def pi
  "pi"
  Math/PI)

(def two-pi
  "two pi"
  (* pi 2))

(def half-pi
  "one half pi"
  (/ pi 2))

(def tau
  "tau is two pi"
  two-pi)

(def half-tau
  "one half tau (pi)"
  pi)

(def quarter-tau
  "one quarter of tau (half-pi"
  half-pi)

;; trigonometry functions

(defn radians
  "convert a fraction of a circle to radian angle"
  [ratio]
  (* tau ratio))

(defn mag
  "get the magnitude of a vector [x y]"
  [x y]
  (Math/hypot x y))

(defn gang
  "get the angle of a vector [x y]"
  [x y]
  (Math/atan2 y x))

(defn ra-to-xy
  "convert polar coordinates to cartesian"
  [r a]
  [(* r (Math/cos a)) (* r (Math/sin a))])

(defn xy-to-ra
  "convert cartesian coordinates to polar"
  [x y]
  [(mag x y) (gang x y)])

(defn trans-xy
  "translate an xy point by dx dy"
  [x y dx dy]
  [(+ x dx) (+ y dy)])

(defn trans-ra
  "translate a point (x y) by a distance (r) in a direction (a radians)
    yielding a new point [x' y']"
  [x y r a]
  (let [[dx dy] (ra-to-xy r a)]
    (trans-xy x y dx dy)))


