(ns time.zones
  (:require [time.zones.data :as data]
            [clojure.set :as se]))


(def offset-cities data/scraped-offsets)
(def zone-cities data/scraped-zones)

(defn just-faves [col]
  (seq (se/intersection data/faves (set col))))

