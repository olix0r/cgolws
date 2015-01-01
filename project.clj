(defproject cgolws "0.1.0-SNAPSHOT"
  :description "Conway's Game of Life, exposed via HTTP"
  :dependencies [[org.clojure/clojure   "1.5.1"]
                 [org.clojure/data.json "0.2.2"]
                 [compojure             "1.1.5"]
                 [com.keminglabs/c2     "0.2.3"]]

  :plugins [[lein-ring "0.8.5"]
            [lein-cljsbuild "0.3.2"]]

  :source-paths ["src/clj"]
  :cljsbuild {:builds
                [{:source-paths ["src/cljs"]
                  :compiler {:output-to "resources/public/cgol.js"
                             :optimizations :whitespace
                             :pretty-print true}}]}

  :profiles {:dev {:dependencies [[ring-mock "0.1.5"]]}}
  :ring {:handler cgolws.handler/app}

  :min-lein-version "2.5.0")
