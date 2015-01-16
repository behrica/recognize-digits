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
            [incanter.charts :as ich]
            [clojure.core.matrix :as m]
            [clojure.core.matrix.dataset :as ds]
            [clojure.repl :refer [doc source]]
            [clj-ml.classifiers :as ml-classifier]
            [clj-ml.data :as ml-data]
            [incanter-gorilla.render :refer [chart-view]]
            recognize-digits.utils)
             
  )

;; @@

;; @@
(defn heat-map-2d-vector-transposed*
  "parses an vector-of-vectors and show it as a dataset"
  ([data2d empty-val & options]
     (let [opts (when options (apply assoc {} options))
           color? (if (false? (:color? opts)) false true)
           include-zero? (if (false? (:include-zero? opts)) false true)
           title (or (:title opts) "")
           x-label (or (:x-label opts) "cols")
           y-label (or (:y-label opts) "rows")
           z-label (or (:z-label opts) "value")
           theme (or (:theme opts) :default)
           xyz-dataset (org.jfree.data.xy.DefaultXYZDataset.)
           x-min 0
           x-max (apply max (map count data2d)) 
           y-min 0
           y-max (count data2d)
           data-transposed (for [x (range x-min x-max 1) y (range y-min y-max 1)]
                             {:x x :y y :z (get-in data2d [y x] empty-val)}
 
)
           ;; org.jfree-data.xy.DefaultXYZDataset wants data on the format:
           ;;[[D x0 x1 x2 x3] [y0 y1 y2 y3 y4] [z0 z1 z2 z3]]
           data (into-array (map double-array [(map :x data-transposed) (map :y data-transposed) (map :z data-transposed)]))
           min-z 0
           max-z 255
           x-axis (doto (org.jfree.chart.axis.NumberAxis. x-label)
                    (.setStandardTickUnits (org.jfree.chart.axis.NumberAxis/createIntegerTickUnits))
                    (.setLowerMargin 0.0)
                    (.setUpperMargin 0.0)
                    (.setAxisLinePaint java.awt.Color/white)
                    (.setTickMarkPaint java.awt.Color/white)
                    (.setAutoRangeIncludesZero include-zero?))
           y-axis (doto (org.jfree.chart.axis.NumberAxis. y-label)
                    (.setStandardTickUnits (org.jfree.chart.axis.NumberAxis/createIntegerTickUnits))
                    (.setLowerMargin 0.0)
                    (.setUpperMargin 0.0)
                    (.setAxisLinePaint java.awt.Color/white)
                    (.setTickMarkPaint java.awt.Color/white)
                    (.setAutoRangeIncludesZero include-zero?))
           colors (or (:colors opts)
                      [[0 0 127] [0 0 212] [0 42 255] [0 127 255] [0 127 255]
                       [0 226 255] [42 255 212] [56 255 198] [255 212 0] [255 198 0]
                       [255 169 0] [255 112 0] [255 56 0] [255 14 0] [255 42 0]
                       [226 0 0]])
           scale (if color?
                   (org.jfree.chart.renderer.LookupPaintScale. min-z max-z java.awt.Color/white)
                   (org.jfree.chart.renderer.GrayPaintScale. min-z max-z))
           add-color (fn [idx color]
                       (.add scale
                             (+ min-z (* (/ idx (count colors)) (- max-z min-z)))
                             (apply #(java.awt.Color. %1 %2 %3) color)))
           scale-axis (org.jfree.chart.axis.NumberAxis. z-label)
           legend (org.jfree.chart.title.PaintScaleLegend. scale scale-axis)
           renderer (org.jfree.chart.renderer.xy.XYBlockRenderer.)
 
           plot (org.jfree.chart.plot.XYPlot. xyz-dataset x-axis y-axis renderer)
           chart (org.jfree.chart.JFreeChart. plot)]
       (do
        (.setPaintScale renderer scale)
        (when color? (doseq [i (range (count colors))]
                       (add-color i (nth colors i))))
        (.addSeries xyz-dataset "Series 1" data)
        (.setBackgroundPaint plot java.awt.Color/lightGray)
        (.setDomainGridlinesVisible plot false)
        (.setRangeGridlinePaint plot java.awt.Color/white)
        (.setAxisOffset plot (org.jfree.ui.RectangleInsets. 5 5 5 5))
        (.setOutlinePaint plot java.awt.Color/blue)
        (.removeLegend chart)
        (.setSubdivisionCount legend 20)
        (.setAxisLocation legend org.jfree.chart.axis.AxisLocation/BOTTOM_OR_LEFT)
        (.setAxisOffset legend 5.0)
        (.setMargin legend (org.jfree.ui.RectangleInsets. 5 5 5 5))
        (.setFrame legend (org.jfree.chart.block.BlockBorder. java.awt.Color/red))
        (.setPadding legend (org.jfree.ui.RectangleInsets. 10 10 10 10))
        (.setStripWidth legend 10)
        (.setPosition legend org.jfree.ui.RectangleEdge/RIGHT)
        (.setTitle chart title)
        (.addSubtitle chart legend)
        (org.jfree.chart.ChartUtilities/applyCurrentTheme chart)
        (ich/set-theme chart theme))
       chart)))


(defn view-digit [digit]
  (let [v (partition 28 (take-last 784 (ml-data/instance-to-vector digit)))
    	v (vec (map vec v))]
	(chart-view (heat-map-2d-vector-transposed* v 0))))
 
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
        attributes (take-last 784 (ds/column-names ds))
        digit-template (cons {"label" classes} attributes)
        train-ml-ds (ml-data/make-dataset "digits" digit-template instances)
         ]
    train-ml-ds))
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
(ml-classifier/classifier-classify bayes-classifier (second train-ml-ds))
;; @@

;; @@

(map view-digit (take 2 (ml-data/dataset-seq train-ml-ds)))

;; @@

;; @@

;; @@
