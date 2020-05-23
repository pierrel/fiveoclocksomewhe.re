(ns website.scraper
  (:require [website.five :as five]
            [website.zones.data :as data]))

(defn -main [& args]
  (clojure.pprint/pprint 
   (data/zone-map five/all-zones
                  (data/parse data/root))))

