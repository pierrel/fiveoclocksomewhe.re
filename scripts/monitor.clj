(ns monitor
  (:require [hickory.core :as h]
            [hickory.select :as sel]
            [clj-http.client :as client]
            [clj-time.local :as t]
            [clj-time.format :as f]))

(defn get-time []
  (f/unparse (f/formatter "hh:mm:ss") (t/local-now)))

(defn to-parsed [url]
  (-> (client/get url) :body h/parse h/as-hickory))

(defn- heading [doc]
  (-> (sel/select (sel/and (sel/class "header")
                           (sel/tag :h1))
                  doc)
      first
      :content
      first))

(defn- categorize
  "GETs the url, parses the body, and tries to categorize it.

  If a problem occurs getting the data then returns the status. `sel` is
  a function that turns the body into a string that categorizes the response."
  [url sel]
  (try
    (-> (to-parsed url) sel)
    (catch clojure.lang.ExceptionInfo e
      (-> (ex-data e) :status str))))

(defn poll [url sel & {:keys [interval timeout]
                       :or {interval 1
                            timeout (* 10 60)}}]
  (let [end-time (+ (System/currentTimeMillis) (* timeout 1000))]
    (loop [results []]
      (let [current-time (get-time)
            cat (categorize url sel)]
        (println (str current-time ": " cat))
        (if (> (System/currentTimeMillis) end-time)
          results
          (do
            (Thread/sleep (* interval 1000))
            (recur (conj results
                         {:time (System/currentTimeMillis)
                          :result cat}))))))))

(defn -main
  [& args]
  (let [url (or (first args) "https://website...")]
    (poll url heading :interval 1)))

