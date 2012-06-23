(ns robot.core
  (:use [compojure.core]
        [cheshire.custom]
        [somnium.congomongo]
        [clojure.string :only (blank?)])
  (:require [compojure.route :as ben]
            [compojure.handler :as handler]))

;; custom encoder for the mongo id type
(add-encoder org.bson.types.ObjectId encode-str)

(defn split-mongo-url [url]
  "split heroku mongo string into parts (mongodb://user:pass@localhost:1234/db)"
  (let [matcher (re-matcher #"^.*://(.*?):(.*?)@(.*?):(\d+)/(.*)$" url)]
    (zipmap [:match :user :pass :host :port :db] (re-find matcher))))

(defn get-mongo-url []
  (if (blank? (System/getProperty "MONGOHQ_URL"))
    "mongodb://user:pass@localhost:27017/robot"
    (System/getProperty "MONGOHQ_URL")))

;; inits mongo with either the MONGOHQ_URL system property or
;; the default mongo port on localhost
(defn init-mongo []
  "Initialise mongo using url from system property"
  (let [params (split-mongo-url (get-mongo-url))]
    (do
      (def conn
        (make-connection
         (:db params)
         :host (:host params)
         :port (Integer. (:port params))))
      (set-connection! conn))))

(defn store [moves]
  (insert! :moves moves))

(defn retrieve [id]
  (generate-string (fetch-one :moves)))

(defn with-user [id moves]
  (assoc moves :user id))

(defroutes main-routes
  (GET "/user/:id/moves" [id]
    (str (retrieve id)))
  (PUT "/user/:id/moves" [id :as req]
    (do
      (store (with-user id (parse-string (slurp (req :body)))))
      (str "OK: " id)))
  (ben/resources "/")
  (ben/not-found "Page not found"))

(init-mongo)

(def app
  (handler/site main-routes))
