(ns time.scraper
  (:require [hickory.core :as h]
            [hickory.select :as sel]
            [clj-http.client :as client]))

(def root "https://www.zeitverschiebung.net/en/all-time-zones.html")

(defn parse [url]
  (-> (client/get url) :body h/parse h/as-hickory))

(def header-sel
  (sel/child
   (sel/tag :div)
   (sel/tag :h1)))

(def country-sel
  (sel/and (sel/tag :span)
           (sel/class "country")))

(def region-sel
  (sel/tag :small))

(defn process-city [url]
  (let [doc (parse url)
        country-el (sel/select (sel/descendant header-sel country-sel)
                               doc)
        region-el (sel/select (sel/descendant header-sel region-sel)
                              doc)]
    {:country (when country-el
                (-> country-el first :content last))
     :region (when region-el
               (-> region-el first :content last))}))

(def row-sel
  (sel/and (sel/tag :tr)
           (sel/has-descendant
            (sel/and (sel/tag :td)))))

(def zone-col-sel
  (sel/nth-child 2))
(def country-col-sel
  (sel/nth-child 3))
(def cities-col-sel
  (sel/nth-child 4))
(def link-sel
  (sel/tag :a))

(defn row-zone
  "Given the row (tr) gets the zone id"
  [hrow]
  (->
   (sel/select (sel/child zone-col-sel link-sel) hrow)
   first
   :content
   last))

(defn row-country
  "Given the row (tr) gets the country name"
  [hrow]
  (->
   (sel/select (sel/child country-col-sel link-sel) hrow)
   first
   :content
   last))

(defn row-cities
  "Given the row (tr) gets the cities"
  [hrow]
  (map #(-> % :content last)
       (sel/select (sel/child cities-col-sel link-sel)
                   hrow)))

(defn row-data
  "Given the row (tr) gets all the data"
  [hrow]
  (let [country (row-country hrow)
        zone (row-zone hrow)]
    {zone (map #(str % ", " country)
               (row-cities hrow))}))

(defn all-row-data [doc]
  (reduce merge
          (remove #(-> % first val empty?)
                  (map row-data
                       (sel/select row-sel doc)))))

(defn -main [& args]
  (clojure.pprint/pprint
   (all-row-data (parse root))))

