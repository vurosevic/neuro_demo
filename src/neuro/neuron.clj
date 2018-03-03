(ns neuro.neuron)

;; neuron

(defrecord Neuron [inputs           ;; input vector
                   weights          ;; weight vector
                   output           ;; output value
                   ])

(defn same
  "f(x)=x"
  [x]
  (-> x))

(defn one
  "f(x)=1"
  [x]
  (-> 1))

(defn dot-product
  "Dot-product - idiomatic clojure"
  [xs ys]
  (reduce + (map * xs ys)))

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

