;; gorilla-repl.fileformat = 1

;; **
;;; # Gorilla REPL
;;; 
;;; Welcome to gorilla :-)
;;; 
;;; Shift + enter evaluates code. Hit alt+g twice in quick succession or click the menu icon (upper-right corner) for more commands ...
;;; 
;;; It's a good habit to run each worksheet in its own namespace: feel free to use the declaration we've provided below if you'd like.
;; **

;; @@
(ns recognize-digits.ws
  (:require [gorilla-plot.core :as plot]
            [incanter.core :as ic]
            [incanter.io   :as io]
			[incanter.stats :as is]
            [clojure.core.matrix :as m]
            [clojure.core.matrix.dataset :as ds]
            [clojure.repl :refer [doc source]]
            [clj-ml.classifiers :as ml-classifier]
            [clj-ml.data :as ml-data]
            recognize-digits.utils)
             
  )
;; @@

;; @@
(def memo-read-csv (memoize recognize-digits.utils/read-csv))
(def classes (vec (map  #(keyword (.toString %)) (range 0 10))))
;; @@

;; @@
(defn get-ml-dataset [filename size]
  (let [ds (memo-read-csv filename size)
        ds (ds/update-column ds "label" #(keyword (.toString %)))
        instances (m/to-nested-vectors ds)
        attributes (take-last 784 (ds/column-names train-ds))
        digit-template (cons {"label" classes} attributes)
        train-ml-ds (ml-data/make-dataset "digits" digit-template instances)
         ]
    train-ml-ds	
    )
  	
  )
;; @@

;; @@

(def bayes-classifier (ml-classifier/make-classifier :bayes :naive))

;; @@

;; @@
(def train-ml-ds (get-ml-dataset "train.csv" 100))
(def train-ml-ds (ml-data/dataset-set-class train-ml-ds 0))  
;; @@

;; @@
(def training-result (ml-classifier/classifier-train bayes-classifier train-ml-ds))
;; @@

;; @@
(def evaluation (ml-classifier/classifier-evaluate bayes-classifier :cross-validation train-ml-ds 10))
(println (:summary evaluation))
(println (:confusion-matrix evaluation))

;; @@

;; @@

;; @@
