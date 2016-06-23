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



;; ((assoc-fz-value-handler min 0.2)
;;  {:description "Соблюдение техники безопасности", :weight 0.036, :danger 1.36, :terms {:big {:description "большой", :ma-fn [[0.7 0.1] [0.9 0.9] [1 0.9]]}, :mid {:description "средний", :ma-fn [[0.2 0.1] [0.3 0.9] [0.6 0.9] [0.8 0.1]]}, :low {:description "малый", :ma-fn [[0 0.9] [0.1 0.9] [0.3 0.1]]}}, :choised-term :mid, :value 0.5})

(def tmp-x {:description "Соблюдение техники безопасности", :weight 0.036, :danger 1.36, :terms {:big {:description "большой", :ma-fn [[0.7 0.1] [0.9 0.9] [1 0.9]]}, :mid {:description "средний", :ma-fn [[0.2 0.1] [0.3 0.9] [0.6 0.9] [0.8 0.1]]}, :low {:description "малый", :ma-fn [[0 0.9] [0.1 0.9] [0.3 0.1]]}}, :choised-term :mid, :value 0.5})
;; (def tmp (fz-value tmp-x max))


;; (.log js/console (str tmp))
(.round js/Math 0.9 4)
