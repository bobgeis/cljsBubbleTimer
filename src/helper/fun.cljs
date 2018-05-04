(ns helper.fun
  "helper functions")


(def raf
  "request animation frame"
  js/requestAnimationFrame)

(def tau
  "tau is two pi"
  (* 2 Math/PI))

(defn mmap
  "map over just the values of a map, producing a new map
  this is a copy of fmap just for maps and map-like structs
  Note how empty m ensures that the product is the same type"
  [f m]
  (into (empty m) (for [[k v] m] [k (f v)])))

(defn square
  "square it"
  [x]
  (* x x))

(defn distance
  "get the distance between two point maps"
  ([{x1 :x y1 :y} {x2 :x y2 :y}]
   (distance x1 y1 x2 y2))
  ([x1 y1 x2 y2]
   (Math/hypot (- x2 x1) (- y2 y1))))
