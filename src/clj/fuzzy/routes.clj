(ns fuzzy.routes
  (:require [compojure.core :refer [GET POST defroutes]]
            [compojure.route :refer [not-found resources]]
            [fuzzy.pages :as pages :refer [main-layout]]))

(defroutes routes
  (GET "/" [_] (main-layout (pages/main)))
  
  (resources "/")
  (not-found "Not Found"))

