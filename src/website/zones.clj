(ns website.zones
  (:require [clojure.string :as s]
            [hickory.core :as h]
            [hickory.select :as sel]
            [clj-http.client :as client]))

(def offset-cities
  {-12 ["Baker Island" "Howland Island"]
   -11 ["American Samoa" "Niue"]
   -10 ["Honolulu"]
   -9 ["Anchorage"]
   -8 ["San Francisco" "Los Angeles" "Vancouver" "Tijuana"]
   -7 ["Phoenix" "Calgary" "Juárez"]
   -6 ["Mexico City" "Chicago" "Guatemala City" "Winnipeg" "Tegucigalpa" "San Jose" "San Salvador"]
   -5 ["Miami" "New York" "Toronto" "Havana" "Lima" "Bogotá" "Kingston"]
   -4 ["Santiago" "Santo Domingo" "Manaus" "Caracas" "La Paz" "Halifax"]
   -3 ["São Paulo" "Buenos Aires" "Montevideo"]
   -2 ["South Sandwich Islands"]
   -1 ["Greenland" "Azores Islands"]
   0 ["London" "Dublin" "Lisbon" "Abidjan" "Accra" "Dakar"]
   1 ["Paris" "Berlin" "Rome" "Madrid" "Warsaw" "Lagos" "Kinshasa" "Algiers" "Casablanca"]
   2 ["Cairo" "Johannesburg" "Khartoum" "Kiev" "Bucharest" "Athens" "Jerusalem" "Sofia"]
   3 ["Moscow" "Istanbul" "Riyadh" "Baghdad" "Addis" "Ababa" "Doha"]
   4 ["Dubai" "Baku" "Tbilisi" "Yerevan" "Samara"]
   5 ["Karachi" "Tashkent" "Yekaterinburg"]
   6 ["Dhaka" "Almaty" "Omsk"]
   7 ["Jakarta" "Ho Chi Minh City" "Bangkok" "Krasnoyarsk"]
   8 ["Shanghai" "Taipei" "Kuala Lampur" "Singapore" "Perth" "Manila" "Makassar" "Irkutsk"]
   9 ["Tokyo" "Seoul" "Pyongyang" "Ambon" "Yakutsk"]
   10 ["Sydney" "Port Moresby" "Vladicostok"]
   11 ["New Caledonia" "Solomon Islands"]
   12 ["Auckland" "Suva" "Petropavlovsk-Kamchatsky"]
   13 ["Tokelau" "Apia" "Tonga"]
   14 ["Line Islands"]})

(def root "https://www.zeitverschiebung.net/en/all-time-zones.html")

(defn parse [path]
  (-> (client/get path) :body h/parse h/as-hickory))

(defn rows-with-offset [offset doc]
  (sel/select (sel/and (sel/tag :tr)
                       (sel/has-descendant
                        (sel/and
                         (sel/tag :strong)
                         (sel/and
                          (sel/find-in-text (re-pattern (str "UTC" offset)))
                          (sel/not (sel/find-in-text #":"))))))
              doc))

(defn cities-in-row [hrow]
  (sel/select (sel/child (sel/and (sel/tag :td)
                                  sel/last-child)
                         (sel/tag :a))
              hrow))

