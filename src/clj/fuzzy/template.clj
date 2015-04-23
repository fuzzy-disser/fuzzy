(ns fuzzy.template)

(defn- add-ns [str-ns sym]
  (symbol (str str-ns "/" sym)))

(defn- expand-vars [args data-sym]
  (mapcat
   identity
   (map #(list % (list
                  `* `(:value (~(keyword %) ~data-sym))
                  `(:weight (~(keyword %) ~data-sym))))
        args)))


(defmacro defschema [name body]
  (let [op (add-ns "fuzzy.fzlogic" (first body))
        params (vec (rest body))
        args-sym (gensym "data")]
    `(defn ~name [~args-sym]
       (let [~@(expand-vars params args-sym)]
       (~op ~@params)))))

(defmacro test-foo [] `(.log js/console "asdsa!!!!!!!!!3"))

;; (expand-vars ['p1 'p2] 'data)
; z1 (* (:value (:z1 vars)) (:weight (:z1 vars)))

;; (defschema test (and p2 p3 p4)) 
















