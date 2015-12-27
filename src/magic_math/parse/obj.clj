(ns magic-math.parse.obj
  (:require [clojure.string :as string]
            [instaparse.core :as insta]
            [magic-math.parse.utils :refer [vec-contains?
                                            vec-contains-all?
                                            intern-kw
                                            safe-inc]]))

(def mana-hierarchy (-> (make-hierarchy)
                        (derive ::B ::Color)
                        (derive ::U ::Color)
                        (derive ::G ::Color)
                        (derive ::R ::Color)
                        (derive ::W ::Color)))

(defmulti reduce-mana (fn [m token] (intern-kw (first token))))
(defmethod reduce-mana ::Color [m token]
  (let [color (first token)]
  (update m color safe-inc)))
(defmethod reduce-mana ::count [m token]
  (if-let [c (second token)]
    (let [int-c (Integer. c)]
      (update m token #(safe-inc % c)))))
(defmethod reduce-mana ::X [m token]
  (update m ::X safe-inc))

(defn mana-reducer
  "reducer function for mana cost aggregation"
  ([] {})
  ([x] x)
  ([m k] (reduce-mana m k)))

(defn mana-cost
  "Convert mana costs in string form to maps of counts"
  [mana-string]
    (transduce
      (comp (filter seq)
            (map keyword))
      mana-reducer
      (string/split (or mana-string "") #"[\}\{]")))

(defprotocol ICard
  "A protocol for cards"
  (get-mana-cost [card] "Get the card's mana cost as a map")
  (get-power-toughness [card] "Get the card's power and toughness")
  (is-spell [card] "Whether the card is a spell or not")
  (is-multicolored [card] "Whether the card is multicolored")
  (is-of-color [card color] "Whether the card is of a given color")
  (is-of-colors [card colors] "Whether the card is of all given colors"))

(defrecord Card
  [layout card-name card-names mana-string converted-cost colors type-str
   supertypes types subtypes rarity text power toughness
   loyalty multiverseid timeshifted mana-cost]
  ICard
  (get-mana-cost [this] (:mana-cost this))
  (get-power-toughness [this] {:power (:power this 0) :toughness (:toughness this 0)})
  (is-spell [this] (vec-contains? (:types this)
                                  ["Instant" "Sorcery" "Enchantment" "Interrupt"]))
  (is-multicolored [this] (> (count (:colors this)) 1))
  (is-of-color [this color] (vec-contains? (:colors this) [color]))
  (is-of-colors [this colors] (vec-contains-all? (:colors this) colors)))

(defn make-card [{mana-string :manaCost card-name :name card-names :names
                  converted-cost :cmc type-str :type
                  :keys [layout colors supertypes types subtypes rarity
                         text power toughness loyalty multiverseid
                         timeshifted]}]
  (let [parsed-mana-cost (mana-cost mana-string)
        converted-power (if (seq power) (Integer. power) nil)
        converted-toughness (if (seq toughness) (Integer. toughness) nil)]
    (Card. layout card-name card-names mana-string converted-cost colors type-str
          supertypes types subtypes rarity text converted-power
          converted-toughness loyalty multiverseid timeshifted parsed-mana-cost)))
