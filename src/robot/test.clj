(ns robot.test)

(defn -main [& args]
  (do
    (prn "Printing system property")
    (prn (System/getProperty "MONGOHQ_URL"))))
