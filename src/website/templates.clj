(ns website.templates
  (:require [website.templates.parts :as parts]))

(defn home []
  (parts/main [:div.section
               [:h2.header "This is the main website"]
               [:h3.header "There really isn't much"]
               [:h3.header "But it's here"]]))

