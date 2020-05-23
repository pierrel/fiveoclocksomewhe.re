(ns time.five
  (:require [java-time :as t]
            [time.zones :as zones]
            [time.utils :as utils]))

(def all-zones (t/available-zone-ids))

(defn zone-id-to-offset
  "Returns the offset (like \"+9:00\") for a given zone id.

  The `zid` is the string representation, not the java object"
  [zid-str]
  (-> zid-str t/zone-id t/zoned-date-time t/zone-offset .getId))

(defn offset-zid
  "Maps offsets to given `zids`"
  [zids]
  (reduce (fn [acc zid]
            (update acc
                    (zone-id-to-offset zid)
                    conj
                    zid))
          {}
          zids))

(def offset-zones (offset-zid all-zones))

(def offsets (keys offset-zones))

(def fiveoclock 17)

(defn offset-to-zones [offset-time]
  (.getZone (t/zoned-date-time offset-time)))

(defn- with-offset [from-time offset]
  (t/with-offset-same-instant
    (t/offset-date-time from-time)
    offset))

(defn- hour [from-time]
  (t/as from-time :hour-of-day))

(defn zoned-times-offset-with-hour
  "ZonedDateTimes within `offsets` matching `wanted-hour` from `from-time`"
  [from-time wanted-hour offsets]
  (let [all-times (map #(with-offset from-time %) offsets)
        wanted-times (filter #(= wanted-hour (hour %)) all-times)]
    (map t/zoned-date-time wanted-times)))

(defn offsets-with-hour
  "Returns all the offsets from `from-time` that are the same as `match-hour`"
  [from-time match-hour offsets]
  (filter #(= match-hour (hour (with-offset from-time %))) offsets))

(defn fiveoclock-at
  "Returns all the principal cities where its 5 o'clock and their time"
  []
  (let [time (t/offset-date-time)
        wanted-hour fiveoclock
        offset-cities offset-zones
        all-offsets offsets
        five-offsets (offsets-with-hour time
                                        wanted-hour
                                        all-offsets)
        matching-zones (reduce concat (map offset-cities five-offsets))
        zones (remove #(-> % zones/zone-cities empty?) matching-zones)
        zone-cities (zipmap zones (map zones/zone-cities zones))
        all-cities (reduce concat (vals zone-cities))
        fave-cities (zones/just-faves all-cities)
        cities (if (empty? fave-cities)
                 all-cities
                 fave-cities)
        city (rand-nth cities)
        zone (get (utils/map-flip zone-cities) city)]
    [(t/format "hh:mm a" (t/with-zone-same-instant
                           (t/zoned-date-time time)
                           zone))
     city]))
