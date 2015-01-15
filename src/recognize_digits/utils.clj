(ns recognize-digits.utils
  (require [clojure.core.matrix :as m]
           [clojure.core.matrix.dataset :as ds]
           [clojure.data.csv :as csv]
           [clojure.java.io :as io]
           )
  (import [com.univocity.parsers.csv CsvParser CsvParserSettings])
  )


(defn- create-settings []
  (let [settings (CsvParserSettings.)]
    (-> settings  (.setMaxColumns 1000 ))
    (-> settings .getFormat (.setLineSeparator "/r"))
    settings
    )
  )

(defn to-int [x] (Integer/valueOf x))

(defn query-seq [parser]
  (lazy-seq
   (when-let [val (.parseNext parser)
            ]
     (if (= 0 (rand-int 1000))
	 (print (first val)))
     (cons (map to-int (vec val)) (query-seq parser)))))

(defn read-csv [filename n]
  (let [settings (create-settings)
         parser (CsvParser. settings)
         _ (.beginParsing parser (clojure.java.io/reader filename))
         col-names (.parseNext parser)
         data (take n (query-seq parser))
         ]
    (ds/dataset col-names (m/matrix :double-array data))))
