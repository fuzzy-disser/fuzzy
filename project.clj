(defproject fuzzy "0.1.0-SNAPSHOT"
  :description "First, simple model for disser"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :source-paths ["src/clj" "src/cljs"]

  :dependencies [[org.clojure/clojure "1.6.0"]

                 [reagent "0.5.0-alpha"]
                 [reagent-utils "0.1.2"]
                 [reagent/reagent-cursor "0.1.2"]
                 [org.clojure/clojurescript "0.0-2850" :scope "provided"]

                 [selmer "0.8.0"]                 
                 [ring "1.3.2"]
                 [ring/ring-defaults "0.1.4"]
                 [prone "0.8.0"]
                 [compojure "1.3.1"]
                 [leiningen "2.5.0"]
                 [figwheel "0.2.5"]]

  :plugins [[lein-cljsbuild "1.0.4"]
            [lein-environ "1.0.0"]
            [lein-ring "0.9.1"]
            [lein-ancient "0.6.1"]
            [lein-asset-minifier "0.2.2"]]

  :ring {:handler fuzzy.handler/app
         ;; :nrepl {:start? true :port 5225}
         ;; :auto-refresh? true
         :uberwar-name "fuzzy.war"}

  :min-lein-version "2.5.0"

  :uberjar-name "fuzzy.jar"

  :clean-targets ^{:protect false} ["resources/public/js"]

  :minify-assets
  {:assets
    {"resources/public/css/site.min.css" "resources/public/css/site.css"}}

  :cljsbuild {:builds {:app {:source-paths ["src/cljs"]
                             :compiler {:output-to     "resources/public/js/app.js"
                                        :output-dir    "resources/public/js/out"
                                        :externs       ["react/externs/react.js"]}}}}

  :profiles
  {:dev
   {
    ;; :repl-options
    ;; this one for browser
    ;; {:init-ns teacherguild.dev
    ;; :port 5445}

    :dependencies [[ring-mock "0.1.5"]
                   [ring/ring-devel  "1.3.2"]
                   [pjstadig/humane-test-output "0.6.0"]]

    :plugins [[lein-figwheel "0.2.5-SNAPSHOT"]]

    :injections [(require 'pjstadig.humane-test-output)
                 (pjstadig.humane-test-output/activate!)]

    :figwheel {:http-server-root "public"
               :server-port 3443
               :nrepl-port 7888
               :css-dirs ["resources/public/css"]
               :ring-handler fuzzy.handler/app}

    ;; :ring {:auto-refresh? true }
    :cljsbuild {:builds {:app {:source-paths ["env/dev/cljs"]
                               :compiler
                               {:optimizations :none
                                :source-map true
                                :source-map-timestamp true
                                :pretty-print  true
                                }}}}
    }

   :uberjar {:hooks [leiningen.cljsbuild minify-assets.plugin/hooks]
             :env {:production true}
             :aot :all
             :omit-source true
             :cljsbuild {:jar true
                         :builds {:app
                                  {:source-paths ["env/prod/cljs"]
                                   :compiler
                                   {:optimizations :advanced
                                    :pretty-print false}}}}}

   :production {:ring {:open-browser? false
                       :stacktraces?  false
                       :auto-reload?  false}}})
