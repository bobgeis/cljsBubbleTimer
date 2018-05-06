(ns helper.log
  "ns for logging helpers"
  (:require
    [cljs.pprint :as pp :refer [pprint]]))


(defn clog
  "pprint arg to console transparently"
  [arg]
  (pprint arg)
  arg)

(defn jlog
  "console.log arg transparently"
  [arg]
  (js/console.log arg)
  arg)

