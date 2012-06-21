(ns robot.core
  (:use [compojure.core]
        [cheshire.core])
  (:require [compojure.route :as ben]
            [compojure.handler :as handler]))

(def my-atom (atom {}))

(defn store [id moves]
  (reset! my-atom moves))

(defn retrieve [id]
  @my-atom)

(defroutes main-routes
  (GET "/user/:id/moves" [id]
    (str (retrieve id)))
  (PUT "/user/:id/moves" [id :as req]
    (do
      (store id (parse-string (slurp (req :body))))
      (str "OK: " id)))
  (ben/resources "/")
  (ben/not-found "Page not found"))

(def app
  (handler/site main-routes))
