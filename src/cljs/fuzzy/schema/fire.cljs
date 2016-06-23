(ns fuzzy.schema.fire
  (:require [fuzzy.fzlogic :as fz :refer [fz-and fz-or]])
  (:require-macros [fuzzy.template :refer [defschema]]))
  
(defschema human-factor
  [or x1 x4 x10])

(defschema electro-station
  [or
   [and y1 z3 y5]
   [and y4 z1 y5]])

(defschema environment
  [or y6 y8 z5])

