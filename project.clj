(defproject robot "1.0.0-SNAPSHOT"
  :description "Robot recruitment game"
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [compojure "1.1.0"]
                 [cheshire "4.0.0"]
                 [congomongo "0.1.9"]]
  :plugins [[lein-ring "0.7.1"]
            [jonase/kibit "0.0.4"]
            [lein-swank "1.4.4"]]
  :ring {:handler robot.core/app}
  :resources-path "resources")
