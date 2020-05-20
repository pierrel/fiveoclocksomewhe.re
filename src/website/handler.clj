(ns website.handler
  (:use ring.middleware.resource
        ring.util.response
        ring.adapter.jetty)
  (:require [website.templates :as templates]))

(defn with-response [resp]
  (-> (response resp)
      (content-type "text/html")
      (status 200)))

(defn routes [{uri :uri}]
  (case uri
    "/" (with-response (templates/home))
    (-> (response "Page not found")
        (status 404))))

(def app
  (wrap-resource routes "public"))
