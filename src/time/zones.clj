(ns time.zones
  (:require [time.zones.data :as data]
            [clojure.set :as se]))

(def zone-cities data/scraped-zones)
