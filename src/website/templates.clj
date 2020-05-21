(ns website.templates
  (:require [website.templates.parts :as parts]
            [website.five :as five]))

(defn home []
  (let [[time cities] (five/fiveoclock-at)]
    (parts/main [:h2.header.center-align "It's " [:strong time] " in"]
                [:h3.header.center-align (rand-nth cities)])))

