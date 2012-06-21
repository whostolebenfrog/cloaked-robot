(defproject robot "1.0.0-SNAPSHOT"
  :description "Robot recruitment game"
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [compojure "1.1.0"]
                 [cheshire "4.0.0"]]
  :plugins [[lein-ring "0.7.1"]
            [lein-swank "1.4.4"]]
  :ring {:handler robot.core/app})