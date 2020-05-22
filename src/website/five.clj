(ns website.five
  (:require [java-time :as t]
            [website.zones :as zones]))

(def offsets (keys zones/offset-cities))

(def fiveoclock 17)

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
        offset-cities zones/offset-cities
        all-offsets offsets
        five-offsets (offsets-with-hour time
                                        wanted-hour
                                        all-offsets)
        one-offset (first five-offsets)
        five-time (with-offset time one-offset)]
    [(t/format "hh:mm a" five-time)
     (reduce concat
             (map offset-cities
                  five-offsets))]))
