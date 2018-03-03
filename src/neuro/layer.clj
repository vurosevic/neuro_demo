(ns neuro.layer
  (:require [neuro.neuron :refer :all]))

(defn make-input-layer
  "make a new input layer with input vector x"
  [x]
  (map #(make-new-input-neuron (vector %)) x))

(defn make-hidden-layer
  "make a new hidden layer with random weights and input vector x"
  ([x num-neurons o-func]
   (map #(make-new-neuron % o-func) (replicate num-neurons x))))

(defn make-output-layer
  "make a new output layer with random weight and input vector x"
  [x o-func]
  (make-new-neuron x o-func))

(defn generate-layer
  "make a new layer with input and weight vectors"
  [x w o-func]
  (for [[x w] (map list x w)]
    (make-neuron x w o-func)))

(defn change-weights-value
  "only change weight vector on neural network"
  [n weigths o-func]
  (for [[x w] (map list (map :inputs (-> n)) weigths) ]
     (make-neuron x w o-func)))

(defn change-neuron-weights-value
  "make a output layer with a one neuron with input and weight vectors"
  [inputs weigths o-func]
    (make-neuron inputs weigths o-func))






