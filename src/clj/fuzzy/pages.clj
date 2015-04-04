(ns fuzzy.pages
  (:require [clojure.string :refer [blank?]]
            [selmer.parser :refer [render-file render]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]))


(selmer.parser/cache-off!)

(defn main-layout 
  [content]
  (render-file
   "templates/main-layout/index.djhtml"
   {:content content}))


(defn main []
  (render-file "templates/main.djhtml" {}))
