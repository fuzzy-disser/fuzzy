(ns fuzzy.dev
  (:require [fuzzy.linear :as linear]
            [figwheel.client :as figwheel :include-macros true]

            [clojure.browser.repl]
            [reagent.core :as r]))

(enable-console-print!)

(figwheel/watch-and-reload
  :websocket-url "ws://localhost:3000/figwheel-ws"
  :jsload-callback (fn [] (r/force-update-all)))

(linear/run)


;; (use 'figwheel-sidecar.repl-api)
;; (cljs-repl)
