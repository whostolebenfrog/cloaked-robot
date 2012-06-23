(ns robot.core
  (:use [compojure.core]
        [cheshire.core]
        [somnium.congomongo])
  (:require [compojure.route :as ben]
            [compojure.handler :as handler]))

(defn split-mongo-url [url]
  "split heroku mongo string into parts (mongodb://user:pass@localhost:1234/db)"
  (let [matcher (re-matcher #"^.*://(.*?):(.*?)@(.*?):(\d+)/(.*)$" url)]
    (zipmap [:match :user :pass :host :port :db] (re-find matcher))))

(defn init-mongo []
  "Initialise mongo using url from system property"
  (let [params (split-mongo-url (get (System/getenv) "MONGOHQ_URL"))]
    (make-connection
     (:db params)
     :host (:host params)
     :port (:port params)
     :user (:user params)
     :pass (:pass params))))

(defn store [id moves]
  ())

(defn retrieve [id]
  {})

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
