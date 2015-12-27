(ns magic-math.core
  (:require [cheshire.core :refer [parse-stream]]))

(defn get-cards
  "Deserialize .json file containing cards"
  [fil]
  (parse-stream (clojure.java.io/reader fil) true))


(defn get-cards-from-set
  ([card-set]
   (:cards card-set))
  ([set-key cards]
   (-> cards set-key :cards)))
