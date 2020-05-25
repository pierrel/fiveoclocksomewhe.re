(ns website.templates
  (:require [website.templates.parts :as parts]
            [website.maps :as maps]
            [time.five :as five]))

(defn home []
  (let [[time city] (five/fiveoclock-at)]
    (parts/main [:h2.header.center-align "It's " [:strong time] " in"]
                [:h3.header.center-align [:a {:href (maps/search city)
                                              :target "_blank"}
                                          city]])))

