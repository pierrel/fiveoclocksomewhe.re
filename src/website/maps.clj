(ns website.maps
  (:require [ring.util.codec :as codec]))

(def root "https://www.google.com/maps/search/?api=1")

(defn search [place]
  (str root "&query=" (codec/url-encode place)))
