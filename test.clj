(ns test.hi)

(defn -main [& args]
  (prn System/getProperty "MONGOHQ_URL"))
