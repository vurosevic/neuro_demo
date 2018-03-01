(ns neuro.neuralnetwork
  (:require [neuro.neuron :refer :all]
            [neuro.layer :refer :all]
            [neuro.data :refer :all]
            [uncomplicate.neanderthal.core :refer :all]
            [uncomplicate.neanderthal.math :refer :all]
            [uncomplicate.neanderthal.native :refer :all]))


(defrecord Neuronetwork [
                   input-layer           ;; input set of Neurons
                   hidden-layer          ;; hidden set of Neurons
                   output-layer          ;; output set of Neurons
                   ])

(defn network
  "make a new neural network with input vector and desired number of hidden neurons"
  [x dim-hidden-layers]
  (let [
        input-layer (make-input-layer x)
        hidden-layer1 (make-hidden-layer (vec (map :output (-> input-layer))) dim-hidden-layers tanh)
        output-layer (make-output-layer (vec (map :output (-> hidden-layer1))) tanh)
        ]
    (->Neuronetwork input-layer
                    hidden-layer1
                    output-layer)))

(defn dtanh
  "help function"
  [y]
  (tanh (- 1 (sqr y))))

(defn output-deltas2
  "delta function for output layer"
  [out target]
  (* (dtanh out) (- target out)))

(defn hidden-error
  "help function for delta function for hidden layer"
  [o-delta weights]
  (dot-product weights (vec (replicate (count weights) o-delta))))

(defn hidden-deltas
  "delta function for hidden layer"
  [o-delta weights output-hidden-layer]
  (for [[w] (map list weights)]
    (let [error (hidden-error o-delta w)]
      (vec (map #(* o-delta %) (vec (map dtanh output-hidden-layer)))))))

(defn change-output-weights-vec
  "make new vector for change output weights"
  [o-delta learning-speed hidden-outputs-vec]
  (vec (map #(* (* o-delta learning-speed) %) hidden-outputs-vec)))

(defn change-hidden-weight-vec
  "make new vector for change hidden weights"
  [h-deltas learning-speed input-hidden-layer]
  (vec (for [[h i] (map list h-deltas input-hidden-layer)]
         (vec (for [[hh ii] (map list h i)]
                (* (* hh learning-speed) ii))))))

(defn changed-weights-vec
  "calculate new weight vector for output layer"
  [old-weights change-vec]
  (vec (map + old-weights change-vec)))

(defn changed-hidden-weights-vec
  "calculate new weight vector for hidden layer"
  [old-weights change-vec]
  (vec (for [[o c] (map list old-weights change-vec)]
         (vec (map + o c)))))

(defn change-input-vec-nn
  "change input vector on neural network and feed forward propagation"
  [nn x]
  (let [prepare-input (vec (map vector x))
        input-layer (generate-layer prepare-input (vec (replicate (count x) [1])) same)
        hidden-layer (generate-layer (vec (replicate (count (:hidden-layer nn)) x))  (vec (map :weights (:hidden-layer nn))) tanh)
        output-layer (make-neuron (vec (map :output hidden-layer)) (vec (:weights (:output-layer nn))) tanh)]
    (->Neuronetwork input-layer
                    hidden-layer
                    output-layer)))

(defn back-propagation
  "one iteration of backpropagation for neural network with desired params"
  [nn target speed-learning]
  (let [input-layer (:input-layer nn)
        hidden-layer (generate-layer
                       (vec (map :inputs (-> (:hidden-layer nn))))
                       (changed-hidden-weights-vec
                         (vec (map :weights (-> (:hidden-layer nn))))
                         (change-hidden-weight-vec
                           (hidden-deltas (output-deltas2 (:output (:output-layer nn)) target)
                                          (vec (map :weights (-> (:hidden-layer nn))))
                                          (vec (map :output (-> (:hidden-layer nn))))
                                          )
                           speed-learning
                           (vec (map :inputs (-> (:hidden-layer nn))))))tanh)
        output-layer (change-neuron-weights-value (vec (map :output hidden-layer))
                                                  (changed-weights-vec (:weights (:output-layer nn)) (change-output-weights-vec (output-deltas2 (:output (:output-layer nn)) target) speed-learning  (vec (map :output hidden-layer))))
                                                  tanh)]
    (->Neuronetwork input-layer
                    hidden-layer
                    output-layer)))

(defn learn
  "learning neural network for one target value"
  [nn current-number max-number speed-learning target]
  (let [num (+ current-number 1)
        ]
    (if (< num max-number)
      (learn (back-propagation nn target speed-learning) num max-number speed-learning target)
      (back-propagation nn target speed-learning))))

(defn train-network
  "train of neural network for all data"
  [atom-nn num-per-line speed-learning]
  (let [input-vec (vec (map :x (read-data-training)))
        target-vec (map :y (read-data-training))]
    (for [[x y] (map list input-vec target-vec)]
      (do
        (reset! atom-nn (learn (change-input-vec-nn @atom-nn x) 0 num-per-line speed-learning y))
        ))))

(defn evaluation
  "evaluation neural network - detail report"
  [atom-nn]
  (let [input-vec (vec (map :x (read-data-test)))
        target-vec (map :y (read-data-test))]
    (for [[x y] (map list input-vec target-vec)]
      {:output (:output (:output-layer (change-input-vec-nn @atom-nn x))) :target y
       :percent    (* 100 (- (/ (:output (:output-layer (change-input-vec-nn @atom-nn x))) y) 1))
       :percent-abs (abs (* 100 (- (/ (:output (:output-layer (change-input-vec-nn @atom-nn x))) y) 1)))}
      )))

(defn evaluation_sum
  "evaluation neural network - average report by deviations"
  [atom-nn]
  (let [u (count (map :percent (evaluation atom-nn)))
        s (reduce + (map :percent (evaluation atom-nn)))
        ]
    (/ s u)))

(defn evaluation_sum_abs
  "evaluation neural network - average report by absolute deviations"
  [atom-nn]
  (let [u (count (map :percent-abs (evaluation atom-nn)))
        s (reduce + (map :percent-abs (evaluation atom-nn)))
        ]
    (/ s u)))



















