(ns helper.log
  "ns for logging helpers"
  (:require
    [cljs.pprint :as pp :refer [pprint]]))

(def tau
  "tau = two * pi"
  (* 2 Math/PI))

(defn plog
  "pprint arg to console transparently"
  [arg]
  (pprint arg)
  arg)

(defn jlog
  "console.log arg transparently"
  [arg]
  (js/console.log arg)
  arg)

