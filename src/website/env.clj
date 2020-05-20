(ns website.env)

(defn docker?
  "Basically if it's running from Docker

  This is set in the Dockerfile: `ENV DOCKER true`"
  []
  (= (System/getenv "DOCKER") "true"))
