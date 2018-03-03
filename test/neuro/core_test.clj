(ns neuro.core-test
  (:require [clojure.test :refer :all]
            [neuro.core :refer :all]
            [neuro.neuron :refer :all]
            [neuro.layer :refer :all]
            [neuro.data :refer :all]
            [uncomplicate.neanderthal.math :refer :all]
            [midje.sweet :refer [facts throws => roughly]]))

(def x [1.0 0.86 0.03 0.08 0.903 0.725 0.574 -0.1 -0.203 -0.031 -0.12413793 0.33 0.225])

(facts
  "test some neuron functions"
  (same 1) => 1
  (one 5) => 1
  (dot-product [1 2 3] [1 2 3]) => 14
  )

(facts
  "test making input neuron"
  (make-new-input-neuron x) => {:inputs [1.0 0.86 0.03 0.08 0.903 0.725 0.574 -0.1 -0.203 -0.031 -0.12413793 0.33 0.225], :output 4.268862070000001, :weights [1 1 1 1 1 1 1 1 1 1 1 1 1]}
  )

(facts
  "test making neuron"
  (make-neuron x [1 1 1 1 1 1 1 1 1 1 1 1 1] tanh) => {:inputs [1.0 0.86 0.03 0.08 0.903 0.725 0.574 -0.1 -0.203 -0.031 -0.12413793 0.33 0.225], :output 0.9996082054159976, :weights [1 1 1 1 1 1 1 1 1 1 1 1 1]}
  )






