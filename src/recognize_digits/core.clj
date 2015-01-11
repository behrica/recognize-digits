(ns recognize-digits.core
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [incanter.core :as ic]
            )
  )

(defn read-train []
  (ic/dataset
   (with-open [in-file (io/reader "xaa")]
     (doall
      (csv/read-csv in-file))))

  )
