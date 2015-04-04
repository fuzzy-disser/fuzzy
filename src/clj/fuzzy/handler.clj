(ns fuzzy.handler
  (:require [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [prone.middleware :refer [wrap-exceptions]]
            [fuzzy.routes :refer [routes]]))

(def handler 
  (wrap-defaults 
   routes 
   (assoc-in 
    site-defaults [:security :anti-forgery] false)))

(def app (wrap-exceptions handler))

