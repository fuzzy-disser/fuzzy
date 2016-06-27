(ns fuzzy.fzlogic)

(defn get-choised-term [x]
  (get-in x [:terms (:choised-term x)]))

(defn mediana [x]
  (let [ma-fn (:ma-fn (get-choised-term x))
        sorted-greater (sort #(> (nth %1 1) (nth %2 1))
                             ma-fn)
        first-two (map first (take 2 sorted-greater))
        mediana-value (/ (apply + first-two) 2)
        rounded-1k (->> mediana-value
                    (* 1000)
                    (.floor js/Math))
        to-percent (/ rounded-1k 10)]
    
    to-percent))

(def defuzz mediana)

(defn fz-value [x]
  (let [ma-fn (:ma-fn (get-choised-term x))
        weight (:weight x)
        danger (:danger x)
        values (map #(* weight (nth % 0)) ma-fn)]
    (apply max values)))

(defn fz-compare [op x y]
  (if (op (fz-value x)
          (fz-value y))
    x y))

(defn fz-min [& vars]
  (reduce #(fz-compare < %1 %2) vars))

(defn fz-max [& vars]
  (reduce #(fz-compare > %1 %2) vars))

(defn fz-and [& vars]
  (apply fz-min vars))

(defn fz-or [& vars]
  (apply fz-max vars))
