(ns website.js.repl
  (:require [weasel.repl :as repl]
            [website.js.core :as core]))

(defonce conn
  (repl/connect "ws://localhost:9001"))

