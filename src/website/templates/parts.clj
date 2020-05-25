(ns website.templates.parts
  (:require [website.env :as env]
            [hiccup.core :refer :all]))

(defn with-common-js [& urls]
  (vec (map (fn [arg]
              [:script {:type "text/javascript" :src arg}])
            (into ["//cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"]
                  urls))))

(def prod-js
  (with-common-js "js/main.js"))

(def dev-js
  (conj (with-common-js "goog/base.js" "repl.js")
        [:script {:type "text/javascript"} "goog.require('website.js.repl');"]))

(defn- javascript [dev?]
  (if dev? dev-js prod-js))

(defn nav []
  [:nav
   [:ul.nav-mobile.center
    [:li [:a {:href "/"} "Home"]]
    [:li [:a {:href "/more"} "More (goes nowhere)"]]]])

(def footer
  [:footer.page-footer
   [:div.footer-copyright
    "© 2020 Jimmy B"
    [:a.grey-text.right
     {:href "https://github.com/pierrel/fiveoclocksomewhe.re"
      :target "_blank"}
     "Source"]]])

(defn main
  "The main template"
  [& content]
  (html [:head
         [:meta {:charset "utf-8"}]
         [:meta {:http-equiv "X-UA-Compatible"
                 :content "IE=edge"}]
         [:meta {:name "viewport"
                 :content "width=device-width, initial-scale=1"}]
         [:title "It's 5 o'clock somewhere"]
         (for [url ["//cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css"
                    "//fonts.googleapis.com/icon?family=Material+Icons"
                    "css/main.css"]]
           [:link {:rel "stylesheet"
                   :href url}])
         ]
        (into [:body
               [:main
                [:div.container
                 content]]
               footer]
              (javascript (not (env/docker?))))))

