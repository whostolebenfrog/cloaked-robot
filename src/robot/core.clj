(ns robot.core
  (:use [compojure.core]
        [cheshire.core])
  (:require [compojure.route :as ben]
            [compojure.handler :as handler]))

(def my-atom (atom {}))

(defroutes main-routes
  (GET "/user/:id/moves" [id]
    (str "User id: " id " has object " @my-atom))
  (PUT "/user/:id/moves" [id :as req]
    (do
      (reset! my-atom (parse-string (slurp (req :body))))
      (str "Putted for user: " id)))
  (ben/resources "/")
  (ben/not-found "Page not found"))

(def app
  (handler/site main-routes))
