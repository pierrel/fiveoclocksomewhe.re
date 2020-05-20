(ns compile-cljs
  (:require [common :as common]))


(defn -main
  [& args]
  (common/prod-build-cljs))
