(ns neuro.core-test
  (:require [clojure.test :refer :all]
            [neuro.core :refer :all]
            [neuro.neuron :refer :all]
            [neuro.layer :refer :all]
            [neuro.neuralnetwork :refer :all]
            [neuro.data :refer :all]
            [uncomplicate.neanderthal.math :refer :all]
            [midje.sweet :refer [facts throws => roughly]]))

(def x [1.0 0.86 0.03 0.08 0.903 0.725 0.574 -0.1 -0.203 -0.031 -0.12413793 0.33 0.225])

;; testing neuron

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


(facts
  "test making new neuron"
  (type (make-new-neuron x tanh)) => neuro.neuron.Neuron
  )

;; testing layer

(facts
  "test making input layer"
  (count (map :inputs (make-input-layer x))) => (count x)
  (map :inputs (make-input-layer x)) => (map list x)
  (map :weights (make-input-layer x)) => (map list (replicate (count x) 1))
  (map :output (make-input-layer x)) => (map conj x)
  )

(facts
  "test making hidden layer"
  (count (make-hidden-layer x 32 tanh)) => 32
  (map :inputs (take 1 (make-hidden-layer x 32 tanh))) => (list x)
 )

(facts
  "test making output layer"
  (:inputs (make-output-layer x tanh)) => x
  (<= (:output (make-output-layer x tanh)) 1)  => true
  (>= (:output (make-output-layer x tanh)) 0)  => true
  )

(facts
  "test generate layer"
  (generate-layer (vec (map vector x)) (vec (replicate (count x) [1])) same) => (make-input-layer x)
  )

;; testing network

(facts
  "test network"
  (count (network x 24)) => 3
  (vec (map :inputs (-> (:input-layer (network x 24))))) => (map list x)
  (count (:hidden-layer (network x 24))) => 24
  (map :inputs (take 1 (:hidden-layer (network x 24)))) => (vector (map conj x))
  (<= (:output (:output-layer (network x 24))) 1) => true
  (>= (:output (:output-layer (network x 24))) 0) => true
  )





