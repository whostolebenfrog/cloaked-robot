(ns robot.core
  (:use [compojure.core]
        [cheshire.custom]
        [somnium.congomongo]
        [clojure.string :only (blank?)])
  (:require [compojure.route :as routes]
            [compojure.handler :as handler]))

(add-encoder org.bson.types.ObjectId encode-str)

(defn split-mongo-url [url]
  "split heroku mongo string into parts (mongodb://user:pass@localhost:1234/db)"
  (let [matcher (re-matcher #"^.*://(.*?):(.*?)@(.*?):(\d+)/(.*)$" url)]
    (zipmap [:match :user :pass :host :port :db] (re-find matcher))))

(defn get-mongo-url []
  (or (System/getenv "MONGOHQ_URL") "mongodb://:@localhost:27107/robot"))

(defn init-mongo []
  "Initialise mongo using url from system property"
  (let [params (split-mongo-url (get-mongo-url))]
    (do
      (def conn
        (make-connection
         (:db params)
         :host (:host params)
         :port (Integer. (:port params))))
      (when (and
             (not (blank? (:pass params)))
             (not (blank? (:user params))))
        (authenticate conn (:user params) (:pass params)))
      (set-connection! conn))))

(defn store [moves]
  (do
    (prn moves)
    (insert! :moves moves)))

(defn retrieve
  ([id]
     (generate-string (fetch :moves :where {:user id} :sort {:seq -1} :limit 1)))
  ([id move]
     (generate-string (fetch-one :moves :where {:user id :seq move}))))

(defn with-user [id moves]
  (assoc moves :user id))

(defroutes main-routes
  (GET "/users/:id/moves/last" [id]
    (retrieve id))
  (GET "/users/:id/moves/:move" [id move]
    (retrieve id (Integer. move)))
  (PUT "/users/:id/moves" [id :as req]
    (do
      (store (with-user id (parse-string (slurp (req :body)))))
      (generate-string {:response "ok" :id id})))
  (routes/resources "/")
  (routes/not-found "Page not found"))

(init-mongo)

(def app
  (handler/site main-routes))
