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
            recognize-digits.utils)
             
  )
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; @@
(def memo-read-csv (memoize recognize-digits.utils/read-csv))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;recognize-digits.ws/memo-read-csv</span>","value":"#'recognize-digits.ws/memo-read-csv"}
;; <=

;; @@
(def train-ds (memo-read-csv "train.csv" 10))

;; @@
;; ->
;;; 1014007353
;; <-
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;recognize-digits.ws/train-ds</span>","value":"#'recognize-digits.ws/train-ds"}
;; <=

;; @@
(m/shape train-ds)
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-unkown'>10</span>","value":"10"},{"type":"html","content":"<span class='clj-unkown'>785</span>","value":"785"}],"value":"[10 785]"}
;; <=
