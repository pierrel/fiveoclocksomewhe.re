(ns dev
  (:require
   [common :as common]
   [clojure.java.browse :as browse]
   [hawk.core :as hawk]))

(defn build-cljs []
  (common/dev-build-cljs))

(build-cljs)
(hawk/watch! [{:paths ["src/[name]/js"]
               :handler (fn [ctx e]
                          (println "recompiling cljs")
                          (build-cljs))}])
(defonce server (common/run-server 3000 false true))

(defn open-browser []
  (-> server .getURI .toString browse/browse-url))
