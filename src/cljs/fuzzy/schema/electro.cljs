(ns fuzzy.schema.electro
  (:require [fuzzy.fzlogic :as fz :refer [fz-and fz-or]])
  (:require-macros [fuzzy.template :refer [defschema]]))
  
(defschema human-factor
  [or x3 x4 x7])

(defschema electro-station
  [and [and y3 y2 y1] y6])

(defschema environment
  [and [and z1 z3 z5] z4])


