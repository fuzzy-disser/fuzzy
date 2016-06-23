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

(defmacro defschema-old [name body]
  (let [op (add-ns "fuzzy.fzlogic" (symbol (str "fz-" (first body))))
        params (vec (rest body))
        args-sym (gensym "data")]
    `(defn ~name [~args-sym]
       (let [~@(expand-vars params args-sym)]
         (~op ~@params)))))

(declare expand-logic)
(defn gen-params-or-logic [sym]
  (fn [x]
    (if (coll? x)
      (expand-logic x sym)
      `(get ~sym ~(keyword x)))))

(defn expand-params [params vars-sym]
  (map (gen-params-or-logic vars-sym)
       params))

(defn expand-logic [body vars-sym]
  (let [op (add-ns "fuzzy.fzlogic" (symbol (str "fz-" (first body))))
        params (rest body)]
    `(~op ~@(expand-params params vars-sym))))


(defmacro defschema [name body]
  (let [vars-sym (gensym "vars")]
    `(defn ~name [~vars-sym]
       ~(expand-logic body vars-sym))))

;; (defschema human-factor [or x3 x4 x10])
(defschema new-schema
           [and x3 x4
            (and z1 z3)
            (or x2 x1
                (and y2 z9))])
