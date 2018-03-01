(ns neuro.neuron
  (:require [uncomplicate.neanderthal.core :refer :all]
            [uncomplicate.neanderthal.math :refer :all]
            [uncomplicate.neanderthal.native :refer :all]))

;; neuron

(defrecord Neuron [inputs           ;; input vector
                   weights          ;; weight vector
                   output           ;; output value
                   ])

(defn same
  "f(x)=x"
  [x]
  (* 1 x))

(defn one
  "f(x)=1"
  [x]
  (-> 1))

(defn singm
  "sigmoid function"
  [x]
  (/ (exp x) (+ (exp x) 1)))

(defn dot-product5
  "Dot-product with use Neanderthal library"
  [x w]
  (dot (dv x) (dv w)))

(defn dot-product
  "Dot-product - idiomatic clojure"
  [xs ys]
  (reduce + (map * xs ys)))

(defn dot-product3 ^double [^doubles xs ^doubles ys]
  "Dot-product - primitive java arrays"
              (let [n (alength xs)]
                (loop [i 0 res 0.0]
                  (if (< i n)
                    (recur (inc i) (+ res (* (aget xs i)
                                             (aget ys i))))
                    res))))

(defn output
  "output from neuron"
  [x w o-func]
  (o-func (dot-product x w)))

(defn make-neuron
  "make a neuron from vectors"
  [x w o-func]
  (->Neuron x w (output x w o-func)))

(defn random-number
  "random number in interval [0 .. 0.1]"
  [x]
  (rand 0.1))

(defn make-new-neuron
  "make a neuron from vectors with random weights"
  [x o-func]
  (make-neuron x (vec (map random-number x)) o-func))

(defn make-new-input-neuron
  "make a new input neuron, input=output"
  [x]
  (make-neuron x (vec (map one x)) same))

