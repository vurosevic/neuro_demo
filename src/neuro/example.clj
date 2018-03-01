(ns neuro.example
  (:require [neuro.neuron :refer :all]
            [neuro.layer :refer :all]
            [neuro.neuralnetwork :refer :all]
            [neuro.data :refer :all]
            [uncomplicate.neanderthal.core :refer :all]
            [uncomplicate.neanderthal.math :refer :all]
            [uncomplicate.neanderthal.native :refer :all]
            [criterium.core :refer :all]))


(def x [1.0 0.86 0.03 0.08 0.903 0.725 0.574 -0.1 -0.203 -0.031 -0.12413793 0.33 0.225])
(def xx [0 0.29	0.13 0.08	0.868	0.859	0.618	-0.208 -0.319 -0.125 -0.2	0.96 0.575])
(def xxx [1	1	1	1	0.918	0.87 0.615 -0.056	-0.144 0.047 -0.004344828	0.25 0.2])


(def test-network (network x 56))
(def vladatt (atom test-network))


(-> test-network)

(:input-layer test-network)
(:hidden-layer test-network)
(:output-layer test-network)
(:inputs (:output-layer test-network))
(:weights (:output-layer test-network))
(:output (:output-layer test-network))
(vec (map :weights (-> (:hidden-layer test-network))))


(for [a (replicate 10 1)]
  (train-network vladatt a 0.05)
  )

(str (for [a (replicate 10 1)]
       (train-network vladatt a 0.05)
       ))

(while (< 2.8 (evaluation_sum_abs vladatt))
  (str
    (for [a (replicate 20 1)]
      (train-network vladatt a 0.05)
      )
    )
  )

(reduce max (map :percent-abs (evaluation vladatt)))
(:output (:output-layer (change-input-vec-nn @vladatt [0	0.71	0.94	1	0.897	0.816	0.612	0.003	-0.05	0.094	0.134827586	0.42	0.375])))


(def bit-bucket-writer
  (proxy [java.io.Writer] []
    (write [buf] nil)
    (close []    nil)
    (flush []    nil)))

(defmacro noprint
  "Evaluates the given expressions with all printing to *out* silenced."
  [& forms]
  `(binding [*out* bit-bucket-writer]
     ~@forms))




