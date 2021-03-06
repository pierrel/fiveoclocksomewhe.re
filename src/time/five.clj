(ns time.five
  (:require [java-time :as t]
            [time.zones :as zones]
            [time.utils :as utils]))

(def all-zones (remove #(nil? (zones/zone-cities %))
                       (t/available-zone-ids)))

(def fiveoclock 17)

(defn- hour [from-time]
  (t/as from-time :hour-of-day))

(defn zones-in-hour [wanted-hour current-time zids]
  (filter #(= wanted-hour
              (hour (t/with-zone-same-instant current-time %)))
          zids))

(defn fiveoclock-at
  "Returns all the principal cities where its 5 o'clock and their time"
  []
  (let [time (t/zoned-date-time)
        wanted-hour fiveoclock
        zones (zones-in-hour wanted-hour time all-zones)
        zone-cities (zipmap zones (map zones/zone-cities zones))
        cities (reduce concat (vals zone-cities))
        city (rand-nth cities)
        zone (get (utils/map-flip zone-cities) city)]
    [(t/format "hh:mm a" (t/with-zone-same-instant
                           (t/zoned-date-time time)
                           zone))
     city]))
