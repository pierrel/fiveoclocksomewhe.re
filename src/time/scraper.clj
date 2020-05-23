(ns time.scraper
  (:require [time.five :as five]
            [time.zones.data :as data]))

(defn -main [& args]
  (clojure.pprint/pprint
   (data/zone-map five/all-zones
                  (data/parse data/root))))

