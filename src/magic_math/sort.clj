(ns magic-math.sort
  (:require [magic-math.parse :refer [mana-cost ]]))

(defn filter-color
  "Return only cards with the given color in their casting costs"
  [cards color]
  (filter (
