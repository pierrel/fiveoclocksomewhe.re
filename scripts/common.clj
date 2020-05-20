(ns common
  (:require
   [website.handler :as handler]
   [me.raynes.fs :as fs]
   [cljs.closure :as cljsc]
   [ring.adapter.jetty :refer [run-jetty]]
   [ring.middleware.reload :refer [wrap-reload]]
   [ring.middleware.file :refer [wrap-file]]))

(def top-resources "dev_resources")
(def resources-dir (str top-resources "/public"))
(def js-file "repl.js")

(defn build-cljs [opts]
  (cljsc/build "src"
               opts))

(defn prod-build-cljs []
  (build-cljs {:main          'website.js.core
               :output-to     "resources/public/js/main.js"
               :optimizations :advanced
               :externs       ["your-externs.js"]}))

(defn dev-build-cljs []
  (fs/delete-dir top-resources)
  (build-cljs {:output-to  (str resources-dir "/" js-file)
               :output-dir resources-dir
               :externs    ["your-externs.js"]}))

(defn app [dev?]
  (if dev?
    (wrap-reload (wrap-file #'handler/app resources-dir))
    #'handler/app))

(defn run-server
  "Runs and returns the server"
  [port block? dev?]
  (run-jetty (app dev?) {:port port :join? block?}))

