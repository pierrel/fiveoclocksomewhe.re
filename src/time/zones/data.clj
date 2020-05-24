(ns time.zones.data
  (:require [hickory.core :as h]
            [hickory.select :as sel]
            [clj-http.client :as client]
            [clojure.java.io :as io]
            [clojure.edn :as edn]))

(def root "https://www.zeitverschiebung.net/en/all-time-zones.html")

(defn parse [url]
  (-> (client/get url) :body h/parse h/as-hickory))

(defn rows-with-offset
  [offset-regex hdoc]
  (sel/select (sel/and (sel/tag :tr)
                       (sel/has-descendant
                        (sel/find-in-text (re-pattern
                                           (str "^UTC" offset-regex "$")))))
              hdoc))

(defn rows-with-zone
  [zone-regex hdoc]
  (sel/select (sel/and (sel/tag :tr)
                       (sel/has-descendant
                        (sel/and (sel/tag :td)
                                 (sel/has-descendant
                                  (sel/find-in-text
                                   (re-pattern zone-regex))))))
              hdoc))

(defn cities-in-row [hrow]
  (let [city-anchors (sel/select
                      (sel/child (sel/and (sel/tag :td)
                                          sel/last-child)
                                 (sel/tag :a))
                      hrow)]
    (map #(-> % :content last) city-anchors)))

(defn cities-with-offset
  "Returns all cities with the given `offset-str` in the hickory doc `hdoc`"
  [offset-regex hdoc]
  (let [rows (rows-with-offset offset-regex hdoc)]
    (reduce concat
            (map cities-in-row rows))))

(defn cities-with-zone
  "Returns all cities with the given `offset-str` in the hickory doc `hdoc`"
  [zone-regex hdoc]
  (let [rows (rows-with-zone zone-regex hdoc)]
    (reduce concat
            (map cities-in-row rows))))

(defn offset-map [all-offsets hdoc]
  (loop [res       {}
         remaining all-offsets]
    (if (empty? remaining)
      res
      (let [offset       (first remaining)
            offset-regex (str (if (<= 0 offset) "\\+" "") offset)]
        (recur (assoc res
                      offset
                      (cities-with-offset offset-regex hdoc))
               (rest remaining))))))

(defn zone-map [all-zones hdoc]
  (loop [res       {}
         remaining all-zones]
    (if (empty? remaining)
      res
      (let [zone (first remaining)]
        (recur (assoc res
                      zone
                      (cities-with-zone zone hdoc))
               (rest remaining))))))

(def scraped-zones
  (with-open [r (-> "data/zone-cities.edn" io/resource io/reader)]
    (edn/read (java.io.PushbackReader. r))))

(def faves
  #{"London"
    "Dublin"
    "Lisbon"
    "Abidjan"
    "Accra"
    "Dakar"
    "Baker Island"
    "Howland Island"
    "Santiago"
    "Santo Domingo"
    "Manaus"
    "Caracas"
    "La Paz"
    "Halifax"
    "Jakarta"
    "Ho Chi Minh City"
    "Bangkok"
    "Krasnoyarsk"
    "Paris"
    "Berlin"
    "Rome"
    "Madrid"
    "Warsaw"
    "Lagos"
    "Kinshasa"
    "Algiers"
    "Casablanca"
    "South Sandwich Islands"
    "Dubai"
    "Baku"
    "Tbilisi"
    "Yerevan"
    "Samara"
    "Greenland"
    "Azores Islands"
    "San Francisco"
    "Los Angeles"
    "Vancouver"
    "Tijuana"
    "Mexico City"
    "Chicago"
    "Guatemala City"
    "Winnipeg"
    "Tegucigalpa"
    "San Jose"
    "San Salvador"
    "Tokelau"
    "Apia"
    "Tonga"
    "São Paulo"
    "Buenos Aires"
    "Montevideo"
    "Dhaka"
    "Almaty"
    "Omsk"
    "Moscow"
    "Istanbul"
    "Riyadh"
    "Baghdad"
    "Addis"
    "Ababa"
    "Doha"
    "Auckland"
    "Suva"
    "Petropavlovsk-Kamchatsky"
    "Cairo"
    "Johannesburg"
    "Khartoum"
    "Kiev"
    "Bucharest"
    "Athens"
    "Jerusalem"
    "Sofia"
    "Phoenix"
    "Calgary"
    "Juárez"
    "New Caledonia"
    "Solomon Islands"
    "American Samoa"
    "Niue"
    "Tokyo"
    "Seoul"
    "Pyongyang"
    "Ambon"
    "Yakutsk"
    "Karachi"
    "Tashkent"
    "Yekaterinburg"
    "Line Islands"
    "Anchorage"
    "Honolulu"
    "Sydney"
    "Port Moresby"
    "Vladicostok"
    "Miami"
    "New York"
    "Toronto"
    "Havana"
    "Lima"
    "Bogotá"
    "Kingston"
    "Shanghai"
    "Taipei"
    "Kuala Lampur"
    "Singapore"
    "Perth"
    "Manila"
    "Makassar"
    "Irkutsk"})

