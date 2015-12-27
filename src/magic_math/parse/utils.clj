(ns magic-math.parse.utils
  (:import java.util.regex.Pattern))

(defn vec-contains? [containing-vec & wanted-vals]
  "Predicate testing if a vector contains one or more values"
  (some? (some (set wanted-vals) containing-vec)))

(defn vec-contains-all? [containing-vec & wanted-vals]
  "Predicate testing if a vector contains multiple provided values"
  (every? (map #(vec-contains? containing-vec [%]) wanted-vals)))

(defn intern-kw
  "Create a copy of a keyword within a namespace"
  ([kw] (intern-kw *ns* kw))
  ([kw tgt-ns] (keyword (str tgt-ns) kw)))

(defn safe-inc
  "Safely increment a possibly-nil value"
  ([v] (safe-inc v 1))
  ([v x] (if (nil? v) x (+ v x))))

(defn search-text [cards ^Pattern pat]
  "Pull out card texts containing pat"
  (for [card cards
        :let [txt (-> card val :text)]
        :when (re-find pat (or txt ""))]
    [(key card) txt]))

