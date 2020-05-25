(ns time.zones.data
  (:require
            [clojure.java.io :as io]
            [clojure.edn :as edn]))

(def scraped-zones
  (with-open [r (-> "data/zone-cities.edn" io/resource io/reader)]
    (edn/read (java.io.PushbackReader. r))))

