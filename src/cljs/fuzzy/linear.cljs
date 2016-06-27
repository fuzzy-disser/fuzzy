(ns fuzzy.linear
  (:require [reagent.core :as reagent :refer [atom]]
            [clojure.string :as string]
            [alandipert.storage-atom :refer [local-storage]]
            [fuzzy.fzlogic :as fz :refer [fz-and fz-or]]
            [fuzzy.schema.electro :as electro]
            [fuzzy.schema.fire :as fire]
            [fuzzy.schema.accident :as accident])
  (:require-macros [fuzzy.template :refer [defschema]]))



(def default-terms {:big {:description "большой"
                          :ma-fn [[0.7 0.1] [0.9 0.9] [1 0.9]]}
                    :mid {:description "средний"
                          :ma-fn [[0.2 0.1] [0.3 0.9] [0.6 0.9] [0.8 0.1]]}
                    :low {:description "малый"
                          :ma-fn [[0 0.9] [0.1 0.9] [0.3 0.1]]}})

(def lang-vars-init
  {:x1 {:description "Неправильные действия"
        :weight 0.042
        :danger 1.58
        :terms default-terms
        :choised-term :low
        :value 0.2}

   :x2 {:description "Контроль технологических процессов"
        :weight 0.041
        :danger 1.55
        :terms default-terms
        :choised-term :low

        :value 0.1}

   :x3 {:description "Соблюдение техники безопасности"
        :weight 0.036 :danger 1.36
        :terms default-terms
        :choised-term :mid

        :value 0.5}

   :x4 {:description "Уровень профессионализма"
        :weight 0.034 :danger 1.28
        :terms default-terms
        :choised-term :mid

        :value 0.5}
   :x7 {:description "Ошибки в оперативных решениях"
        :weight 0.022 :danger 0.08
        :terms default-terms
        :choised-term :low

        :value 0.1}

   :x6 {:description "Обученность действиям в нештатных ситуациях"
        :weight 0.031
        :danger 1.17
        :terms default-terms
        :choised-term :mid

        :value 0.5}
   :x10 {:description "Физическое состояние"
         :weight 0.001
         :danger 0.03
         :terms default-terms
         :choised-term :mid

         :value 0.7}

   :y3 {:description "Срок эксплуатации ЭУ"
        :weight 0.072 :danger 1.72
        :terms default-terms
        :choised-term :mid

        :value 0.5}
    
   :y2 {:description "Степень износа изоляционных частей ЭУ"
        :weight 0.78 :danger 1.85
        :terms default-terms
        :choised-term :mid

        :value 0.5}
    
   :y1 {:description "Уровень опасности возникновения аварийных режимов"
        :weight 0.093 :danger 3.01
        :terms default-terms
        :choised-term :mid

        :value 0.5}

   :y4 {:description "Степень износа токоведущих частей ЭУ"
        :weight 0.057
        :danger 1.35
        :terms default-terms
        :choised-term :low

        :value 0.1}

   :y5 {:description "Отказ технологического электрооборудования"
        :weight 0.046
        :danger 1.35
        :terms default-terms
        :choised-term :big

        :value 0.9}

   :y6 {:description "Отказ(отсутствие) средств электрозашиты"
        :weight 0.045
        :danger 1.06
        :terms default-terms
        :choised-term :low

        :value 0.1}

   :y8 {:description "Эфективность средств электрозащиты"
        :weight 0.031
        :danger 0.73
        :terms default-terms
        :choised-term :low

        :value 0.3}

   :y7 {:desc "Возможность возникновения ОТС"
        :weight 0.034
        :danger 0.81
        :terms default-terms
        :choised-term :low

        :value 0.1}
    
   :z1 {:description "Уровень деструктивных воздействий параметров микроклимата"
        :weight 0.055 :danger 2.62
        :terms default-terms
        :choised-term :mid

        :value 0.5}
    
   :z3 {:description "Диагностика технического состояния ЭУ"
        :weight 0.042 :danger 2
        :terms default-terms
        :choised-term :mid

        :value 0.5}
   :z2 {:description "Качество текущего ремонта технологического электрооборудования"
        :weight 0.049 :danger 2.34
        :terms default-terms
        :choised-term :mid

        :value 0.5}
   :z5 {:description "Состояние условий труда"
        :weight 0.2 :danger 0.09
        :terms default-terms
        :choised-term :low

        :value 0.1}
    
   :z4 {:description "Частота возникновения возникновения опасных факторов и превышение критических значений параметров"
        :weight 0.028 :danger 1.33
        :terms default-terms
        :choised-term :mid

        :value 0.5}
   })
(def lang-vars
  (local-storage (atom lang-vars-init)
                 :lang-vars))

(defn log [what] (.log js/console (str what)))
(defn dir [o] (.dir js/console o))
(defn select-swap-handler [a k]
  (fn [select-el]
    (let [new-value (keyword (.. select-el -target -value))]
      (swap! a assoc k new-value))))

(defn lang-term-control [risk-param]
  (let [term (reagent/cursor lang-vars [risk-param])
        choised (:choised-term @term)]
    [:div.padding-5
     [:i.span4 (:description @term)]
     [:select.span2
      {:default-value (:choised-term @term)
       :on-change (select-swap-handler term :choised-term)}
      (doall
        (for [term-key (keys (:terms @term))]
          (let [t (get (:terms @term) term-key)]
            [:option
             {:value term-key
              :key (str risk-param term-key)}
             (:description t)])))]
     ;; [:h6 (fz/fz-value @term)]
     ]))
;; [:input.span1
;;       {:type "text"
;;        :value (:value @term)
;;        :on-change #(reset! term
;;                            (assoc @term :value
;;                                   (-> % .-target .-value)))}]
(defn get-choised-ma-fn [risk-param]
  (let [choised-term (get-in @lang-vars [risk-param :choised-term])]
    (get-in @lang-vars [risk-param :terms choised-term])))
;; (get-choised-ma-fn :x1)


(defn electro-fire
  []
  (let [human-factor-value (fire/human-factor @lang-vars)
        electro-station-value (fire/electro-station @lang-vars)
        environment-value (fire/environment @lang-vars)
        total-value (fz-and human-factor-value
                            electro-station-value
                            environment-value)]
    [:div.padding-20
     [:h4 "Итог: " (fz/defuzz total-value) "%"]
   
     [:div.row
      [:div.span3.bg-gray.padding-5
       ;; [:h6 (:choised-term (:x1 @lang-vars))]
       [lang-term-control :x1]
       [lang-term-control :x4]
       [lang-term-control :x10]
       [:hr]
       [:h4 "Человеческий фактор: "
        (-> human-factor-value
            fz/get-choised-term
            :description)]
       ]
    
      [:div.span4.bg-gray.padding-5
       [lang-term-control :y1]
       [lang-term-control :z3]
       [lang-term-control :y5]
       [lang-term-control :y4]
       [lang-term-control :z1]
       [lang-term-control :z2]
       [:hr]
       [:h4 "Электроустановка: "
        (-> electro-station-value
            fz/get-choised-term
            :description)]
       ]

      [:div.span4.bg-gray.padding-5
       [lang-term-control :y6]
       [lang-term-control :y8]
       [lang-term-control :z5]
       [:hr]
       [:h4 "Среда: "
        (-> environment-value
            fz/get-choised-term
            :description)]]
      ]

     [:hr]
     [:img {:src "/3.7.png"}]])
  )

(defn electro-injure
  []
  [:div.padding-20
   [:h4 "Итог: "
    (fz-and
     (electro/human-factor @lang-vars)
     (electro/electro-station @lang-vars)
     (electro/environment @lang-vars))]
   
   [:div.row
    [:div.span3.bg-gray.padding-5
     [lang-term-control :x3]
     [lang-term-control :x4]
     [lang-term-control :x7]
     [:hr]
     [:h4 "Человеческий фактор: " (electro/human-factor @lang-vars)]
     ]
    
    [:div.span4.bg-gray.padding-5
     [lang-term-control :y3]
     [lang-term-control :y2]
     [lang-term-control :y1]
     [lang-term-control :y6]
     [:hr]
     [:h4 "Электроустановка: " (electro/electro-station @lang-vars)]
     ]

    [:div.span4.bg-gray.padding-5
     [lang-term-control :z1]
     [lang-term-control :z3]
     [lang-term-control :z5]
     [lang-term-control :z4]
     [:hr]
     [:h4 "Среда: " (electro/environment @lang-vars)]
     
     ]]

   [:hr]
   [:img {:src "/3.8.png"}]])

(defn accident-no-power []
  [:div.padding-20
   [:h4 "Итог: "
    (fz-and
     (accident/human-factor @lang-vars)
     (accident/electro-station @lang-vars)
     (accident/environment @lang-vars))]
   
   [:div.row
    [:div.span3.bg-gray.padding-5
     [lang-term-control :x1]
     [lang-term-control :x2]
     [lang-term-control :x4]
     [lang-term-control :x6]
     [lang-term-control :x7]
     [:hr]
     [:h4 "Человеческий фактор: " (accident/human-factor @lang-vars)]
     ]
    
    [:div.span4.bg-gray.padding-5
     [lang-term-control :y4]
     [lang-term-control :y2]
     [lang-term-control :y1]
     [lang-term-control :y7]
     [lang-term-control :y8]
     [lang-term-control :y6]
     [:hr]
     [:h4 "Электроустановка: " (accident/electro-station @lang-vars)]
     ]

    [:div.span4.bg-gray.padding-5
     [lang-term-control :z1]
     [lang-term-control :z3]
     [lang-term-control :z2]
     [lang-term-control :z4]
     [:hr]
     [:h4 "Среда: " (accident/environment @lang-vars)]
     
     ]]

   [:hr]
   [:img {:src "/3.9.png"}]])

(def pages
  [{:title "Пожар в электроустановке"
    :fn electro-fire}
   {:title "Электротравма с летальным исходом"
    :fn electro-injure}
   {:title "Авария -- перерыв энергоснабжения"
    :fn accident-no-power}])

(def current-page (atom (first pages)))

(defn nav-menu [pages current-page]
  [:ul.span12.nav.nav-pills
   (let [current-page-title (:title @current-page)]
     (for [page pages]
       [:li
        {:key (str (random-uuid))
         :class (when (= (:title page) current-page-title) "active")}
        [:a
         {:href "#"
          :on-click #(reset! current-page page)}
         (:title page)]]))])

(defn ma-tostring [ma]
  (string/join " + "
               (map #(string/join "/" %) ma)))

(def current-risk-key (atom :x4))
(defn vars-config []
  [:div.row.span12
   [:div.span1
    [:select.span1
     {:default-value @current-risk-key}
     (doall
      (for [var-key (sort (keys @lang-vars))]
        [:option
         {:key var-key :value (name var-key)
          :on-change #(reset! current-risk-key
                              var-key)}
         var-key]))]]
   (let [risk-param (reagent/cursor @lang-vars @current-risk-key)
         term-in-edit-key (atom (:choised-term @risk-param))
         term-in-edit (reagent/cursor risk-param [:terms @term-in-edit-key]) 
         ]
     [:div.span10
      [:div.span1
       [:input.form-control.span1
        {:value (:weight @risk-param)}]]
      [:div.span2
       (doall
        (for [term-key (keys (:terms @risk-param))]
          (let [term (reagent/cursor risk-param [:terms term-key])]
            [:div.form-group
             {:key term-key}
             [:label (:description @term)]
             [:input.form-control.span4
              {:value (ma-tostring
                       (:ma-fn @term))}]])))]])
   [:hr.span12]]
  
  )
(defn layout []
  (fn []
    [:div.row
     [:div.span12
      [nav-menu pages current-page]
      [vars-config]
      [(:fn @current-page)]]
    ]))

(defn ^:export run []
  (reagent/render [layout] (.getElementById js/document "app")))

